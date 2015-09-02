package uk.co.bbc.microservices.entry;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {

	private static String AMPQ_URL = "amqp://dhalfxhj:TJ-8jR4Z9ikYuFVy2mULh75CjnisKSmL@bunny.cloudamqp.com/dhalfxhj";
	private static int NUM_MESSAGES = 5;

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
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
		for(int i = 0; i < NUM_MESSAGES; i++) {
			String helloVivo = "{'id': '" + i + "','type': 'postMessage','body': 'This is a new post!'}";
			channel.basicPublish(exchangeName, routingKey, null, helloVivo.getBytes());
		}
	}

}
