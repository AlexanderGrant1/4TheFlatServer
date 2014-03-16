package fourTheFlatServer.Stores;

import java.util.Set;
import java.util.UUID;

public class Group {

	private UUID groupID;
	Set<String> users;
	private String address;
	private Set<String> allowedProducts;
	private Set<String> shoppingList;
	private boolean userShopping;
	
	public UUID getGroupID()
	{
		return groupID;
	}
	
	public void setGroupID(UUID groupID)
	{
		this.groupID = groupID;
	}
	
	public void setUsers(Set<String> users)
	{
		this.users = users;
	}
	
	public Set<String> getUsers()
	{
		return users;
	}
		
	
	public String getAddress()
	{
	  return address;
	}
	
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public void setAllowedProducts(Set<String> allowedProducts)
	{
		this.allowedProducts = allowedProducts;
	}
	
	public Set<String> getAllowedProducts()
	{
		return allowedProducts;
	}
	
	public void setShoppingList(Set<String> shoppingList)
	{
		this.shoppingList =shoppingList;
	}
	
	public Set<String> getShoppingList()
	{
		return shoppingList;
	}

	
	public void setUserShopping(boolean userShopping)
	{
		this.userShopping = userShopping;
	}
	
	public boolean getuserShopping()
	{
		return userShopping;
	}

	
	
}
