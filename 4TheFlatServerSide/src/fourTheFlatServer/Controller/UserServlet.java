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
import fourTheFlatServer.Stores.UserReturn;
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
		urlSplit = General.Utils.formatStringArray(urlSplit);
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
		UserReturn userReturn = new UserReturn();
		userReturn.setUsername(user.getUsername());
		userReturn.setGroupID(user.getGroupID());
		String jason = PojoMapper.toJson(userReturn, true);

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
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 5)
		{
			response.getWriter().print("Invalid URL.");
			return;
		}
		String username = urlSplit[3];
		String password = urlSplit[4];
		
		//Check that the username is alphanumeric
		if(!username.matches("^[a-zA-Z0-9_]*$"))
		{
			response.getWriter().print("Username must be alphanumeric.");
			return;
		}
		
		if(AuthenticateUser.registerUser(username, password))
		{
			
			User user = UserMethods.getUserByUsername(username);
			UserReturn userReturn = new UserReturn();
			userReturn.setUsername(user.getUsername());
			userReturn.setGroupID(user.getGroupID());
			String jason = PojoMapper.toJson(userReturn, true);
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
	 *      Changes the user's password
	 *      user/<currentPassword>/<newPassword>
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 6)
		{
			response.getWriter().print("Invalid URL.");
			return;
		}
		String username = urlSplit[3];
		String currentPassword = urlSplit[4];
		String newPassword = urlSplit[5];
		
		//Check that the username and current password provided are correct
		if(AuthenticateUser.validateLoginCredentials(username, currentPassword) == null)
		{
			response.getWriter().print("Incorrect username or password.");
			return;
		}
		//Change the user's password
		if(AuthenticateUser.changePassword(username, newPassword))
		{
			//Print a success message
			response.getWriter().print("Password changed.");
			return;
		}
		//If we get here the password was not changed so display an error message
		response.getWriter().print("An error has occurred.");
		
	}

}
