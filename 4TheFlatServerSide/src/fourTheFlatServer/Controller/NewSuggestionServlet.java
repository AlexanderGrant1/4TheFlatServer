package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.Approvals;
import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Model.MessageMethods;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.Group;

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
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if (urlSplit.length != 6) {
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String user = urlSplit[3];
		int type = Integer.parseInt(urlSplit[4]);
		String suggestion = urlSplit[5];
		UUID userGroup = UserMethods.getGroupIdByUsername(user);
		
		if(userGroup != null)
		{
			Group group = GroupMethods.getGroupByUUID(userGroup);
			if(group.getUsers().size() == 1)
			{
				switch(type)
				{
					case 0:
					{
						//add a new product
						GroupMethods.addAllowedProduct(UserMethods.getGroupIdByUsername(user), suggestion);
						//success
						break;
					}
					case 1:
					{
						GroupMethods.addUserToGroup(UserMethods.getGroupIdByUsername(user), suggestion);
						//success
						break;
					}
					case 2:
					{
						GroupMethods.changeGroupAddress(UserMethods.getGroupIdByUsername(user), suggestion);
						//success
						break;
					}
				}
				return;
			}
		}
	
		
		//SUGGEST PRODUCT FOR ALLOWED PRODUCT LIST
		if(type== 0)
		{
			Approvals.allowedProductApproved(user,suggestion);
			
			MessageMethods.sendMessages(user, suggestion, 0);
			response.getWriter().print("Product Suggestion: "+suggestion);				
		}
		
		//SUGGEST USER BE ADDED TO GROUP
		else if(type == 1)
		{
			boolean userExists = UserMethods.userExists(user);
			UUID groupID = UserMethods.getGroupIdByUsername(user);
			if(userExists && groupID == null)
			{
				Approvals.groupUserApproved(user, suggestion);
				
				MessageMethods.sendMessages(user, suggestion, 1);
				response.getWriter().print("New user: "+suggestion);	
			}
			else
			{
				if(!userExists)
				{
					response.getWriter().print("This user does not exist.");
					return;
				}
				if(groupID != null)
				{
					response.getWriter().print("User is already in a group.");
					return;
				}
				
			}

		}	
		
		//CHANGE ADDRESS OF FLAT
		else if(type == 2)
		{
			Approvals.groupAddressApproved(user, suggestion);
			
			MessageMethods.sendMessages(user, suggestion, 2);
			
			
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
