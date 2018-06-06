package yamdnet.broadcast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import toools.thread.Threads;
import yamdnet.AsynchronousMessaging;
import yamdnet.Message;
import yamdnet.P2PApplication;
import yamdnet.Peer;
import yamdnet.PeerProxy;
import yamdnet.TopologyListener;

public class BroadcastService extends P2PApplication
		implements Runnable, TopologyListener, AsynchronousMessaging
{
	private final Set<BroadcastMessage> alreadyReceivedMsgs = new HashSet<>();

	public BroadcastService(Peer peer)
	{
		super(peer);
	}

	public void broadcast(Message m)
	{
		forward(new BroadcastMessage(m));
	}

	private void forward(BroadcastMessage bm)
	{
		synchronized (getPeer().getProxies())
		{
			for (PeerProxy d : getPeer().getProxies())
			{
				getPeer().send(bm, d);
			}
		}
	}

	@Override
	public void message_async(PeerProxy from, Message msg)
	{
		BroadcastMessage bm = (BroadcastMessage) msg;

		synchronized (alreadyReceivedMsgs)
		{
			// not yet received
			if ( ! alreadyReceivedMsgs.contains(bm))
			{
				bm.incrementDistance();
				alreadyReceivedMsgs.add(bm);

				if (bm.getDistanceFromSource() <= bm.coverage && ! bm.isExpired())
				{
					forward(bm);
				}

				getPeer().deliverToApplications(from, bm.object);
			}
		}
	}

	@Override
	public void peerJoined(PeerProxy newPeer)
	{
		synchronized (alreadyReceivedMsgs)
		{
			for (BroadcastMessage m : alreadyReceivedMsgs)
			{
				getPeer().send(m, newPeer);
			}
		}
	}

	@Override
	public void peerLeft(PeerProxy p)
	{
	}

	@Override
	public String getName()
	{
		return "broadcast service";
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void run()
	{
		while (isRunning())
		{
			removeInvalidMessages();
			Threads.sleepMs(1000);
		}
	}

	private void removeInvalidMessages()
	{
		synchronized (alreadyReceivedMsgs)
		{
			Iterator<BroadcastMessage> i = alreadyReceivedMsgs.iterator();

			while (i.hasNext())
			{
				BroadcastMessage m = i.next();

				if (m.getDistanceFromSource() > m.coverage || m.isExpired())
				{
					i.remove();
				}
			}
		}
	}
}
