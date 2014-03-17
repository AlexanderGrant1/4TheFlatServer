package fourTheFlatServer.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.AuthenticateUser;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.User;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet({"/register/*"})
public class RegisterUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUserServlet() {
        super();
        // TODO Auto-generated constructor stub
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

		//Check that the username is alphanumeric
		if(username.matches("^.*[^a-zA-Z ].*$"))
		{
			response.getWriter().print("Usernames must be alphanumeric.");
		}
		
		boolean register = AuthenticateUser.registerUser(username, password);
		
		User user = UserMethods.getUserByUsername(username);
		
		if(register)
		{
			String jason = PojoMapper.toJson(user, true);
			response.getWriter().print(jason);
		}
		else
		{
			response.getWriter().print("User name already registered!");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
