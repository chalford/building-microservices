package uk.co.bbc.vivo.tagpublisher;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.co.bbc.vivo.tagpublisher.health.TagPublisherHealthCheck;
import uk.co.bbc.vivo.tagpublisher.integration.PublicationMessageManager;

public class TagPublisherApplication extends Application<TagPublisherConfiguration> {

	public static void main(String... args) throws Exception {
		new TagPublisherApplication().run(args);
	}
	
	@Override
	public String getName() {
		return "content-publisher";
	}
	
	@Override
	public void initialize(Bootstrap<TagPublisherConfiguration> bootstrap) {};
	
	@Override
	public void run(TagPublisherConfiguration configuration, Environment environment) throws Exception {
		final TagPublisherHealthCheck healthCheck = new TagPublisherHealthCheck();
		final PublicationMessageManager postHandlerManager = new PublicationMessageManager(configuration.getAmqpUrl());
		
		environment.lifecycle().manage(postHandlerManager);
		environment.healthChecks().register("poster", healthCheck);
	}

}
