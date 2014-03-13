package fourTheFlatServer.Model;

import java.util.LinkedList;

public class User {
	
	private String username;
	private boolean isShopping;
	private LinkedList<String> pendingApproval;
	private LinkedList<Integer> moneyToGet;
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String newUsername)
	{
		username = newUsername;
	}
	
	public void setPendingApproval(LinkedList<String> newList)
	{
		pendingApproval = newList;
	}
	
	public LinkedList<String> getPendingApproval()
	{
		return pendingApproval;
	}
	
	public void setMoneyToGet(LinkedList<Integer> newList)
	{
		moneyToGet = newList;
	}
	
	public LinkedList<Integer> getMoneyToGet()
	{
		return moneyToGet;
	}
	
	public void setIsShopping(boolean shopping)
	{
		isShopping = shopping;
	}
	
	public boolean getIsShopping()
	{
		return isShopping;
	}

}
