 package fourTheFlatServer.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.User;
import fourTheFlatServer.lib.CassandraConnection;

public class UserMethods {
	
	public static boolean userExists(String username)
	{
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("SELECT * from users where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username);
		ResultSet rs = session.execute(boundStatement);
		ResultSet details = rs;
		session.close();
		return details.one() != null;
	}
	
	public static UUID getGroupIdByUsername(String username)
	{
		if(!userExists(username))
		{
			return null;
		}
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("SELECT group from users where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username);
		ResultSet rs = session.execute(boundStatement);
		Row r = rs.one();
		
		UUID groupID = r.getUUID("group");
		session.close();

		return groupID;
	}
	
	public static User getUserByUsername(String username)
	{
		if(!userExists(username))
		{
			return null;
		}
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("SELECT * from users where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username);
		ResultSet rs = session.execute(boundStatement);
		Row r = rs.one();
		
		// Set user object attributes
		User user = new User();
		//TODO add more attributes like pending_approval etc
		user.setUsername(username);
		user.setIsShopping(r.getBool("is_shopping"));
		user.setGroupID(r.getUUID("group"));
		user.setMoney(r.getMap("money", String.class, Integer.class));
		user.setPendingProducts(r.getSet("products_to_add", String.class));
		user.setPendingUsers(r.getSet("users_to_add", String.class));
		user.setFavProduct(r.getString("best_prod"));
		user.setFavShop(r.getString("best_shop"));
		user.setAvgShopCost(r.getInt("avg_shop_cost"));
		user.setAvgShopWhen(r.getInt("avg_shop_when"));
		session.close();
		return user;
	}
	
	public static boolean checkApprovedProducts(String username, String product)
	{
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("SELECT products_to_add from users where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username);
		ResultSet rs = session.execute(boundStatement);
		Row r = rs.one();
		Set<String> addedProducts = r.getSet("products_to_add",String.class);
		session.close();
		for(String s : addedProducts)
		{
			if(s.equals(product))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkApprovedUsers(String username, String product)
	{
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("SELECT users_to_add from users where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username);
		ResultSet rs = session.execute(boundStatement);
		Row r = rs.one();
		Set<String> addedUsers = r.getSet("users_to_add",String.class);
		session.close();
		for(String s : addedUsers)
		{
			if(s.equals(product))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkApprovedAddress(String username, String address)
	{
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("SELECT preferred_address from users where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username);
		ResultSet rs = session.execute(boundStatement);
		Row r = rs.one();
		String approvedAddress = r.getString("preferred_address");
		session.close();
		if(approvedAddress != null &&  approvedAddress.equals(address))
		{
			return true;
		}
		return false;
	}
	
	public static boolean removeApprovedAddress(String username, String address)
	{
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("UPDATE users SET preferred_address = ? where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(address, username);
		ResultSet rs = session.execute(boundStatement);
		session.close();
		return false;
	}
	
	public static void removeApprovedProduct(String username, String product)
	{
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("UPDATE users SET products_to_add = products_to_add - {'"+product+"'} where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username);
		ResultSet rs = session.execute(boundStatement);
		session.close();
	}
	
	public static void removeApprovedUser(String username, String approvedUser)
	{
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("UPDATE users SET users_to_add = users_to_add - {'"+approvedUser+"'} where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username);
		ResultSet rs = session.execute(boundStatement);
		session.close();
	}
	
	public static boolean setIsShopping(boolean value,String username)
	{
		try
		{
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("UPDATE users SET is_shopping = ? where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(value, username);
		ResultSet rs = session.execute(boundStatement);
		session.close();
		return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	
	public static Map<String, Integer> getUserBooks(String userName)
	{
		Map<String, Integer> data = new HashMap<String, Integer>();
		// Start session
		Session session = CassandraConnection.getCluster().connect("flat_db");
		// Prepare the Statement
		PreparedStatement statement = session
				.prepare("SELECT money FROM users where user_name=?");
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(userName);
		ResultSet rs = session.execute(boundStatement);
		Row details = rs.one();
		
		return details.getMap("money", String.class, Integer.class);
	
	}
	
}
