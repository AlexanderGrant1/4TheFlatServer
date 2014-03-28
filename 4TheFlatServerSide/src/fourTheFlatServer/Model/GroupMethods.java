package fourTheFlatServer.Model;

import java.util.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.Set;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.Stores.GroupReturn;
import fourTheFlatServer.lib.CassandraConnection;

public class GroupMethods {

	public static GroupReturn getGroupByUUID(UUID groupID) {

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		Row details = rs.one();

		if (!details.equals(null)) {

			Set<String> emptySet = new HashSet<String>();
			GroupReturn groupDetails = new GroupReturn();

			groupDetails.setGroupID(details.getUUID("group_id"));
			groupDetails.setAddress(details.getString("address"));
			groupDetails.setAllowedProducts(details.getSet("allowed_products",
					String.class));
			groupDetails.setShoppingList(details.getMap("shopping_list",
					String.class, Integer.class));
			groupDetails.setUsers(details.getSet("users", String.class));

			session.close();
			return groupDetails;
		}
		session.close();
		return null;

	}
	
	public static Group getGroupByUUIDAnalytics(UUID groupID) {

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

			groupDetails
			.setUserShopping(details.getString("user_shopping"));
	groupDetails.setLastShopWhere(details.getMap("last_shop_where",
			Date.class, String.class));
	groupDetails.setLastShopWho(details.getMap("last_shop_who",
			Date.class, String.class));
	groupDetails.setShopCost(details.getMap("shop_cost", Date.class, Integer.class));
	groupDetails.setAddress(details.getString("address"));
	groupDetails.setUsers(details.getSet("users", String.class));
	groupDetails.setUserShopping(details.getString("user_shopping"));
	groupDetails.setAvgShopCost(details.getInt("avg_shop_cost"));
	groupDetails.setBestShopper(details.getString("best_shopper"));
	groupDetails.setTimeBetweenShops(details.getInt("time_between_shops"));
	groupDetails.setBestStore(details.getString("best_store"));
			session.close();
			return groupDetails;
		}
		session.close();
		return null;

	}


	public static boolean userRequestPending(UUID groupID, String username) {
		GroupReturn g = GroupMethods.getGroupByUUID(groupID);
		Set<String> users = g.getUsers();
		Session session = CassandraConnection.getCluster().connect("flat_db");
		for (String user : users) {
			PreparedStatement statement = session
					.prepare("SELECT * FROM user_messages where user_name = ?");

			BoundStatement boundStatement = new BoundStatement(statement);
			boundStatement.bind(user);
			ResultSet rs = session.execute(boundStatement);

			for (Row r : rs) {
				if (r.getInt("type") == 1
						&& r.getString("text").equals(username)) {
					session.close();
					return true;
				}
			}
		}
		session.close();
		return false;
	}

	public static boolean productRequestPending(UUID groupID, String product) {
		GroupReturn g = GroupMethods.getGroupByUUID(groupID);
		Set<String> users = g.getUsers();
		Session session = CassandraConnection.getCluster().connect("flat_db");
		for (String user : users) {
			PreparedStatement statement = session
					.prepare("SELECT * FROM user_messages where user_name = ?");

			BoundStatement boundStatement = new BoundStatement(statement);
			boundStatement.bind(user);
			ResultSet rs = session.execute(boundStatement);

			for (Row r : rs) {
				if (r.getInt("type") == 0
						&& r.getString("text").equals(product)) {
					session.close();
					return true;
				}
			}
		}
		session.close();
		return false;
	}

	public static boolean addressMessagePending(UUID groupID) {
		GroupReturn g = GroupMethods.getGroupByUUID(groupID);
		Set<String> users = g.getUsers();
		Session session = CassandraConnection.getCluster().connect("flat_db");
		for (String user : users) {
			PreparedStatement statement = session
					.prepare("SELECT * FROM user_messages where user_name = ?");

			BoundStatement boundStatement = new BoundStatement(statement);
			boundStatement.bind(user);
			ResultSet rs = session.execute(boundStatement);

			for (Row r : rs) {
				if (r.getInt("type") == 2) {
					session.close();
					return true;
				}
			}
		}
		session.close();
		return false;
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
				.prepare("UPDATE user_group SET shopping_list= shopping_list+ {'"
						+ product + "' : 0} WHERE group_id = ?");

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

	public static GroupReturn createNewGroup(String userName) {

		UUID groupID = java.util.UUID.fromString(new com.eaio.uuid.UUID()
				.toString());
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement createGroupStatment = session
				.prepare("Insert into user_group(group_id) VALUES (?)");

		BoundStatement boundStatement = new BoundStatement(createGroupStatment);
		boundStatement.bind(groupID);
		session.execute(boundStatement);

		addUserToGroup(groupID, userName);

		GroupReturn newGroup = getGroupByUUID(groupID);
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

	public static int shopCost(UUID groupID, String who, String where) {
		int cost = 0;

		Calendar now = Calendar.getInstance();
		long seconds = now.getTimeInMillis();
		Date date = new Date(seconds);
		Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tStamp = format.format(date).toString();

		Map<String, Integer> list = getShoppingList(groupID);

		for (Map.Entry<String, Integer> m : list.entrySet()) {
			if (m.getValue() != 0) {
				cost += m.getValue();
				removeItemFromShoppingList(groupID, m.getKey());
				updateProductGroupDetails(groupID, m.getKey(), m.getValue(),
						tStamp);
			}
		}

		Session session = CassandraConnection.getCluster().connect("flat_db");

		// ,last_shop_where = last_shop_where +{'2014-01-01':'Dundee Riverside
		// Extra'

		PreparedStatement statment = session
				.prepare("Update user_group SET shop_cost = shop_cost + {'"
						+ tStamp + "':" + cost + "}, "
						+ "last_shop_who = last_shop_who + {'" + tStamp + "':'"
						+ who + "'}," + "last_shop_where = last_shop_where +{'"
						+ tStamp + "':'" + where + "'}"
						+ " where group_id = ?;");
		BoundStatement boundStatement = new BoundStatement(statment);
		boundStatement.bind(groupID);
		session.execute(boundStatement);

		return cost;
	}

	public static void updateProductGroupDetails(UUID groupID, String prod,
			int price, String tStamp) {

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statment = session
				.prepare("Update product_list SET last_bought_cost = last_bought_cost + {'"
						+ tStamp
						+ "':"
						+ price
						+ "} where product_name = ? and group_id = ?;");
		BoundStatement boundStatement = new BoundStatement(statment);
		boundStatement.bind(prod, groupID);
		session.execute(boundStatement);
		session.close();

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

				groupDetails
						.setUserShopping(row.getString("user_shopping"));
				groupDetails.setLastShopWhere(row.getMap("last_shop_where",
						Date.class, String.class));
				groupDetails.setLastShopWho(row.getMap("last_shop_who",
						Date.class, String.class));
				groupDetails.setShopCost(row.getMap("shop_cost", Date.class, Integer.class));
				groupDetails.setAddress(row.getString("address"));
				groupDetails.setUsers(row.getSet("users", String.class));
				groupDetails.setUserShopping(row.getString("user_shopping"));
				groupDetails.setAvgShopCost(row.getInt("avg_shop_cost"));
				groupDetails.setBestShopper(row.getString("best_shopper"));
				groupDetails.setTimeBetweenShops(row
						.getInt("time_between_shops"));
				groupDetails.setBestStore(row.getString("best_store"));
				groupList.add(groupDetails);
			}

		}

		session.close();
		return groupList;
	}

}
