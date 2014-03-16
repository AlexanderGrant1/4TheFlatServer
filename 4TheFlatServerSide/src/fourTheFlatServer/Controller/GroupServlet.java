package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.Group;
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
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		if(urlSplit.length != 4)
		{
			System.out.println("Invalid url");
			return;
		}
		String username = urlSplit[3];

		
		UUID groupID = UserMethods.getGroupIdByUsername(username);
		
		
		System.out.println("GROUP ID FOUND: "+groupID.toString());
		
		if(groupID == null)
		{
			System.out.println("Group not found!");
			return;
		}
		
		Group group = GroupMethods.getGroupByUUID(groupID);
		
		if(group != null){
			response.getWriter().print(PojoMapper.toJson(group, true));
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
