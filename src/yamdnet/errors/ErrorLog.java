package yamdnet.errors;

import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import yamdnet.AsynchronousMessaging;
import yamdnet.Message;
import yamdnet.P2PApplication;
import yamdnet.Peer;
import yamdnet.PeerProxy;
import yamdnet.Swingable;

public class ErrorLog extends P2PApplication
		implements Runnable, Swingable, AsynchronousMessaging
{
	private final JList<Throwable> list = new JList<>();
	private final JTextArea label = new JTextArea();
	private Vector<Throwable> errorList = new Vector<>();
	private final JPanel panel = new JPanel(new GridLayout(2, 1));

	public ErrorLog(Peer peer)
	{
		super(peer);
		panel.add(new JScrollPane(list));
		panel.add(new JScrollPane(label));

		list.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				Throwable t = list.getSelectedValue();
				String s = "";

				for (StackTraceElement se : t.getStackTrace())
				{
					s += se + "\n";
				}

				label.setText(s);
			}
		});
	}

	@Override
	public void message_async(PeerProxy o, Message msg)
	{
		errorList.add(((Error) msg).error);
		list.setListData(errorList);
	}

	@Override
	public JComponent getComponent()
	{
		return panel;
	}

	@Override
	public String getName()
	{
		return "Errors";
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void run()
	{
	}

	public void report(Throwable e)
	{
		errorList.add(e);
		list.setListData(errorList);
	}
}
