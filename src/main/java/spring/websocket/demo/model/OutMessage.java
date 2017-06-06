package spring.websocket.demo.model;

/**
 * Class used for outgoing messages
 * 
 * @author Simranjit.Singh
 *
 */
public class OutMessage extends InMessage {
	private String timestamp;

	public OutMessage() {

	}

	public OutMessage(String content, String timestamp,String sender,String photoData) {
		super.content = content;
		this.timestamp = timestamp;
		super.sender=sender;
		super.photoData=photoData;

	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}

