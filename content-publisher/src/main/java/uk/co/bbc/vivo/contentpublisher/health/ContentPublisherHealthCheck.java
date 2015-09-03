package uk.co.bbc.vivo.contentpublisher.health;

import com.codahale.metrics.health.HealthCheck;

import uk.co.bbc.vivo.contentpublisher.integration.ContentWriterOutboundGateway;

public class ContentPublisherHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		boolean mongoSuccess = ContentWriterOutboundGateway.getInstance().isHealthy();
		if (!mongoSuccess) {
			return Result.unhealthy("MongoDB is currently having issues");
		}
		return Result.healthy();
	}
}
