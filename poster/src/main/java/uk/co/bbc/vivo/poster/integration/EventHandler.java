package uk.co.bbc.vivo.poster.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.bbc.vivo.poster.core.MongoDao;

public class EventHandler {
	
	private final MongoDao mongoDao;
	private PostManager postManager;
	
	private static Logger LOG = LoggerFactory.getLogger(EventHandler.class);
	
	public EventHandler(PostManager postManager) {
		mongoDao = MongoDao.getInstance();
		this.postManager = postManager;
	}
	
	public void routeEvent(Event event) {
		String body = event.getBody();
		try {
			mongoDao.write(body);
		} catch (Exception e) {
			LOG.error("Error writing content to the Content Store", e);
		}
		postManager.emitPublishedEvent(body);
	}

}
