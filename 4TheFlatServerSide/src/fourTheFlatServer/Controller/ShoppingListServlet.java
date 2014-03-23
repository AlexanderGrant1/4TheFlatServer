package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.Map;
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
		
		Map<String, Integer> list =  GroupMethods.getShoppingList(groupUUID);
		for(Map.Entry<String, Integer> m : list.entrySet())
		{
			response.getWriter().println(m.getKey());
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
		if(GroupMethods.itemInShoppingList(groupUUID, product))
		{
			response.getWriter().print("Item is already in the shopping list.");
			return;
		}
		if(GroupMethods.addItemToShoppingList(groupUUID, product))
		{
			Map<String, Integer> list =  GroupMethods.getShoppingList(groupUUID);
			for(Map.Entry<String, Integer> m : list.entrySet())
			{
				response.getWriter().println(m.getKey());
			}
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
		
		if(GroupMethods.getGroupByUUID(groupID) == null)
		{
			response.getWriter().print("Group does not exist.");
			return;
		}
		
		if(!GroupMethods.itemInShoppingList(groupID,product))
		{
			response.getWriter().print("Item is not in shopping list.");
			return;
		}
		
		if(GroupMethods.removeItemFromShoppingList(groupID, product))
		{
			Map<String, Integer> list =  GroupMethods.getShoppingList(groupID);
			for(Map.Entry<String, Integer> m : list.entrySet())
			{
				response.getWriter().println(m.getKey());
			}
			return;
		}
		response.getWriter().print("Cannot delete an item that is not in the shopping list");
		return;
	}
}
