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
		return rs.one() != null;
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

		return r.getUUID("group");
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
		
		user.setMoneyToGet(r.getSet("money_to_get", Integer.class));
		user.setPendingApproval(r.getSet("pending_approval", String.class));

		return user;
	}

}