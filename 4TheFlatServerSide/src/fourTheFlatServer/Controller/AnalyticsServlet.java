package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.AnalyticMethods;
import fourTheFlatServer.Stores.GroupAnalytics;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class AnalyticsServlet
 */
@WebServlet("/analytics/*")
public class AnalyticsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnalyticsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 *analytics/<groupId> returns the analytical data for that group
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = fourTheFlatServer.General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 4)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String groupId = urlSplit[3];
		
		response.getWriter().println("SHOP: "+AnalyticMethods.userFavShop(groupId));
		response.getWriter().println("COST: "+AnalyticMethods.userAvgShop(groupId));
		response.getWriter().println("DAYS: "+AnalyticMethods.userAvgShopWhen(groupId));
		//UUID groupID = UUID.fromString(groupId);
		
		/*
		GroupAnalytics ga = AnalyticMethods.dataForAGroup(groupID);
		
		String s = PojoMapper.toJson(ga, true);
		
		response.getWriter().println(s);
		*/
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
