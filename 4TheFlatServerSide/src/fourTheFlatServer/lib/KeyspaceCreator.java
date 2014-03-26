package fourTheFlatServer.lib;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public final class KeyspaceCreator {

	public KeyspaceCreator() {

	}

	public static void SetUpKeySpaces(Cluster c) {

		String dropKeyspace = "Drop keyspace if exists flat_db";
		String createKeyspace = "create keyspace flat_db  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";

		Session session = c.connect();

		// UPDLOAD AS WAR IN THE FOLLOWING WAY TO CREATE A NEW DATABASE:
		// 1. UNCOMMENT dropKeyspace statment, uploaded and run
		// 2. COMMENT OUT THE KEYSPACE CREATOR IN "CassandraConnection" (LINE
		// 61) and upload again

		try {

			PreparedStatement statement = session.prepare(dropKeyspace);
			BoundStatement boundStatement = new BoundStatement(statement);
			session.execute(boundStatement);
			System.out.println("Keyspace droped!");

		} catch (Exception et) {
			System.out.println("Can't drop keyspace " + et);
		}

		try {

			PreparedStatement statement = session.prepare(createKeyspace);
			BoundStatement boundStatement = new BoundStatement(statement);
			session.execute(boundStatement);
			System.out.println("Keyspace created!");

		} catch (Exception et) {
			System.out.println("Can't create keyspace " + et);
		}

		session = c.connect("flat_db");
//TODO LOAD IN DATABASE CREATES AND TEST DATA
		try {
			System.out.println(new java.io.File( "." ).getCanonicalPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		session.close();
	}

}