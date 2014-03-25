package fourTheFlatServer.Stores;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
public class Product {
	
	private String product;
	
	private UUID groupID;
	private int purchaseFrequency;

	private	Map<Date, String> last_bought_where ; 
	private Map<Date, String> last_bought_who;
	
	
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

	public Map<Date, String> getLast_bought_where() {
		return last_bought_where;
	}

	public void setLast_bought_where(Map<Date, String> last_bought_where) {
		this.last_bought_where = last_bought_where;
	}

	public Map<Date, String> getLast_bought_who() {
		return last_bought_who;
	}

	public void setLast_bought_who(Map<Date, String> last_bought_who) {
		this.last_bought_who = last_bought_who;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	
	
}