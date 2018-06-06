package p2p.churn;

import java.util.HashSet;
import java.util.Set;

import midecnet.AsynchronousMessaging;
import midecnet.Message;
import midecnet.P2PApplication;
import midecnet.Peer;
import midecnet.PeerProxy;
import toools.io.Cout;
import toools.thread.Threads;

public class Churn extends P2PApplication implements Runnable, AsynchronousMessaging
{
	public final Set<PeerContact> ips = new HashSet<>();
	private ChurnModel model;

	public Churn(Peer peer)
	{
		super(peer);
	}

	@Override
	public void run()
	{
		while (isRunning())
		{
			long pauseDuration = (long) (1000 / model.getRate());
			Threads.sleepMs(pauseDuration);
			model.apply(this);
		}

		Cout.debugSuperVisible("STOP");
	}

	public ChurnModel getModel()
	{
		return model;
	}

	public void setModel(ChurnModel model)
	{
		this.model = model;
	}

	public void deploy(ChurnModel model)
	{
		getPeer().broadcast(model);
	}

	@Override
	public void message_async(PeerProxy from, Message msg)
	{
		if (msg instanceof PeerContact)
		{
			ips.add((PeerContact) msg);
		}
		else if (msg instanceof ChurnModel)
		{
			this.model = (ChurnModel) msg;
		}
		else
		{
			throw new IllegalStateException(msg.getClass().getName());
		}
	}


	@Override
	public String getName()
	{
		return "churn";
	}

	@Override
	public void dispose()
	{
	}

}
