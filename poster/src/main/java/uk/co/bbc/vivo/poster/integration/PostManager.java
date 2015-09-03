package uk.co.bbc.vivo.poster.integration;

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
	private EventHandler eventHandler;
	
	public PostManager(String amqpUrl) {
		this.amqpUrl = amqpUrl;
	}
	
	public boolean emitPublishedEvent(String message) {
		Event postEvent = new Event("PostCreated", message, new Date());
		boolean postSucceeded = false;
		try {
			sendEvent(postEvent);
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
	
	private void sendEvent(Event postEvent) throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
		Channel channel = connection.createChannel();
		String outgoingExchangeName = "building-microservices-published-topic";
		String topicKey = "published";
		channel.exchangeDeclare(outgoingExchangeName, "topic");
		String helloVivo = "{'type': '" + postEvent.getType() + 
				"','body': '" + postEvent.getBody() + "'}";
		AMQP.BasicProperties.Builder propBuilder = new AMQP.BasicProperties.Builder();
		propBuilder.type("PostCreated");
		channel.basicPublish(outgoingExchangeName, topicKey, propBuilder.build(), helloVivo.getBytes());
	}
	
	private void consumeClientQueue() throws IOException {
		Channel channel = connection.createChannel();
		String routingKey = "training-microservices-routing";
		String incomingQueueName = "training-microservices-queue";
		String incomingExchangeName = "training-microservices";
		channel.queueBind(incomingQueueName, incomingExchangeName, routingKey);
		
		channel.basicConsume(incomingQueueName, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
                    AMQP.BasicProperties properties, byte[] body) throws IOException {
				String messageBody = new String(body, "UTF-8");
				LOG.info("Received (" + properties.getType()
				+ ") message: " + messageBody);
				Event incomingEvent = new Event(properties.getType(), messageBody, new Date());
				eventHandler.routeEvent(incomingEvent);
			}
		});
	}

	public void start() throws Exception {
		eventHandler = new EventHandler(this);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(amqpUrl);
		connection = factory.newConnection();
		consumeClientQueue();
	}

	public void stop() throws Exception {
		connection.close();
	}

}
