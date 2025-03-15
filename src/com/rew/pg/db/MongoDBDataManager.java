package com.rew.pg.db;

import java.net.URI;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.rew.payment.utility.PGUtils;
import com.rew.pg.dto.ReconMaster;
import com.rew.pg.dto.TransactionMaster;

public class MongoDBDataManager {
	private static Logger logger = LoggerFactory.getLogger(MongoDBDataManager.class);

	public TransactionMaster save(HashMap<String, String> dataMap) {

		dataMap.put("riskCode", "");
		dataMap.put("riskFlag", "");
		dataMap.put("riskLabel", "");
		MongoClient mongoClient = null;
		TransactionMaster TM = new TransactionMaster();
		try {
			mongoClient = MongoDBConnectionHandler.getMongoClient();
			MongoDatabase database = mongoClient.getDatabase("pg");
			System.out.println("mongo db conn: " + database);

			MongoCollection<Document> collection = database.getCollection("tbl_transactionmaster");
			System.out.println("Collection tbl_transactionmaster selected successfully");

			Document document = new Document("Transaction Master", "MongoDB");

			dataMap.forEach((key, value) -> document.append(key, value));

			// Inserting document into the collection
			collection.insertOne(document);
			System.out.println("Document inserted successfully");
			return TM;
		} catch (Exception e) {
			logger.error("Error while innserting in monog db for txn id {}", dataMap.get("id"), e);
		} finally {
			if (mongoClient != null)
				mongoClient.close();
		}
		return null;
	}

	public int update(HashMap<String, String> dataMap) {
		return 0;
		}

	/**
	 * @author Adesh
	 * @param productId
	 * @param merchantId
	 * @param spId
	 * @param paymentMode
	 * @param bankId
	 * @param amount
	 * @param txnId
	 * @param merchantTxnId
	 */
	public void saveReconMaster(String productId, String merchantId, String spId, String paymentMode,
			String bankId, String amount, String txnId, String merchantTxnId) {
				
		}

	
}
