package fourTheFlatServer.Stores;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
public class Product {
	
	private String product;
	
	private UUID groupID;
	private int purchaseFrequency;

	private	Map<Date, Integer> last_bought_cost; 
	
	
	public UUID getGroupID()
	{
		return groupID;
	}
	
	public void setGroupID(UUID groupID)
	{
		this.groupID = groupID;
	}
	
	public int getPurchaseFrequency()
	{
		return purchaseFrequency;
	}
	
	public void setPurchaseFrequency(int purchaseFrequency)
	{
		this.purchaseFrequency = purchaseFrequency;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Map<Date, Integer> getLast_bought_cost() {
		return last_bought_cost;
	}

	public void setLast_bought_cost(Map<Date, Integer> last_bought_cost) {
		this.last_bought_cost = last_bought_cost;
	}
	
	
}