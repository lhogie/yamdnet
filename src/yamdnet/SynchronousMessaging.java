package yamdnet;

public interface SynchronousMessaging
{
	Message message_sync(PeerProxy from, Message msg);
}
