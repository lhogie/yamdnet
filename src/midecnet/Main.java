package midecnet;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import midecnet.broadcast.BroadcastService;
import midecnet.chat.Chat;
import p2p.churn.BasicChurnModel;
import p2p.churn.Churn;
import p2p.churn.PeerContact;
import p2p.errors.ErrorLog;
import p2p.exit.ExitApplication;
import p2p.map.MapApplication;

public class Main
{
	public static void main(String[] args) throws UnknownHostException
	{
		if (args.length == 0)
		{
			args = new String[] { System.getProperty("user.name"), };
		}

		final int port = Server.DEFAULT_PORT;

		int nbPeers = args.length;
		int nbCols = (int) Math.sqrt(nbPeers);

		if (nbCols * nbCols < nbPeers)
			++nbCols;

		int nbRows = (int) Math.sqrt(nbPeers);
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds();
		int width = Math.min(screenSize.width / nbCols, 600);
		int height = Math.min(screenSize.height / nbRows, 800);
		Set<Peer> peers = new HashSet<>();

		for (int i = 0; i < args.length; ++i)
		{
			int thisPort = port + i;
			String name = args[i];
			Peer peer = new Peer(name, thisPort);
			peers.add(peer);

			JTabbedPane tabs = new JTabbedPane();
			JFrame f = new JFrame("P2P - " + name);

			peer.getTopologyListeners().add(new TopologyListener()
			{

				@Override
				public void peerLeft(PeerProxy p)
				{
					upateTitle();
				}

				@Override
				public void peerJoined(PeerProxy newPeer)
				{
					upateTitle();
				}

				private void upateTitle()
				{
					f.setTitle("DENEJA - " + name + " - (" + peer.getProxies().size()
							+ " peers)");
				}
			});

			f.setContentPane(tabs);
			f.setSize(width, height);
			f.setLocation(width * (i % nbCols), height * (i / nbCols));

			// new Monitor(peer);
			new BroadcastService(peer);
			new ErrorLog(peer);
			Chat chat = new Chat(peer);
			//new MapApplication(peer);
			new ExitApplication(peer);
			// new ExecApplication(peer);

			for (P2PApplication app : peer.getApplications())
			{
				if (app instanceof Swingable)
				{
					tabs.addTab(app.getName(), ((Swingable) app).getComponent());
				}
			}

			tabs.setSelectedComponent(chat.getComponent());
			f.setVisible(true);
		}

		// initialize churn for all peers
		for (Peer p : peers)
		{
			Churn churn = new Churn(p);
			churn.setModel(new BasicChurnModel());

			for (Peer p2 : peers)
			{
				if (p2.getListeningPort() != p.getListeningPort())
				{
					churn.ips.add(new PeerContact(InetAddress.getLocalHost(),
							p2.getListeningPort()));
				}
			}
		}

		for (Peer p : peers)
		{
			for (P2PApplication app : p.getApplications())
			{
				app.start();
			}
		}
	}
}
