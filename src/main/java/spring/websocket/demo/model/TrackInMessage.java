package spring.websocket.demo.model;

/**
 * Class used as POJO for accepting information about typing users
 * 
 * @author Simranjit.Singh
 *
 */
public class TrackInMessage {
	
	private String user;

	public TrackInMessage() {

	}
	
	public TrackInMessage(String message) {
		this.user=message;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
