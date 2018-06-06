package midecnet;

import java.io.Serializable;

public interface Message extends Serializable
{
	boolean isSynchronous();

	boolean reaches(P2PApplication app);
}
