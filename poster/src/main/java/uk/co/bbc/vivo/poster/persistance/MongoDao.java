package uk.co.bbc.vivo.poster.persistance;

public class MongoDao {
	
	private static MongoDao instance;
	
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

}
