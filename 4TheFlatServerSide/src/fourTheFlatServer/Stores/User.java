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
	private String favProduct;
	private String favShop;
	private int avgShopCost;
	private int avgShopWhen;
	private Map<String, Integer> avgProdSpend;
	
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

	public String getFavProduct() {
		return favProduct;
	}

	public void setFavProduct(String favProduct) {
		this.favProduct = favProduct;
	}

	public String getFavShop() {
		return favShop;
	}

	public void setFavShop(String favShop) {
		this.favShop = favShop;
	}

	public int getAvgShopCost() {
		return avgShopCost;
	}

	public void setAvgShopCost(int avgShopCost) {
		this.avgShopCost = avgShopCost;
	}

	public int getAvgShopWhen() {
		return avgShopWhen;
	}

	public void setAvgShopWhen(int avgShopWhen) {
		this.avgShopWhen = avgShopWhen;
	}

	public Map<String, Integer> getAvgProdSpend() {
		return avgProdSpend;
	}

	public void setAvgProdSpend(Map<String, Integer> avgProdSpend) {
		this.avgProdSpend = avgProdSpend;
	}

	public int getAveragePrice(String productName)
	{
		if(avgProdSpend == null)
		{
			return 0;
		}
		System.out.println(productName + " hello");
		if(avgProdSpend.containsKey(productName))
		{
			return avgProdSpend.get(productName);
		}	
		else
		{
			return 0;
		}
	}
}
