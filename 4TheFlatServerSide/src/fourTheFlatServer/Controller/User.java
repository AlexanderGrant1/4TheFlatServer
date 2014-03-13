package fourTheFlatServer.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.lib.CassandraConnection;

/**
 * Servlet implementation class User
 */
@WebServlet({"/user","/user/*"})
public class User extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Cluster cluster;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public User() {
        super();
       cluster = CassandraConnection.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		String username = urlSplit[3];
		String password = urlSplit[4];
		
		Session session = cluster.connect("flat_db");
		
		PreparedStatement statement = session
				.prepare("SELECT * from users where user_name = '" + username+"'");

		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		
		
		Row r = rs.one();
			
		
		System.out.println(username +" is shopping: "+r.getBool("is_shopping"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
