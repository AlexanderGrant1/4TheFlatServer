package fourTheFlatServer.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.AddApprovalToUser;
import fourTheFlatServer.Model.MessageMethods;

/**
 * Servlet implementation class NewSuggestionServlet
 */
@WebServlet("/newsuggestion/*")
public class NewSuggestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewSuggestionServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = General.Utils.formatStringArray(urlSplit);
		
		if (urlSplit.length != 6) {
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String user = urlSplit[3];
		String type = urlSplit[4];
		String suggestion = urlSplit[5];
		
		AddApprovalToUser aau = new AddApprovalToUser(); 
		MessageMethods mos = new MessageMethods();
		
		//SUGGEST PRODUCT FOR ALLOWED PRODUCT LIST
		if(type.equals("0"))
		{
			aau.allowedProductApproved(user,suggestion);
			
			mos.sendMessages(user, suggestion, 0);
			response.getWriter().print("Product Suggestion: "+suggestion);				
		}
		
		//SUGGEST USER BE ADDED TO GROUP
		else if(type.equals("1"))
		{
			aau.groupUserApproved(user, suggestion);
			
			mos.sendMessages(user, suggestion, 1);
			response.getWriter().print("New user: "+suggestion);	
		}	
		
		//CHANGE ADDRESS OF FLAT
		else if(type.equals("2"))
		{
			aau.groupAddressApproved(user, suggestion);
			
			mos.sendMessages(user, suggestion, 2);
			
			
			response.getWriter().print("Change flat address: "+suggestion);
		}
		else
		{
			response.getWriter().print("Not a valid type!");
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
