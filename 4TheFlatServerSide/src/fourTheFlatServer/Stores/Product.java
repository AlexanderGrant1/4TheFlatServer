package fourTheFlatServer.Stores;
import java.util.Date;
import java.util.UUID;
public class Product {
	private UUID productID;
	private Date lastBought;
	private int purchaseFrequency;
	private Date[] purchaseDates;
	
	public UUID getProductID()
	{
		return productID;
	}
	
	public void setProductID(UUID productID)
	{
		this.productID = productID;
	}
	
	public void setLastBought(Date lastBought)
	{
		this.lastBought = lastBought;
	}
	
	public Date getLastBought()
	{
		return lastBought;
	}
	
	public int getPurchaseFrequency()
	{
		return purchaseFrequency;
	}
	
	public void setPurchaseFrequency(int purchaseFrequency)
	{
		this.purchaseFrequency = purchaseFrequency;
	}
	
	public Date[] getPurchaseDates()
	{
		return purchaseDates;
	}
	
	public Date[] setPurchaseDates()
	{
		return purchaseDates;
	}
}