package spring.websocket.demo.model;

/**
 * Class used as POJO for sending information about typing users.
 * 
 * @author Simranjit.Singh
 *
 */
public class TrackOutMessage {
	private String user;

	public TrackOutMessage() {

	}

	public TrackOutMessage(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
