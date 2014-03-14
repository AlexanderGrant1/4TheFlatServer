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
		try {
			// Add some keyspaces here
			String createkeyspace = "create keyspace if not exists flat_db  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";

			String[] moreCreates = new String[4];

			//CREATE USER GROUP TABLE
			moreCreates[0] = "CREATE TABLE if not exists user_group("
					+ "group_id timeuuid,"
					+ "users set<varchar>,"
					+ "address varchar,"
					+"allowed_products set<varchar>,"
					+"shopping_list set<varchar>,"
					+"user_shopping boolean,"
					+" PRIMARY KEY(group_id));";
			
			//CREATE USER TABLE
			moreCreates[1] = "CREATE TABLE if not exists users ("
					+ "user_name varchar,"
					+ "password varchar,"
					+ "money_to_get set <int>,"
					+ "shopping_locations varchar,"
					+ "is_shopping boolean,"
					+ "pending_approval set <varchar>, "
					+ "PRIMARY KEY(user_name));";

			//CREATE PRODUTS TABLE
			moreCreates[2] = "CREATE TABLE if not exists product_list("
					+ "product_name varchar,"
					+ "group_id timeuuid,"
					+ "last_bought timestamp,"
					+ "purchase_frquency int,"
					+ "purchase_date timestamp,"
					+ "PRIMARY KEY((product_name), group_id));";
			
			
			Session session = c.connect();
			try {
				PreparedStatement statement = session.prepare(createkeyspace);
				BoundStatement boundStatement = new BoundStatement(statement);
				ResultSet rs = session.execute(boundStatement);

			} catch (Exception et) {
				System.out.println("Can't create twitterdb " + et);
			}

			for (int i = 0; i < 3; i++) {
				// now add some column families
				session.close();
				session = c.connect("flat_db");
				System.out.println("" +moreCreates[i]);

				try {
					SimpleStatement cqlQuery = new SimpleStatement(moreCreates[i]);
					session.execute(cqlQuery);
				} catch (Exception et) {
					System.out.println("Can't create tweet table " + et);
				}
				session.close();
			}
			
		} catch (Exception et) {
			System.out.println("Other keyspace or coulm definition error" + et);
		}

	}
}
