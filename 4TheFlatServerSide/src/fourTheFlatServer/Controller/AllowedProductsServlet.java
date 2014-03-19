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
 * Servlet implementation class AllowedProducts
 */
@WebServlet("/allowedproducts/*")
public class AllowedProductsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllowedProductsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 *AllowedProducts/<groupUUID>
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		if(urlSplit.length != 4)
		{
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String groupId = urlSplit[3];
		UUID groupUUID = UUID.fromString(groupId);
		//Check that the group exists
		if(GroupMethods.getGroupByUUID(groupUUID) == null)
		{
			return;
		}
		for(String s : GroupMethods.getAllowedProducts(groupUUID))
		{
			response.getWriter().println(s);
		}
	}

}
