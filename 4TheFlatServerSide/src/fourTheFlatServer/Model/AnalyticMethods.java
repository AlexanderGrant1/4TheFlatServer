package fourTheFlatServer.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.Stores.GroupAnalytics;
import fourTheFlatServer.Stores.Product;
import fourTheFlatServer.lib.CassandraConnection;

public class AnalyticMethods {

	public static GroupAnalytics dataForAGroup(UUID groupID) {
		GroupAnalytics ga = new GroupAnalytics();

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		Row r = rs.one();

		ga.setAvgShopCost(r.getInt("avg_shop_cost"));
		ga.setBestShopper(r.getString("best_shopper"));
		ga.setTimeBetweenShops(r.getInt("time_between_shops"));
		ga.setBestStore(r.getString("best_store"));

		return ga;
	}

	public static String calcBestShopper(UUID groupID) {

		Map<Date, String> pastShoppers;

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT last_shop_who from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		Row r = rs.one();

		pastShoppers = r.getMap("last_shop_who", Date.class, String.class);
		String shopper = mostStringMap(pastShoppers);

		PreparedStatement setBestShopper = session
				.prepare("UPDATE user_group SET best_shopper = ? where group_id = ?");

		BoundStatement boundStatement2 = new BoundStatement(setBestShopper);
		boundStatement2.bind(shopper, groupID);
		session.execute(boundStatement2);

		session.close();

		return shopper;
	}

	public static String calcBestStore(UUID groupID) {
		Map<Date, String> pastStores;

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT last_shop_where from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		Row r = rs.one();

		pastStores = r.getMap("last_shop_where", Date.class, String.class);
		String store = mostStringMap(pastStores);

		PreparedStatement setBestStore = session
				.prepare("UPDATE user_group SET best_store = ? where group_id = ?");

		BoundStatement boundStatement2 = new BoundStatement(setBestStore);
		boundStatement2.bind(store, groupID);
		session.execute(boundStatement2);

		session.close();

		return store;
	}

	public static int calcAvgShopWhen(UUID groupID) {
		Map<Date, Integer> pastShoppers;

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT shop_cost from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);
		Row r = rs.one();

		pastShoppers = r.getMap("shop_cost", Date.class, Integer.class);
		int days = averageDateDiff(pastShoppers);

		PreparedStatement setBestStore = session
				.prepare("UPDATE user_group SET time_between_shops = ? where group_id = ?");

		BoundStatement boundStatement2 = new BoundStatement(setBestStore);
		boundStatement2.bind(days, groupID);
		session.execute(boundStatement2);

		session.close();

