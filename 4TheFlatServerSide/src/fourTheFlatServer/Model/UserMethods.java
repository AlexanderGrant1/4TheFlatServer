 package fourTheFlatServer.Model;

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
		user.setMoneyToGet(r.getSet("money_to_get", Integer.class));
		user.setPendingProducts(r.getSet("products_to_add", String.class));
		user.setPendingUsers(r.getSet("users_to_add", String.class));
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
		for(String s : addedProducts)
		{
			if(s.equals(product))
			{
				return true;
			}
		}
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

}
