package fourTheFlatServer.Model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.Stores.User;
import fourTheFlatServer.lib.CassandraConnection;

public class GetAllData {

	public static LinkedList<Group> getAllGroups() {

		LinkedList<Group> groupList = new LinkedList<Group>();

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * from user_group");

		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);

		if (rs.isExhausted()) {
			System.out.println("No user_groups found");

			session.close();

			return null;
		} else {

			for (Row row : rs) {
				Set<String> emptySet = new HashSet<String>();
				Group groupDetails = new Group();

				groupDetails.setGroupID(row.getUUID("group_id"));
				// groupDetails.setAddress(row.getString("address"));

				try {
					groupDetails.setAllowedProducts(row.getSet(
							"allowed_products", String.class));
				} catch (java.lang.NullPointerException e) {
					groupDetails.setAllowedProducts(emptySet);
				}

				try {
					groupDetails.setShoppingList(row.getSet("shopping_list",
							String.class));
				} catch (java.lang.NullPointerException e) {
					groupDetails.setShoppingList(emptySet);
				}

				groupDetails.setUsers(row.getSet("users", String.class));
				groupDetails.setUserShopping(row.getBool("user_shopping"));

				groupList.add(groupDetails);
			}

		}

		session.close();
		return groupList;
	}

	public static LinkedList<User> getAllUsers() {

		LinkedList<User> userList = new LinkedList<User>();

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * from users");

		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);

		if (rs.isExhausted()) {
			System.out.println("No user_groups found");

			session.close();

			return null;
		} else {

			for (Row row : rs) {	
				// Set user object attributes
				User user = new User();
				//TODO add more attributes like pending_approval etc
				user.setUsername(row.getString("user_name"));
				

		    	System.out.println("MODEL UN: "+user.getUsername());
		    	
				user.setIsShopping(row.getBool("is_shopping"));
				user.setGroupID(row.getUUID("group"));
				user.setMoneyToGet(row.getSet("money_to_get", Integer.class));
				user.setPendingApproval(row.getSet("pending_approval", String.class));
				
				userList.add(user);
			}

		}

		session.close();
		
		return userList;
	}

}