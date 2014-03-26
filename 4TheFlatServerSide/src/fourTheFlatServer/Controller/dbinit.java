package fourTheFlatServer.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;

import fourTheFlatServer.lib.CassandraConnection;

/**
 * Servlet implementation class dbinit
 */
@WebServlet("/dbinit/*")
public class dbinit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public dbinit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cluster c = CassandraConnection.getCluster();
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
		
		try {
			System.out.println(new java.io.File( "." ).getCanonicalPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        InputStream in = getServletContext().getResourceAsStream("/scripts.txt");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try{
        String text;
        while ((text = reader.readLine()) != null) {
        	System.out.println(text);
			SimpleStatement cqlQuery = new SimpleStatement(text);
			session.execute(cqlQuery);
        }
		response.getWriter().print("DB CREATED");
        }
        catch (Exception e)
        {
        	response.getWriter().print("SOMETHING WENT WRONG..... CHECK THE LOG?");
        }
		session.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
