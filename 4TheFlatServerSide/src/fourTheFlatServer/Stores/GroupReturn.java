package fourTheFlatServer.Stores;

import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class GroupReturn {


private UUID groupID;
Set<String> users;
private String address;
private Set<String> allowedProducts;
private Map<String, Integer> shoppingList;
private String userShopping;

public UUID getGroupID() {
	return groupID;
}
public void setGroupID(UUID groupID) {
	this.groupID = groupID;
}
public Set<String> getUsers()
{
	return users;
}
public void setUsers(Set<String> users)
{
	this.users = users;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public Set<String> getAllowedProducts() {
	return allowedProducts;
}
public void setAllowedProducts(Set<String> allowedProducts) {
	this.allowedProducts = allowedProducts;
}
public Map<String, Integer> getShoppingList() {
	return shoppingList;
}
public void setShoppingList(Map<String, Integer> shoppingList) {
	this.shoppingList = shoppingList;
}
public String getUserShopping() {
	return userShopping;
}
public void setUserShopping(String userShopping) {
	this.userShopping = userShopping;
}
	
	
	
}
