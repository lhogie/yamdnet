package midecnet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class PeerProxy
{
	private final String friendlyName;
	private final Socket socket;
	private final ObjectInputStream ois;
	private final ObjectOutputStream oos;

	public PeerProxy(String friendlyName, Socket s, ObjectOutputStream out,
			ObjectInputStream in)
	{
		this.friendlyName = friendlyName;
		this.socket = s;
		this.oos = out;
		this.ois = in;
	}

	public Message send(Message msg) throws IOException
	{
		oos.writeObject(msg);

		if (msg.isSynchronous())
		{
			return readObject();
		}
		else
		{
			return null;
		}
	}

	public Message readObject() throws IOException
	{
		return readObject(ois);
	}

	public static Message readObject(ObjectInputStream ois) throws IOException
	{
		try
		{
			return (Message) ois.readObject();
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalStateException(e);
		}
	}

	public InetAddress getInetAddress()
	{
		return socket.getInetAddress();
	}

	public void close()
	{
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
		}
	}

	public int getPort()
	{
		return socket.getPort();
	}

	public String getName()
	{
		return friendlyName;
	}

	@Override
	public String toString()
	{
		return friendlyName + "@" + socket.getInetAddress().getHostName() + ":"
				+ socket.getPort();
	}

}
