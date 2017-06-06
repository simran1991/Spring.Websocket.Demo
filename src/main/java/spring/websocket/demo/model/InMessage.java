package spring.websocket.demo.model;

/**
 * Class used as POJO for incoming messages
 * 
 * @author Simranjit.Singh
 *
 */
public class InMessage {
	protected String sender;
	protected String content;
	protected String photoData;

	public InMessage() {
	}

	public InMessage(String sender, String reciever, String content) {
		this.setSender(sender);
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getPhotoData() {
		return photoData;
	}

	public void setPhotoData(String photoData) {
		this.photoData = photoData;
	}
}
