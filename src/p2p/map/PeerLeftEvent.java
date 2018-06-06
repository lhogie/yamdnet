package p2p.map;

public class PeerLeftEvent extends PeerEvent
{

	public PeerLeftEvent(String a, String b)
	{
		super(a, b);
	}

	@Override
	void update(NetworkMap map)
	{
		map.remove(a, b);
	}
}
