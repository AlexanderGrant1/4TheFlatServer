package fourTheFlatServer.Model;

import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.lib.CassandraConnection;

public class MessageOtherUsers {

	public void sendMessages(String sender, String text, int type) {
		Set<String> users = getGroupUsers(UserMethods
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

	private Set<String> getGroupUsers(UUID groupID) {
		Set<String> users;

		Session session = CassandraConnection.getCluster().connect("flat_db");

		PreparedStatement statement = session
				.prepare("SELECT users  FROM user_group where group_id = ?");

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(groupID);
		ResultSet rs = session.execute(boundStatement);

		Row set = rs.one();

		users = set.getSet("users", String.class);

		session.close();

		return users;
	}

}
