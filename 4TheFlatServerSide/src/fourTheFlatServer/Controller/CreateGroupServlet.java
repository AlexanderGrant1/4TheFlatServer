package fourTheFlatServer.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class RegisterUserGruop
 */
@WebServlet("/creategroup/*")
public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateGroupServlet() {
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
		
		Group group = GroupMethods.createNewGroup(username);
		
		if(group != null)
		{
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
