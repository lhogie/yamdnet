package yamdnet.broadcast;

import yamdnet.Message;
import yamdnet.P2PApplication;

public class Response implements Message
{
	public final Object content;
	
	public Response(Object content)
	{
		this.content = content;
	}
	
	@Override
	public boolean reaches(P2PApplication app)
	{
		throw new IllegalStateException();
	}

	@Override
	public boolean isSynchronous()
	{
		return false;
	}

}
