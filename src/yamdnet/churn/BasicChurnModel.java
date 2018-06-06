package yamdnet.churn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yamdnet.PeerProxy;

/**
 * Do one connection or disconnection per second.
 * 
 * @author lhogie
 *
 */

public class BasicChurnModel extends ChurnModel
{
	private final Random r = new Random();

	@Override
	public void apply(Churn churn)
	{
		// nothing can be done
		if (churn.getPeer().getProxies().isEmpty() && churn.ips.isEmpty())
			return;

		// new random connection
		if (r.nextBoolean() || churn.getPeer().getProxies().isEmpty())
		{
			// pick a random contact in the known peer lists
			PeerContact randomPeer = new ArrayList<>(churn.ips)
					.get(r.nextInt(churn.ips.size()));
			churn.getPeer().connect(randomPeer.ip, randomPeer.port);
		}
		// new random disconnection
		else
		{
			// pick a random connected peer
			List<PeerProxy> proxies = new ArrayList<>(churn.getPeer().getProxies());
			PeerProxy randomProxy = proxies.get(r.nextInt(proxies.size()));
			churn.getPeer().disconnect(randomProxy);
		}
	}

	// between 5 and 0.1
	@Override
	public double getRate()
	{
		return r.nextDouble()*5 + 0.1;
	}

}
