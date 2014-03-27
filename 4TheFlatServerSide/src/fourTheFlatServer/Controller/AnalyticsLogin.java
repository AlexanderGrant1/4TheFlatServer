package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.General.Cryptography;
import fourTheFlatServer.Model.AuthenticateUser;
import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.User;

/**
 * Servlet implementation class AnalyticsLogin
 */
@WebServlet("/analyticslogin/*")
public class AnalyticsLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnalyticsLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("userPassword");
		password = Cryptography.sha256(password);

		if(username == null || password == null)
		{
			System.out.println(username + password);
			response.sendRedirect(request.getContextPath()+"/analyticslogin.jsp");
			return;
		}
		
		if(AuthenticateUser.validateLoginCredentials(username, password) == null)
		{
			System.out.println("couldn't validate login details");
			response.sendRedirect(request.getContextPath()+"/analyticslogin.jsp");
			return;
		}
				
		User activeUser = UserMethods.getUserByUsername(username);
		
		UUID groupID = UserMethods.getGroupIdByUsername(username);

		request.getSession().setAttribute("activeUser", activeUser);
		response.sendRedirect(request.getContextPath()+"/groupanalytics/");
	}

}
