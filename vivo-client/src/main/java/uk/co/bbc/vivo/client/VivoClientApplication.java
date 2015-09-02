package uk.co.bbc.vivo.client;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.co.bbc.vivo.client.health.TemplateHealthCheck;
import uk.co.bbc.vivo.client.resources.VivoClientResource;

public class VivoClientApplication extends Application<VivoClientConfiguration> {

	public static void main(String... args) throws Exception {
		new VivoClientApplication().run(args);
	}
	
	@Override
	public String getName() {
		return "vivo-client";
	}
	
	@Override
	public void initialize(Bootstrap<VivoClientConfiguration> bootstrap) {};
	
	@Override
	public void run(VivoClientConfiguration configuration, Environment environment) throws Exception {
		final VivoClientResource vivoClientResource = new VivoClientResource();
		final TemplateHealthCheck healthCheck =
		        new TemplateHealthCheck(configuration.getTemplate());
		
		environment.healthChecks().register("template", healthCheck);
		environment.jersey().register(vivoClientResource);
	}

}
