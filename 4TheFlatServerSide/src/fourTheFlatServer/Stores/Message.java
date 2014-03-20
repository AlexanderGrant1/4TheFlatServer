package fourTheFlatServer.Stores;

import java.util.UUID;

public class Message {

	private UUID alertID;
	
	//Contains the one word need to convey decision, 
	//eg if message for a new user to be added to the group only contains user name	
	//have a template string on app "Do you want " + username + " to be added to the group?"
	private String message; 
	private int type;

	public UUID getAlertID() {
		return alertID;
	}

	public void setAlertID(UUID alertID) {
		this.alertID = alertID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
