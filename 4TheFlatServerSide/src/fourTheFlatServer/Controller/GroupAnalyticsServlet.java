package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.General.ValueComparator;
import fourTheFlatServer.Model.AnalyticMethods;
import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Model.ProductMethods;
import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.Stores.GroupAnalytics;
import fourTheFlatServer.Stores.Product;
import fourTheFlatServer.Stores.User;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class AnalyticsServlet
 */
@WebServlet("/groupanalytics/*")
public class GroupAnalyticsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupAnalyticsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 *analytics/<groupId> returns the analytical data for that group
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		User activeUser = (User)request.getSession().getAttribute("activeUser");
		
		if(activeUser == null && !"XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
		{
			response.sendRedirect(request.getContextPath()+"/analyticslogin.jsp");
			return;
		}
		
		UUID groupID = activeUser.getGroupID();
		
		if(groupID == null && !"XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
		{
			request.setAttribute("errorMessage", "You are not part of a group.");
			request.getRequestDispatcher(request.getContextPath()+"/analyticslogin.jsp");
			return;
		}
		Group group = GroupMethods.getGroupByUUIDAnalytics(groupID);
		GroupAnalytics ga = AnalyticMethods.dataForAGroup(groupID);
		LinkedList<Product> productList = ProductMethods.getGroupProds(groupID);
		Map<String, Integer> shopMap = AnalyticMethods.occurencesMap(group.getLastShopWhere());
		Map<String, Integer> userMap = AnalyticMethods.occurencesMap(group.getLastShopWho());
		int averageShopPrice = AnalyticMethods.calcAvgCost(groupID);
		int averageTimeBetween = AnalyticMethods.calcAvgShopWhen(groupID);
		
		ValueComparator bvc =  new ValueComparator(shopMap);
		
        TreeMap<String,Integer> sorted_shop_map = new TreeMap<String,Integer>(bvc);
        sorted_shop_map.putAll(shopMap);
        ValueComparator bvc2 =  new ValueComparator(userMap);
        TreeMap<String,Integer> sorted_user_map = new TreeMap<String,Integer>(bvc2);
        sorted_user_map.putAll(userMap);
        
        Collections.sort(productList, new Comparator<Product>(){
        	   @Override
        	   public int compare(Product o1, Product o2){
        	        if(o1.getAvgCost() > o2.getAvgCost()){
        	           return -1; 
        	        }
        	        if(o1.getAvgCost() < o2.getAvgCost()){
        	           return 1; 
        	        }
        	        return 0;
        	   }
        	}); 
		
		request.setAttribute("user", request.getSession().getAttribute("activeUser"));
		request.setAttribute("averageTimeBetween", averageTimeBetween);
		request.setAttribute("averageShopPrice", averageShopPrice);
		request.setAttribute("userShopAnalytics", sorted_user_map);
		request.setAttribute("shopAnalytics", sorted_shop_map);
		request.setAttribute("groupAnalytics", ga);
		request.setAttribute("groupProducts", productList);
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			if(activeUser != null)
			{
				request.getRequestDispatcher("/analyticstable.jsp").forward(request, response);	
			}
			else
			{
				request.getRequestDispatcher("/notloggedin.jsp").forward(request, response);	
			}
		}
		else
		{
			request.getRequestDispatcher("/analytics.jsp").forward(request, response);
		
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 *analytics/<groupId> calculate and returns the analytical data for that group
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = fourTheFlatServer.General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 4)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String groupId = urlSplit[3];
		UUID groupID = UUID.fromString(groupId);
		
		GroupAnalytics ga = new GroupAnalytics();
		
		
		//BEST SHOPPER
		ga.setBestShopper(AnalyticMethods.calcBestShopper(groupID));
		ga.setBestStore(AnalyticMethods.calcBestStore(groupID));
		ga.setTimeBetweenShops(AnalyticMethods.calcAvgShopWhen(groupID));
		ga.setAvgShopCost(AnalyticMethods.calcAvgCost(groupID));
				
		
		AnalyticMethods.upDateGroupProducts(groupID);
		
		String s = PojoMapper.toJson(ga, true);
		
		response.getWriter().println(s);

		
	}

}
