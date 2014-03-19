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
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 4)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String groupId = urlSplit[3];
		UUID groupUUID = UUID.fromString(groupId);
		//Check that the group exists
		if(GroupMethods.getGroupByUUID(groupUUID) == null)
		{
			response.getWriter().print("Group does not exist.");
			return;
		}
		for(String s : GroupMethods.getShoppingList(groupUUID))
		{
			response.getWriter().println(s);
		}
	}
	
	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 5)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String groupId = urlSplit[3];
		String product = urlSplit[4];
		UUID groupUUID = UUID.fromString(groupId);
		//Check that the group exists
		if(GroupMethods.getGroupByUUID(groupUUID) == null)
		{
			response.getWriter().print("Group does not exist.");
			return;
		}
		if(GroupMethods.addItemToShoppingList(groupUUID, product))
		{
			response.getWriter().print("success");
			return;
		}
		response.getWriter().print("Failure");
				
	}
	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse response)
	 *shoppinglist/<groupUUID>/product
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 5)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String group = urlSplit[3];
		String product = urlSplit[4];
		UUID groupID = UUID.fromString(group);
		if(GroupMethods.removeItemFromShoppingList(groupID, product))
		{
			for(String s : GroupMethods.getShoppingList(groupID))
			{
				response.getWriter().println(s);
			}
			return;
		}
		response.getWriter().print("Cannot delete an item that is not in the shopping list");
		return;
	}
}
