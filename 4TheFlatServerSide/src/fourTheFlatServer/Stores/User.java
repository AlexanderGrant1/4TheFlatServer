package fourTheFlatServer.Stores;

import java.util.Set;
import java.util.UUID;

public class User {
	
	private String username;
	private UUID group;
	private boolean isShopping;
	private Set<String> pendingApproval;
	private Set<Integer> moneyToGet;
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String newUsername)
	{
		username = newUsername;
	}
	
	public void setPendingApproval(Set<String> newList)
	{
		pendingApproval = newList;
	}
	
	public Set<String> getPendingApproval()
	{
		return pendingApproval;
	}
	
	public void setMoneyToGet(Set<Integer> newList)
	{
		moneyToGet = newList;
	}
	
	public Set<Integer> getMoneyToGet()
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

	public UUID getGroupID() {
		return group;
	}

	public void setGroupID(UUID group) {
		this.group = group;
	}

}
