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

import fourTheFlatServer.Model.Authentication;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.User;
import fourTheFlatServer.lib.CassandraConnection;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class User
 */
@WebServlet({"/user","/user/*"})
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		if(urlSplit.length != 5)
		{
			System.out.println("Invalid url");
			return;
		}
		String username = urlSplit[3];
		String password = urlSplit[4];
		
		User user = Authentication.validateLoginCredentials(username,password);
		
		if(user == null)
		{
			System.out.println("Incorrect username and password combination.");
			return;
		}
		
		String jason = PojoMapper.toJson(user, true);
		
		response.getWriter().print(jason);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
