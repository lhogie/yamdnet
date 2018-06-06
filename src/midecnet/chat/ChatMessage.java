package midecnet.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import midecnet.Message;
import midecnet.P2PApplication;

public class ChatMessage implements Message
{
	private static final long serialVersionUID = 0;

	final public int id = new Random().nextInt();
	public final Serializable content;
	public List<String> senders = new ArrayList<>();

	public ChatMessage(Serializable content)
	{
		this.content = content;
	}

	@Override
	public boolean reaches(P2PApplication a)
	{
		return a instanceof Chat;
	}

	@Override
	public boolean isSynchronous()
	{
		return false;
	}
}
