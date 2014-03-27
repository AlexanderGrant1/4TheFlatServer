package fourTheFlatServer.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Model.MessageMethods;
import fourTheFlatServer.Model.ProductMethods;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.Stores.Message;
import fourTheFlatServer.Stores.Product;
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

		//PRINT USER_GROUPS TABLE
		LinkedList<Group> groupList = GroupMethods.getAllGroups();

	    response.getWriter().println("GROUP!");
	    response.getWriter().println("");

	    for(Group g: groupList)
	    {
	    	response.getWriter().print(PojoMapper.toJson(g, true));
	    	 response.getWriter().println("");
	    	 
	    	 Set<String> users = GroupMethods.getGroupUsers(g.getGroupID());
	    	 
	    	 response.getWriter().println("");
	    	 response.getWriter().println("GROUP USERS!");
	 	    response.getWriter().println("");
	    	 
	    	 for(String u : users)
	    	 {
	    		 response.getWriter().println("USER "+u);
	    		 User user = UserMethods.getUserByUsername(u);
	    		 
	    		 response.getWriter().print(PojoMapper.toJson(user, true));
		    	 response.getWriter().println("");
		    	 response.getWriter().println(u+"'s Messages");
	    		 
		    	 LinkedList<Message> uM = MessageMethods.getUserMessages(u);
		    	 
		    	 for(Message m : uM)
		    	 {
		    		 response.getWriter().print(PojoMapper.toJson(m, true));
			    	 response.getWriter().println("");
		    	 }
		    	 response.getWriter().println("");
		    	 response.getWriter().println("");
		    	 response.getWriter().println("");
		    	 response.getWriter().println("/////////////////////////////");
	    	 }
	    	 response.getWriter().println("");
	    	 response.getWriter().println("");
	    	 response.getWriter().println("Product history");
	    	 response.getWriter().println("");
	    	 
	    	 LinkedList<Product> prods = ProductMethods.getGroupProds(g.getGroupID());
	    	 
	    	 for(Product p : prods)
	    	 {
	    		 response.getWriter().print(PojoMapper.toJson(p, true));
		    	 response.getWriter().println("");
	    	 }
	    }

	    response.getWriter().println("");
              
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}