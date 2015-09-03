package uk.co.bbc.vivo.contentpublisher.integration;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import io.dropwizard.lifecycle.Managed;

public class PublicationMessageManager implements Managed {
	
	private final String amqpUrl;
	private Connection connection;
	private EventHandler eventHandler;
	private final Logger LOG = LoggerFactory.getLogger(PublicationMessageManager.class);
	
	// Define an Exchange and a Queue on that channel
	private String exchangeName = "building-microservices-published-topic";
	//  channel.exchangeDeclare(exchangeName, "direct", true)
	private String bindingKey = "#";
	
	public PublicationMessageManager(String amqpUrl) {
		this.amqpUrl = amqpUrl;
	}
	
	private void consumeClientQueue() throws IOException {
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(exchangeName, "topic");
		String queueName = channel.queueDeclare().getQueue();
		
		// Bind queue to the bindingKey
		channel.queueBind(queueName, exchangeName, bindingKey);
		
		LOG.info("Waiting for messages on the topic...");
		channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
                    AMQP.BasicProperties properties, byte[] body) throws IOException {
				String messageBody = new String(body, "UTF-8");
				LOG.info("Received message: " + messageBody);
				String type = properties.getHeaders().get("type").toString();
				Event incomingEvent = new Event(type, messageBody, new Date());
				eventHandler.routeEvent(incomingEvent);
			}
		});
	}

	public void start() throws Exception {
		eventHandler = new EventHandler();
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(amqpUrl);
		connection = factory.newConnection();
		consumeClientQueue();
	}

	public void stop() throws Exception {
		connection.close();
	}

}