		return days;

	}

	public static int calcAvgCost(UUID groupID) {
		Map<Date, Integer> pastCost;

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT shop_cost from user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);
		Row r = rs.one();

		pastCost = r.getMap("shop_cost", Date.class, Integer.class);
		int cost = averageCost(pastCost);

		PreparedStatement setAvgCost = session
				.prepare("UPDATE user_group SET avg_shop_cost = ? where group_id = ?");

		BoundStatement boundStatement2 = new BoundStatement(setAvgCost);
		boundStatement2.bind(cost, groupID);
		session.execute(boundStatement2);

		session.close();

		return cost;

	}

	public static void upDateGroupProducts(UUID groupID) {
		LinkedList<Product> prodList = new LinkedList<Product>();

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * from product_list where group_id = ? ALLOW FILTERING");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		if (rs.isExhausted()) {
			System.out.println("No products found");

		} else {

			for (Row row : rs) {
				String prod = row.getString("product_name");
				Map<Date, Integer> pastDetails = new HashMap<Date, Integer>();

				pastDetails = row.getMap("last_bought_cost", Date.class,
						Integer.class);

				int days = averageDateDiff(pastDetails);
				int cost = averageCost(pastDetails);

				System.out.println("PRODUCT: " + prod + "    DAYS: " + days
						+ "      COST: " + cost);
				PreparedStatement updateProds = session
						.prepare("UPDATE product_list SET purchase_frequency = ?, avg_buy_cost = ? where product_name = ?  and group_id = ?");

				BoundStatement boundStatement2 = new BoundStatement(updateProds);
				boundStatement2.bind(days, cost, prod, groupID);
				session.execute(boundStatement2);
			}

		}

		session.close();
	}

	public static String userFavShop(String username) {

		UUID groupID = UserMethods.getGroupIdByUsername(username);

		Group g = GroupMethods.getGroupByUUIDAnalytics(groupID);

		Set<Date> when = whenUserShopped(g.getLastShopWho(), username);

		Map<Date, String> shopWhere = g.getLastShopWhere();
		Map<String, Integer> shopCount = new HashMap<String, Integer>();

		for (Date d : when) {
			String shop = shopWhere.get(d);
			if (shopCount.containsKey(shop)) {
				shopCount.put(shop, shopCount.get(shop) + 1);
			} else {
				shopCount.put(shop, 1);
			}
		}

		int highest = 0;
		String winner = "";
		for (Entry<String, Integer> s : shopCount.entrySet()) {

			if (s.getValue() >= highest) {
				highest = s.getValue();
				winner = s.getKey();
			}

		}

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statment = session
				.prepare("UPDATE users SET best_shop = ? where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statment);
		boundStatement.bind(winner, username);
		session.execute(boundStatement);

		session.close();

		return winner;
	}

	public static int userAvgShopWhen(String username) {
		UUID groupID = UserMethods.getGroupIdByUsername(username);

		Group g = GroupMethods.getGroupByUUIDAnalytics(groupID);

		Set<Date> when = whenUserShopped(g.getLastShopWho(), username);

		DateTime first = null;
		DateTime last = null;

		int i = 0;
		for (Date d : when) {

			DateTime dt = new DateTime(d);
			if (last == null || dt.compareTo(last) < 0) {
				last = dt;
			} else if (first == null || dt.compareTo(first) > 0) {
				first = dt;
			}

			i++;
		}

		int days = 0;
		System.out.println("FIRST: " + first + "      LAST: " + last);
		if (last != null && first != null) {

			days = (Days.daysBetween(last, first).getDays() / i);
		}

		Session session = CassandraConnection.getCluster().connect("flat_db");
		PreparedStatement statment = session
				.prepare("UPDATE users SET avg_shop_when = ? where user_name = ?");

		BoundStatement boundStatement = new BoundStatement(statment);
		boundStatement.bind(days, username);
		session.execute(boundStatement);

		session.close();

		return days;
	}

	public static void userCalcProdHistory(String username) {

		UUID groupID = UserMethods.getGroupIdByUsername(username);

		Group g = GroupMethods.getGroupByUUIDAnalytics(groupID);

		Set<Date> when = whenUserShopped(g.getLastShopWho(), username);

		LinkedList<Product> prods = ProductMethods.getGroupProds(groupID);

		// count how many times a user bought a product
		Map<String, Integer> prodCount = new HashMap<String, Integer>();

		// stores the avg spend on a product by a user
		Map<String, Integer> userSpend = new HashMap<String, Integer>();

		if (prods != null) {
			// for every product bought by the group
			for (Product p : prods) {
				// count how many times the product appears
				int count = 0;
				// store when and for how much the prod was bought
				Map<Date, Integer> prodDeets = p.getLast_bought_cost();
				// stores total amount spent on product by user
				int totalSpend = 0;
				// for every date the user shopped
				for (Date d : when) {
					// if the product was bought on that date
					if (prodDeets.containsKey(d)) {
						count++;
						totalSpend += prodDeets.get(d);
					}
				}
				// store hoe many times the product was bought by the user
				prodCount.put(p.getProduct(), count);
				if (count > 0) {
					// store the average amount the user spent on the prod
					userSpend.put(p.getProduct(), (totalSpend / count));
				}
			}

			System.out.println("COUNT: " + prodCount.toString());

			int highest = 0;
			String winner = "";
			for (Entry<String, Integer> s : prodCount.entrySet()) {

				if (s.getValue() >= highest) {
					highest = s.getValue();
					winner = s.getKey();
				}

			}

			Session session = CassandraConnection.getCluster().connect(
					"flat_db");

			PreparedStatement statment = session
					.prepare("UPDATE users SET best_prod = ? where user_name = ?");

			BoundStatement boundStatement = new BoundStatement(statment);
			boundStatement.bind(winner, username);
			session.execute(boundStatement);

			PreparedStatement storAvgSpend = session
					.prepare("UPDATE users SET avg_spend_prod = ? where user_name = ?");

			BoundStatement boundStatement2 = new BoundStatement(storAvgSpend);
			boundStatement2.bind(userSpend, username);
			session.execute(boundStatement2);

			session.close();

			System.out.println("PRICE: " + userSpend.toString());
		}

	}

	public static int userAvgShop(String username) {
		UUID groupID = UserMethods.getGroupIdByUsername(username);

		Group g = GroupMethods.getGroupByUUIDAnalytics(groupID);

		Set<Date> when = whenUserShopped(g.getLastShopWho(), username);

		if (when != null) {
			Map<Date, Integer> costWhen = g.getShopCost();

			int total = 0;
			int counter = 1;

			for (Date d : when) {
				total += costWhen.get(d);
				counter++;
			}

			int avg = total / counter;

			Session session = CassandraConnection.getCluster().connect(
					"flat_db");

			PreparedStatement statment = session
					.prepare("UPDATE users SET avg_shop_cost = ? where user_name = ?");

			BoundStatement boundStatement = new BoundStatement(statment);
			boundStatement.bind(avg, username);
			session.execute(boundStatement);

			session.close();

			return avg;
		}

		else {
			return 0;
		}
	}

	public static Set<Date> whenUserShopped(Map<Date, String> userShops,
			String username) {
		Set<Date> result = new HashSet<Date>();

		for (Entry<Date, String> s : userShops.entrySet()) {

			if (s.getValue().equals(username)) {
				result.add(s.getKey());
			}

		}

		return result;
	}

	public static Map<String, Integer> occurencesMap(Map<Date, String> map) {
		Map<String, Integer> result = new HashMap<String, Integer>();

		// create a map to count how many times each string appears
		for (Entry<Date, String> mE : map.entrySet()) {

			if (result.containsKey(mE.getValue())) {
				result.put(mE.getValue(), result.get(mE.getValue()) + 1);
			} else {
				result.put(mE.getValue(), 1);
			}

		}
		return result;
	}

	private static String mostStringMap(Map<Date, String> map) {
		Map<String, Integer> result = new HashMap<String, Integer>();

		// create a map to count how many times each string appears
		for (Entry<Date, String> mE : map.entrySet()) {

			if (result.containsKey(mE.getValue())) {
				result.put(mE.getValue(), result.get(mE.getValue()) + 1);
			} else {
				result.put(mE.getValue(), 1);
			}

		}

		String winner = "";
		int count = 0;
		for (Entry<String, Integer> rE : result.entrySet()) {
			int shopperCount = rE.getValue();

			if (shopperCount >= count) {
				winner = rE.getKey();
				count = rE.getValue();
			}
		}

		return winner;
	}

	private static int averageDateDiff(Map<Date, Integer> map) {

		if (map == null) {
			return 0;
		}

		DateTime first = null;
		DateTime last = null;

		int i = 0;
		for (Entry<Date, Integer> mE : map.entrySet()) {

			DateTime dt = new DateTime(mE.getKey());
			if (last == null || dt.compareTo(last) < 0) {
				last = dt;
			} else if (first == null || dt.compareTo(first) > 0) {
				first = dt;
			}

			i++;
		}

		if (last == null || first == null) {
			return 0;
		} else {
			return (Days.daysBetween(last, first).getDays() / i);
		}

	}

	private static int averageCost(Map<Date, Integer> map) {

		int total = 0;
		int i = 0;
		// create a map to count how many times each string appears
		for (Entry<Date, Integer> mE : map.entrySet()) {

			total += mE.getValue();

			i++;
		}

		if (i > 0)
			return total / i;
		else
			return 0;
	}

}
