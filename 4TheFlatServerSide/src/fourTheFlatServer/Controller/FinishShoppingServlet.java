package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Model.MoneyMethods;
import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class AllowedProducts
 */
@WebServlet("/allowedproducts/*")
public class FinishShoppingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllowedProductsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 *AllowedProducts/<groupUUID>
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		if(urlSplit.length != 5)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String username = urlSplit[3];
		int cost = urlSplit[4];
		//Check that the user exists and is in a group
		if(UserMethods.getGroupIDByUsername(username) == null)
		{
			response.getWriter().print("That username doesn't belong to a group.");
			return;
		}
		//Change the user's password
		if(MoneyMethods.changePassword(username, cost))
		{
			//Print a success message
			response.getWriter().print("Money owed updated.");
			return;
		}
	}

}