package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.Approvals;
import fourTheFlatServer.Model.Authentication;
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
	 *      newsuggestion/<username>/<password>/<type>/<suggestion>
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = fourTheFlatServer.General.Utils.formatStringArray(urlSplit);

		if (urlSplit.length != 7) {
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String user = urlSplit[3];
		String password = urlSplit[4];
		int type = Integer.parseInt(urlSplit[5]);
		String suggestion = urlSplit[6];
		
		if(Authentication.validateLoginCredentials(user, password) != null)
		{
			response.getWriter().print("Invalid username or password.");
			return;
		}
		
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
						if(GroupMethods.productRequestPending(userGroup, suggestion))
						{
							response.getWriter().print("Product request already pending.");
							return;
						}
						GroupMethods.addAllowedProduct(UserMethods.getGroupIdByUsername(user), suggestion);
						response.getWriter().print("Product added.");	
						//success
						break;
					}
					case 1:
					{
						if(GroupMethods.userRequestPending(userGroup, suggestion))
						{
							response.getWriter().print("User request already pending.");
							return;
						}
						GroupMethods.addUserToGroup(UserMethods.getGroupIdByUsername(user), suggestion);
						response.getWriter().print("User added.");	
						//success
						break;
					}
					case 2:
					{
						UUID groupID = UserMethods.getGroupIdByUsername(suggestion);
						if(GroupMethods.addressMessagePending(groupID))
						{
							response.getWriter().print("Address change already pending.");
							return;
						}
						GroupMethods.changeGroupAddress(UserMethods.getGroupIdByUsername(user), suggestion);
						response.getWriter().print("Address changed.");	
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
			if(GroupMethods.productRequestPending(userGroup, suggestion))
			{
				response.getWriter().print("Product request already pending.");
				return;
			}
			Approvals.allowedProductApproved(user,suggestion);
			
			MessageMethods.sendMessages(user, suggestion, 0);
			response.getWriter().print("Product suggested.");				
		}
		
		//SUGGEST USER BE ADDED TO GROUP
		else if(type == 1)
		{
			boolean userExists = UserMethods.userExists(suggestion);
			UUID groupID = UserMethods.getGroupIdByUsername(suggestion);
			System.out.println(groupID);
			if(userExists && groupID == null)
			{
				if(GroupMethods.userRequestPending(userGroup, suggestion))
				{
					response.getWriter().print("User request already pending.");
					return;
				}
				Approvals.groupUserApproved(user, suggestion);
				
				MessageMethods.sendMessages(user, suggestion, 1);
				response.getWriter().print("User suggested.");	
			}
			else
			{
				if(!userExists)
				{
					response.getWriter().print("User does not exist.");
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
			UUID groupID = UserMethods.getGroupIdByUsername(user);
			if(GroupMethods.addressMessagePending(groupID))
			{
				response.getWriter().print("Address change already pending.");
				return;
			}
			Approvals.groupAddressApproved(user, suggestion);
			MessageMethods.sendMessages(user, suggestion, 2);
			response.getWriter().print("Change flat suggested.");
		}
		else
		{
			response.getWriter().print("Not a valid type!");
		}
	}
}
