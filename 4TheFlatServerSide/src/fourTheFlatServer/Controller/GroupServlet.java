package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Model.MessageMethods;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.Stores.GroupReturn;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class GroupServlet
 */
@WebServlet("/group/*")
public class GroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * group/<username> returns the group of that user
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
		String username = urlSplit[3];


		UUID groupID = UserMethods.getGroupIdByUsername(username);

		if(groupID == null)
		{
			System.out.println("Group not found!");
			return;
		}

		GroupReturn group = GroupMethods.getGroupByUUID(groupID);

		if(group != null){
			response.getWriter().print(PojoMapper.toJson(group, true));

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 *group/<username>/<address> Create new group and add user to it
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = fourTheFlatServer.General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 5)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String username = urlSplit[3];
		String address = urlSplit[4];

		//Check that the user exists
		if(!UserMethods.userExists(username))
		{
			response.getWriter().print("User does not exist");
			return;
		}
		//If the user is not already in a group, add them to a group
		if(UserMethods.getGroupIdByUsername(username) == null)
		{
			GroupReturn newGroup = GroupMethods.createNewGroup(username);
			GroupMethods.changeGroupAddress(newGroup.getGroupID(), address);
			newGroup = GroupMethods.getGroupByUUID(newGroup.getGroupID());
			response.getWriter().print(PojoMapper.toJson(newGroup, true));
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 *group/<groupID>/<username> add user to group
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = fourTheFlatServer.General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 5)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String group = urlSplit[3];
		String username = urlSplit[4];

		UUID groupID = UUID.fromString(group);
		//Check that the group we're trying to add a user to exists
		if(GroupMethods.getGroupByUUID(groupID) == null)
		{
			response.getWriter().print("Group does not exist");
			return;
		}
		//Check that the user exists
		if(!UserMethods.userExists(username))
		{
			response.getWriter().print("User does not exist");
			return;
		}
		//If the user doesn't already have a group then add them to the given group
		if(UserMethods.getGroupIdByUsername(username) == null)
		{
			GroupMethods.addUserToGroup(groupID, username);
			response.getWriter().print(PojoMapper.toJson(group, true));
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 *group/<groupUUID>/<username> removes a user to a group
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = fourTheFlatServer.General.Utils.formatStringArray(urlSplit);
		if(urlSplit.length != 5)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String group = urlSplit[3];
		String username = urlSplit[4];

		UUID groupID = UUID.fromString(group);

		UUID userGroupID = UserMethods.getGroupIdByUsername(username);

		if(userGroupID == null)
		{
			response.getWriter().print("User is not in the given group.");
			return;
		}

		//Check that the group exists before continuing
		if(GroupMethods.getGroupByUUID(groupID) == null)
		{
			response.getWriter().print("Group does not exist");
			return;
		}

		//Check that the user exists before continuing
		if(!UserMethods.userExists(username))
		{
			response.getWriter().print("User does not exist");
			return;
		}

		//If the user is in the given group then remove them and print the group's JSON
		if(UserMethods.getGroupIdByUsername(username).equals(groupID))
		{
			if(GroupMethods.removeUserFromGroup(username, groupID))
			{
				GroupReturn userGroup = GroupMethods.getGroupByUUID(groupID);
				MessageMethods.deleteUserMessages(username);
				response.getWriter().print(PojoMapper.toJson(userGroup, true));
				return;
			}
			response.getWriter().print("Failed to remove user from group.");
			return;
		}
		response.getWriter().print("User is not in the given group.");
}
}