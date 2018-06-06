package yamdnet.computing;

import yamdnet.Message;

public abstract class ExecMessage implements Message
{
	public abstract Message execute();

	@Override
	public boolean isSynchronous()
	{
		return true;
	}
}
