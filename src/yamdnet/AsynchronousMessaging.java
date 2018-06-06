package yamdnet;

public interface AsynchronousMessaging
{
	void message_async(PeerProxy from, Message msg);
}
