package fourTheFlatServer.Controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

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

import fourTheFlatServer.Model.AuthenticateUser;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.User;
import fourTheFlatServer.lib.CassandraConnection;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class User
 */
@WebServlet({ "/user", "/user/*" })
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		if (urlSplit.length != 5) {
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String username = urlSplit[3];
		String password = urlSplit[4];

		User user = AuthenticateUser.validateLoginCredentials(username,password);

		if (user == null) {
			response.getWriter().print("Invalid username or password.");
			return;
		}

		String jason = PojoMapper.toJson(user, true);

		 response.setContentType("application/json");
		response.getWriter().print(jason);

	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 *      Registers a user
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		if(urlSplit.length != 5)
		{
			response.getWriter().print("Invalid URL.");
			return;
		}
		String username = urlSplit[3];
		String password = urlSplit[4];

		response.getWriter().println(username);
		
		//Check that the username is alphanumeric
		if(!username.matches("^[a-zA-Z0-9_]*$"))
		{
			response.getWriter().print("Username must be alphanumeric.");
			return;
		}
		
		if(AuthenticateUser.registerUser(username, password))
		{
			User user = UserMethods.getUserByUsername(username);
			String jason = PojoMapper.toJson(user, true);
			response.getWriter().print(jason);
		}
		else
		{
			response.getWriter().print("Username already registered.");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().print(request.getHeader("hello"));
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().print("hello");
	}

}
