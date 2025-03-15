package com.rew.pg.db;

import java.sql.Connection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionHandler {
    private static Logger logger = LoggerFactory.getLogger(DBConnectionHandler.class);

	public DBConnectionHandler() {
	}

	public static Connection getConnection() {
		Connection conn = null; 
		DataSource ds;
		try {
			Context context = new InitialContext();
			ds = (DataSource) context.lookup("java:comp/env/jdbc/getDB");
			System.out.println("ds: " + ds);
			if (ds != null) { 
				conn = ds.getConnection();
				conn.setAutoCommit(true);
			} 
			System.out.println("conn: " + conn);
	
			} catch (Exception e) { 
				logger.error(e.getMessage(),e); 
			} 
			return conn; 
		}
	 

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.DAYS);
	}
}
