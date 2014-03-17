package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.GetAllData;
import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.Stores.User;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class PrintDBServlet
 */
@WebServlet("/printdb")
public class PrintDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrintDBServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//PRINT USER_GROUPS
		LinkedList<Group> groupList = GetAllData.getAllGroups();
		
	    response.getWriter().println("GROUPS!");
	    response.getWriter().println("");
	    
	    for(Group g: groupList)
	    {
	    	response.getWriter().print(PojoMapper.toJson(g, true));
	    	 response.getWriter().println("");
	    }
	    
	    response.getWriter().println("");
	    
	    
		//PRINT USERS
	    LinkedList<User> userList = GetAllData.getAllUsers();
	
	    response.getWriter().println("Users!");
	    response.getWriter().println("");
	    for(User u: userList)
	    {
	    	response.getWriter().print(PojoMapper.toJson(u, true));
	    	 response.getWriter().println("");
	    }
		
	    response.getWriter().println("");
		//PRINT PRODUCTS
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
