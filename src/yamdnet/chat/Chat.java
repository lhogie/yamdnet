package yamdnet.chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import yamdnet.AsynchronousMessaging;
import yamdnet.Message;
import yamdnet.P2PApplication;
import yamdnet.Peer;
import yamdnet.PeerProxy;
import yamdnet.Swingable;

public class Chat extends P2PApplication implements Swingable, AsynchronousMessaging
{
	private final JPanel panel = new JPanel(new BorderLayout());
	private final JTextArea conversationPane = new JTextArea();
	private final JTextField textInput = new JTextField();

	public Chat(Peer peer)
	{
		super(peer);
		panel.add(conversationPane, BorderLayout.CENTER);
		panel.add(textInput, BorderLayout.SOUTH);
		textInput.requestFocus();

		textInput.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ChatMessage m = new ChatMessage(textInput.getText().trim());
				getPeer().broadcast(m);
				textInput.setText("");
			}
		});
	}

	@Override
	public void message_async(PeerProxy sender, Message msg)
	{
		append(((ChatMessage) msg).content.toString());
	}

	private void append(String s)
	{
		conversationPane.append(s + '\n');
	}

	@Override
	public JComponent getComponent()
	{
		return panel;
	}

	@Override
	public String getName()
	{
		return "chat";
	}

	@Override
	public void dispose()
	{
	}

}
