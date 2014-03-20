package fourTheFlatServer.Model;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.User;
import fourTheFlatServer.lib.CassandraConnection;

public class AuthenticateUser {

	public static User validateLoginCredentials(java.lang.String username, String password) {
		if (!UserMethods.userExists(username)) {
			return null;
		}
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * from users where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username);
		ResultSet rs = session.execute(boundStatement);
		Row r = rs.one();

		if (password.equals(r.getString("password"))) {
			// Set user object attributes
			User user = new User();
			// TODO add more attributes like pending_approval etc
			user.setUsername(username);
			user.setIsShopping(r.getBool("is_shopping"));
			user.setGroupID(r.getUUID("group"));
			user.setMoneyToGet(r.getSet("money_to_get", Integer.class));
			user.setPendingProducts(r.getSet("products_to_add", String.class));
			user.setPendingUsers(r.getSet("users_to_add", String.class));
			session.close();
			return user;
		}
		else
		{
			session.close();
			return null;
		}
	}
	
	public static boolean registerUser(String username, String password) {
		if (UserMethods.userExists(username)) {
			//username already registered so return false
			return false;
		}
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("INSERT INTO users(user_name,password) VALUES(?,?)");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(username,password);
		session.execute(boundStatement);
		session.close();
		return true;
	}
	
	public static boolean changePassword(String username, String newPassword)
	{
		if (!UserMethods.userExists(username)) {
			System.out.println("Username already registered");
			return false;
		}
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("UPDATE users SET password = ? where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(newPassword,username);
		session.execute(boundStatement);
		session.close();
		return true;
	}
}
