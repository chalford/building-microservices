package uk.co.bbc.vivo.contentpublisher.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentWriterOutboundGateway {
	
	private static ContentWriterOutboundGateway instance;
	private static Logger LOG = LoggerFactory.getLogger(ContentWriterOutboundGateway.class);
	
	private ContentWriterOutboundGateway() {}
	
	public boolean isHealthy() {
		// Not implemented yet, so probably not healthy!
		return false;
	}
	
	public static ContentWriterOutboundGateway getInstance() {
		if(instance == null) {
			instance = new ContentWriterOutboundGateway();
		}
		return instance;
	}
	
	public void write(String uuid, String content) throws Exception {
		// Fake a call to the content writer
		Thread.sleep(2000);
		LOG.info("Wrote " + uuid + " to the Content Store.");
	}

}
