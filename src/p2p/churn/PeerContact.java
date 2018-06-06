package p2p.churn;

import java.net.InetAddress;

import midecnet.Server;

public class PeerContact extends ChurnMessage
{
	InetAddress ip;
	int port = Server.DEFAULT_PORT;

	public PeerContact(InetAddress ip, int p)
	{
		this.ip = ip;
		this.port = p;
	}

	@Override
	public String toString()
	{
		return ip + ":" + port;
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof PeerContact && obj.toString().equals(toString());
	}
}
