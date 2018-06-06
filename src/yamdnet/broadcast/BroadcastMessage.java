package yamdnet.broadcast;

import java.util.Random;

import yamdnet.Message;
import yamdnet.P2PApplication;

public class BroadcastMessage implements Message
{
	private final int ID = new Random().nextInt();

	// not coverage limit by default
	public int coverage = Integer.MAX_VALUE;

	// no expiration date by default
	public long expirationDate = Long.MAX_VALUE;

	private int distanceFromSource = 0;
	
	
	final Message object;

	public BroadcastMessage(Message msg)
	{
		this.object = msg;
	}

	public int getDistanceFromSource()
	{
		return distanceFromSource;
	}

	public void incrementDistance()
	{
		++distanceFromSource;
	}

	@Override
	public boolean reaches(P2PApplication app)
	{
		return app instanceof BroadcastService;
	}

	@Override
	public int hashCode()
	{
		return ID;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof BroadcastMessage && hashCode() == obj.hashCode();
	}

	public boolean isExpired()
	{
		return System.currentTimeMillis() > expirationDate;
	}

	@Override
	public boolean isSynchronous()
	{
		return false;
	}
}
