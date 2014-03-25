package fourTheFlatServer.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.GetAllData;
import fourTheFlatServer.Model.GroupMethods;
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
		LinkedList<Group> groupList = GetAllData.getAllGroups();

	    response.getWriter().println("GROUPS!");
	    response.getWriter().println("");

	    for(Group g: groupList)
	    {
	    	response.getWriter().print(PojoMapper.toJson(g, true));
	    	 response.getWriter().println("");
	    }

	    response.getWriter().println("");


		//PRINT USERS TABLE
	    LinkedList<User> userList = GetAllData.getAllUsers();

	    response.getWriter().println("Users!");
	    response.getWriter().println("");
	    for(User u: userList)
	    {
	    	response.getWriter().print(PojoMapper.toJson(u, true));
	    	 response.getWriter().println("");
	    }

	    response.getWriter().println("");


	    //PRINT MESSAGE TABLE
	    response.getWriter().println("Messages!");
	    response.getWriter().println("");

        LinkedList<Message> messages = GetAllData.getAllMessages();

        for(Message m : messages)
        {
        	
	    	response.getWriter().print(PojoMapper.toJson(m, true));
	    	 response.getWriter().println("");
        }
        
	    response.getWriter().println("");
        
        
	    //PRINT PRODUCTS_LIST TABLE
	    response.getWriter().println("Products table");
	    response.getWriter().println("");
        
	    LinkedList<Product> prods = GetAllData.getAllProds();
	    
	    
	    if(prods != null)
	    {
	    for(Product p : prods)
        {
        	
	    	response.getWriter().print(PojoMapper.toJson(p, true));
	    	response.getWriter().println("");
        }
	    }
		//PRINT AVAILABLE_PRODUCTS TEXT FILE

	    PrintWriter writer = response.getWriter();


	    response.getWriter().println("Available Products in text file!");
	    response.getWriter().println("");
        InputStream in = getServletContext().getResourceAsStream("/Available_Products.txt");
             
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        
        String text;
        while ((text = reader.readLine()) != null) {
            writer.println(text);
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