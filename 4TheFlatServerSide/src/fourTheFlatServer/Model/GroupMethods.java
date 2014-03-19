package fourTheFlatServer.Model;

import java.util.HashSet;
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


		if(!details.equals(null))
		{

			Set<String> emptySet = new HashSet<String>();
			Group groupDetails = new Group();

			groupDetails.setGroupID(details.getUUID("group_id"));
			 groupDetails.setAddress(details.getString("address"));
				groupDetails.setAllowedProducts(details.getSet(
						"allowed_products", String.class));
				groupDetails.setShoppingList(details.getSet("shopping_list",
						String.class));
			groupDetails.setUsers(details.getSet("users", String.class));
			groupDetails.setUserShopping(details.getBool("user_shopping"));
			session.close();
			return groupDetails;
		}
		session.close();
		return null;

	}
	
	public static Set<String> getAllowedProducts(UUID groupID) {

		if(getGroupByUUID(groupID) == null)
		{
			return null;
		}
		
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT allowed_products from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);


		Row details = rs.one();


		if(!details.equals(null))
		{
			Set<String> allowedProducts;
			allowedProducts = details.getSet("allowed_products", String.class);
			session.close();
			return allowedProducts;
		}
		session.close();
		return null;

	}
	
	public static Set<String> getShoppingList(UUID groupID) {

		if(getGroupByUUID(groupID) == null)
		{
			return null;
		}
		
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT shopping_list from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);


		Row details = rs.one();


		if(!details.equals(null))
		{
			Set<String> shoppingList;
			shoppingList = details.getSet("shopping_list", String.class);
			session.close();
			return shoppingList;
		}
		session.close();
		return null;
	}
	
	public static boolean addItemToShoppingList(UUID groupID, String product) {

		if(getGroupByUUID(groupID) == null)
		{
			return false;
		}
		
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("UPDATE user_group SET shopping_list = shopping_list + {'"+product+"'} where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);


		Row details = rs.one();
		if(details == null)
		{
			session.close();
			return false;
		}
		session.close();
		return true;

	}
	
	public static Group createNewGroup(String userName) {

		
		UUID groupID = java.util.UUID.fromString(new com.eaio.uuid.UUID().toString());
System.out.println("GENERATED UUID: "+groupID);		
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement createGroupStatment = session
				.prepare("Insert into user_group(group_id, user_shopping) VALUES (?, false)");
		
		BoundStatement boundStatement = new BoundStatement(createGroupStatment);
		boundStatement.bind(groupID);
		session.execute(boundStatement);
		
		addUserToGroup(userName, groupID);
	
		Group newGroup = getGroupByUUID(groupID);		
		session.close();
		return newGroup;
	}

	public static boolean addUserToGroup(String userName, UUID group) {

		if(UserMethods.getGroupIdByUsername(userName) != null)
		{
			return false;
		}
		
		Session session = CassandraConnection.getCluster().connect("flat_db");
	
		PreparedStatement addUserToGroup = session
				.prepare("UPDATE user_group SET users = users + {'"+userName+"'} where group_id = ?");
		
		BoundStatement boundStatement = new BoundStatement(addUserToGroup);
		boundStatement.bind(group);
		session.execute(boundStatement);
		
		PreparedStatement addGroupToUser = session.prepare("UPDATE users SET group = ? where user_name = ?");
		
		BoundStatement boundStatement2 = new BoundStatement(addGroupToUser);
		boundStatement2.bind(group, userName);
		session.execute(boundStatement2);		
		session.close();
		return true;
		
	}

}
