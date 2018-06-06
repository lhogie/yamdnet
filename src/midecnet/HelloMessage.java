package midecnet;

class HelloMessage implements Message
{
	final String name;

	public HelloMessage(String friendlyName)
	{
		this.name = friendlyName;
	}

	@Override
	public boolean reaches(P2PApplication a)
	{
		return false;
	}

	@Override
	public boolean isSynchronous()
	{
		return true;
	}
}
