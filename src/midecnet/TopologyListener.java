package midecnet;

public interface TopologyListener
{
	void peerJoined(PeerProxy newPeer);

	void peerLeft(PeerProxy p);

}
