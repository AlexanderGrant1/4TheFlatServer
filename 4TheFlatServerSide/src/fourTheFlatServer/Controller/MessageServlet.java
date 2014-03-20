package fourTheFlatServer.Controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fourTheFlatServer.Model.AddApprovalToUser;
import fourTheFlatServer.Model.GroupMethods;
import fourTheFlatServer.Model.MessageMethods;
import fourTheFlatServer.Model.UserMethods;
import fourTheFlatServer.Stores.Message;
import fourTheFlatServer.lib.PojoMapper;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet("/messages/*")
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get user messages
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response) message/<username>
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if (urlSplit.length != 4) {
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String username = urlSplit[3];

		LinkedList<Message> messageList = MessageMethods
				.getUserMessages(username);

		for (Message m : messageList) {

				response.getWriter().println(PojoMapper.toJson(m, true));
		}
	}

	/**
	 * Respond to message
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
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
		String username = urlSplit[3];
		UUID messageID = UUID.fromString(urlSplit[4]);
		String approves = urlSplit[5];
		if (!MessageMethods.messageExists(messageID, username)) {
			response.getWriter().print("Message does not exist.");
			return;
		}
		Message m = MessageMethods.getMessageByUUIDAndUsername(messageID,
				username);
		int type = m.getType();
		String subject = m.getMessage();
		Set<String> groupUsers = GroupMethods.getGroupUsers(UserMethods
				.getGroupIdByUsername(username));
		
		//IF USER APROVES
		if (approves.equals("yes")) {
			switch (type) {
			case 0: {
				// add a new product
				AddApprovalToUser.allowedProductApproved(username, subject);
				for (String user : groupUsers) {
					if (!user.equals(username)) {
						if (!UserMethods.checkApprovedProducts(username,
								subject)) {
							return;
						}
					}
				}
				for (String user : groupUsers) {
					UserMethods.removeApprovedProduct(user, subject);
				}
				String outcome = "Suggestion to add " + subject
						+ " to be bought was successfully approved.";
				MessageMethods.sendSuggestionOutcome(groupUsers, outcome, true);
				// success
				break;
			}
			case 1: {
				// add a new user
				AddApprovalToUser.groupUserApproved(username, subject);
				break;
			}
			case 2: {
				// agree with new address
				AddApprovalToUser.groupAddressApproved(username, subject);
				break;
			}
			}
			
		//IF USER DOES NOT APROVE
		} else {
			
			//////need this....
			UUID messageGroupID = m.getMessageID();
			switch (type) {
			case 0: {
				// don't add a new product
				for (String s : groupUsers) {
					UserMethods.removeApprovedProduct(s, subject);
					
					///////.....and this
					MessageMethods.deleteUserMessage(messageGroupID, s);
				
				
				}
				String outcome = "Suggestion to add " + subject
						+ " to be bought failed.";
				MessageMethods
						.sendSuggestionOutcome(groupUsers, outcome, false);
				break;
			}
			case 1: {
				// don't add a new user
				break;
			}
			case 2: {
				// disagree with new address
				break;
			}
			}
		}
	}

	/**
	 * Delete message
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String[] urlSplit = requestURI.split("/");
		urlSplit = General.Utils.formatStringArray(urlSplit);
		if (urlSplit.length != 5) {
			response.getWriter().print("Incorrect URL format.");
			return;
		}
		String username = urlSplit[3];
		UUID messageID = UUID.fromString(urlSplit[4]);
		if (MessageMethods.messageExists(messageID, username)) {
			MessageMethods.deleteUserMessage(messageID, username);
			response.getWriter().print("message deleted");
		}
	}

}