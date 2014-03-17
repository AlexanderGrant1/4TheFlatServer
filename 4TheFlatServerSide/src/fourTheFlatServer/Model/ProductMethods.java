package fourTheFlatServer.Model;

import java.util.LinkedList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.lib.CassandraConnection;

public class ProductMethods {

	public static boolean productExists(String product) {
		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT * from product_list where product_name = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(product);
		ResultSet rs = session.execute(boundStatement);
		return rs.one() != null;
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
}
