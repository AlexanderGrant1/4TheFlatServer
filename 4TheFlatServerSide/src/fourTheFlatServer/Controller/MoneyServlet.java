package fourTheFlatServer.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.Authentication;
import fourTheFlatServer.Model.MoneyMethods;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.MapStore;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class MoneyServlet
 */
@WebServlet("/money/*")
public class MoneyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoneyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = fourTheFlatServer.General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 5)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String username = urlSplit[3];
		String password = urlSplit[4];
		
		if(Authentication.validateLoginCredentials(username, password) != null)
		{
			response.getWriter().print("Invalid username or password.");
			return;
		}
		
		MapStore ms = new MapStore();
		
		ms.seMap(UserMethods.getUserBooks(username));
		
		String books = PojoMapper.toJson(ms, true);
		
		response.getWriter().print(books);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * money/<who is receiving>/<who is giving>/<giverPassword>
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = fourTheFlatServer.General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 6)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String receiver = urlSplit[3];
		String giver = urlSplit[4];
		String giverPassword = urlSplit[5];
		
		
		MoneyMethods.clearDebt(receiver, giver);
	}

}
