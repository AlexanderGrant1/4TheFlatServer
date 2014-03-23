package fourTheFlatServer.Stores;

import java.util.Map;

public class ShoppingList {

	Map<String, Integer> list;
	
	public Map<String, Integer> getList()
	{
		return list;
	}
	
	public void setList(Map<String, Integer> list)
	{
		this.list = list;
	}
}
