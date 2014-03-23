package fourTheFlatServer.Stores;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class User {
	
	private String username;
	private UUID group;
	private boolean isShopping;
	private Set<String> pendingProducts;
	private Set<String> pendingUsers;
	private Map<String, Integer> money;
	private String preferedAddress; 
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String newUsername)
	{
		username = newUsername;
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

	public Map<String, Integer> getMoney() {
		return money;
	}

	public void setMoney(Map<String, Integer> money) {
		this.money = money;
	}

}
