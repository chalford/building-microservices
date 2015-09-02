package uk.co.bbc.vivo.client.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

import uk.co.bbc.vivo.client.integration.PostDispatcher;

@Path("/vivo-client")
@Produces(MediaType.APPLICATION_JSON)
public class VivoClientResource {
	
	private final PostDispatcher postDispatcher = new PostDispatcher();
	private final Logger LOG = LoggerFactory.getLogger(VivoClientResource.class);
	
	@POST
	@Timed
	@Consumes(MediaType.TEXT_PLAIN)
	public void postMessage(String message) {
		LOG.info("Message being posted: " + message);
		LOG.info("Emitting Post event");
		postDispatcher.emitPostEvent(message);
	}

}
