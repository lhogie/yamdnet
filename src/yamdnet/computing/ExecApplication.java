package yamdnet.computing;

import yamdnet.Message;
import yamdnet.P2PApplication;
import yamdnet.Peer;
import yamdnet.PeerProxy;
import yamdnet.SynchronousMessaging;

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
