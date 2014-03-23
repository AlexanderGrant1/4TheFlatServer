package fourTheFlatServer.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.Stores.Message;
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
				Map<String, Integer> emptyMap = new HashMap<String, Integer>();
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
					groupDetails.setShoppingList(row.getMap("shopping_list",
							String.class, Integer.class));
				} catch (java.lang.NullPointerException e) {
					groupDetails.setShoppingList(emptyMap);
				}

				groupDetails.setAddress(row.getString("address"));
				groupDetails.setUsers(row.getSet("users", String.class));
				groupDetails.setUserShopping(row.getString("user_shopping"));

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
				user.setIsShopping(row.getBool("is_shopping"));
				user.setGroupID(row.getUUID("group"));
				user.setMoneyToGet(row.getSet("money_to_get", Integer.class));
				user.setPendingProducts(row.getSet("products_to_add", String.class));
				user.setPendingUsers(row.getSet("users_to_add", String.class));
				user.setPreferedAddress(row.getString("preferred_address"));
				userList.add(user);
			}

		}

		session.close();

		return userList;
	}

	public static LinkedList<Message> getAllMessages()
	{

			LinkedList<Message> allM = new LinkedList<Message>();

			Session session = CassandraConnection.getCluster().connect("flat_db");

			PreparedStatement statement = session
					.prepare("SELECT * FROM user_messages");

			BoundStatement boundStatement = new BoundStatement(statement);
			ResultSet rs = session.execute(boundStatement);

			session.close();

			for(Row r : rs)
			{
				Message m = new Message();
				m.setMessageID(r.getUUID("message_id"));
				m.setMessage(r.getString("text"));
				m.setReceiver(r.getString("user_name"));
				m.setType(r.getInt("type"));

				allM.add(m);
			}

			return allM;
	}

}