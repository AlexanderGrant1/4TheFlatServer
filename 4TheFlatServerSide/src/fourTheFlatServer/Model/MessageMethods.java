package fourTheFlatServer.Model;

import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.Message;
import fourTheFlatServer.lib.CassandraConnection;

public class MessageMethods {

	public static void sendMessages(String sender, String text, int type) {
		Set<String> users = GroupMethods.getGroupUsers(UserMethods
				.getGroupIdByUsername(sender));

		Session session = CassandraConnection.getCluster().connect("flat_db");

		for (String s : users) {

			if (!s.equals(sender)) {

				UUID mesageID = java.util.UUID
						.fromString(new com.eaio.uuid.UUID().toString());

				PreparedStatement statement = session
						.prepare("INSERT into user_messages (user_name, message_id, text, type) values (?, ?, ?, ?)");
				BoundStatement boundStatement = new BoundStatement(statement);
				boundStatement.bind(s, mesageID, text, type);
				session.execute(boundStatement);

			}

		}

	}
	
	public static boolean messageExists(UUID messageID, String username)
	{
			Session session = CassandraConnection.getCluster().connect("flat_db");

			PreparedStatement statement = session
					.prepare("SELECT * FROM user_messages where message_id = ? AND user_name=?");

			BoundStatement boundStatement = new BoundStatement(statement);
			boundStatement.bind(messageID, username);
			ResultSet rs = session.execute(boundStatement);
			Row details = rs.one();
			session.close();

			return details != null;
	}
	
	public static LinkedList<Message> getUserMessages(String username)
	{
			Session session = CassandraConnection.getCluster().connect("flat_db");

			PreparedStatement statement = session
					.prepare("SELECT * FROM user_messages where user_name = ?");

			BoundStatement boundStatement = new BoundStatement(statement);
			boundStatement.bind(username);
			ResultSet rs = session.execute(boundStatement);

			LinkedList<Message> messageList = new LinkedList<Message>();
			
			for(Row r: rs)
			{
				Message m = new Message();
				m.setMessageID(r.getUUID("message_id"));
				m.setMessage(r.getString("text"));
				m.setType(r.getInt("type"));
				messageList.add(m);
			}

			session.close();

			return messageList;
	}
	
	public static Message getMessageByUUIDAndUsername(UUID messageID, String username)
	{
			Session session = CassandraConnection.getCluster().connect("flat_db");

			PreparedStatement statement = session
					.prepare("SELECT * FROM user_messages where message_id = ? AND user_name = ?");

			BoundStatement boundStatement = new BoundStatement(statement);
			boundStatement.bind(messageID, username);
			ResultSet rs = session.execute(boundStatement);
			
			Row details = rs.one();
			
			if(details != null)
			{
				Message m = new Message();
				m.setMessageID(details.getUUID("message_id"));
				m.setMessage(details.getString("text"));
				m.setType(details.getInt("type"));
				session.close();
				return m;
			}

			session.close();

			return null;
	}
	
	public static boolean deleteUserMessage(UUID messageID, String username)
	{
			Session session = CassandraConnection.getCluster().connect("flat_db");

			PreparedStatement statement = session
					.prepare("DELETE FROM user_messages where message_id = ? AND user_name = ?");

			BoundStatement boundStatement = new BoundStatement(statement);
			boundStatement.bind(messageID, username);
			ResultSet rs = session.execute(boundStatement);
			session.close();

			return true;
	}


}
