package uk.co.bbc.vivo.tagpublisher.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NexusWriterOutboundGateway {
	
	private static NexusWriterOutboundGateway instance;
	private static Logger LOG = LoggerFactory.getLogger(NexusWriterOutboundGateway.class);
	
	private NexusWriterOutboundGateway() {}
	
	public boolean isHealthy() {
		// Not implemented yet, so probably not healthy!
		return false;
	}
	
	public static NexusWriterOutboundGateway getInstance() {
		if(instance == null) {
			instance = new NexusWriterOutboundGateway();
		}
		return instance;
	}
	
	public void write(String uuid, String content) throws Exception {
		// Fake a call to the content writer
		Thread.sleep(2000);
		LOG.info("Wrote " + uuid + " to Nexus.");
	}

}
