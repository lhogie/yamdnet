package yamdnet.map;

import yamdnet.P2PApplication;

public class PeerJoinedEvent extends PeerEvent
{

	public PeerJoinedEvent(String a, String b)
	{
		super(a, b);
	}

	@Override
	void update(NetworkMap map)
	{
		map.add(a, b);
	}


}
