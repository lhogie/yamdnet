package yamdnet.exit;

import yamdnet.Message;
import yamdnet.P2PApplication;

public class ExitMessage implements Message
{
	public int exitCode = 0;

	@Override
	public boolean reaches(P2PApplication a)
	{
		return a instanceof ExitApplication;
	}

	@Override
	public boolean isSynchronous()
	{
		return false;
	}
}
