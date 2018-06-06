package p2p.map;

import java.util.Random;

import midecnet.Message;
import midecnet.P2PApplication;

public abstract class PeerEvent implements Message, Comparable<PeerEvent>
{
	final String a, b;
	final long date;
	final int ID = new Random().nextInt();

	public PeerEvent(String a, String b)
	{
		this.a = a;
		this.b = b;
		this.date = System.currentTimeMillis();
	}

	@Override
	public boolean isSynchronous()
	{
		return false;
	}

	public boolean has(String s)
	{
		return s.equals(a) || s.equals(b);
	}

	public String other(String p)
	{
		if (p.equals(a))
		{
			return b;
		}
		else if (p.equals(b))
		{
			return a;
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	@Override
	public int compareTo(PeerEvent o)
	{
		return Long.compare(date, o.date);
	}

	abstract void update(NetworkMap map);

	public long getAge()
	{
		return System.currentTimeMillis() - date;
	}

	@Override
	public boolean reaches(P2PApplication a)
	{
		return a instanceof MapApplication;
	}
}
