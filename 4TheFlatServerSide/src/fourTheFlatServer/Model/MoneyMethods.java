package fourTheFlatServer.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

public class MoneyMethods {

	public static boolean updateMoneyOwed(String shopper, int cost) {
		// Get groupid
		UUID groupID = UserMethods.getGroupIdByUsername(shopper);
		// Get all users in that group
		Set<String> group = GroupMethods.getGroupUsers(groupID);
		// Split the cost by number of users
		Float indCost = (float) (cost / group.size());
		System.out.println("individual cost: " + indCost);
		// Prepare and execute statements for each remaining member in the group
		for (String u : group) {

			if (!u.equals(shopper)) {
				// Get current amount owed by that user to user who did shopping
				Integer currentMoney = getCurrentMoneyOwed(u, shopper);

				System.out.println("Current money: " + currentMoney);
				if (currentMoney == null) {
					currentMoney = 0;
				}

				// Money now owed by that user to the shopper
				int moneyOwedNow = (int) (currentMoney + indCost);
				// Money owed by the shopper to that user
				int reverseMoney = moneyOwedNow * -1;
				// Start session
				Session session = CassandraConnection.getCluster().connect(
						"flat_db");
				// Prepare the Statements - 1 to update the current non shopper
				// and the other the shopper
				PreparedStatement statement1 = session
						.prepare("UPDATE Users SET money['" + shopper + "']="
								+ moneyOwedNow + " WHERE user_name=?");
				BoundStatement boundStatement1 = new BoundStatement(statement1);
				boundStatement1.bind(u);
				session.execute(boundStatement1);
				PreparedStatement statement2 = session
						.prepare("UPDATE Users SET money['" + u + "']="
								+ reverseMoney + " WHERE user_name=?");
				BoundStatement boundStatement2 = new BoundStatement(statement2);
				boundStatement2.bind(shopper);
				session.execute(boundStatement2);
				session.close();
			}
		}
		return true;
	}

	public static int getCurrentMoneyOwed(String user, String shopper) {

		int value = 0;
		Map<String, Integer> data = new HashMap<String, Integer>();
		// Start session
		Session session = CassandraConnection.getCluster().connect("flat_db");
		// Prepare the Statement
		PreparedStatement statement = session
				.prepare("SELECT money FROM users where user_name=?");
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(user);
		ResultSet rs = session.execute(boundStatement);
		Row details = rs.one();
		if (!rs.isExhausted()) {
			System.out.println("GOT IN HIZZAH: " + rs.toString());
		}
		if (!details.equals(null)) {
			data = details.getMap("money", String.class, Integer.class);
			for (Map.Entry<String, Integer> m : data.entrySet()) {

				if (m.getKey().equals(shopper)) {
					value = m.getValue();
				}

			}

		}
		session.close();
		return value;
	}

	public static void clearDebt(String reciver, String giver) {
//System.out.println("reciver: "+reciver+"     giver: "+giver);
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement1 = session
				.prepare("UPDATE Users SET money['" + giver + "']= 0 WHERE user_name=?");
		BoundStatement boundStatement1 = new BoundStatement(statement1);
		boundStatement1.bind(reciver);
		
		session.execute(boundStatement1);
		
		PreparedStatement statement2 = session
				.prepare("UPDATE Users SET money['" + reciver + "']= 0 WHERE user_name=?");
		BoundStatement boundStatement2 = new BoundStatement(statement2);
		boundStatement2.bind(giver);
		
		session.execute(boundStatement2);
		
		session.close();
	}
}
