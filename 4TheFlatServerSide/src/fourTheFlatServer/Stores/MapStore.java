package fourTheFlatServer.Stores;

import java.util.Map;

public class MapStore {

	Map<String, Integer> map;
	
	public Map<String, Integer> getMap()
	{
		return map;
	}
	
	public void seMap(Map<String, Integer> map)
	{
		this.map = map;
	}
}
