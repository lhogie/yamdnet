package yamdnet;

import javax.swing.JComponent;
import javax.swing.JLabel;

import toools.thread.Threads;

class Monitor extends P2PApplication implements Runnable, Swingable
{
	private int nbMsgs;
	private final JLabel label = new JLabel();

	public Monitor(Peer peer)
	{
		super(peer);
	}



	private synchronized void update()
	{
		String s = "<html>";
		s += "<br>" + nbMsgs + " messages";
		s += "<br>" + getPeer().getProxies().size() + " peers";
		s += "<ul>";

		for (PeerProxy n : getPeer().getProxies())
		{
			s += "<li>" + n;
		}

		s += "</ul><br>" + getPeer().getApplications().size() + " applications";
		s += "<ul>";

		for (P2PApplication n : getPeer().getApplications())
		{
			s += "<li>" + n.getName();
		}

		label.setText(s);
	}

	@Override
	public JComponent getComponent()
	{
		return label;
	}

	@Override
	public String getName()
	{
		return "Monitor";
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
			update();
			Threads.sleepMs(1000);
		}
	}

}
