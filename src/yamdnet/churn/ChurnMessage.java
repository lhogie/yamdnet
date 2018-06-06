package yamdnet.churn;

import yamdnet.Message;
import yamdnet.P2PApplication;

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
