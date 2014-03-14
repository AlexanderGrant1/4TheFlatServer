package fourTheFlatServer.Model;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.User;
import fourTheFlatServer.lib.CassandraConnection;

public class Authentication {

	public static User validateLoginCredentials(String username, String password) {
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

			user.setMoneyToGet(r.getSet("money_to_get", Integer.class));
			user.setPendingApproval(r.getSet("pending_approval", String.class));
			return user;
		}
		else
		{
			return null;
		}
	}
}
