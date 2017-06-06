package spring.websocket.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import spring.websocket.demo.model.InMessage;
import spring.websocket.demo.model.OutMessage;
import spring.websocket.demo.model.TrackInMessage;
import spring.websocket.demo.model.TrackOutMessage;

/**
 * Class used as controller for handling custom messages
 * 
 * @author Simranjit.Singh
 *
 */
@Controller
public class WebSocketController {
	private static final String DATE_FORMAT = "yyyy.MM.dd.HH.mm.ss";

	/**
	 * Method is used to capture messages from client and send them to the
	 * subscribers
	 * 
	 * @param message
	 * @return OutMessage
	 */
	@MessageMapping("/chat")
	@SendTo("/topic/chat")
	public OutMessage greetings(InMessage message) {
		OutMessage outMessage = new OutMessage(message.getContent(),
				new SimpleDateFormat(DATE_FORMAT).format(new Date()), message.getSender(), message.getPhotoData());
		return outMessage;
	}

	/**
	 * Method is used to track the user that are typing
	 * 
	 * @param message
	 * @return TypeTrackOut
	 */
	@MessageMapping("/track")
	@SendTo("/topic/tracking")
	public TrackOutMessage typingTracker(TrackInMessage message) {
		return new TrackOutMessage(message.getUser());
	}
}