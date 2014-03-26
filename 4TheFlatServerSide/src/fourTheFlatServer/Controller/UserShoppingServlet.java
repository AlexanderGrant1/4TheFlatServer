package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Model.MoneyMethods;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.MapStore;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class UserShoppingServlet
 */
@WebServlet("/usershopping/*")
public class UserShoppingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final MapStore ShoppingList = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserShoppingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 *usershopping/<username> start shopping session for a user
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 4)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String username = urlSplit[3];
		UUID groupID = UserMethods.getGroupIdByUsername(username);
		
		UserMethods.setIsShopping(true, username);
		GroupMethods.setShopper(username, groupID);
	
		MapStore list = new MapStore();
		list.seMap(GroupMethods.getShoppingList(groupID));
		
		String s = PojoMapper.toJson(list, true);
		response.getWriter().println(s);	
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 *group/<groupID>/<product>/<price> say product has been purchased and for what price
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 6)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		UUID groupID = UUID.fromString(urlSplit[3]);
		String product = urlSplit[4];
		int price = Integer.parseInt(urlSplit[5]);
		
		boolean sucsess = GroupMethods.setProductPrice(groupID, product, price);
		
		response.getWriter().println(sucsess);			
	}


/**
 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse response)
 *usershopping/<username>/<shop name>
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
	String username = urlSplit[3];
	String where = urlSplit[4];
			
	UUID groupID = UserMethods.getGroupIdByUsername(username);
	
	//GET TOTAL SHOP COST 	//REMOVE BOUGHT ITEMS FROM LIST
	int cost = GroupMethods.shopCost(groupID, username, where);
			
	
	if(MoneyMethods.updateMoneyOwed(username, cost))
	{
		//Print a success message
		response.getWriter().print("Money owed updated.");
	}
		
	UserMethods.setIsShopping(false, username);
	GroupMethods.setShopper(null, groupID);
}

}