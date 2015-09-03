package uk.co.bbc.vivo.contentpublisher;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.co.bbc.vivo.contentpublisher.health.ContentPublisherHealthCheck;
import uk.co.bbc.vivo.contentpublisher.integration.PublicationMessageManager;

public class ContentPublisherApplication extends Application<ContentPublisherConfiguration> {

	public static void main(String... args) throws Exception {
		new ContentPublisherApplication().run(args);
	}
	
	@Override
	public String getName() {
		return "content-publisher";
	}
	
	@Override
	public void initialize(Bootstrap<ContentPublisherConfiguration> bootstrap) {};
	
	@Override
	public void run(ContentPublisherConfiguration configuration, Environment environment) throws Exception {
		final ContentPublisherHealthCheck healthCheck = new ContentPublisherHealthCheck();
		final PublicationMessageManager postHandlerManager = new PublicationMessageManager(configuration.getAmqpUrl());
		
		environment.lifecycle().manage(postHandlerManager);
		environment.healthChecks().register("poster", healthCheck);
	}

}
