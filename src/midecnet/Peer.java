package midecnet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import midecnet.broadcast.BroadcastService;
import p2p.errors.ErrorLog;

public class Peer
{
	private final String friendlyName;
	private final Set<PeerProxy> proxies = new HashSet<>();
	private final List<P2PApplication> applications = new ArrayList<>();
	private final int port;
	private final List<TopologyListener> topoListeners = new ArrayList<>();

	public Peer(String friendlyName, int port)
	{
		this.friendlyName = friendlyName;
		this.port = port;

		new Server(this).start();
		new Client(this).start();
	}

	public int getListeningPort()
	{
		return port;
	}

	public synchronized PeerProxy newConnection(Socket socket) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

		// say hello
		oos.writeObject(new HelloMessage(friendlyName));
		HelloMessage h = (HelloMessage) PeerProxy.readObject(ois);

		// if there was already a connection to this peer
		if (lookup(h.name) != null)
		{
			socket.close();
			return null;
		}

		PeerProxy newNeighbor = new PeerProxy(h.name, socket, oos, ois);

		synchronized (proxies)
		{
			proxies.add(newNeighbor);
		}

		for (TopologyListener a : topoListeners)
			a.peerJoined(newNeighbor);

		new Thread("reading incoming stuff from " + newNeighbor)
		{
			@Override
			public void run()
			{
				try
				{
					while (true)
					{
						Message msg = (Message) newNeighbor.readObject();
						deliverToApplications(newNeighbor, msg);
					}
				}
				catch (Throwable e)
				{
					reportError(e);
					disconnect(newNeighbor);
				}
			}

		}.start();

		return newNeighbor;
	}

	public void deliverToApplications(PeerProxy from, Message msg)
	{
		int c = 0;

		for (P2PApplication app : applications)
		{
			if (msg.reaches(app))
			{
				++c;

				if (msg.isSynchronous())
				{
					Message response = ((SynchronousMessaging) app).message_sync(from,
							msg);

					try
					{
						from.send(response);
					}
					catch (IOException e)
					{
						reportError(e);
						disconnect(from);
					}
				}
				else
				{
					((AsynchronousMessaging) app).message_async(from, msg);
				}
			}
		}

		if (c == 0)
			reportError(new IllegalStateException(
					"message not delivered to any app: " + msg));

	}

	public void reportError(Throwable e)
	{
		ErrorLog el = (ErrorLog) lookupService(app -> app instanceof ErrorLog);
		el.report(e);
	}

	public Message send(Message msg, PeerProxy dest)
	{
		try
		{
			return dest.send(msg);
		}
		catch (IOException e)
		{
			reportError(e);

			disconnect(dest);
			return null;
		}
	}

	public void disconnect(PeerProxy peer)
	{
		synchronized (proxies)
		{
			proxies.remove(peer);
		}

		peer.close();

		for (TopologyListener a : topoListeners)
			a.peerLeft(peer);
	}

	public PeerProxy lookup(String name)
	{
		for (PeerProxy pp : proxies)
		{
			if (pp.getName().equals(name))
			{
				return pp;
			}
		}

		return null;
	}

	public PeerProxy lookup(InetAddress ip, int port)
	{
		for (PeerProxy pp : proxies)
		{
			if (pp.getInetAddress().equals(ip) && pp.getPort() == port)
			{
				return pp;
			}
		}

		return null;
	}

	public PeerProxy connect(InetAddress ip, int port)
	{
		try
		{
			return newConnection(new Socket(ip, port));
		}
		catch (IOException e)
		{
			return null;
		}
	}

	public String getName()
	{
		return friendlyName;
	}

	public Set<PeerProxy> getProxies()
	{
		return proxies;
	}

	public List<P2PApplication> getApplications()
	{
		return applications;
	}

	public void broadcast(Message msg)
	{
		((BroadcastService) lookupService(a -> a instanceof BroadcastService))
				.broadcast(msg);
	}

	public P2PApplication lookupService(Predicate<P2PApplication> p)
	{
		for (P2PApplication app : applications)
		{
			if (p.test(app))
			{
				return app;
			}
		}

		return null;
	}

	public List<TopologyListener> getTopologyListeners()
	{
		return topoListeners;
	}
}
