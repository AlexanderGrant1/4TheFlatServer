package fourTheFlatServer.Model;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.Set;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.lib.CassandraConnection;

public class GroupMethods {

	public static Group getGroupByUUID(UUID groupID) {

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		Row details = rs.one();

		if (!details.equals(null)) {

			Set<String> emptySet = new HashSet<String>();
			Group groupDetails = new Group();

			groupDetails.setGroupID(details.getUUID("group_id"));
			groupDetails.setAddress(details.getString("address"));
			groupDetails.setAllowedProducts(details.getSet("allowed_products",
					String.class));
			groupDetails.setShoppingList(details.getMap("shopping_list",
					String.class, Integer.class));
			groupDetails.setUsers(details.getSet("users", String.class));
			groupDetails.setUserShopping(details.getString("user_shopping"));
			session.close();
			return groupDetails;
		}
		session.close();
		return null;

	}

	public static Set<String> getGroupUsers(UUID groupID) {
		Set<String> users;

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT users  FROM user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		Row set = rs.one();

		users = set.getSet("users", String.class);

		session.close();

		return users;
	}

	public static void addAllowedProduct(UUID groupID, String product) {

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("UPDATE user_group SET allowed_products = allowed_products + {'"
						+ product + "'} where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);
	}

	public static Set<String> getAllowedProducts(UUID groupID) {

		if (getGroupByUUID(groupID) == null) {
			return null;
		}

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT allowed_products from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		Row details = rs.one();

		if (!details.equals(null)) {
			Set<String> allowedProducts;
			allowedProducts = details.getSet("allowed_products", String.class);
			session.close();
			return allowedProducts;
		}
		session.close();
		return null;

	}

	public static Map<String, Integer> getShoppingList(UUID groupID) {

		if (getGroupByUUID(groupID) == null) {
			return null;
		}

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT shopping_list from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		Row details = rs.one();

		if (!details.equals(null)) {
			Map<String, Integer> shoppingList;
			shoppingList = details.getMap("shopping_list", String.class,
					Integer.class);
			session.close();
			return shoppingList;
		}
		session.close();
		return null;
	}

	public static boolean setShopper(String username, UUID groupID) {
		try {
			Session session = CassandraConnection.getCluster().connect(
					"flat_db");

			PreparedStatement statement = session
					.prepare("UPDATE user_group SET user_shopping = ? where group_id = ?");

			BoundStatement boundStatement = new BoundStatement(statement);
			boundStatement.bind(username, groupID);
			session.execute(boundStatement);
			session.close();
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public static boolean addItemToShoppingList(UUID groupID, String product) {

		
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("UPDATE user_group SET shopping_list= shopping_list+ {'"+product+"' : 0} WHERE group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		session.execute(boundStatement);
		session.close();
		return true;

	}

	public static boolean removeItemFromShoppingList(UUID groupID,
			String product) {

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("DELETE shopping_list[?] FROM user_group WHERE group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(product, groupID);
		session.execute(boundStatement);
		session.close();
		return true;
	}

	public static boolean itemInShoppingList(UUID groupID, String product) {
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * FROM user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		Row r = rs.one();

		Map<String, Integer> list = r.getMap("shopping_list", String.class,
				Integer.class);

		for (Map.Entry<String, Integer> m : list.entrySet()) {
			if (product.equals(m.getKey())) {
				session.close();
				return true;
			}
		}

		session.close();
		return false;

	}

	public static Group createNewGroup(String userName) {

		UUID groupID = java.util.UUID.fromString(new com.eaio.uuid.UUID()
				.toString());
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement createGroupStatment = session
				.prepare("Insert into user_group(group_id, user_shopping) VALUES (?, false)");

		BoundStatement boundStatement = new BoundStatement(createGroupStatment);
		boundStatement.bind(groupID);
		session.execute(boundStatement);

		addUserToGroup(groupID, userName);

		Group newGroup = getGroupByUUID(groupID);
		session.close();
		return newGroup;
	}

	public static boolean addUserToGroup(UUID group, String userName) {

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement addUserToGroup = session
				.prepare("UPDATE user_group SET users = users + {'" + userName
						+ "'} where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(addUserToGroup);
		boundStatement.bind(group);
		session.execute(boundStatement);

		PreparedStatement addGroupToUser = session
				.prepare("UPDATE users SET group = ? where user_name = ?");

		BoundStatement boundStatement2 = new BoundStatement(addGroupToUser);
		boundStatement2.bind(group, userName);
		session.execute(boundStatement2);
		session.close();
		return true;

	}

	public static boolean changeGroupAddress(UUID groupID, String newAddress) {
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement addUserToGroup = session
				.prepare("UPDATE user_group SET address = ?  where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(addUserToGroup);
		boundStatement.bind(newAddress, groupID);
		session.execute(boundStatement);
		session.close();

		return true;
	}

	public static boolean removeUserFromGroup(String userName, UUID group) {

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement addUserToGroup = session
				.prepare("UPDATE user_group SET users = users - {'" + userName
						+ "'} where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(addUserToGroup);
		boundStatement.bind(group);
		session.execute(boundStatement);

		PreparedStatement addGroupToUser = session
				.prepare("UPDATE users SET group = null where user_name = ?");

		BoundStatement boundStatement2 = new BoundStatement(addGroupToUser);
		boundStatement2.bind(userName);
		session.execute(boundStatement2);
		session.close();
		return true;

	}

	public static int shopCost(UUID groupID) {
		int cost = 0;

		Map<String, Integer> list = getShoppingList(groupID);

		for (Map.Entry<String, Integer> m : list.entrySet()) {
			if (m.getValue() != 0) {
				cost += m.getValue();
				removeItemFromShoppingList(groupID, m.getKey());
			}
		}

		return cost;
	}

	public static boolean setProductPrice(UUID groupID, String prod, int price) {

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statment = session
				.prepare("UPDATE user_group SET shopping_list[?] = ? WHERE group_id = ?");
		BoundStatement boundStatement = new BoundStatement(statment);
		boundStatement.bind(prod, price, groupID);
		session.execute(boundStatement);
		session.close();

		return true;
	}

}
