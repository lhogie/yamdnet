package midecnet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;

import toools.thread.Threads;

class Client extends Thread
{
	private final Peer peer;

	public Client(Peer peer)
	{
		this.peer = peer;
	}

	@Override
	public void run()
	{
		File file = new File("peers.lst");

		while (true)
		{
			if (file.exists())
			{
				try
				{
					FileReader fileReader = new FileReader(file);
					BufferedReader bufferedReader = new BufferedReader(fileReader);

					while (true)
					{
						String line = bufferedReader.readLine();

						if (line == null)
							break;

						String[] a = line.split(":");
						InetAddress ip = InetAddress.getByName(a[0]);
						int port = Integer.valueOf(a[1]);

						// if this is the local peer
						if (isThisMyIpAddress(ip) && port == peer.getListeningPort())
							continue;

						// if the peer is already connected
						if (peer.lookup(ip, port) != null)
							continue;

						Socket s = new Socket();

						try
						{
							s.connect(new InetSocketAddress(ip, port), 1000);
							peer.newConnection(s);
						}
						catch (IOException e)
						{
						}
					}

					bufferedReader.close();
				}
				catch (IOException e)
				{
					System.err.println("I/O error: " + e.getMessage());
				}
			}

			Threads.sleepMs(5000);
		}
	}

	public static boolean isThisMyIpAddress(InetAddress addr)
	{
		// Check if the address is a valid special local or loop back
		if (addr.isAnyLocalAddress() || addr.isLoopbackAddress())
			return true;

		// Check if the address is defined on any interface
		try
		{
			return NetworkInterface.getByInetAddress(addr) != null;
		}
		catch (SocketException e)
		{
			return false;
		}
	}
}
