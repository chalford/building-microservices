package uk.co.bbc.vivo.tagpublisher.integration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import io.dropwizard.lifecycle.Managed;

public class PostManager implements Managed {
	
	private final String amqpUrl;
	private Connection connection;
	private final Logger LOG = LoggerFactory.getLogger(PostManager.class);
	
	// Define an Exchange and a Queue on that channel
	private String exchangeName = "training-microservices";
	//  channel.exchangeDeclare(exchangeName, "direct", true)
	private String queueName = "training-microservices-queue";
	private String routingKey = "training-microservices-routing";
	
	public PostManager(String amqpUrl) {
		this.amqpUrl = amqpUrl;
	}
	
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
		Channel channel = connection.createChannel();
		channel.queueBind(queueName, exchangeName, routingKey);
		String helloVivo = "{'type': '" + postEvent.getType() + 
				"','body': '" + postEvent.getBody() + "'}";
		channel.basicPublish(exchangeName, routingKey, null, helloVivo.getBytes());
	}
	
	private void consumeClientQueue() throws IOException {
		Channel channel = connection.createChannel();
		channel.queueBind(queueName, exchangeName, routingKey);
		
		channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
                    AMQP.BasicProperties properties, byte[] body) throws IOException {
				LOG.info("Received message: " + new String(body, "UTF-8"));
			}
		});
	}

	public void start() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(amqpUrl);
		connection = factory.newConnection();
		consumeClientQueue();
	}

	public void stop() throws Exception {
		connection.close();
	}

}
