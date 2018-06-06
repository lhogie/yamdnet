package p2p.exit;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import midecnet.AsynchronousMessaging;
import midecnet.Message;
import midecnet.P2PApplication;
import midecnet.Peer;
import midecnet.PeerProxy;
import midecnet.Swingable;

public class ExitApplication extends P2PApplication
		implements Swingable, AsynchronousMessaging
{
	JPanel buttons = new JPanel(new GridBagLayout());

	public ExitApplication(Peer peer)
	{
		super(peer);
		JButton shutdownButton = new JButton("Shutdown");

		shutdownButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				shutdown();
			}
		});

		JButton restartButton = new JButton("Restart");

		restartButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				restart();
			}
		});

		buttons.add(shutdownButton);
		buttons.add(restartButton);
	}

	private final static int SHUTDOWN = 56;
	private final static int RESTART = 57;

	public void shutdown()
	{
		trigger(SHUTDOWN);
	}

	public void restart()
	{
		trigger(RESTART);
	}

	public void trigger(int exitCode)
	{
		ExitMessage msg = new ExitMessage();
		msg.exitCode = exitCode;
		getPeer().broadcast(msg);

		for (P2PApplication app : getPeer().getApplications())
		{
			app.dispose();
		}

		System.exit(msg.exitCode);
	}

	@Override
	public void message_async(PeerProxy from, Message msg)
	{
		if (msg instanceof ExitMessage)
		{
			ExitMessage em = (ExitMessage) msg;
			trigger(em.exitCode);
		}
	}

	@Override
	public JComponent getComponent()
	{
		return buttons;
	}

	@Override
	public String getName()
	{
		return "Shutdown";
	}

	@Override
	public void dispose()
	{
	}
}
