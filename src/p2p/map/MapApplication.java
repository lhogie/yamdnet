package p2p.map;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import egviz.JGraph;
import egviz.Node;
import midecnet.AsynchronousMessaging;
import midecnet.Message;
import midecnet.P2PApplication;
import midecnet.Peer;
import midecnet.PeerProxy;
import midecnet.Swingable;
import midecnet.TopologyListener;

public class MapApplication extends P2PApplication
		implements TopologyListener, Swingable, AsynchronousMessaging
{
	private final NetworkMap map = new NetworkMap();
	private final JGraph g = new JGraph()
	{

		@Override
		public int getArcType(Node u, Node v)
		{
			return map.get((String) u.e).contains(v.e) ? 1 : 0;
		}

		@Override
		public String getText(Node u)
		{
			return u.e.toString();
		}

		@Override
		public int getSize(Node u)
		{
			return 10;
		}

		@Override
		public Color getFillColor(Node u)
		{
			return Color.white;
		}

		@Override
		public Color getColor(Node u)
		{
			return Color.black;
		}

		@Override
		public ImageIcon getIcon(Node u)
		{
			return null;
		}
	};

	public MapApplication(Peer peer)
	{
		super(peer);
	}

	@Override
	public void message_async(PeerProxy from, Message e)
	{
		((PeerEvent) e).update(map);
	}

	@Override
	public void peerJoined(PeerProxy p)
	{
		PeerEvent e = new PeerJoinedEvent(getPeer().getName(), p.getName());
		e.update(map);
		getPeer().broadcast(e);
	}

	@Override
	public void peerLeft(PeerProxy p)
	{
		PeerEvent e = new PeerLeftEvent(getPeer().getName(), p.getName());
		e.update(map);
		getPeer().broadcast(e);
	}

	@Override
	public JComponent getComponent()
	{
		return g.getBundleComponent();
	}

	@Override
	public String getName()
	{
		return "map";
	}

	@Override
	public void dispose()
	{
	}

}
