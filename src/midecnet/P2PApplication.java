package midecnet;

public abstract class P2PApplication
{
	private final Peer peer;
	public boolean running = false;

	public P2PApplication(Peer peer)
	{
		this.peer = peer;
		peer.getApplications().add(this);

		if (this instanceof TopologyListener)
		{
			peer.getTopologyListeners().add((TopologyListener) this);
		}
	}

	public Peer getPeer()
	{
		return peer;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void start()
	{
		if (running)
			throw new IllegalStateException(this + " is already running");

		running = true;

		if (this instanceof Runnable)
		{
			new Thread((Runnable) this).start();
		}

	}

	public void stop()
	{
		if ( ! running)
			throw new IllegalStateException(this + " is not running");

		running = false;
	}

	public abstract String getName();

	public abstract void dispose();

	@Override
	public String toString()
	{
		return getName() + "@" + getPeer().getName();
	}
}
