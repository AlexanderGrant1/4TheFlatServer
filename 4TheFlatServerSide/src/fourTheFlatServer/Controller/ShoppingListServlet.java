package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.GroupMethods;

/**
 * Servlet implementation class ShoppingList
 */
@WebServlet("/shoppinglist/*")
public class ShoppingListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * ShoppingList/<groupId> returns the shopping list for that group
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		if(urlSplit.length != 4)
		{
			System.out.println("Invalid url");
			return;
		}
		String groupId = urlSplit[3];
		UUID groupUUID = UUID.fromString(groupId);
		//Check that the group exists
		if(GroupMethods.getGroupByUUID(groupUUID) == null)
		{
			return;
		}
		for(String s : GroupMethods.getShoppingList(groupUUID))
		{
			response.getWriter().println(s);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		if(urlSplit.length != 5)
		{
			System.out.println("Invalid url");
			return;
		}
		String groupId = urlSplit[3];
		String product = urlSplit[4];
		UUID groupUUID = UUID.fromString(groupId);
		//Check that the group exists
		if(GroupMethods.getGroupByUUID(groupUUID) == null)
		{
			return;
		}
		if(GroupMethods.addItemToShoppingList(groupUUID, product))
		{
			response.getWriter().print("success");
			return;
		}
		response.getWriter().print("Failure");
				
	}
}
