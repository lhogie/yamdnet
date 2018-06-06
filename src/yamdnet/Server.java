package yamdnet;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Thread
{
	public static final int DEFAULT_PORT = 17344;

	private final Peer peer;

	public Server(Peer peer)
	{
		this.peer = peer;
	}

	@Override
	public void run()
	{
		try
		{
			ServerSocket s = new ServerSocket(peer.getListeningPort());

			while (true)
			{
				try
				{
					peer.newConnection(s.accept());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
