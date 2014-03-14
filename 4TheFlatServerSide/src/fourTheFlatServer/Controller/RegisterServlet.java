package fourTheFlatServer.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.Authentication;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.User;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet({"/register/*"})
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
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

		
		boolean register = Authentication.registerUser(username, password);
		
		User user = UserMethods.getUserByUsername(username);
		
		if(user != null)
		{
			String jason = PojoMapper.toJson(user, true);
			response.getWriter().print(jason);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
