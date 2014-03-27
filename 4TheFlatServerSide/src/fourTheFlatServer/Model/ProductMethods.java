package fourTheFlatServer.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.Product;
import fourTheFlatServer.lib.CassandraConnection;

public class ProductMethods {

	public static boolean productExists(String product) {
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * from product_list where product_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(product);
		ResultSet rs = session.execute(boundStatement);
		Row r = rs.one();
		session.close();
		return r != null;
	}

	public static LinkedList<String> getAllProdNames() {
		LinkedList<String> prodNames = new LinkedList<String>();

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT product_name from product_list");

		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);

		if (rs.isExhausted()) {
			System.out.println("No Products found");

		} else {

			for (Row row : rs) {
			prodNames.add(row.getString("product_name"));
			}
		}
		
		session.close();
		return prodNames;
	}
	
	
	
	public static LinkedList<Product> getAllProds(UUID groupID) {

		LinkedList<Product> prodList = new LinkedList<Product>();

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * from product_list where group_id = ? ALLOW FILTERING");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		if (rs.isExhausted()) {
			System.out.println("No products found");

			session.close();

			return null;
		} else {

			for (Row row : rs) {	
				Map<Date, Integer> emptyMap = new HashMap<Date, Integer>();
				Product rowDetails = new Product();

				rowDetails.setGroupID(row.getUUID("group_id"));

				try {
					rowDetails.setLast_bought_cost(row.getMap("last_bought_cost", Date.class, Integer.class));
				} catch (java.lang.NullPointerException e) {
					rowDetails.setLast_bought_cost(emptyMap);
				}

				//purchase_frequency int,avg_buy_cost int
				rowDetails.setProduct(row.getString("product_name"));
				rowDetails.setPurchaseFrequency(row.getInt("purchase_frequency"));
				rowDetails.setAvgCost(row.getInt("avg_buy_cost"));
				prodList.add(rowDetails);
			}

		}

		session.close();
		return prodList;
	}
	
}
