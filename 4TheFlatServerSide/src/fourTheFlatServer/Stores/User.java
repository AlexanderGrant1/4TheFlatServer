package fourTheFlatServer.Stores;

import java.util.Set;
import java.util.UUID;

public class User {
	
	private String username;
	private UUID group;
	private boolean isShopping;
	private Set<String> pendingProducts;
	private Set<String> pendingUsers;
	private Set<Integer> moneyToGet;
	private String preferedAddress; 
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String newUsername)
	{
		username = newUsername;
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

	public Set<String> getPendingProducts() {
		return pendingProducts;
	}

	public void setPendingProducts(Set<String> pendingProducts) {
		this.pendingProducts = pendingProducts;
	}

	public Set<String> getPendingUsers() {
		return pendingUsers;
	}

	public void setPendingUsers(Set<String> pendingUsers) {
		this.pendingUsers = pendingUsers;
	}

	public String getPreferedAddress() {
		return preferedAddress;
	}

	public void setPreferedAddress(String preferedAddress) {
		this.preferedAddress = preferedAddress;
	}

}
