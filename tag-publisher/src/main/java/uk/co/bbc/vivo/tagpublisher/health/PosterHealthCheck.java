package uk.co.bbc.vivo.tagpublisher.health;

import com.codahale.metrics.health.HealthCheck;

import uk.co.bbc.vivo.tagpublisher.persistance.MongoDao;

public class PosterHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		boolean mongoSuccess = MongoDao.getInstance().isHealthy();
		if (!mongoSuccess) {
			return Result.unhealthy("MongoDB is currently having issues");
		}
		return Result.healthy();
	}
}
