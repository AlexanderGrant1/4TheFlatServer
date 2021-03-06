package fourTheFlatServer.Model;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import fourTheFlatServer.lib.CassandraConnection;

public class Approvals {

	public static boolean groupUserApproved(String suggestingUser, String userToAdd) {

		try {
			addToSet(suggestingUser, userToAdd, "users_to_add");

			System.out.println("Add user approval successful!");

			return true;
		} catch (Exception e) {
			System.out.println("Add user approval unsuccessful!");
			return false;
		}
	}

	public static boolean allowedProductApproved(String suggestingUser, String productToAdd) {

		try {
			addToSet(suggestingUser, productToAdd, "products_to_add");

			System.out.println("Add product approval successful!");

			return true;
		} catch (Exception e) {
			System.out.println("Add product approval unsuccessful!");
			return false;
		}
	}
	
	
	public static boolean groupAddressApproved(String suggestingUser, String addressToAdd) {

		try {
			try {
				Session session = CassandraConnection.getCluster().connect(
						"flat_db");

				
	System.out.println("UPDATE users SET preferred_address = "+addressToAdd+" where user_name = "+suggestingUser);
				PreparedStatement statement = session.prepare("UPDATE users SET preferred_address = ? where user_name = ?");
				BoundStatement boundStatement = new BoundStatement(statement);
				boundStatement.bind(addressToAdd, suggestingUser);
				session.execute(boundStatement);
				session.close();
				
				System.out.println("Add address approval successful!");
				return true;
			} catch (Exception e) {
				System.out.println("Add address approval unsuccessful!");
				return false;
			}
			} catch (Exception e) {
			System.out.println("Add address approval unsuccessful!");
			return false;
		}
	}
	
	
	private static boolean addToSet(String suggestingUser, String toAdd,
			String setToChange) {

		try {
			Session session = CassandraConnection.getCluster().connect(
					"flat_db");

			PreparedStatement statement = session.prepare("UPDATE users SET "
					+ setToChange + " = " + setToChange + " + {'" + toAdd
					+ "'} where user_name = ?");
			BoundStatement boundStatement = new BoundStatement(statement);
			boundStatement.bind(suggestingUser);
			session.execute(boundStatement);
			session.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
