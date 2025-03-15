package com.rew.pg.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.rew.payment.utility.PGUtils;

public class MongoDBConnectionHandler {
    private static Logger logger = LoggerFactory.getLogger(MongoDBConnectionHandler.class);

	public MongoDBConnectionHandler() {
	}

	
	 
	public static MongoClient getMongoClient() 
    {
        //MongoDatabase conn = null;
        MongoClient mongoClient = null;
        try {
                    String MongoUrl=PGUtils.getPropertyValue("MongoConnectionUrl");
            ConnectionString connectionString = new ConnectionString(MongoUrl);
        
            MongoClientSettings settings = MongoClientSettings.builder()
                      .applyConnectionString(connectionString)
                      .retryWrites(true)
                      .build();;
            
             mongoClient = MongoClients.create(settings);
            
        
    
            } catch (Exception e) { 
                logger.error(e.getMessage(),e); 
            } 
            return mongoClient; 
    }

	
}
