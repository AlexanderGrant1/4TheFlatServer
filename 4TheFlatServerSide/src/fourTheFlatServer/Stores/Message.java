package fourTheFlatServer.Stores;

import java.util.UUID;

public class Message {

	private UUID messageID;
	
	//Contains the one word need to convey decision, 
	//eg if message for a new user to be added to the group only contains user name	
	//have a template string on app "Do you want " + username + " to be added to the group?"
	private String message; 
	private int type;
	private String receiver;

	public UUID getMessageID() {
		return messageID;
	}

	public void setMessageID(UUID messageID) {
		this.messageID = messageID;
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

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
