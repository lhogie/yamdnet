package p2p.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NetworkMap
{
	private final Map<String, Set<String>> m = new HashMap<>();

	public Set<String> get(String p)
	{
		return m.get(p);
	}

	public Set<String> peers()
	{
		return m.keySet();
	}

	public void add(String a, String b)
	{
		if (!m.containsKey(a))
			m.put(a, new HashSet<>());

		if (!m.containsKey(b))
			m.put(b, new HashSet<>());

		m.get(a).add(b);
		m.get(b).add(a);
	}

	public void remove(String a, String b)
	{
		if (m.containsKey(a))
			m.get(a).remove(b);

		if (m.containsKey(b))
			m.get(b).remove(a);
	}
}
