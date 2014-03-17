package fourTheFlatServer.lib;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;

public final class KeyspaceCreator {

	public KeyspaceCreator() {

	}

	public static void SetUpKeySpaces(Cluster c) {
		//try {
			// Add some keyspaces here
			String[] moreCreates = new String[4];

			String dropKeyspace = "Drop keyspace if exists flat_db";
			String createKeyspace = "create keyspace flat_db  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";

			// CREATE USER GROUP TABLE
			moreCreates[0] = "CREATE TABLE if not exists user_group("
					+ "group_id timeuuid," + "users set<varchar>,"
					+ "address varchar," + "allowed_products set<varchar>,"
					+ "shopping_list set<varchar>," + "user_shopping boolean,"
					+ " PRIMARY KEY(group_id));";

			// CREATE USER TABLE
			moreCreates[1] = "CREATE TABLE if not exists users ("
					+ "user_name varchar," + "password varchar,"
					+ "money_to_get set <int>," + "shopping_locations varchar,"
					+ "is_shopping boolean," + "group timeuuid,"
					+ "pending_approval set <varchar>, "
					+ "PRIMARY KEY(user_name));";

			// CREATE PRODUTS TABLE
			moreCreates[2] = "CREATE TABLE if not exists product_list("
					+ "product_name varchar," + "group_id timeuuid,"
					+ "last_bought timestamp," + "purchase_frquency int,"
					+ "purchase_date timestamp,"
					+ "PRIMARY KEY((product_name), group_id));";

			Session session = c.connect();

			//UPDLOAD AS WAR IN THE FOLLOWING WAY TO CREATE A NEW DATABASE:
					//1. UNCOMMENT dropKeyspace statment, uploaded and run
					//2. COMMENT OUT dropKeyspace statment, uploaded and run
					//3. COMMENT OUT THE KEYSPACE CREATOR IN "CassandraConnection" (LINE 61) and upload again
			
			/*
			try {

				PreparedStatement statement = session
						.prepare(dropKeyspace);
				BoundStatement boundStatement = new BoundStatement(statement);
				ResultSet rs = session.execute(boundStatement);
				System.out.println("Keyspace droped!");

			} catch (Exception et) {
				System.out.println("Can't drop keyspace " + et);
			}
			*/
			try {

				PreparedStatement statement = session
						.prepare(createKeyspace);
				BoundStatement boundStatement = new BoundStatement(statement);
				ResultSet rs = session.execute(boundStatement);
				System.out.println("Keyspace created!");

			} catch (Exception et) {
				System.out.println("Can't create keyspace " + et);
			}
			
			
			for (int i = 0; i < 3; i++) {
				// now add some column families
				session = c.connect("flat_db");
				System.out.println("" + moreCreates[i]);

				try {
					SimpleStatement cqlQuery = new SimpleStatement(
							moreCreates[i]);
					session.execute(cqlQuery);
				} catch (Exception et) {
					System.out.println("Can't create tweet table " + et);
				}
				session.close();
			}

		//} catch (Exception et) {
			//System.out.println("Other keyspace or coulm definition error" + et);
	    //}

	}
}
