package yamdnet.errors;

import yamdnet.Message;
import yamdnet.P2PApplication;

public class Error implements Message
{
	Throwable error;

	@Override
	public boolean reaches(P2PApplication app)
	{
		return app instanceof ErrorLog;
	}

	@Override
	public boolean isSynchronous()
	{
		return false;
	}

}
