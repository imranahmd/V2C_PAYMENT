package com.rew.payment.rms;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.pg.db.DBConnectionHandler;
import com.rew.pg.db.MongoDBConnectionHandler;
import com.rew.pg.dto.ReconMaster;

public class ReconMangamentSystem {
	private static Logger logger = LoggerFactory.getLogger(MongoDBConnectionHandler.class);

	public String ReconService(String txnId, String Status,String bankId, String merchantId,String amount,String merchantTxnId,String spId,String paymentMode ) {

		List<List<String>> reconCalculatedData = null;
		List<String> reconData = null;
		Connection conn = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		ReconMaster rm = new ReconMaster();
		logger.info("Calculation :::: of  " + merchantId);
		logger.info("Calculation :::: of  " + spId);
		logger.info("Calculation :::: of  " + paymentMode);
		logger.info("Calculation :::: of  " + bankId);
		logger.info("Calculation :::: of  " + amount);
		logger.info("Calculation :::: of  " + txnId);
		logger.info("Calculation :::: of  " + merchantTxnId);

		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "Call pro_ReconCalculation(?,?,?,?,?,?,?,?,?)";

			logger.info("Calculation of recon:::::::::::::::::::::::::::::: ");
			if (conn != null) {

				callableStatement = conn.prepareCall(sql);
				callableStatement.setString(1, merchantId);
				callableStatement.setString(2, spId);
				callableStatement.setString(3, paymentMode);
				callableStatement.setString(4, bankId);
				callableStatement.setString(5, amount);
				callableStatement.setString(6, txnId);
				callableStatement.setString(7, merchantTxnId);
				callableStatement.setString(8, "0");
				callableStatement.setString(9,Status);

				logger.info("Calculation of recon:::::::::::::::::::::::::::::: "+callableStatement);

				callableStatement.execute();
				logger.info("Calculation of recon:::::::::::::::::::::::::::::: ");

				
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: saveData() :: Error Occurred : ", (Throwable) e);

			return "";
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (callableStatement != null) {

					callableStatement.close();
					callableStatement = null;
				}
				if (conn != null) {

					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error("DataManager.java ::: saveData() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException4);
			}
		}
		return "";


	}
}
