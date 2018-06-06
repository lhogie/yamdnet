package p2p.churn;

import midecnet.Message;
import midecnet.P2PApplication;

public class ChurnMessage implements Message
{
	@Override
	public boolean reaches(P2PApplication app)
	{
		return app instanceof Churn;
	}

	@Override
	public boolean isSynchronous()
	{
		return false;
	}
}
