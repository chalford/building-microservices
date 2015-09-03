package uk.co.bbc.vivo.tagpublisher.integration;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventHandler {
	
	private final NexusWriterOutboundGateway contentWriter;
	
	private static Logger LOG = LoggerFactory.getLogger(EventHandler.class);
	
	public EventHandler() {
		contentWriter = NexusWriterOutboundGateway.getInstance();
	}
	
	public void routeEvent(Event event) {
		String uuid = UUID.randomUUID().toString();
		String body = event.getBody();
		try {
			contentWriter.write(uuid, body);
		} catch (Exception e) {
			LOG.error("Error writing content to the Content Store", e);
		}
	}

}
