package uk.co.bbc.vivo.client.integration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class PostDispatcher {
	
	private static String AMPQ_URL = "amqp://dhalfxhj:TJ-8jR4Z9ikYuFVy2mULh75CjnisKSmL@bunny.cloudamqp.com/dhalfxhj";
	
	public boolean emitPostEvent(String message) {
		Event postEvent = new Event("CreatePost", message, new Date());
		boolean postSucceeded = false;
		try {
			sendPostEvent(postEvent);
			postSucceeded = true;
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postSucceeded;
	}
	
	private void sendPostEvent(Event postEvent) throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(AMPQ_URL);
		Connection conn = factory.newConnection();
		
		Channel channel = conn.createChannel();
		
		// Define an Exchange and a Queue on that channel
		String exchangeName = "training-microservices";
		//  channel.exchangeDeclare(exchangeName, "direct", true)
		String queueName = "training-microservices-queue";
		String routingKey = "training-microservices-routing";
		channel.queueBind(queueName, exchangeName, routingKey);
		String helloVivo = "{'type': '" + postEvent.getType() + 
				"','body': '" + postEvent.getBody() + "'}";
		channel.basicPublish(exchangeName, routingKey, null, helloVivo.getBytes());
	}

}
