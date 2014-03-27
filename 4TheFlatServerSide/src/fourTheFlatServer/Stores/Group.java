package fourTheFlatServer.Stores;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Group {

	private UUID groupID;
	Set<String> users;
	private String address;
	private Set<String> allowedProducts;
	private Map<String, Integer> shoppingList;
	private String userShopping;
	private Map<Date,Integer> shopCost;
	private Map<Date,String> lastShopWhere;
	private Map<Date,String> lastShopWho;
	private String bestShopper;
	private int timeBetweenShops;
	private int avgShopCost;
	private String bestStore;
	
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
	
	
	public void setUserShopping(String userShopping)
	{
		this.userShopping = userShopping;
	}
	
	public String getuserShopping()
	{
		return userShopping;
	}

	public Map<String, Integer> getShoppingList() {
		return shoppingList;
	}

	public void setShoppingList(Map<String, Integer> shoppingList) {
		this.shoppingList = shoppingList;
	}

	public Map<Date,Integer> getShopCost() {
		return shopCost;
	}

	public void setShopCost(Map<Date,Integer> shopCost) {
		this.shopCost = shopCost;
	}

	public Map<Date,String> getLastShopWhere() {
		return lastShopWhere;
	}

	public void setLastShopWhere(Map<Date,String> lastShopWhere) {
		this.lastShopWhere = lastShopWhere;
	}

	public Map<Date,String> getLastShopWho() {
		return lastShopWho;
	}

	public void setLastShopWho(Map<Date,String> lastShopWho) {
		this.lastShopWho = lastShopWho;
	}

	public String getBestShopper() {
		return bestShopper;
	}

	public void setBestShopper(String bestShopper) {
		this.bestShopper = bestShopper;
	}

	public int getTimeBetweenShops() {
		return timeBetweenShops;
	}

	public void setTimeBetweenShops(int timeBetweenShops) {
		this.timeBetweenShops = timeBetweenShops;
	}

	public int getAvgShopCost() {
		return avgShopCost;
	}

	public void setAvgShopCost(int avgShopCost) {
		this.avgShopCost = avgShopCost;
	}

	public String getBestStore() {
		return bestStore;
	}

	public void setBestStore(String bestStore) {
		this.bestStore = bestStore;
	}

	
	
}
