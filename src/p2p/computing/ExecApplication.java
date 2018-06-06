package p2p.computing;

import midecnet.Message;
import midecnet.P2PApplication;
import midecnet.Peer;
import midecnet.PeerProxy;
import midecnet.SynchronousMessaging;

public class ExecApplication extends P2PApplication implements SynchronousMessaging
{

	public ExecApplication(Peer peer)
	{
		super(peer);
	}

	public Object exec(ExecMessage exec)
	{
		getPeer().broadcast(exec);
		return exec.execute();
	}

	@Override
	public Message message_sync(PeerProxy from, Message msg)
	{
		ExecMessage em = (ExecMessage) msg;
		return em.execute();
	}

	@Override
	public String getName()
	{
		return "Exec";
	}

	@Override
	public void dispose()
	{
	}

}
