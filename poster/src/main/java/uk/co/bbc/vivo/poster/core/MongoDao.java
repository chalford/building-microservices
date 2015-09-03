package uk.co.bbc.vivo.poster.core;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDao {
	
	private static MongoDao instance;
	private static Logger LOG = LoggerFactory.getLogger(MongoDao.class);
	
	private MongoDao() {}
	
	public boolean isHealthy() {
		// Not implemented yet, so probably not healthy!
		return false;
	}
	
	public static MongoDao getInstance() {
		if(instance == null) {
			instance = new MongoDao();
		}
		return instance;
	}
	
	public void write(String message) throws Exception {
		// Fake mongo write
		Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		LOG.info("Wrote: " + message + " to mongo.");
	}

}
