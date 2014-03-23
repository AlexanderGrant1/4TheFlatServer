package fourTheFlatServer.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 4)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String username = urlSplit[3];
		
		MapStore ms = new MapStore();
		
		ms.seMap(UserMethods.getUserBooks(username));
		
		String books = PojoMapper.toJson(ms, true);
		
		response.getWriter().print(books);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * money/<who is receiving>/<who is giving>
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 5)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String reciver = urlSplit[3];
		String giver = urlSplit[4];
		
		
		MoneyMethods.clearDebt(reciver, giver);
	}

}
