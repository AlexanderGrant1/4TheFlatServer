package fourTheFlatServer.Stores;

public class GroupAnalytics {
	private String bestShopper;
	private String bestStore;
	private int timeBetweenShops;
	private int avgShopCost;
	
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
