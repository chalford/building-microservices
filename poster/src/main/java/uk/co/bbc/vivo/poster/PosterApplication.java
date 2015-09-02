package uk.co.bbc.vivo.poster;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.co.bbc.vivo.poster.health.PosterHealthCheck;
import uk.co.bbc.vivo.poster.integration.PostManager;

public class PosterApplication extends Application<PosterConfiguration> {

	public static void main(String... args) throws Exception {
		new PosterApplication().run(args);
	}
	
	@Override
	public String getName() {
		return "poster";
	}
	
	@Override
	public void initialize(Bootstrap<PosterConfiguration> bootstrap) {};
	
	@Override
	public void run(PosterConfiguration configuration, Environment environment) throws Exception {
		final PosterHealthCheck healthCheck = new PosterHealthCheck();
		final PostManager postHandlerManager = new PostManager(configuration.getAmqpUrl());
		
		environment.lifecycle().manage(postHandlerManager);
		environment.healthChecks().register("poster", healthCheck);
	}

}
