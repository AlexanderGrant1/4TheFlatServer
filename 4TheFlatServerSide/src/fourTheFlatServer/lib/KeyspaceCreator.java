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

			String[] moreCreates = new String[4];
			String  testData[] = new String[8];

			String dropKeyspace = "Drop keyspace if exists flat_db";
			String createKeyspace = "create keyspace flat_db  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";

			// CREATE USER GROUP TABLE
			moreCreates[0] = "CREATE TABLE if not exists user_group("
					+ "group_id timeuuid," + "users set<varchar>,"
					+ "address varchar," + "allowed_products set<varchar>,"
					+ "shopping_list map<text, int>," + "user_shopping varchar,"
					+ " PRIMARY KEY(group_id));";

			// CREATE USER TABLE
			moreCreates[1] = "CREATE TABLE if not exists users ("
					+ "user_name varchar," + "password varchar,"
					+ "money_to_get set <int>," + "shopping_locations varchar,"
					+ "is_shopping boolean," + "group timeuuid,"
					+ "products_to_add set <varchar>, "
					+ "users_to_add set<varchar>,"
					+"preferred_address varchar,"
					+ "PRIMARY KEY(user_name));";

			// CREATE PRODUTS TABLE
			moreCreates[2] = "CREATE TABLE if not exists product_list("
					+ "product_name varchar," + "group_id timeuuid,"
					+ "last_bought timestamp," + "purchase_frquency int,"
					+ "purchase_date timestamp,"
					+ "PRIMARY KEY((product_name), group_id));";

			moreCreates[3] = "CREATE TABLE user_messages("
							+"user_name varchar,"
							+"message_id timeUUID,"
							+"text varchar,"
							+"type int,"
							+"PRIMARY KEY((user_name), message_id));";
			

			//Create TEST user_group
			testData[0] = "Insert into user_group(group_id, address, allowed_products, shopping_list, users) values (cc4bcc90-ad52-11e3-a13d-74e543b5285b,'123 Test Street', {'Tea', 'Eggs', 'Milk', 'Cheese', 'Toilet Paper'}, {'Eggs':0}, {'test1','test2','test3','test4','test5','test6','test7'});";

			//Create TEST users
			testData[1] = "Insert into users(user_name, group, is_shopping, password) values ('test1',cc4bcc90-ad52-11e3-a13d-74e543b5285b, false, '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08');";
			testData[2] = "Insert into users(user_name, group, is_shopping, password) values ('test2',cc4bcc90-ad52-11e3-a13d-74e543b5285b, false, '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08');";
			testData[3] = "Insert into users(user_name, group, is_shopping, password) values ('test3',cc4bcc90-ad52-11e3-a13d-74e543b5285b, false, '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08');";
			testData[4] = "Insert into users(user_name, group, is_shopping, password) values ('test4',cc4bcc90-ad52-11e3-a13d-74e543b5285b, false, '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08');";
			testData[5] = "Insert into users(user_name, group, is_shopping, password) values ('test5',cc4bcc90-ad52-11e3-a13d-74e543b5285b, false, '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08');";
			testData[6] = "Insert into users(user_name, group, is_shopping, password) values ('test6',cc4bcc90-ad52-11e3-a13d-74e543b5285b, false, '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08');";
			testData[7] = "Insert into users(user_name, group, is_shopping, password) values ('test7',cc4bcc90-ad52-11e3-a13d-74e543b5285b, false, '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08');";


			Session session = c.connect();

			//UPDLOAD AS WAR IN THE FOLLOWING WAY TO CREATE A NEW DATABASE:
					//1. UNCOMMENT dropKeyspace statment, uploaded and run
					//2. COMMENT OUT THE KEYSPACE CREATOR IN "CassandraConnection" (LINE 61) and upload again


			try {

				PreparedStatement statement = session
						.prepare(dropKeyspace);
				BoundStatement boundStatement = new BoundStatement(statement);
				ResultSet rs = session.execute(boundStatement);
				System.out.println("Keyspace droped!");

			} catch (Exception et) {
				System.out.println("Can't drop keyspace " + et);
			}


			try {

				PreparedStatement statement = session
						.prepare(createKeyspace);
				BoundStatement boundStatement = new BoundStatement(statement);
				ResultSet rs = session.execute(boundStatement);
				System.out.println("Keyspace created!");

			} catch (Exception et) {
				System.out.println("Can't create keyspace " + et);
			}


			for (int i = 0; i < 4; i++) {
				// now add some column families
				session = c.connect("flat_db");
				System.out.println("" + moreCreates[i]);

				try {
					SimpleStatement cqlQuery = new SimpleStatement(
							moreCreates[i]);
					session.execute(cqlQuery);
				} catch (Exception et) {
					System.out.println("Can't table: " + et);
				}
				session.close();
			}

			//load in test data
			for (int i = 0; i < 8; i++) {
				session = c.connect("flat_db");

				try {
					SimpleStatement cqlQuery = new SimpleStatement(
							testData[i]);
					session.execute(cqlQuery);
				} catch (Exception et) {
					System.out.println("Can't create test data:  " + et);
				}
				session.close();
			}


	}
}