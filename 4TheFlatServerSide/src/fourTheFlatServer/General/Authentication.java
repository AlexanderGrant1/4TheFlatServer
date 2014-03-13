package fourTheFlatServer.General;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Model.User;
import fourTheFlatServer.lib.CassandraConnection;

public class Authentication {

	
	public static User validateLoginCredentials(String username, String password)
	{
		if(!UserMethods.userExists(username))
		{
			return null;
		}
		Session session = CassandraConnection.getCluster().connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("SELECT * from users where user_name = '?' AND password='?'");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username,password);
		ResultSet rs = session.execute(boundStatement);
		Row r = rs.one();
		
		// Set user object attributes
		User user = new User();
		//TODO add more attributes like pending_approval etc
		user.setUsername(username);
		user.setIsShopping(r.getBool("is_shopping"));
		return user;
	}
}
