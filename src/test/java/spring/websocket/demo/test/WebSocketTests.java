package spring.websocket.demo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.JsonPathExpectationsHelper;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.websocket.demo.config.WebSocketConfig;
import spring.websocket.demo.model.InMessage;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		WebSocketConfig.class,
})
public class WebSocketTests {

	@Autowired private AbstractSubscribableChannel clientInboundChannel;

	@Autowired private AbstractSubscribableChannel clientOutboundChannel;


	
	@Autowired private AbstractSubscribableChannel brokerChannel;

	private TestChannelInterceptor clientOutboundChannelInterceptor;

	private TestChannelInterceptor brokerChannelInterceptor;
	


	@Before
	public void setUp() throws Exception {

		this.brokerChannelInterceptor = new TestChannelInterceptor();
		this.clientOutboundChannelInterceptor = new TestChannelInterceptor();
		this.brokerChannel.addInterceptor(this.brokerChannelInterceptor);
		this.clientOutboundChannel.addInterceptor(this.clientOutboundChannelInterceptor);
		this.clientInboundChannel.addInterceptor(this.clientOutboundChannelInterceptor);
	}


	@Test
	public void getChatMessages() throws Exception {

		InMessage incomMessage=new InMessage();
		incomMessage.setContent("hello");
		incomMessage.setSender("simran");
		
		byte[] payload = new ObjectMapper().writeValueAsBytes(incomMessage);
		
		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
		headers.setSubscriptionId("0");
		headers.setDestination("/app/chat");
		headers.setSessionId("0");
		
		Message<byte[]> message = MessageBuilder.withPayload(payload).setHeaders(headers).build();

		this.clientOutboundChannelInterceptor.setIncludedDestinations("/app/chat");
		this.clientInboundChannel.send(message);

		Message<?> reply = this.clientOutboundChannelInterceptor.awaitMessage(5);
		assertNotNull(reply);

		StompHeaderAccessor replyHeaders = StompHeaderAccessor.wrap(reply);
		assertEquals("0", replyHeaders.getSessionId());
		assertEquals("0", replyHeaders.getSubscriptionId());
		assertEquals("/app/chat", replyHeaders.getDestination());

		String json = new String((byte[]) reply.getPayload(), Charset.forName("UTF-8"));
		new JsonPathExpectationsHelper("$.sender").assertValue(json,"simran");
		new JsonPathExpectationsHelper("$.content").assertValue(json,"hello");
	}
	
}