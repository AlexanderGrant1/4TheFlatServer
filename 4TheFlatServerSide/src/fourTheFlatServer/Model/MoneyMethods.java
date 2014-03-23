package fourTheFlatServer.Model;

import java.util.HashSet;
import java.util.UUID;
import java.util.Set;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import fourTheFlatServer.Stores.Group;
import fourTheFlatServer.lib.CassandraConnection;

public class MoneyMethods {
	

	
	public static boolean updateMoneyOwed(String shopper, Int cost)
	{
		UserMethods uMethods = new UserMethods();
		//Get groupid
		UUID groupID = uMethods.getGroupIDByUsername(shopper);
		//Get all users in that group
		Set<String> group = getGroupUsers(groupID);
		//Split the cost by number of users
		Float indCost = cost/group.getSize();
		//Split out users and remove user submitting the cost
		group.remove(shopper);
		//Prepare and execute statements for each remaining member in the group
		Iterator iter = group.iterator();
		while(iter.hasNext())
		{
			//Get the next userName
			String currentUser = iter.next();
			//Get current amount owed by that user to user who did shopping
			Map<String,Integer> owed = getUserMoneyOwed(currentUser);
			Integer currentMoney;
			try{
				currentMoney = owed.get(currentUser);
			}
			catch(Exception e){
				currentMoney=0;
			}			
			//Money now owed by that user to the shopper
			int moneyOwedNow = currentMoney + indCost;
			//Money owed by the shopper to that user
			int reverseMoney = moneyOwedNow*-1;
			//Start session
			Session session = CassandraConnection.getCluster().connect("flat_db");
			//Prepare the Statements - 1 to update the current non shopper
			//and the other the shopper
			PreparedStatement statement1 = session.prepare("UPDATE Users SET money['"+shopper+"']="+moneyOwedNow+" WHERE user_name=?");		
			BoundStatement boundStatement1 = new BoundStatement(statement1);
			boundStatement1.bind(currentUser);
			session.execute(boundStatement1);
			PreparedStatement statement2 = session.prepare("UPDATE Users SET money['"+shopper+"']="+reverseMoney+" WHERE user_name=?");		
			BoundStatement boundStatement2 = new BoundStatement(statement2);
			boundStatement2.bind(shopper);
			session.execute(boundStatement2);
			session.close();
		}
		return true;
	}
	
	public static Map<String,Integer> getCurrentMoneyOwed(String user)
	{
		Map<String,Integer> data = new Map<String,Integer>();
		//Start session
		Session session = CassandraConnection.getCluster().connect("flat_db");
		//Prepare the Statement
		PreparedStatement statement = session.prepare("SELECT money FROM users where user_name=?");	
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(userName);
		ResultSet rs = session.execute(boundStatement);

		Row details = rs.one();
		if(!details.equals(null))
		{
			data = details.getMap("money", String, Integer);
		}
		session.close();
		return data;		
	}

}
