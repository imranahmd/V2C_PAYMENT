package com.rew.pg.db;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import com.rew.payment.utility.PGUtils;
import com.rew.payment.utility.RefundStatus;
import com.rew.payment.utility.S2SCallback;
//import com.rew.pg.dto.AtompgDetails;
import com.rew.pg.dto.BankMaster;
import com.rew.pg.dto.CheckOutMaster;
import com.rew.pg.dto.MerchantDTO;
import com.rew.pg.dto.RefundRequestAPI;
import com.rew.pg.dto.ResellerDTO;
import com.rew.pg.dto.RiskGlobalDTO;
import com.rew.pg.dto.SbiAquiringDTO;
import com.rew.pg.dto.ServiceProviderDto;
import com.rew.pg.dto.TransactionMaster;
import com.rew.pg.dto.TxnRetry;

public class DataManager {
	private static Logger logger = LoggerFactory.getLogger(DataManager.class);

	public TransactionMaster saveData(TransactionMaster TM) {
		Connection conn = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		String atrn = "-1";
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "Call pro_transactionmasterNewV4(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (conn != null) {
				atrn = String.valueOf(PGUtils.getPropertyValue("atrnPrefix"))
						+ new SimpleDateFormat("yyDDDHHmmsss").format(new Date()) + getAtrnPostfix(); // PGUtils.randomNumeric(4);
				logger.info("saveData() atrn is ::::::::: " + atrn);

				final int tm = this.checkTxnMaster(atrn, TM.getTxnId(), TM.getMerchantId());
				logger.info("saveData() duplicate Id Status>" + TM.getRespStatus() + ">>>>>>> " + tm);
				logger.info("saveData() bank Id >>>>>>>> " + TM.getBankId());
				if (tm == 0) {
					logger.info("Rseller_Id:::::::::" + TM.getReseller_id());
					logger.info("txn:::::::::" + TM.getReseller_txn_id());

					conn.setAutoCommit(false);
					callableStatement = conn.prepareCall(sql);
					callableStatement.setString(1, TM.getTxnId());
					callableStatement.setString(2, TM.getMerchantId());
					callableStatement.setString(3, TM.getDateTime());
					callableStatement.setString(4, TM.getProcessId());
					callableStatement.setString(5, TM.getAmount());
					callableStatement.setString(6, TM.getProductId());
					callableStatement.setString(7, TM.getReturn_url());
					callableStatement.setString(8, TM.getCustMobile());
					callableStatement.setString(9,  TM.getCustMail());
					callableStatement.setString(10, TM.getUdf1());
					callableStatement.setString(11, TM.getUdf2());
					callableStatement.setString(12, TM.getUdf3());
					callableStatement.setString(13, TM.getUdf4());
					callableStatement.setString(14, TM.getUdf5());
					callableStatement.setString(15, "NRNS");
					callableStatement.setString(16, TM.getInstrumentId());
					callableStatement.setString(17, TM.getServiceRRN());
					callableStatement.setString(18, TM.getServiceTxnId());
					callableStatement.setString(19, TM.getRespStatus());
					callableStatement.setString(20, "To");
					callableStatement.setString(21, TM.getHostAddress());
					callableStatement.setString(22, TM.getSurcharge());
					callableStatement.setString(23, TM.getRespDateTime());
					callableStatement.setString(24, TM.getModified_On());
					callableStatement.setString(25, TM.getModified_By());
					callableStatement.setString(26, TM.getServiceAuthId());
					callableStatement.setString(27, TM.getRespMessage());
					callableStatement.setString(28, atrn);
					callableStatement.setString(29, TM.getBankId());
					callableStatement.setString(30, TM.getCardDetails());
					callableStatement.setString(31, TM.getCardType());
					callableStatement.setString(32, TM.getUploadedBy());
					callableStatement.setString(33, TM.getUdf6());
					callableStatement.setString(34, TM.getReseller_id());
					callableStatement.setString(35, TM.getReseller_txn_id());

					callableStatement.execute();
					conn.commit();
					rs = callableStatement.getResultSet();
					int status = -1;
					if (rs != null && rs.next()) {
						status = Integer.parseInt(rs.getString("Ret"));
					}
					if (status == 0) {
						logger.info("DataManager.java ::: saveData() :: Merchant Transtaion Id alredy exists.");
						atrn = "0";
					}
					if (status == 1) {
						logger.info(
								"DataManager.java ::: saveData() :: Transaction Reference Number Generated : " + atrn);
					}
					
					  TM.setId(atrn); 
						/*
						 * TM.setId(atrn); HashMap<String, String> dataMap = new LinkedHashMap<String,
						 * String>(); dataMap.put("_id", TM.getId()); dataMap.put("id", TM.getId());
						 * dataMap.put("txnId", TM.getTxnId()); dataMap.put("merchantId",
						 * TM.getMerchantId()); dataMap.put("dateTime", TM.getDateTime());
						 * dataMap.put("processId", TM.getProcessId()); dataMap.put("amount",
						 * TM.getAmount()); dataMap.put("productId", TM.getProductId());
						 * dataMap.put("returnUrl", TM.getReturn_url()); dataMap.put("custMobile",
						 * TM.getCustMobile()); dataMap.put("custMail", TM.getCustMail());
						 * dataMap.put("udf1", TM.getUdf1()); dataMap.put("udf2", TM.getUdf2());
						 * dataMap.put("udf3", TM.getUdf3()); dataMap.put("udf4", TM.getUdf4());
						 * dataMap.put("udf5", TM.getUdf5()); dataMap.put("reconstatus", "NRNS");
						 * dataMap.put("instrumentId", TM.getInstrumentId()); dataMap.put("serviceRRN",
						 * TM.getServiceRRN()); dataMap.put("serviceTxnId", TM.getServiceTxnId());
						 * dataMap.put("respStatus", TM.getRespStatus()); dataMap.put("transStatus",
						 * "To"); dataMap.put("hostAddress", TM.getHostAddress());
						 * dataMap.put("surcharge", TM.getSurcharge()); dataMap.put("respDateTime",
						 * TM.getRespDateTime()); dataMap.put("modifiedOn", TM.getModified_On());
						 * dataMap.put("modifiedBy", TM.getModified_By()); dataMap.put("serviceAuthId",
						 * TM.getServiceAuthId()); dataMap.put("respMessage", TM.getRespMessage());
						 * dataMap.put("atrn", atrn); dataMap.put("bankId", TM.getBankId());
						 * dataMap.put("cardDetail", TM.getCardDetails()); dataMap.put("cardType",
						 * TM.getCardType()); dataMap.put("uploadBy", TM.getUploadedBy());
						 * MongoDBDataManager mdm = new MongoDBDataManager(); TransactionMaster T =
						 * mdm.save(dataMap);
						 * 
						 * if (T != null) { logger.info("Data inserted susccefully in mongo" +
						 * T.getId()); } else { logger.info("Data inserted failed in mongo"); }
						 */
				} else {
					TM = null;
					final String sResp = "Error D001 : something went wrong";
					logger.info("DataManager.java ::: saveData() ::  : " + sResp);
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: saveData() :: Error Occurred : ", (Throwable) e);

			return TM;
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

		return TM;
	}

	public String getAtrnPostfix() {
		Connection conn = null;
		CallableStatement cs = null;
		String atrnPostFix = null;
		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "Call pr_newserial_no(?,?)";

			if (conn != null) {

				cs = conn.prepareCall(sql);
				cs.setString(1, "randomNo");
				cs.registerOutParameter(2, java.sql.Types.VARCHAR);
				cs.executeUpdate();
				atrnPostFix = cs.getString(2);
				logger.info("getAtrnPostfix ==========>" + atrnPostFix);
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getAtrnPostfix() :: Error Occurred : ", e);
		} finally {
			try {

				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error("DataManager.java ::: getAtrnPostfix() :: Error Occurred while closing Connection : ",
						localSQLException4);
			}
		}

		return atrnPostFix;

	}

	public int insertTBLGetTxnDetails(String merchant_id, String txnid, String pg_id, String response,
			String return_url, String rodt) {
		logger.info("calling insertTBLGetTxnDetails method");
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "insert into tbl_gettxndetails(merchant_id, txnid, pg_id, response, return_url, rodt) values(?,?,?,?,?,?)";
			logger.info("DataManager.java ::: insertTBLGetTxnDetails() :: query=" + sql);
			if (conn != null) {

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchant_id);
				stmt.setString(2, txnid);
				stmt.setString(3, pg_id);
				stmt.setString(4, response);
				stmt.setString(5, return_url);
				stmt.setString(6, rodt);

				result = stmt.executeUpdate();
				logger.info("DataManager.java ::: insertTBLGetTxnDetails() :: result========>" + result);

			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.error("DataManager.java ::: insertTBLGetTxnDetails() :: Error Occurred : ", e);
		} finally {
			try {

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error(
						"DataManager.java ::: insertTBLGetTxnDetails() :: Error Occurred while closing Connection : ",
						localSQLException4);
			}
		}

		return result;

	}

	public int checkTxnMaster(final String id, String txn_id, String merchant_id) {

		logger.info("DataManager.java ::: checkTxnMaster() :: id=" + id + " txn_id=" + txn_id + " merchant_id="
				+ merchant_id);
		int status = 0;

		int found = checkTxnVerification(txn_id, merchant_id);

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		logger.info("DataManager.java ::: checkTxnMaster() :: fetch Data against the Txn Id '" + id + "'");
		try {

			if (found == 1) {
				status = 1;
				return status;
			} else {
				conn = DBConnectionHandler.getConnection();
				// final String sql = "select txn_id
				// ,merchant_id,date_time,process_id,txn_amount,service_id,return_url,mobile_no,email_id
				// ,udf1,udf2,id,trans_status,instrument_id,udf3,udf4,remote_ip,resp_date_time,udf5,resp_status,resp_message,service_rrn,bank_id,card_details,card_type,error_code,sp_error_code,rms_score,rms_reason,service_txn_id,sur_charge
				// from tbl_transactionmaster where id = '" + id + "' ";
				final String sql = "select txn_id ,merchant_id,date_time,process_id,txn_amount,service_id,return_url,mobile_no,email_id ,udf1,udf2,id,trans_status,instrument_id,udf3,udf4,remote_ip,resp_date_time,udf5,resp_status,resp_message,service_rrn,bank_id,card_details,card_type,error_code,sp_error_code,rms_score,rms_reason,service_txn_id,sur_charge from tbl_transactionmaster where id =? ";

				if (conn != null) {
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, id);
					rs = stmt.executeQuery();

					logger.info("DataManager.java ::: checkTxnMaster() :: query >>" + sql);
					if (rs != null && rs.next()) {
						status = 1;

						logger.info(
								"DataManager.java ::: checkTxnMaster() status for duplicate Id+" + id + ">>>>> True");
					} else {
						status = 0;
						logger.info(
								"DataManager.java ::: checkTxnMaster() status for duplicate Id+" + id + ">>>>> false");
					}
				}
			}

		} catch (Exception e) {
			logger.error("DataManager.java ::: checkTxnMaster() :: Error Occurred : ", (Throwable) e);

			return status;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error("DataManager.java ::: checkTxnMaster() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException4);
			}
		}

		return status;
	}

	public int checkTxnVerification(String txnid, String merchantid) {
		logger.info("check for duplicate merchant unique id start >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + txnid
				+ "   " + merchantid);
		int found = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if (txnid != null && merchantid != null && !txnid.isEmpty() && !merchantid.isEmpty()) {
				conn = DBConnectionHandler.getConnection();
				final String sql = "select txn_id ,merchant_id, id, date_time, trans_status, txn_amount, resp_message, service_rrn, return_url, error_code, instrument_id, resp_date_time,mobile_no,email_id,udf1,udf2,udf3,udf4,udf5, rodt from tbl_transactionmaster where txn_id = ? and merchant_id = ?";
				if (conn != null) {

					stmt = conn.prepareStatement(sql);
					stmt.setString(1, txnid);
					stmt.setString(2, merchantid);
					rs = stmt.executeQuery();
					logger.info("DataManager.java ::: checkTxnVerification() ::: query " + sql);
					if (rs != null && rs.next()) {

						logger.info(
								"DataManager.java ::: checkTxnVerification() :: record found for merchant uniqie id="
										+ txnid);
						found = 1;
					} else {
						logger.info(
								"DataManager.java ::: checkTxnVerification() :: No rows returned for merchant uniqie  id="
										+ txnid);
						found = 0;
					}
				}
			} else {

				logger.info(
						"DataManager.java ::: checkTxnVerification() :: Merchant Id or Txn Id is Null or Blank : MID -> "
								+ merchantid + " , Txn ID -> " + txnid);

			}
		} catch (Exception e) {
			logger.error(
					"DataManager.java ::: checkTxnVerification() :: Error Occurred while fetching Data for Txn Id '"
							+ txnid + "' And MID '" + merchantid + "' : ",
					e);

		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se3) {
				logger.error(
						"DataManager.java ::: checkTxnVerification() :: Error Occurred while closing Connection : ",
						(Throwable) se3);
			}
		}

		return found;
	}

	public int insertPushDataTemp(final String pgrefid, final String txnid, final String merchantid, final String url,
			final String encdata, final String stts, final String type) {
		logger.info("insertPushDataTemp():::::" + pgrefid + "  ,  " + txnid + "  ,  " + merchantid + "  ,  " + url
				+ "  ,  " + type);
		String query = null;
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		int status = 0;
		try {
			if (type.equalsIgnoreCase("insert")) {
				logger.info("inside insert");
				query = "insert into tbl_pushurl_schedular(pg_ref_id, txn_id, merchant_id, url,params, resp_data, counter, status) values(?,?,?,?,?,?,?,?)";
				if (con != null) {
					ps = con.prepareStatement(query);
					ps.setString(1, pgrefid);
					ps.setString(2, txnid);
					ps.setString(3, merchantid);
					ps.setString(4, url);
					ps.setString(5, encdata);
					ps.setString(6, null);
					ps.setInt(7, 0);
					ps.setString(8, "FAILED");
					ps.executeUpdate();
					logger.info("insertPushDataTemp : " + query);
					status = 1;
					logger.info("inserted successfully for " + pgrefid);
				}
			} else if (type.equalsIgnoreCase("delete")) {
				logger.info("inside delete");
				query = "delete from tbl_pushurl_schedular where pg_ref_id=?";
				if (con != null) {
					ps = con.prepareStatement(query);
					ps.setString(1, pgrefid);
					ps.executeUpdate();
					logger.info("insertPushDataTemp : " + query);
					status = 2;
					logger.info("deleted successfully for " + pgrefid);
				}
			} else if (type.equalsIgnoreCase("update")) {
				logger.info("inside update");
				query = "update tbl_pushurl_schedular set resp_data=? where pg_ref_id=?";
				if (con != null) {
					ps = con.prepareStatement(query);
					ps.setString(1, stts);
					ps.setString(2, pgrefid);
					ps.executeUpdate();
					status = 3;
					logger.info("insertPushDataTemp : " + query);
					logger.info("updated successfully for " + pgrefid);
				}
			}
		} catch (SQLException e) {
			logger.error("DataManager.java ::: insertPushDataTemp() :: Error Occurred : ", (Throwable) e);

			return status;
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e3) {
				logger.error("DataManager.java ::: insertPushDataTemp() :: Error Occurred while closing Connection : ",
						(Throwable) e3);
			}
		}

		return status;
	}

	public MerchantDTO getMerchant(final String merchantId) {

		logger.info("DataManager.java ::: getMerchant() :: merchantId=" + merchantId);
		MerchantDTO MM = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select transaction_key,merchant_status,merchant_name,mer_return_url,mer_website_url,integration_type,"
					+ "max_token_size,is_vas,is_push_url,push_url,isretryAllowed,merchant_category_code,is_save_card,reseller_id,is_loader_access,whitelistIp from tbl_mstmerchant where MerchantId = ?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchantId);
				rs = stmt.executeQuery();
				if (rs.next()) {
					MM = new MerchantDTO();
					BigDecimal amount = new BigDecimal(rs.getString("max_token_size"));
					logger.info("max_token_size=" + amount);
					MM.setMerchantId(merchantId);
					MM.setTransactionKey(rs.getString("transaction_key"));
					MM.setStatus(rs.getString("merchant_status"));
					MM.setName(rs.getString("merchant_name"));
					MM.setMerchReturnURL(rs.getString("mer_return_url"));
					MM.setMerchWebsiteURL(rs.getString("mer_website_url"));
					MM.setIntegrationType(rs.getString("integration_type"));
					MM.setMaxTokenSize(amount);
					logger.info("max_token_size=" + MM.getMaxTokenSize());
					MM.setIsVAS(rs.getString("is_vas"));
					MM.setIs_push_url(rs.getString("is_push_url"));
					MM.setPushUrl(rs.getString("push_url"));
					MM.setIsRetry(rs.getString("isretryAllowed"));
					MM.setMcc(rs.getString("merchant_category_code"));
					MM.setIsSaveCard(rs.getString("is_save_card"));
					MM.setReseller_Id(rs.getString("reseller_id"));
					MM.setIs_loader_access(rs.getString("is_loader_access"));
					MM.setCustIP(rs.getString("whitelistIp"));
					logger.info("is_loader_access ::::  {}", MM.getIs_loader_access());
					logger.info("before returning MM" + MM.toString());
					return MM;
				}
			}
		} catch (Exception e) {
			logger.info("error in merchantdto " + e.getMessage());

			return MM;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getMerchant() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return MM;
	}

	public ResellerDTO getReseller(final String Reseller_Id) {

		logger.info("DataManager.java ::: getReseller_Id() :: Reseller_Id=" + Reseller_Id);
		ResellerDTO RR = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select reseller_name,contact_person,email_id,contact_number,legal_name,brand_name,business_type,pin_code,return_url,status,integration_type from tbl_reseller_personal_details where reseller_id = ?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, Reseller_Id);
				rs = stmt.executeQuery();
				if (rs.next()) {
					RR = new ResellerDTO();
					RR.setReseller_Id(Reseller_Id);
					RR.setReseller_name(rs.getString("reseller_name"));
					RR.setContact_person(rs.getString("contact_person"));
					RR.setEmail_id(rs.getString("email_id"));
					RR.setContact_number(rs.getString("contact_number"));
					RR.setLegal_name(rs.getString("legal_name"));
					RR.setBrand_name(rs.getString("brand_name"));
					RR.setBusiness_type(rs.getString("business_type"));
					RR.setPin_code(rs.getString("pin_code"));
					RR.setReturn_url(rs.getString("return_url"));
					RR.setStatus(rs.getString("status"));
					RR.setIntegration_type(rs.getString("integration_type"));

					logger.info("RR VALUE :::::::::::::::::::::;; " + RR.toString());

					logger.info("DataManager.java ::: getReseller_Id()" + RR.getIntegration_type());

					logger.info("before returning RR");
					return RR;
				}
			}
		} catch (Exception e) {
			logger.info("error in ResellerDto " + e.getMessage());

			return RR;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getMerchant() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return RR;
	}

	public ArrayList<String> getProductIdList(final String merchantId) {
		logger.info("getProductIdList() merchantId=" + merchantId);
		ArrayList<String> prodList = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select product_id from tbl_merchant_product where merchant_id = ?";
			if (conn != null) {
				stmt = conn.prepareStatement("select product_id from tbl_merchant_product where merchant_id = ?");
				stmt.setString(1, merchantId);
				rs = stmt.executeQuery();
				prodList = new ArrayList<String>();
				while (rs.next()) {
					prodList.add(rs.getString("product_id"));
				}
				logger.info("DataManager.java ::: getProductIdList() :: Product Id List Size : " + prodList.size());
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getProductIdList() :: Error Occurred : ", (Throwable) e);

			return prodList;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getProductIdList() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return prodList;
	}

	/*
	 * public Vector<List<String>> getStoredCards(final String custMail, final
	 * String custMobile, final String merchId) {
	 * 
	 * logger.info("getStoredCards :: custMail=" + custMail + " custMobile=" +
	 * custMobile + " merchId=" + merchId); final Vector<List<String>> storedCards =
	 * new Vector<List<String>>(); Connection conn = null; PreparedStatement stmt =
	 * null; ResultSet rs = null; try { conn = DBConnectionHandler.getConnection();
	 * final String sql =
	 * "select cardNo,expiryDate,cardName,instrumentId,cardType,merchantId,custMail,custMobile,secretKey from tbl_checkoutmaster where custMail = ? and custMobile = ? and merchantId = ? and status = ?"
	 * ; if (conn != null) { stmt = conn.prepareStatement(sql); stmt.setString(1,
	 * custMail); stmt.setString(2, custMobile); stmt.setString(3, merchId);
	 * stmt.setString(4, "A"); rs = stmt.executeQuery(); if (rs != null) { while
	 * (rs.next()) { final List<String> cardDetails = new ArrayList<String>();
	 * cardDetails.add(rs.getString("cardNo"));
	 * cardDetails.add(rs.getString("expiryDate"));
	 * cardDetails.add(rs.getString("cardName"));
	 * cardDetails.add(rs.getString("instrumentId"));
	 * cardDetails.add(rs.getString("cardType"));
	 * cardDetails.add(rs.getString("merchantId"));
	 * cardDetails.add(rs.getString("custMail"));
	 * cardDetails.add(rs.getString("custMobile"));
	 * cardDetails.add(rs.getString("secretKey")); storedCards.add(cardDetails); } }
	 * } } catch (Exception e) {
	 * logger.error("DataManager.java ::: getStoredCards() :: Error Occurred : ",
	 * (Throwable) e);
	 * 
	 * return storedCards; } finally { try { if (rs != null) { rs.close(); rs =
	 * null; } if (stmt != null) { stmt.close(); stmt = null; } if (conn != null) {
	 * conn.close(); conn = null; } } catch (SQLException localSQLException6) {
	 * logger.
	 * error("DataManager.java ::: getStoredCards() :: Error Occurred while closing Connection : "
	 * , (Throwable) localSQLException6); } }
	 * 
	 * return storedCards; }
	 */

	public Vector<List<String>> getStoredCards(final String custMail, final String custMobile, final String merchId) {

		logger.info("getStoredCards :: custMail=" + custMail + " custMobile=" + custMobile + " merchId=" + merchId);
		final Vector<List<String>> storedCards = new Vector<List<String>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select cardNo,expiryDate,cardName,instrumentId,cardType,merchantId,custMail,custMobile,secretKey from tbl_checkoutmaster where custMail = ? and custMobile = ? and merchantId = ? and status = ?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, custMail);
				stmt.setString(2, custMobile);
				stmt.setString(3, merchId);
				stmt.setString(4, "A");
				rs = stmt.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						final List<String> cardDetails = new ArrayList<String>();
						cardDetails.add(rs.getString("cardNo"));
						cardDetails.add(rs.getString("expiryDate"));
						cardDetails.add(rs.getString("cardName"));
						cardDetails.add(rs.getString("instrumentId"));
						cardDetails.add(rs.getString("cardType"));
						cardDetails.add(rs.getString("merchantId"));
						cardDetails.add(rs.getString("custMail"));
						cardDetails.add(rs.getString("custMobile"));
						cardDetails.add(rs.getString("secretKey"));
						storedCards.add(cardDetails);
					}
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getStoredCards() :: Error Occurred : ", (Throwable) e);

			return storedCards;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getStoredCards() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return storedCards;
	}

	public Vector<List<String>> getStoredCardsVAS(final String custMail, final String custMobile,
			final String merchId) {

		logger.info("getStoredCards :: custMail=" + custMail + " custMobile=" + custMobile + " merchId=" + merchId);
		final Vector<List<String>> storedCards = new Vector<List<String>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select cardNo,expiryDate,cardName,instrumentId,cardType,merchantId,custMail,custMobile,secretKey,tokenReferenceId, panLast4, tokenExpiryDate, cryptogram, clientReferenceId,id, customerId from tbl_checkoutmaster where custMail = ? and custMobile = ? and merchantId = ? and status = ? and token_status=?";
			if (conn != null) {

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, PGUtils.cryptoJs(custMail));
				stmt.setString(2, PGUtils.cryptoJs(custMobile));
				stmt.setString(3, merchId);
				stmt.setString(4, "A");
				stmt.setString(5, "ACTIVE");
				rs = stmt.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						final List<String> cardDetails = new ArrayList<String>();
						cardDetails.add(rs.getString("cardNo"));
						cardDetails.add(rs.getString("expiryDate"));
						cardDetails.add(rs.getString("cardName"));
						cardDetails.add(rs.getString("instrumentId"));
						cardDetails.add(rs.getString("cardType"));
						cardDetails.add(rs.getString("merchantId"));
						cardDetails.add(rs.getString("custMail"));
						cardDetails.add(rs.getString("custMobile"));
						cardDetails.add(rs.getString("secretKey"));
						cardDetails.add(rs.getString("tokenReferenceId"));
						cardDetails.add(rs.getString("panLast4"));
						cardDetails.add(rs.getString("tokenExpiryDate"));
						cardDetails.add(rs.getString("cryptogram"));
						cardDetails.add(rs.getString("clientReferenceId"));
						cardDetails.add(rs.getString("id"));
						cardDetails.add(rs.getString("customerId"));

						storedCards.add(cardDetails);
					}
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getStoredCards() :: Error Occurred : ", (Throwable) e);

			return storedCards;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getStoredCards() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return storedCards;
	}

	public String CheckStatus(String mid) {
		logger.info("getSPKey() mid=" + mid);
		String sProviderSQL = null;
		int count = 0;
		String transCount = null;
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		try {
			sProviderSQL = "select status  from tbl_mstmerchant where merchantId = ?";
			if (con != null) {
				ps = con.prepareStatement(sProviderSQL);
				ps.setString(1, mid);
				logger.info("getSPKey() : " + sProviderSQL);
				rs = ps.executeQuery();
				if (rs.next()) {
					transCount = rs.getString("status");
					logger.error("DataManager.java ::: Logger() ::" + transCount);

					rs.close();
					ps.close();
					con.close();
				}
			}
		} catch (SQLException e) {
			logger.error("DataManager.java ::: getSPKey() :: Error Occurred : ", (Throwable) e);

			return transCount;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.info("DataManager.java ::: getSPKey() :: Error Occurred while closing Connection : ",
						(Throwable) e4);
			}
		}

		return transCount;
	}

	public Vector<List<String>> getStoredCardsCC(final String custMail, final String custMobile, final String merchId) {

		logger.info("getStoredCardsCC() custMail=" + custMail + " custMobile=" + custMobile + " merchId=" + merchId);
		final Vector<List<String>> storedCards = new Vector<List<String>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select cardNo,expiryDate,cardName,instrumentId,cardType,merchantId,custMail,custMobile,secretKey from tbl_checkoutmaster where custMail = ? and custMobile = ? and merchantId = ? and status = ? and instrumentId = ? ";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, custMail);
				stmt.setString(2, custMobile);
				stmt.setString(3, merchId);
				stmt.setString(4, "A");
				stmt.setString(5, "CC");
				rs = stmt.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						final List<String> cardDetails = new ArrayList<String>();
						cardDetails.add(rs.getString("cardNo"));
						cardDetails.add(rs.getString("expiryDate"));
						cardDetails.add(rs.getString("cardName"));
						cardDetails.add(rs.getString("instrumentId"));
						cardDetails.add(rs.getString("cardType"));
						cardDetails.add(rs.getString("merchantId"));
						cardDetails.add(rs.getString("custMail"));
						cardDetails.add(rs.getString("custMobile"));
						cardDetails.add(rs.getString("secretKey"));
						storedCards.add(cardDetails);
					}
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getStoredCards() :: Error Occurred : ", (Throwable) e);

			return storedCards;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getStoredCards() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return storedCards;
	}

	public Vector<List<String>> getStoredCardsDC(final String custMail, final String custMobile, final String merchId) {

		logger.info("getStoredCardsDC() custMail=" + custMail + " custMobile=" + custMobile + " merchId" + merchId);
		final Vector<List<String>> storedCards = new Vector<List<String>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select cardNo,expiryDate,cardName,instrumentId,cardType,merchantId,custMail,custMobile,secretKey from tbl_checkoutmaster where custMail = ? and custMobile = ? and merchantId = ? and status = ? and instrumentId = ? ";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, custMail);
				stmt.setString(2, custMobile);
				stmt.setString(3, merchId);
				stmt.setString(4, "A");
				stmt.setString(5, "DC");
				rs = stmt.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						final List<String> cardDetails = new ArrayList<String>();
						cardDetails.add(rs.getString("cardNo"));
						cardDetails.add(rs.getString("expiryDate"));
						cardDetails.add(rs.getString("cardName"));
						cardDetails.add(rs.getString("instrumentId"));
						cardDetails.add(rs.getString("cardType"));
						cardDetails.add(rs.getString("merchantId"));
						cardDetails.add(rs.getString("custMail"));
						cardDetails.add(rs.getString("custMobile"));
						cardDetails.add(rs.getString("secretKey"));
						storedCards.add(cardDetails);
					}
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getStoredCards() :: Error Occurred : ", (Throwable) e);

			return storedCards;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getStoredCards() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return storedCards;
	}

	/*
	 * public int getBinvalidation(String id,String crno,String instrumentid){
	 * logger.info("DataManager.java :: getBinvalidation() id="+id+" crno="
	 * +crno+" instrumentid="+instrumentid); Connection con=null; CallableStatement
	 * cs=null; int status=0; try{ con= DBConnectionHandler.getConnection(); String
	 * query="{call pr_binValidation1(?,?,?)}"; cs=con.prepareCall(query);
	 * cs.setString(1,id ); cs.setString(2,crno); cs.setString(3, instrumentid);
	 * 
	 * status=cs.executeUpdate();
	 * 
	 * logger.info("DataManager.java :: getBinvalidation() :: for txnId="
	 * +id+" + Status >>>>>> "+status); return status; } catch (Exception e) {
	 * e.printStackTrace();
	 * logger.error("DataManger getBinvalidation ::: Getting Error ",e); }
	 * 
	 * finally { try { if (cs != null) {
	 * 
	 * cs.close(); } if (con != null) { con.close();
	 * 
	 * }
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace();
	 * logger.error("DataManger getBinvalidation ::: Getting Error ",e); } } return
	 * status; }
	 */

	public String getBinvalidation(String id, String crno, String instrumentid) {
		logger.info(
				"DataManager.java :: getBinvalidation() id=" + id + " crno=" + crno + " instrumentid=" + instrumentid);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String ctype = "NA";

		try {
			con = DBConnectionHandler.getConnection();
			if (con != null) {

				String query = "select card_type  from cardbins  where bin_lval like concat(?,'%') or bin_hval like concat(?,'%')";
				logger.info("DataManager.java :: getBinvalidation()  ::: query >>>>> " + query);
				ps = con.prepareStatement(query);
				ps.setString(1, crno);
				ps.setString(2, crno);

				rs = ps.executeQuery();

				if (rs.next()) {
					String cardType = rs.getString("card_type");
					logger.info("BinValidate() :: for txnId=" + id + " rs.getString(card_type)=" + cardType + " ctype="
							+ ctype);
					if (cardType.equalsIgnoreCase("Credit Card") && ctype.equalsIgnoreCase("NA")) {
						ctype = "CC";
					} else if (cardType.equalsIgnoreCase("Debit Card") && ctype.equalsIgnoreCase("NA")) {
						ctype = "DC";
					} else {
						logger.info("BinValidate() ::  doesnot contains range for this cardno");
						ctype = instrumentid;
					}

				} else {
					logger.info("BinValidate() ::  doesnot contains resultset for this cardno");
					ctype = instrumentid;
				}
				logger.info("DataManager.java :: getBinvalidation() :: for txnId=" + id + " + Status >>>>>> " + ctype);
				return ctype;
			} else {
				logger.info("DataManager.java :: getBinvalidation() :: for txnId=" + id + " connection is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DataManger getBinvalidation ::: Getting Error ", e);
		}

		finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {

					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("DataManger getBinvalidation ::: Getting Error ", e);
			}
		}
		return ctype;
	}

	public String checkSecretKey(final String custMail, final String custMobile) {

		logger.info("checkSecretKey() custMail=" + custMail + " custMobile=" + custMobile);
		String checkKey = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select secretKey from tbl_checkoutmaster where custMail = ? and custMobile = ?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, custMail);
				stmt.setString(2, custMobile);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					checkKey = rs.getString(1);
				} else {
					logger.info("DataManager.java ::: checkSecretKey() :: Key Not found for Combination '" + custMail
							+ "' and '" + custMobile + "'");
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: checkSecretKey() :: Error Occurred : ", (Throwable) e);

			return checkKey;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: checkSecretKey() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return checkKey;
	}

	public void saveCardDetailsTokenize(final CheckOutMaster CM) {
		Connection conn = null;
		CallableStatement callableStatement = null;
		try {
			conn = DBConnectionHandler.getConnection();
			// final String sql = "Call
			// pro_saveCardDetails(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			final String sql = "Call pro_saveCardDetailsCardTokenization(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // card
																																						// no
																																						// replace
																																						// with
																																						// tokenRefId

			if (conn != null) {
				conn.setAutoCommit(false);
				callableStatement = conn.prepareCall(sql);
				callableStatement.setString(1, CM.getMerchantId());
				callableStatement.setString(2, CM.getCardNo());
				callableStatement.setString(3, CM.getExpiryDate());
				callableStatement.setString(4, CM.getCardName());
				callableStatement.setString(5, CM.getCardType());
				callableStatement.setString(6, CM.getInstrumentId());
				callableStatement.setString(7, PGUtils.cryptoJs(CM.getCustMail()));
				callableStatement.setString(8, PGUtils.cryptoJs(CM.getCustMobile()));
				callableStatement.setString(9, CM.getStatus());
				callableStatement.setString(10, CM.getInsertedOn());
				callableStatement.setString(11, CM.getInsertedBy());
				callableStatement.setString(12, CM.getModifiedOn());
				callableStatement.setString(13, CM.getModifiedBy());
				callableStatement.setString(14, CM.getSecretKey());
				callableStatement.setString(15, CM.getStatusCode());
				callableStatement.setString(16, CM.getErrorDesc());
				callableStatement.setString(17, CM.getTokenStatus());
				callableStatement.setString(18, CM.getMessage());
				callableStatement.setString(19, CM.getTransactionId());
				callableStatement.setString(20, CM.getVar1());
				callableStatement.setString(21, CM.getVar2());
				callableStatement.setString(22, CM.getVar3());
				callableStatement.setString(23, CM.getClientReferenceId());
				callableStatement.setString(24, CM.getTokenReferenceId());
				callableStatement.setString(25, CM.getTokenLast4());
				callableStatement.setString(26, CM.getTokenExpiryDate());
				callableStatement.setString(27, CM.getPanLast4());
				callableStatement.setString(28, CM.getEncTokenInfo());
				callableStatement.setString(29, CM.getPaymentAccountReference());
				callableStatement.setString(30, CM.getTokenAssetId());
				callableStatement.setString(31, CM.getOriginalToken());
				callableStatement.setString(32, CM.getPanReferenceId());
				callableStatement.setString(33, CM.getProvider());
				callableStatement.setString(34, CM.getMappedTokenBin());
				callableStatement.setString(35, CM.getIv());
				callableStatement.setString(36, CM.getToken_status());
				callableStatement.setString(37, CM.getCryptogram());
				callableStatement.setString(38, CM.getCustomerId());

				callableStatement.execute();
				conn.commit();
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: saveCardDetails() :: Error Occurred : ", (Throwable) e);

			return;
		} finally {
			try {
				if (callableStatement != null) {
					callableStatement.close();
					callableStatement = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: saveCardDetails() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

	}

	public void saveCardDetails(final CheckOutMaster CM) {
		Connection conn = null;
		CallableStatement callableStatement = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "Call pro_saveCardDetails(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (conn != null) {
				conn.setAutoCommit(false);
				callableStatement = conn.prepareCall(sql);
				callableStatement.setString(1, CM.getMerchantId());
				callableStatement.setString(2, CM.getCardNo());
				callableStatement.setString(3, CM.getExpiryDate());
				callableStatement.setString(4, CM.getCardName());
				callableStatement.setString(5, CM.getCardType());
				callableStatement.setString(6, CM.getInstrumentId());
				callableStatement.setString(7, CM.getCustMail());
				callableStatement.setString(8, CM.getCustMobile());
				callableStatement.setString(9, CM.getStatus());
				callableStatement.setString(10, CM.getInsertedOn());
				callableStatement.setString(11, CM.getInsertedBy());
				callableStatement.setString(12, CM.getModifiedOn());
				callableStatement.setString(13, CM.getModifiedBy());
				callableStatement.setString(14, CM.getSecretKey());
				callableStatement.execute();
				conn.commit();
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: saveCardDetails() :: Error Occurred : ", (Throwable) e);

			return;
		} finally {
			try {
				if (callableStatement != null) {
					callableStatement.close();
					callableStatement = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: saveCardDetails() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

	}

	public int removeTokenizeCard(final CheckOutMaster CM) {

		logger.info(">>>>>>>>>>>::" + CM.getMerchantId() + " " + CM.getModifiedOn() + " " + CM.getToken_status() + " "
				+ CM.getTokenReferenceId() + " " + CM.getMerchantId() + " " + CM.getId());
		Connection conn = null;
		PreparedStatement stmt = null;
		int i = 0;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "update tbl_checkoutmaster set status= ? ,modified_by = ? ,modified_on = ?, token_status=? where tokenReferenceId = ? and merchantId= ? and id= ?";
			if (conn != null) {
				conn.setAutoCommit(false);
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, "I");
				stmt.setString(2, CM.getMerchantId());
				stmt.setString(3, CM.getModifiedOn());
				stmt.setString(4, CM.getToken_status());
				stmt.setString(5, CM.getTokenReferenceId());
				stmt.setString(6, CM.getMerchantId());
				stmt.setString(7, CM.getId());

				i = stmt.executeUpdate();
				conn.commit();
			}
		} catch (Exception e) {
			i = -1;
			logger.error("DataManager.java ::: removeCard() :: Error Occurred : ", (Throwable) e);

			return i;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: removeCard() :: Error Occurred while closing Connection : "
						+ localSQLException6);
			}
		}

		return i;
	}

	public int removeCard(final CheckOutMaster CM) {
		Connection conn = null;
		PreparedStatement stmt = null;
		int i = 0;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "update tbl_checkoutmaster set status= ? ,modified_by = ? ,modified_on = ? where cardNo = ? and merchantId= ? and custMail= ? and custMobile= ?";
			if (conn != null) {
				conn.setAutoCommit(false);
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, "I");
				stmt.setString(2, CM.getMerchantId());
				stmt.setString(3, CM.getModifiedOn());
				stmt.setString(4, CM.getCardNo());
				stmt.setString(5, CM.getMerchantId());
				stmt.setString(6, CM.getCustMail());
				stmt.setString(7, CM.getCustMobile());
				i = stmt.executeUpdate();
				conn.commit();
			}
		} catch (Exception e) {
			i = -1;
			logger.error("DataManager.java ::: removeCard() :: Error Occurred : ", (Throwable) e);

			return i;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: removeCard() :: Error Occurred while closing Connection : "
						+ localSQLException6);
			}
		}

		return i;
	}

	public String binValidate(final String cardNumber, final String binType, String merchantId) {

		logger.info("cardNumber=" + cardNumber + " binType=" + binType);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = "N";

		String type = "";
		if (binType.equalsIgnoreCase("ONUS_CREDIT_EMI")) {
			type = "ONUS_CREDIT";
		} else {
			type = binType;
		}

		try {
			conn = DBConnectionHandler.getConnection();
			if (conn != null) {
				final String sql = "select * from tbl_hdfc_bin_validate where bin_code =  ? and bin_type = ?";

				ps = conn.prepareStatement(sql);
				ps.setString(1, cardNumber);
				ps.setString(2, type);
				rs = ps.executeQuery();
				/*
				 * if (!rs.next()) {} status = "Y";
				 */
				if (rs.next()) {
					status = "Y";
					status = binValidateBlockedCard(cardNumber, binType, merchantId);
					if (binType.equalsIgnoreCase("ONUS_CREDIT_EMI")) {
						String instrumentId = "CC";
						status = binValidateBlockedCardEMI(cardNumber, instrumentId);
					}
					if (status.equalsIgnoreCase("N")) {
						status = "Y";
					}
				}
			} else {
				logger.info("DataManager.java :: connection is null binValidate()");
			}
		} catch (Exception e) {
			status = e.getMessage();
			logger.error("DataManager.java ::: binValidate() :: Error Occurred : ", (Throwable) e);

			return status;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: binValidate() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return status;
	}

	private String binValidateBlockedCardEMI(String cardNumber, String instrumentId) {
		logger.info("cardNumber=" + cardNumber + " instrumentId=" + instrumentId);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = "N";
		try {
			conn = DBConnectionHandler.getConnection();

			if (conn != null) {
				String sql = "select id from block_instrument_bin  where bin = ? and instrument = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, cardNumber);
				ps.setString(2, instrumentId);

				rs = ps.executeQuery();

				if (rs.next()) {
					status = "IDB";
				} else {
					status = "N";
				}
			} else {
				logger.info("DataManager.java :: connection is null binValidateBlockedCardEMI()");
			}
		} catch (Exception e) {
			status = e.getMessage();
			logger.error("DataManager.java ::: binValidateBlockedCardEMI() :: Error Occurred : ", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error(
						"DataManager.java ::: binValidateBlockedCardEMI() :: Error Occurred while closing Connection : ",
						localSQLException4);
			}
		}
		return status;

	}

	public String binValidateOtherCard(final String cardNumber, final String binType, String merchantId) {

		logger.info("cardNumber=" + cardNumber + " binType=" + binType + " merchantId=" + merchantId);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = "N";
		try {
			conn = DBConnectionHandler.getConnection();
			if (conn != null) {
				final String sql = "select * from tbl_hdfc_bin_validate where bin_code =  ? ";

				ps = conn.prepareStatement(sql);
				ps.setString(1, cardNumber);
				rs = ps.executeQuery();
				/* if (!rs.next()) {} */
				/*
				 * while(rs.next()) { status = "Y"; }
				 */

				if (rs.next()) {
					status = "Y";
				} else {
					status = binValidateBlockedCard(cardNumber, binType, merchantId);
				}

			}
			// logger.info("DataManager.java :: connection is null binValidateOtherCard()");
		} catch (Exception e) {
			status = e.getMessage();
			logger.error("DataManager.java ::: binValidateOtherCard() :: Error Occurred : ", (Throwable) e);

			return status;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: binValidate() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return status;
	}

	public String getKeyEncKey() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String keyEncKey = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select Key_Value from tbl_keymaster where id = ? and Key_Name = ?";
			if (conn != null) {
				stmt = conn.prepareStatement("select Key_Value from tbl_keymaster where id = ? and Key_Name = ?");
				stmt.setInt(1, 1);
				stmt.setString(2, "KeyEncryptionKey");
				rs = stmt.executeQuery();
			}
			if (rs != null && rs.next()) {
				keyEncKey = rs.getString(1);
			} else {
				logger.info("DataManager ::: getKeyEncKey() :: KeyEncryptionKey not found.");
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getKeyEncKey() :: Error Occurred : ", (Throwable) e);

			return keyEncKey;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getKeyEncKey() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return keyEncKey;
	}

	/*
	 * public void updateTxn(final TransactionMaster TM) {
	 * logger.info("updateTxn() ::: id="+TM.getId()); Connection conn = null;
	 * PreparedStatement stmt = null; try { conn =
	 * DBConnectionHandler.getConnection(); if (conn != null) { final String sql =
	 * "update tbl_transactionmaster set instrument_id = ?,service_rrn = ?,service_txn_id = ?,service_auth_id = ?,resp_status = ?,trans_status = ?,resp_message = ?,udf3 = ?,udf4 = ?,udf5 = ?,process_id = ?,resp_date_time = ?,Modified_On = ?,Modified_By = ?,bank_id = ?,card_details = ?,card_type = ?,error_code = ?,sp_error_code = ?,rms_score = ?,rms_reason = ?,sur_charge=? where id = ?"
	 * ; logger.info("Query ::: "+sql)); stmt = conn.prepareStatement(sql);
	 * stmt.setString(1, TM.getInstrumentId()); stmt.setString(2,
	 * TM.getServiceRRN()); stmt.setString(3, TM.getServiceTxnId());
	 * stmt.setString(4, TM.getServiceAuthId()); stmt.setString(5,
	 * TM.getRespStatus()); stmt.setString(6, TM.getTransStatus());
	 * stmt.setString(7, TM.getRespMessage()); stmt.setString(8, TM.getUdf3());
	 * stmt.setString(9, TM.getUdf4()); stmt.setString(10, TM.getUdf5());
	 * stmt.setString(11, TM.getProcessId()); stmt.setString(12,
	 * TM.getRespDateTime()); stmt.setString(13, TM.getModified_On());
	 * stmt.setString(14, TM.getModified_By()); stmt.setString(15, TM.getBankId());
	 * stmt.setString(16, TM.getCardDetails()); stmt.setString(17,
	 * TM.getCardType()); stmt.setString(18, TM.getErrorCode()); stmt.setString(19,
	 * TM.getSpErrorCode()); stmt.setString(20, TM.getRmsScore());
	 * stmt.setString(21, TM.getRmsReason()); stmt.setString(22, TM.getSurcharge());
	 * stmt.setString(23, TM.getId()); stmt.executeUpdate(); } } catch (Exception e)
	 * { logger.error("DataManager.java ::: updateTxn() :: Error Occurred : ",
	 * (Throwable)e);
	 * 
	 * return; } finally { try { if (stmt != null) { stmt.close(); stmt = null; } if
	 * (conn != null) { conn.close(); conn = null; } } catch (SQLException
	 * localSQLException6) { logger.
	 * error("DataManager.java ::: updateTxn() :: Error Occurred while closing Connection : "
	 * , (Throwable)localSQLException6); } }
	 * 
	 * }
	 */

	public int updateTxnIntent(final TransactionMaster TM) {
		logger.info("updateTxn() ::: id=" + TM.getId());
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean is_deadlock_or_tiemout = false;
		int tries = 5;
		int status = 0;

		do {
			try {
				logger.info("updateTxn() ::: id=" + TM.getId());
				conn = DBConnectionHandler.getConnection();
				if (conn != null) {
					final String sql = "update tbl_transactionmaster set instrument_id = ?,service_rrn = ?,service_txn_id = ?,service_auth_id = ?,resp_status = ?,trans_status = ?,resp_message = ?,udf3 = ?,udf4 = ?,udf5 = ?,process_id = ?,resp_date_time = ?,Modified_On = ?,Modified_By = ?,bank_id = ?,card_details = ?,card_type = ?,error_code = ?,sp_error_code = ?,rms_score = ?,rms_reason = ?,sur_charge=?,udf6=? where id = ?";
					logger.info("Query ::: " + sql);
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, TM.getInstrumentId());
					stmt.setString(2, TM.getServiceRRN());
					stmt.setString(3, TM.getServiceTxnId());
					stmt.setString(4, TM.getServiceAuthId());
					stmt.setString(5, TM.getRespStatus());
					stmt.setString(6, TM.getTransStatus());
					stmt.setString(7, TM.getRespMessage());
					stmt.setString(8, TM.getUdf3());
					stmt.setString(9, TM.getUdf4());
					stmt.setString(10, TM.getUdf5());
					stmt.setString(11, TM.getProcessId());
					stmt.setString(12, TM.getRespDateTime());
					stmt.setString(13, TM.getModified_On());
					stmt.setString(14, TM.getModified_By());
					stmt.setString(15, TM.getBankId());
					stmt.setString(16, TM.getCardDetails());
					stmt.setString(17, TM.getCardType());
					stmt.setString(18, TM.getErrorCode());
					stmt.setString(19, TM.getSpErrorCode());
					stmt.setString(20, TM.getRmsScore());
					stmt.setString(21, TM.getRmsReason());
					stmt.setString(22, TM.getSurcharge());
					stmt.setString(23, TM.getUdf6());
					stmt.setString(24, TM.getId());

					/* Update transaction master with Mongo DB */
					/*
					 * HashMap<String, String> dataMap = new LinkedHashMap<String, String>();
					 * dataMap.put("instrumentId", TM.getInstrumentId()); dataMap.put("serviceRRN",
					 * TM.getServiceRRN()); dataMap.put("serviceTxnId", TM.getServiceTxnId());
					 * dataMap.put("serviceAuthId", TM.getServiceAuthId());
					 * dataMap.put("respStatus", TM.getRespStatus()); dataMap.put("transStatus",
					 * TM.getTransStatus()); dataMap.put("respMessage", TM.getRespMessage());
					 * dataMap.put("udf3", TM.getUdf3()); dataMap.put("udf4", TM.getUdf4());
					 * dataMap.put("udf5", TM.getUdf5()); dataMap.put("udf6", TM.getUdf6());
					 * dataMap.put("processId", TM.getProcessId()); dataMap.put("respDatTime",
					 * TM.getRespDateTime()); dataMap.put("modifiedOn", TM.getModified_On());
					 * dataMap.put("modifiedBy", TM.getModified_By()); dataMap.put("bankId",
					 * TM.getBankId()); dataMap.put("cardDetails", TM.getCardDetails());
					 * dataMap.put("cardType", TM.getCardType()); dataMap.put("errorCode",
					 * TM.getErrorCode()); dataMap.put("spErrorCode", TM.getSpErrorCode());
					 * dataMap.put("rmsScore", TM.getRmsScore()); dataMap.put("rmsReason",
					 * TM.getRmsReason()); dataMap.put("surcharge", TM.getSurcharge());
					 * dataMap.put("id", TM.getId());
					 * 
					 *  status = 1;
					 */

					/*
					 * MongoDBDataManager mdm = new MongoDBDataManager(); mdm.update(dataMap);
					 */
					stmt.executeUpdate();
					MerchantDTO merchantDTO = new DataManager().getMerchant(TM.getMerchantId());
if(merchantDTO.getIntegrationType().equalsIgnoreCase("1")) {
					logger.info("Response Send to merchant ");
					int value = S2SCallback.PostCallbackResponse(TM.getId());
					logger.info("Response Send to merchant "+value);

}
				

				}
			} catch (SQLException e) {
				logger.error("DataManager.java ::: updateTxn() :: for id=" + TM.getId() + " ----ErrorCode="
						+ e.getErrorCode() + "-------Error Occurred : ", (Throwable) e);

				if (e.getErrorCode() == MysqlErrorNumbers.ER_LOCK_DEADLOCK
						|| e.getErrorCode() == MysqlErrorNumbers.ER_LOCK_WAIT_TIMEOUT) {

					logger.info("Deadlock or lock wait occurs for id=" + TM.getId());
					is_deadlock_or_tiemout = true;

					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {

						e1.printStackTrace();
					}

				} else {
					return status;
				}

			} finally {
				try {
					if (stmt != null) {
						stmt.close();
						stmt = null;
					}
					if (conn != null) {
						conn.close();
						conn = null;
					}
				} catch (SQLException localSQLException6) {
					logger.error("DataManager.java ::: updateTxn() :: Error Occurred while closing Connection : ",
							(Throwable) localSQLException6);
				}
			}
		} while (is_deadlock_or_tiemout && tries-- > 0);


		return status;

	}

	public int updateTxn(final TransactionMaster TM) {
		logger.info("updateTxn() ::: id=" + TM.getId());
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean is_deadlock_or_tiemout = false;
		int tries = 5;
		int status = 0;

		do {
			try {
				logger.info("updateTxn() ::: id=" + TM.getId() + " >>>>>>>> TRIES COUNT=" + tries);
				conn = DBConnectionHandler.getConnection();
				if (conn != null) {
					final String sql = "update tbl_transactionmaster set instrument_id = ?,service_rrn = ?,service_txn_id = ?,service_auth_id = ?,resp_status = ?,trans_status = ?,resp_message = ?,udf3 = ?,udf4 = ?,udf5 = ?,process_id = ?,resp_date_time = ?,Modified_On = ?,Modified_By = ?,bank_id = ?,card_details = ?,card_type = ?,error_code = ?,sp_error_code = ?,rms_score = ?,rms_reason = ?,sur_charge=? where id = ?";
					logger.info("Query ::: " + sql);
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, TM.getInstrumentId());
					stmt.setString(2, TM.getServiceRRN());
					stmt.setString(3, TM.getServiceTxnId());
					stmt.setString(4, TM.getServiceAuthId());
					stmt.setString(5, TM.getRespStatus());
					stmt.setString(6, TM.getTransStatus());
					stmt.setString(7, TM.getRespMessage());
					stmt.setString(8, TM.getUdf3());
					stmt.setString(9, TM.getUdf4());
					stmt.setString(10, TM.getUdf5());
					stmt.setString(11, TM.getProcessId());
					stmt.setString(12, TM.getRespDateTime());
					stmt.setString(13, TM.getModified_On());
					stmt.setString(14, TM.getModified_By());
					stmt.setString(15, TM.getBankId());
					stmt.setString(16, TM.getCardDetails());
					stmt.setString(17, TM.getCardType());
					stmt.setString(18, TM.getErrorCode());
					stmt.setString(19, TM.getSpErrorCode());
					stmt.setString(20, TM.getRmsScore());
					stmt.setString(21, TM.getRmsReason());
					stmt.setString(22, TM.getSurcharge());
					stmt.setString(23, TM.getId());

					/* Update transaction master with Mongo DB */
					
					stmt.executeUpdate();
					status = 1;

				}
			} catch (SQLException e) {
				logger.error("DataManager.java ::: updateTxn() :: for id=" + TM.getId() + " ----ErrorCode="
						+ e.getErrorCode() + "-------Error Occurred : ", (Throwable) e);

				if (e.getErrorCode() == MysqlErrorNumbers.ER_LOCK_DEADLOCK
						|| e.getErrorCode() == MysqlErrorNumbers.ER_LOCK_WAIT_TIMEOUT) {

					logger.info("Deadlock or lock wait occurs for id=" + TM.getId());
					is_deadlock_or_tiemout = true;

					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {

						e1.printStackTrace();
					}

				} else {
					return status;
				}

			} finally {
				try {
					if (stmt != null) {
						stmt.close();
						stmt = null;
					}
					if (conn != null) {
						conn.close();
						conn = null;
					}
				} catch (SQLException localSQLException6) {
					logger.error("DataManager.java ::: updateTxn() :: Error Occurred while closing Connection : ",
							(Throwable) localSQLException6);
				}
			}
		} while (is_deadlock_or_tiemout && tries-- > 0);

		return status;

	}

	public int updateTxnWithCard(final TransactionMaster TM) {
		logger.info("updateTxn() ::: id=" + TM.getId());
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean is_deadlock_or_tiemout = false;
		int tries = 5;
		int status = 0;

		do {
			try {
				logger.info("updateTxn() ::: id=" + TM.getId() + " >>>>>>>> TRIES COUNT=" + tries);
				conn = DBConnectionHandler.getConnection();
				if (conn != null) {
					final String sql = "update tbl_transactionmaster set instrument_id = ?,service_rrn = ?,service_txn_id = ?,service_auth_id = ?,resp_status = ?,trans_status = ?,resp_message = ?,udf3 = ?,udf4 = ?,udf5 = ?, process_id = ?,resp_date_time = ?,Modified_On = ?,Modified_By = ?,bank_id = ?,card_details = ?,card_type = ?,error_code = ?,sp_error_code = ?,rms_score = ?,rms_reason = ?,sur_charge=?,card_name=?, customerId=? where id = ?";
					logger.info("Query ::: " + sql);
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, TM.getInstrumentId());
					stmt.setString(2, TM.getServiceRRN());
					stmt.setString(3, TM.getServiceTxnId());
					stmt.setString(4, TM.getServiceAuthId());
					stmt.setString(5, TM.getRespStatus());
					stmt.setString(6, TM.getTransStatus());
					stmt.setString(7, TM.getRespMessage());
					stmt.setString(8, TM.getUdf3());
					stmt.setString(9, TM.getUdf4());
					stmt.setString(10, TM.getUdf5());
					stmt.setString(11, TM.getProcessId());
					stmt.setString(12, TM.getRespDateTime());
					stmt.setString(13, TM.getModified_On());
					stmt.setString(14, TM.getModified_By());
					stmt.setString(15, TM.getBankId());
					stmt.setString(16, TM.getCardDetails());
					stmt.setString(17, TM.getCardType());
					stmt.setString(18, TM.getErrorCode());
					stmt.setString(19, TM.getSpErrorCode());
					stmt.setString(20, TM.getRmsScore());
					stmt.setString(21, TM.getRmsReason());
					stmt.setString(22, TM.getSurcharge());
					stmt.setString(23, TM.getCardName());
					stmt.setString(24, TM.getCustomerId());
					stmt.setString(25, TM.getId());
					/*
					 * stmt.setString(26, TM.getUdf6());
					 */
					/* Update transaction master with Mongo DB */
					/*
					 * HashMap<String, String> dataMap = new LinkedHashMap<String, String>();
					 * dataMap.put("instrumentId", TM.getInstrumentId()); dataMap.put("serviceRRN",
					 * TM.getServiceRRN()); dataMap.put("serviceTxnId", TM.getServiceTxnId());
					 * dataMap.put("serviceAuthId", TM.getServiceAuthId());
					 * dataMap.put("respStatus", TM.getRespStatus()); dataMap.put("transStatus",
					 * TM.getTransStatus()); dataMap.put("respMessage", TM.getRespMessage());
					 * dataMap.put("udf3", TM.getUdf3()); dataMap.put("udf4", TM.getUdf4());
					 * dataMap.put("udf5", TM.getUdf5()); dataMap.put("udf6", TM.getUdf6());
					 * dataMap.put("processId", TM.getProcessId()); dataMap.put("respDatTime",
					 * TM.getRespDateTime()); dataMap.put("modifiedOn", TM.getModified_On());
					 * dataMap.put("modifiedBy", TM.getModified_By()); dataMap.put("bankId",
					 * TM.getBankId()); dataMap.put("cardDetails", TM.getCardDetails());
					 * dataMap.put("cardType", TM.getCardType()); dataMap.put("errorCode",
					 * TM.getErrorCode()); dataMap.put("spErrorCode", TM.getSpErrorCode());
					 * dataMap.put("rmsScore", TM.getRmsScore()); dataMap.put("rmsReason",
					 * TM.getRmsReason()); dataMap.put("surcharge", TM.getSurcharge());
					 * dataMap.put("id", TM.getId()); MongoDBDataManager mdm = new
					 * MongoDBDataManager(); int i = mdm.update(dataMap);
					 */
					stmt.executeUpdate();
					status = 1;

				}
			} catch (SQLException e) {
				logger.error("DataManager.java ::: updateTxn() :: for id=" + TM.getId() + " ----ErrorCode="
						+ e.getErrorCode() + "-------Error Occurred : ", (Throwable) e);

				if (e.getErrorCode() == MysqlErrorNumbers.ER_LOCK_DEADLOCK
						|| e.getErrorCode() == MysqlErrorNumbers.ER_LOCK_WAIT_TIMEOUT) {

					logger.info("Deadlock or lock wait occurs for id=" + TM.getId());
					is_deadlock_or_tiemout = true;

					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {

						e1.printStackTrace();
					}

				} else {
					return status;
				}

			} finally {
				try {
					if (stmt != null) {
						stmt.close();
						stmt = null;
					}
					if (conn != null) {
						conn.close();
						conn = null;
					}
				} catch (SQLException localSQLException6) {
					logger.error("DataManager.java ::: updateTxn() :: Error Occurred while closing Connection : ",
							(Throwable) localSQLException6);
				}
			}
		} while (is_deadlock_or_tiemout && tries-- > 0);

		return status;

	}

	public TransactionMaster getTxnMaster(final String id) {
		logger.info("getTxnMaster() id=" + id);
		TransactionMaster TM = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		logger.info("DataManager.java ::: getTxnMaster() :: fetch Data against the Txn Id '" + id + "'");
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select txn_id ,merchant_id,date_time,process_id,txn_amount,service_id,return_url,mobile_no,email_id ,udf1,udf2,id,trans_status,instrument_id,udf3,udf4,remote_ip,resp_date_time,udf5,udf6,resp_status,resp_message,service_rrn,bank_id,card_details,card_type,error_code,sp_error_code,rms_score,rms_reason,service_txn_id,sur_charge,Modified_By,UploadedBy,is_posted_back_res,is_verified_once,reseller_Id,reseller_txn_Id from tbl_transactionmaster use index (PRIMARY) where id = ?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, id);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					logger.info("DataManager.java ::: getTxnMaster() :: Inside rs " + id);
					TM = new TransactionMaster();
					/*
					 * TM.setTxnId(rs.getString(1)); TM.setMerchantId(rs.getString(2));
					 * TM.setDateTime(rs.getString(3)); TM.setProcessId(rs.getString(4));
					 * TM.setAmount(rs.getString(5)); TM.setProductId(rs.getString(6));
					 * TM.setReturn_url(rs.getString(7)); TM.setCustMobile(rs.getString(8));
					 * TM.setCustMail(rs.getString(9)); TM.setUdf1(rs.getString(10));
					 * TM.setUdf2(rs.getString(11)); TM.setTransStatus(rs.getString(13));
					 * TM.setInstrumentId(rs.getString(14)); TM.setUdf3(rs.getString(15));
					 * TM.setUdf4(rs.getString(16)); TM.setId(id);
					 * TM.setHostAddress(rs.getString(17)); TM.setRespDateTime(rs.getString(18));
					 * TM.setUdf5(rs.getString(19)); TM.setRespStatus(rs.getString(20));
					 * TM.setRespMessage(rs.getString(21)); TM.setServiceRRN(rs.getString(22));
					 * TM.setBankId(rs.getString(23)); TM.setCardDetails(rs.getString(24));
					 * TM.setCardType(rs.getString(25)); TM.setErrorCode(rs.getString(26));
					 * TM.setSpErrorCode(rs.getString(27)); TM.setRmsScore(rs.getString(28));
					 * TM.setRmsReason(rs.getString(29)); TM.setServiceTxnId(rs.getString(30));
					 * TM.setSurcharge(rs.getString(31)); TM.setModified_By(rs.getString(32));
					 */

					TM.setTxnId(rs.getString("txn_id"));
					TM.setMerchantId(rs.getString("merchant_id"));
					TM.setDateTime(rs.getString("date_time"));
					TM.setProcessId(rs.getString("process_id"));
					TM.setAmount(rs.getString("txn_amount"));
					TM.setProductId(rs.getString("service_id"));
					TM.setReturn_url(rs.getString("return_url"));
					TM.setCustMobile(rs.getString("mobile_no"));
					TM.setCustMail(rs.getString("email_id"));
					TM.setUdf1(rs.getString("udf1"));
					TM.setUdf2(rs.getString("udf2"));
					TM.setTransStatus(rs.getString("trans_status"));
					TM.setInstrumentId(rs.getString("instrument_id"));
					TM.setUdf3(rs.getString("udf3"));
					TM.setUdf4(rs.getString("udf4"));
					TM.setId(rs.getString("id"));
					TM.setHostAddress(rs.getString("remote_ip"));
					TM.setRespDateTime(rs.getString("resp_date_time"));
					TM.setUdf5(rs.getString("udf5"));
					TM.setRespStatus(rs.getString("resp_status"));
					TM.setRespMessage(rs.getString("resp_message"));
					TM.setServiceRRN(rs.getString("service_rrn"));
					TM.setBankId(rs.getString("bank_id"));
					TM.setCardDetails(rs.getString("card_details"));
					TM.setCardType(rs.getString("card_type"));
					TM.setErrorCode(rs.getString("error_code"));
					TM.setSpErrorCode(rs.getString("sp_error_code"));
					TM.setRmsScore(rs.getString("rms_score"));
					TM.setRmsReason(rs.getString("rms_reason"));
					TM.setServiceTxnId(rs.getString("service_txn_id"));
					TM.setSurcharge(rs.getString("sur_charge"));
					TM.setModified_By(rs.getString("Modified_By"));
					TM.setUploadedBy(rs.getString("UploadedBy"));
					TM.setIsPostedBackRes(rs.getString("is_posted_back_res"));
					TM.setIsVerifiedOnce(rs.getString("is_verified_once"));
					TM.setUdf6(rs.getString("udf6"));
					TM.setReseller_id(rs.getString("reseller_Id"));
					TM.setReseller_txn_id(rs.getString("reseller_txn_Id"));

				} else {
					logger.info("DataManager.java ::: getTxnMaster() :: No Data found against the Txn Id '" + id + "'");
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred : ", (Throwable) e);

			return TM;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return TM;
	}
	
	public TransactionMaster getTxnMasterBYServiceId(final String Txnid) {
		logger.info("getTxnMaster() service id=" + Txnid);
		TransactionMaster TM = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		logger.info("DataManager.java ::: getTxnMaster() :: fetch Data against the Txn Id '" + Txnid + "'");
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select txn_id ,merchant_id,date_time,process_id,txn_amount,service_id,return_url,mobile_no,email_id ,udf1,udf2,id,trans_status,instrument_id,udf3,udf4,remote_ip,resp_date_time,udf5,udf6,resp_status,resp_message,service_rrn,bank_id,card_details,card_type,error_code,sp_error_code,rms_score,rms_reason,service_txn_id,sur_charge,Modified_By,UploadedBy,is_posted_back_res,is_verified_once,reseller_Id,reseller_txn_Id from tbl_transactionmaster where service_txn_id = ?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, Txnid);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					logger.info("DataManager.java ::: getTxnMaster() :: Inside rs " + Txnid);
					TM = new TransactionMaster();
					/*
					 * TM.setTxnId(rs.getString(1)); TM.setMerchantId(rs.getString(2));
					 * TM.setDateTime(rs.getString(3)); TM.setProcessId(rs.getString(4));
					 * TM.setAmount(rs.getString(5)); TM.setProductId(rs.getString(6));
					 * TM.setReturn_url(rs.getString(7)); TM.setCustMobile(rs.getString(8));
					 * TM.setCustMail(rs.getString(9)); TM.setUdf1(rs.getString(10));
					 * TM.setUdf2(rs.getString(11)); TM.setTransStatus(rs.getString(13));
					 * TM.setInstrumentId(rs.getString(14)); TM.setUdf3(rs.getString(15));
					 * TM.setUdf4(rs.getString(16)); TM.setId(id);
					 * TM.setHostAddress(rs.getString(17)); TM.setRespDateTime(rs.getString(18));
					 * TM.setUdf5(rs.getString(19)); TM.setRespStatus(rs.getString(20));
					 * TM.setRespMessage(rs.getString(21)); TM.setServiceRRN(rs.getString(22));
					 * TM.setBankId(rs.getString(23)); TM.setCardDetails(rs.getString(24));
					 * TM.setCardType(rs.getString(25)); TM.setErrorCode(rs.getString(26));
					 * TM.setSpErrorCode(rs.getString(27)); TM.setRmsScore(rs.getString(28));
					 * TM.setRmsReason(rs.getString(29)); TM.setServiceTxnId(rs.getString(30));
					 * TM.setSurcharge(rs.getString(31)); TM.setModified_By(rs.getString(32));
					 */

					TM.setTxnId(rs.getString("txn_id"));
					TM.setMerchantId(rs.getString("merchant_id"));
					TM.setDateTime(rs.getString("date_time"));
					TM.setProcessId(rs.getString("process_id"));
					TM.setAmount(rs.getString("txn_amount"));
					TM.setProductId(rs.getString("service_id"));
					TM.setReturn_url(rs.getString("return_url"));
					TM.setCustMobile(rs.getString("mobile_no"));
					TM.setCustMail(rs.getString("email_id"));
					TM.setUdf1(rs.getString("udf1"));
					TM.setUdf2(rs.getString("udf2"));
					TM.setTransStatus(rs.getString("trans_status"));
					TM.setInstrumentId(rs.getString("instrument_id"));
					TM.setUdf3(rs.getString("udf3"));
					TM.setUdf4(rs.getString("udf4"));
					TM.setId(rs.getString("id"));
					TM.setHostAddress(rs.getString("remote_ip"));
					TM.setRespDateTime(rs.getString("resp_date_time"));
					TM.setUdf5(rs.getString("udf5"));
					TM.setRespStatus(rs.getString("resp_status"));
					TM.setRespMessage(rs.getString("resp_message"));
					TM.setServiceRRN(rs.getString("service_rrn"));
					TM.setBankId(rs.getString("bank_id"));
					TM.setCardDetails(rs.getString("card_details"));
					TM.setCardType(rs.getString("card_type"));
					TM.setErrorCode(rs.getString("error_code"));
					TM.setSpErrorCode(rs.getString("sp_error_code"));
					TM.setRmsScore(rs.getString("rms_score"));
					TM.setRmsReason(rs.getString("rms_reason"));
					TM.setServiceTxnId(rs.getString("service_txn_id"));
					TM.setSurcharge(rs.getString("sur_charge"));
					TM.setModified_By(rs.getString("Modified_By"));
					TM.setUploadedBy(rs.getString("UploadedBy"));
					TM.setIsPostedBackRes(rs.getString("is_posted_back_res"));
					TM.setIsVerifiedOnce(rs.getString("is_verified_once"));
					TM.setUdf6(rs.getString("udf6"));
					TM.setReseller_id(rs.getString("reseller_Id"));
					TM.setReseller_txn_id(rs.getString("reseller_txn_Id"));

				} else {
					logger.info("DataManager.java ::: getTxnMaster() :: No Data found against the Txn Id '" + Txnid + "'");
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred : ", (Throwable) e);

			return TM;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return TM;
	}


	public int txnidverify(String txn_Id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		int midcount = 0;
		int trxcount = 0;
//		logger.info("to check duplicate txn id--- "+txn_Id+"count---"+count);
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "SELECT   count(merchant_ID) FROM tbl_transactionmaster where txn_Id = '" + txn_Id
					+ "' ";//
			if (conn != null) {// count(*) ,
				stmt = conn.prepareStatement(sql);
				// stmt.setString(1, mi);txn_Id

				rs = stmt.executeQuery();

			}
			logger.info(
					"-------------------------------------------------------------------------------------- rs" + rs);
			if (rs != null && rs.next()) {
				// trxcount = rs.getInt(1);
				midcount = rs.getInt(1);
				logger.info("midcount====" + midcount + "-----------------------------trxcount===" + trxcount);
			} else {
				logger.info("DataManager ::: txn_check :: enable not found.");
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: txn_check   :: Error Occurred : ", (Throwable) e);

			return midcount;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getKeyEncKey() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return midcount;
	}

	public List<String> getCalculateSurcharge(String bankId, String sInstrumentId, String Amount, String merchantid,
			String emioption, String txnid) {
		logger.info("getCalculateSurcharge() bankId= " + bankId + " sInstrumentId=" + sInstrumentId + " Amount="
				+ Amount + " merchantid=" + merchantid + " emioption=" + emioption + " txnid=" + txnid);
		String sClassFileLoader = null, spId = null, surchargeData = null;
		int payout = 0;
		Double retamount = Double.valueOf(0.0D);
		Connection con = null;
		CallableStatement cs = null;
		List<String> lstdata = null;
		int status = 0;

		try {
			logger.info("DataManager.java :: getCalculateSurcharge() :: txnid=" + txnid + " bankId :: " + bankId
					+ " sInstrumentId :::" + sInstrumentId + " getAmount::" + Amount + "  getMerchantId ::" + merchantid
					+ "   emioption=" + emioption);
			con = DBConnectionHandler.getConnection();
			// String query = "{call pr_calculateSurcharge1(?,?,?,?,?,?,?)}";
			if (con != null) {
				String query = "{call pr_calculateSurchargeInstrumentWise(?,?,?,?,?,?,?,?)}";
				cs = con.prepareCall(query);
				cs.setString(1, bankId);
				cs.setString(2, sInstrumentId);
				cs.setString(3, merchantid);
				cs.setString(4, Amount);
				cs.registerOutParameter(5, 8);
				cs.registerOutParameter(6, 12);
				cs.registerOutParameter(7, 12);
				cs.registerOutParameter(8, 12);
				// cs.setString(8,emioption);
				status = cs.executeUpdate();

				logger.info(
						"DataManager.java :: getCalculateSurcharge() :: txnid=" + txnid + " Status >>>>>> " + status);

				retamount = Double.valueOf(cs.getDouble(5));
				sClassFileLoader = cs.getString(6);
				spId = cs.getString(7);
				payout = cs.getInt(8);

				lstdata = new ArrayList<String>();
				lstdata.add(retamount + "");
				lstdata.add(sClassFileLoader);
				lstdata.add(spId);
				lstdata.add(String.valueOf(payout));

				logger.info("DataManager.java :: getCalculateSurcharge() :: txnid=" + txnid + " retamount :::: "
						+ retamount + " sClassFileLoader :::: " + sClassFileLoader + " retspid  ::: " + spId);
				return lstdata;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DataManager.java :: getCalculateSurcharge() :: txnid=" + txnid
					+ " :::while procedure pr_calculateSurcharge :::Getting Error ", e);

		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("DataManager.java :: getCalculateSurcharge() :: txnid=" + txnid
						+ "  :::while procedure pr_calculateSurcharge :::Getting Error ", e);
			}
		}
		return lstdata;

	}

	public TransactionMaster getTransMasterbyAuthID(final String authId) {
		logger.info("getTransMasterbyAuthID() authId=" + authId);
		TransactionMaster TM = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select id ,merchant_id,date_time,process_id,txn_amount,service_id,return_url,mobile_no,email_id ,udf1,udf2,id,trans_status,instrument_id,udf3,udf4,remote_ip,resp_date_time,udf5,resp_status,resp_message,service_rrn,bank_id,card_details,card_type,error_code,sp_error_code,rms_score,rms_reason,service_txn_id,sur_charge from tbl_transactionmaster where service_auth_id = ?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, authId);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					TM = new TransactionMaster();
					TM.setMerchantId(rs.getString(2));
					TM.setDateTime(rs.getString(3));
					TM.setProcessId(rs.getString(4));
					TM.setAmount(rs.getString(5));
					TM.setProductId(rs.getString(6));
					TM.setReturn_url(rs.getString(7));
					TM.setCustMobile(rs.getString(8));
					TM.setCustMail(rs.getString(9));
					TM.setUdf1(rs.getString(10));
					TM.setUdf2(rs.getString(11));
					TM.setTransStatus(rs.getString(13));
					TM.setInstrumentId(rs.getString(14));
					TM.setUdf3(rs.getString(15));
					TM.setUdf4(rs.getString(16));
					TM.setId(rs.getString(1));
					TM.setHostAddress(rs.getString(17));
					TM.setRespDateTime(rs.getString(18));
					TM.setUdf5(rs.getString(19));
					TM.setRespStatus(rs.getString(20));
					TM.setRespMessage(rs.getString(21));
					TM.setServiceRRN(rs.getString(22));
					TM.setBankId(rs.getString(23));
					TM.setCardDetails(rs.getString(24));
					TM.setCardType(rs.getString(25));
					TM.setErrorCode(rs.getString(26));
					TM.setSpErrorCode(rs.getString(27));
					TM.setRmsScore(rs.getString(28));
					TM.setRmsReason(rs.getString(29));
					TM.setServiceTxnId(rs.getString(30));
					TM.setSurcharge(rs.getString(31));
				} else {
					logger.info("DataManager.java ::: getTransMasterbyAuthID() :: No Data found against the Auth Id '"
							+ authId + "'");
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getTransMasterbyAuthID() :: Error Occurred : ", (Throwable) e);

			return TM;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return TM;
	}

//--------------------------------------- Risk Mangement-------------------
	public RiskGlobalDTO RiskGolbalIp(String CustIp, String Mid) {
		logger.info("DataManager.java ::: Rms() :");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		RiskGlobalDTO RC = null;

		int i = 1;
		try {

			conn = DBConnectionHandler.getConnection();
			final String sql = "Call pro_riskFieldManagement(?,?)";
			if (conn != null) {
				stmt = conn.prepareStatement("Call pro_riskFieldManagement(?,?)");
				stmt.setString(1, "1");
				stmt.setString(2, "");
				rs = stmt.executeQuery();
				if (rs != null) {
					RC = new RiskGlobalDTO();
					while (rs.next()) {
						logger.info("DataManager.java :::" + rs.getString(1));
						logger.info("DataManager.java :::" + rs.getString(2));
						if (rs.getString(2).equalsIgnoreCase("IP_Blacklist")) {
							RC.setCustIp(rs.getString(2));
							RC.setCustIp(rs.getString(4));
							RC.setCustIp_RiskCode(rs.getString(9));
						} else if (rs.getString(2).equalsIgnoreCase("Country_code")) {
							RC.setCountry_code(rs.getString(2));
							RC.setCountry_Value(rs.getString(4));
							RC.setCountry_coderiskcode(rs.getString(9));
						} else if (rs.getString(2).equalsIgnoreCase("pincode")) {
							RC.setPin_code(Mid);

							RC.setPincodeValue(rs.getString(4));
							RC.setRisk_code(rs.getString(9));
						}
					}
					/*
					 * if(rs.getString(1).equalsIgnoreCase("IP_Blacklist")) {
					 * RC.setCustIp(rs.getString(1));
					 * logger.info("DataManager.java ::: Rms() :::"+RC.getCustIp());
					 * 
					 * }
					 */

				} else {

				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getTransMasterbyAuthID() :: Error Occurred : ", (Throwable) e);

			return RC;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return RC;
	}

	public List getBankList() {
		final List lstBank = new ArrayList();
		Connection conn = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "call pro_bankList()";
			if (conn != null) {
				callableStatement = conn.prepareCall("call pro_bankList()");
				callableStatement.execute();
				rs = callableStatement.getResultSet();
				while (rs.next()) {
					final BankMaster bank = new BankMaster();
					bank.setBankId(rs.getString("bank_id"));
					bank.setBankName(rs.getString("bank_name"));
					lstBank.add(bank);
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getBankList() :: Error Occurred : ", (Throwable) e);

			return lstBank;
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
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getBankList() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return lstBank;
	}

	public List getInstrumentBankList(final String merchantId, final String amount) {
		logger.info("getInstrumentBankList() :: merchantId=" + merchantId + " amount=" + amount);
		final LinkedHashMap<String, String> bankHash = new LinkedHashMap<String, String>();
		final List returnlist = new ArrayList();
		final Hashtable instrumentHash = new Hashtable();
		Connection conn = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "call pro_instrumentbank(?,?)";
			if (conn != null) {
				// callableStatement = conn.prepareCall("call pro_instrumentbank(?,?)");
				callableStatement = conn.prepareCall("call pro_instrumentbankNew(?,?)");
				callableStatement.setString(1, merchantId);
				callableStatement.setString(2, amount);
				callableStatement.execute();
				rs = callableStatement.getResultSet();
				while (rs.next()) {
					if (instrumentHash.get(rs.getString("Instrument_Id")) == null) {
						instrumentHash.put(rs.getString("Instrument_Id"), rs.getString("Instrument_Name"));
					}
					if(rs.getString("isintent") !=null && rs.getString("Instrument_Id").equalsIgnoreCase("UPI") && (rs.getString("isintent").equalsIgnoreCase("1") || rs.getString("isintent").equalsIgnoreCase("0") || rs.getString("isintent").equalsIgnoreCase("2")))
					{
						instrumentHash.put("isintentUPI", rs.getString("isintent"));
					}
					if (rs.getString("Bank_Id") != null && rs.getString("Instrument_Id").equalsIgnoreCase("NB")) {
						bankHash.put(rs.getString("Bank_Id"), rs.getString("Bank_Name"));
					}

					if (rs.getString("Bank_Id") != null && rs.getString("Bank_Id").equalsIgnoreCase("BQR Cards")) {
						instrumentHash.put("BQR", "BQR");
					}
					if (rs.getString("Bank_Id") != null && rs.getString("Bank_Id").equalsIgnoreCase("ATOMUPI")) {
						instrumentHash.put("ATOMUPI", "ATOMUPI");
						instrumentHash.remove("UPI");
					}
				}
				returnlist.add(0, instrumentHash);
				returnlist.add(1, bankHash);

				logger.info("getInstrumentBankList() >>>>>>>result>>>>>>>>>>>>> " + returnlist.toString());
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getInstrumentBankList() :: Error Occurred : ", (Throwable) e);

			return returnlist;
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
			} catch (SQLException localSQLException6) {
				logger.error(
						"DataManager.java ::: getInstrumentBankList() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return returnlist;
	}

	private ArrayList<String> getClassFileLoader(final String sServiceProviderId, final Connection conn) {
		logger.info("getClassFileLoader() :: sServiceProviderId=" + sServiceProviderId + " conn=" + conn);
		final ArrayList<String> sClassFileLoader = new ArrayList<String>();
		final String sSql = "select sp_class_invoker,sp_id from tbl_mstserviceprovider where sp_id = ?";
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		try {
			prepareStatement = conn.prepareStatement(sSql);
			prepareStatement.setString(1, sServiceProviderId);
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				sClassFileLoader.add(resultSet.getString(1));
				sClassFileLoader.add(resultSet.getString(2));
			}
			resultSet.close();
			prepareStatement.close();
		} catch (SQLException e) {
			logger.error("DataManager.java ::: getClassFileLoader() :: Error Occurred : ", (Throwable) e);

			return sClassFileLoader;
		} finally {
			try {
				if (prepareStatement != null) {
					prepareStatement.close();
					prepareStatement = null;
				}
				if (resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
			} catch (SQLException e4) {
				logger.error("DataManager.java ::: getClassFileLoader() :: Error Occurred while closing Connection : ",
						(Throwable) e4);
			}
		}

		return sClassFileLoader;
	}

	public ArrayList<String> getProviderDetails(final String sInstrumentId, final String bankId) {
		logger.info("getProviderDetails():::::sInstrumentId::::" + sInstrumentId + "bankId" + bankId);
		String sProviderSQL = null;
		ArrayList<String> sClassFile = null;
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		try {
			sProviderSQL = "select sp_id, sp_bank_id from tbl_sp_bank_mapping where instrument_id = ? and bank_id = ? order by preference asc";
			if (con != null) {
				ps = con.prepareStatement(sProviderSQL);
				ps.setString(1, sInstrumentId);
				ps.setString(2, bankId);
				logger.info("sProviderSQL : " + sProviderSQL);
				rs = ps.executeQuery();
				if (rs.next()) {
					sClassFile = this.getClassFileLoader(rs.getString(1), con);
					sClassFile.add(rs.getString(2));
				}
				rs.close();
				ps.close();
				con.close();
			}
		} catch (SQLException e) {
			logger.error("DataManager.java ::: getProviderDetails() :: Error Occurred : ", (Throwable) e);

			return sClassFile;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.error("DataManager.java ::: getProviderDetails() :: Error Occurred while closing Connection : ",
						(Throwable) e4);
			}
		}

		return sClassFile;
	}

	public ArrayList<String> getSPMidKey(final String mid, final String bankId, final String instrumentId,
			final String spId) {
		logger.info("getSPMidKey(_) :: mid=" + mid + " bankId=" + bankId + " instrumentId=" + instrumentId + " spId="
				+ spId);
		String sProviderSQL = null;
		ArrayList<String> spMidKey = new ArrayList<String>();
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		try {
			// sProviderSQL = "select mid, tid from tbl_merchant_mdr where merchant_id = '"
			// + mid + "' and sp_id = '" + spId + "' and instrument_id = '" + instrumentId +
			// "' and bank_id = '" + bankId + "'";
			sProviderSQL = "select mid, tid from tbl_merchant_mdr where merchant_id =? and sp_id = ? and instrument_id =? and bank_id = ?";
			logger.info("getSPMidKey() : " + sProviderSQL);
			if (con != null) {
				ps = con.prepareStatement(sProviderSQL);
				ps.setString(1, mid);
				ps.setString(2, spId);
				ps.setString(3, instrumentId);
				ps.setString(4, bankId);
				rs = ps.executeQuery();
				if (rs.next()) {
					logger.info("==================================inside while===============================");
					spMidKey.add(rs.getString(1));
					spMidKey.add(rs.getString(2));
					logger.info("spMidKey ::: " + spMidKey.get(0).toString());
					logger.info("spMidKey ::: " + spMidKey.get(1).toString());
				}

				rs.close();
				ps.close();
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("DataManager.java ::: getSPMidKey() :: Error Occurred : ", (Throwable) e);

			return spMidKey;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.error("DataManager.java ::: getSPMidKey() :: Error Occurred while closing Connection : ",
						(Throwable) e4);
			}
		}

		return spMidKey;
	}

	public String getSPKey(final String mid) {
		logger.info("getSPKey() mid=" + mid);
		String sProviderSQL = null;
		String spKey = null;
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		try {
			sProviderSQL = "select distinct tid from tbl_merchant_mdr where mid = ?";
			if (con != null) {
				ps = con.prepareStatement(sProviderSQL);
				ps.setString(1, mid);
				logger.info("getSPKey() : " + sProviderSQL);
				rs = ps.executeQuery();
				if (rs.next()) {
					spKey = rs.getString(1);
				}
				rs.close();
				ps.close();
				con.close();
			}
		} catch (SQLException e) {
			logger.error("DataManager.java ::: getSPKey() :: Error Occurred : ", (Throwable) e);

			return spKey;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.info("DataManager.java ::: getSPKey() :: Error Occurred while closing Connection : ",
						(Throwable) e4);
			}
		}

		return spKey;
	}

	public String getMerchantVPA(String mid, String Sp_Id, String instrument_Id) {
		logger.info("Merchnat() mid=" + mid);
		logger.info("Merchnat() Sp_id=" + Sp_Id);
		logger.info("Merchnat() instrument_Id=" + instrument_Id);

		String Query = null;
		String Merchant_Vpa = null;
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		try {
			Query = "select response ->>'$.merVirtualAdd' as Merchant_Vpa from tbl_mstsubmerchantdata where Merchant = ? and sp_id=? and instrument=?";
			if (con != null) {
				ps = con.prepareStatement(Query);
				ps.setString(1, mid);
				ps.setString(2, Sp_Id);
				ps.setString(3, instrument_Id);

				logger.info("Query() : " + Query);
				rs = ps.executeQuery();
				if (rs.next()) {
					Merchant_Vpa = rs.getString("Merchant_Vpa");
				}
				logger.info("MerchantVpa::::::::: " + Merchant_Vpa);
				rs.close();
				ps.close();
				con.close();
			}
		} catch (SQLException e) {
			logger.error("DataManager.java ::: getSPKey() :: Error Occurred : ", (Throwable) e);

			return Merchant_Vpa;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.info("DataManager.java ::: Merchant() :: Error Occurred while closing Connection : ",
						(Throwable) e4);
			}
		}

		return Merchant_Vpa;
	}

	public String getMerchantVPA_INB(String mid, String spId, String instrumentId) {
		logger.info("Merchnat ::  mid= {} : Sp_id= {} : instrument_Id= {} ", mid, spId, instrumentId);

		String query = null;
		String merchantVpa = null;
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		try {
			query = "select request ->>'$.paymentAddress' as Merchant_Vpa from tbl_mstsubmerchantdata where Merchant = ? and sp_id=? and instrument=?";
			if (con != null) {
				ps = con.prepareStatement(query);
				ps.setString(1, mid);
				ps.setString(2, spId);
				ps.setString(3, instrumentId);

				logger.info("Query :: {} ", query);

				rs = ps.executeQuery();
				if (rs.next()) {
					merchantVpa = rs.getString("Merchant_Vpa");
				}
				logger.info("MerchantVpa::::::::: {} ", merchantVpa);
				rs.close();
				ps.close();
				con.close();
			}
		} catch (SQLException e) {
			logger.error("DataManager.java ::: getSPKey() :: Error Occurred : ", (Throwable) e);

			return merchantVpa;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.info("DataManager.java ::: Merchant() :: Error Occurred while closing Connection : ",
						(Throwable) e4);
			}
		}

		return merchantVpa;
	}

	public String getIsLoaderValue(final String mid) {
		logger.info("getIsLoaderAccess() mid=" + mid);
		String Query1 = null;
		String loaderValue = null;
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		try {
			Query1 = "select is_loader_access from tbl_mstmerchant where MerchantId = ?";
			if (con != null) {
				ps = con.prepareStatement(Query1);
				ps.setString(1, mid);
				logger.info("loader() : " + Query1);
				rs = ps.executeQuery();
				if (rs.next()) {
					loaderValue = rs.getString(1);
				}
				rs.close();
				ps.close();
				con.close();
			}
		} catch (SQLException e) {
			logger.error("DataManager.java ::: loaderValue() :: Error Occurred : ", (Throwable) e);

			return loaderValue;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.info("DataManager.java ::: loaderValue() :: Error Occurred while closing Connection : ",
						(Throwable) e4);
			}
		}

		return loaderValue;
	}

	public static JSONObject getErrorCode(final String spErrorCode, final String spId) {

		logger.info("getErrorCode() spErrorCode=" + spErrorCode + "   spId=" + spId);
		String sProviderSQL = null;
		PreparedStatement ps = null;
		JSONObject respJSON = new JSONObject();
		;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		try {
			sProviderSQL = "select mp.agg_error_code, ms.error_code_desc from tbl_error_code_mapping mp inner join tbl_error_code_master ms on mp.agg_error_code = ms.error_code where sp_id = ? and sp_error_code = ?";
			if (con != null) {
				ps = con.prepareStatement(sProviderSQL);
				ps.setString(1, spId);
				ps.setString(2, spErrorCode);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					respJSON.put("aggErrorCode", rs.getString("agg_error_code"));
					respJSON.put("aggErrorDesc", rs.getString("error_code_desc"));
				} else {
					respJSON.put("aggErrorCode", "FFFFF");
					respJSON.put("aggErrorDesc", "Transaction Failed.Undefined Error.");
				}
			}
		} catch (SQLException e) {
			logger.error("DataManager.java ::: getSPKey() :: Error Occurred : ", (Throwable) e);
			respJSON.put("aggErrorCode", "NA");
			respJSON.put("aggErrorDesc", "Please confirm the Transaction Status using Transaction Deatils API.");

			return respJSON;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.error("DataManager.java ::: getSPKey() :: Error Occurred while closing Connection : ",
						(Throwable) e4);
			}
		}

		return respJSON;
	}

	public TransactionMaster getTxnVerification(final String txnid, final String merchantid) {
		logger.info("getTxnVerification() >>>>>>>>>>>>>>>>>>>txnid=" + txnid + "merchantid=" + merchantid);
		final TransactionMaster TM = new TransactionMaster();
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			logger.info("Starting Conditons====================" + txnid + " " + merchantid);
			if (txnid != null && merchantid != null && !txnid.isEmpty() && !merchantid.isEmpty()) {
				conn = DBConnectionHandler.getConnection();
				// final String sql = "select txn_id ,merchant_id, id, date_time, trans_status,
				// txn_amount, resp_message, service_rrn, return_url, error_code, instrument_id,
				// resp_date_time,mobile_no,email_id,udf1,udf2,udf3,udf4,udf5, rodt from
				// tbl_transactionmaster where txn_id = ? and merchant_id = ?";

				final String sql = "select txn_id ,merchant_id, id, date_time, trans_status, txn_amount, resp_message, service_rrn, return_url, error_code, instrument_id, resp_date_time,mobile_no,email_id,udf1,udf2,udf3,udf4,udf5, rodt from tbl_transactionmaster where  merchant_id = ? and txn_id=? union select txn_id ,merchant_id, id, date_time, trans_status, txn_amount, resp_message, service_rrn, return_url, error_code, instrument_id, resp_date_time,mobile_no,email_id,udf1,udf2,udf3,udf4,udf5, rodt from tbl_transactionmasterhst where  merchant_id = ? and txn_id=?";
				logger.info("DataManager.java ::: getTxnVerification()11 :: '" + sql);
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchantid);
				stmt.setString(2, txnid);
				stmt.setString(3, merchantid);
				stmt.setString(4, txnid);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					logger.info("Found Rs ========== if===========" + rs);
					logger.info("DataManager.java ::: getTxnVerification()22 rs :: Date Time Received from DB : "
							+ rs.getString(4) + "," + rs.getString(10) + "," + rs.getString(11));
					TM.setTxnId(rs.getString(1));
					TM.setMerchantId(rs.getString(2));
					TM.setId(rs.getString(3));
					TM.setDateTime(rs.getString(4));
					TM.setTransStatus(rs.getString(5));
					TM.setAmount(rs.getString(6));
					TM.setRespMessage(rs.getString(7));
					TM.setServiceRRN(rs.getString(8));
					TM.setReturn_url(rs.getString(9));
					TM.setErrorCode(rs.getString(10));
					TM.setInstrumentId(rs.getString(11));
					TM.setRespDateTime(rs.getString(12));
					TM.setCustMobile(rs.getString(13));
					TM.setCustMail(rs.getString(14));
					TM.setUdf1(rs.getString(15));
					TM.setUdf2(rs.getString(16));
					TM.setUdf3(rs.getString(17));
					TM.setUdf4(rs.getString(18));
					TM.setUdf5(rs.getString(19));
				} else {
					logger.info("Enter Else condition=====================+++");
					String query = "select txn_id ,merchant_id, id, date_time, trans_status, txn_amount, resp_message, service_rrn, return_url, error_code, instrument_id, resp_date_time,mobile_no,email_id,udf1,udf2,udf3,udf4,udf5, rodt from tbl_timedouttransactionmaster where txn_id = ? and merchant_id = ?";
					logger.info("DataManager.java ::: getTxnVerification() :: tbl_timedouttransactionmaster" + query);

					stmt1 = conn.prepareStatement(query);
					stmt1.setString(1, txnid);
					stmt1.setString(2, merchantid);
					rs1 = stmt1.executeQuery();
					if (rs1 != null && rs1.next()) {
						logger.info(
								("DataManager.java ::: getTxnVerification()33 rs2 else ::txn found in tbl_timedouttransactionmaster"
										+ rs));
						TM.setTxnId(rs1.getString(1));
						TM.setMerchantId(rs1.getString(2));
						TM.setId(rs1.getString(3));
						TM.setDateTime(rs1.getString(4));
						TM.setTransStatus(rs1.getString(5));
						TM.setAmount(rs1.getString(6));
						TM.setRespMessage(rs1.getString(7));
						TM.setServiceRRN(rs1.getString(8));
						TM.setReturn_url(rs1.getString(9));
						TM.setErrorCode(rs1.getString(10));
						TM.setInstrumentId(rs1.getString(11));
						TM.setRespDateTime(rs1.getString(12));
						TM.setCustMobile(rs1.getString(13));
						TM.setCustMail(rs1.getString(14));
						TM.setUdf1(rs1.getString(15));
						TM.setUdf2(rs1.getString(16));
						TM.setUdf3(rs1.getString(17));
						TM.setUdf4(rs1.getString(18));
						TM.setUdf5(rs1.getString(19));
					} else {
						logger.info("Enter 2nd Else===========================");
						logger.info(
								"DataManager.java ::: getTxnVerification() :: No data found in both tbl_transactionmaster and tbl_timedouttransactionmaster");
						TM.setTxnId(txnid);
						TM.setMerchantId(merchantid);
						TM.setId("NA");
						TM.setDateTime("NA");
						TM.setTransStatus("NA");
						TM.setAmount("NA");
						TM.setRespMessage("Invalid Merchant ID or Txn ID");
						TM.setServiceRRN("NA");
						TM.setReturn_url("NA");
						TM.setErrorCode("NA");
						TM.setInstrumentId("NA");
						TM.setRespDateTime("NA");
						TM.setCustMobile("NA");
						TM.setCustMail("NA");
						TM.setUdf1("NA");
						TM.setUdf2("NA");
						TM.setUdf3("NA");
						TM.setUdf4("NA");
						TM.setUdf5("NA");
					}
				}
			} else {
				logger.info("Enter 3nd Else===========================");

				logger.info(
						"DataManager.java ::: getTxnVerification() :: Merchant Id or Txn Id is Null or Blank : MID -> "
								+ merchantid + " , Txn ID -> " + txnid);
				TM.setTxnId("NA");
				TM.setMerchantId("NA");
				TM.setId("NA");
				TM.setDateTime("NA");
				TM.setTransStatus("NA");
				TM.setAmount("NA");
				TM.setRespMessage("Merchant Id or Txn Id is Null or Blank");
				TM.setServiceRRN("NA");
				TM.setReturn_url("NA");
				TM.setErrorCode("NA");
				TM.setInstrumentId("NA");
			}
		} catch (Exception e) {
			logger.info("Enter  catch===========================");

			logger.error("DataManager.java ::: getTxnVerification() :: Error Occurred while fetching Data for Txn Id '"
					+ txnid + "' And MID '" + merchantid + "' : ", e);
			TM.setTxnId(txnid);
			TM.setMerchantId(merchantid);
			TM.setId("NA");
			TM.setDateTime("NA");
			TM.setTransStatus("NA");
			TM.setAmount("NA");
			TM.setRespMessage("Error Occurred while fetching Data.");
			TM.setServiceRRN("NA");
			TM.setReturn_url("NA");
			TM.setErrorCode("NA");
			TM.setInstrumentId("NA");

			return TM;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (stmt1 != null) {
					stmt1.close();
					stmt1 = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se3) {
				logger.error("DataManager.java ::: getTxnVerification() :: Error Occurred while closing Connection : ",
						(Throwable) se3);
			}
		}

		return TM;
	}

	public Map<String, String> getRMSDetails(final String merchantId, final String ipAddress, final String txtAmt,
			final String cardNo, final String cardType) {

		logger.info("getRMSDetails() merchantId=" + merchantId + " ipAddress=" + ipAddress + " txtAmt==" + txtAmt
				+ " carNo=" + cardNo + " cardType=" + cardType);
		Map<String, String> rmsData = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Connection con = DBConnectionHandler.getConnection();
		try {
			if (con != null) {
				final String query = "{call pro_RMSAPI(?,?,?,?,?)}";
				callableStatement = con.prepareCall("{call pro_RMSAPI(?,?,?,?,?)}");
				callableStatement.setString(1, merchantId);
				callableStatement.setString(2, ipAddress);
				callableStatement.setString(3, txtAmt);
				callableStatement.setString(4, cardNo);
				callableStatement.setString(5, cardType);
				logger.info(
						"Pass Value RMS Procedure : {" + merchantId + "," + ipAddress + "," + txtAmt + "," + cardType);
				callableStatement.execute();
				rs = callableStatement.getResultSet();
				final ResultSetMetaData rsmd = rs.getMetaData();
				final int columnCount = rsmd.getColumnCount();
				if (rs != null) {
					rmsData = new HashMap<String, String>();
					while (rs.next()) {
						for (int i = 1; i <= columnCount; ++i) {
							rmsData.put(rsmd.getColumnLabel(i), rs.getString(rsmd.getColumnLabel(i)));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return rmsData;
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (callableStatement != null) {
					callableStatement.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
				logger.error(e2.getMessage(), e2);

			}
		}
		return rmsData;
	}

	/*
	 * public RefundRequestAPI getRefundTxnDetails(final String merchantid, final
	 * String txnid, final String refundAmount, final String atrn) { final
	 * RefundRequestAPI RRA = new RefundRequestAPI(); Connection conn = null;
	 * ResultSet rs = null; CallableStatement callableStatement = null; try { if
	 * (txnid != null && merchantid != null && !txnid.isEmpty() &&
	 * !merchantid.isEmpty()) { conn = DBConnectionHandler.getConnection(); if (conn
	 * == null) {} final String query = "{call pro_refundApplyAPI(?,?,?)}";
	 * callableStatement = conn.prepareCall("{call pro_refundApplyAPI(?,?,?)}");
	 * callableStatement.setString(1, merchantid); callableStatement.setString(2,
	 * txnid); callableStatement.setString(3, refundAmount);
	 * callableStatement.execute(); rs = callableStatement.getResultSet();
	 * Label_0254: { if (rs != null) { if (rs.next()) { break Label_0254; } } }
	 * RRA.setId(atrn); RRA.setTxnId(txnid); RRA.setMerchantId(merchantid);
	 * RRA.setRefund_amount(refundAmount);
	 * RRA.setRespMessage(rs.getString("respMessage"));
	 * RRA.setErrorCode(rs.getString("error_code"));
	 * RRA.setRefund_type(rs.getString("refundType")); if
	 * (!rs.getString("error_code").equalsIgnoreCase("RF001")) {
	 * RRA.setAmount(rs.getString("txn_amount"));
	 * RRA.setServiceRRN(rs.getString("service_rrn")); } RRA.setAmount("NA");
	 * RRA.setServiceRRN("NA"); } logger.
	 * info("DataManager.java ::: getRefundTxnDetails() :: Merchant Id or Txn Id is Null or Blank : MID -> "
	 * + merchantid + " , Txn ID -> " + txnid)); RRA.setTxnId("NA");
	 * RRA.setMerchantId("NA"); RRA.setId("NA"); RRA.setDateTime("NA");
	 * RRA.setTransStatus("NA"); RRA.setAmount("NA");
	 * RRA.setRespMessage("Merchant Id or Txn Id is Null or Blank");
	 * RRA.setServiceRRN("NA"); RRA.setReturn_url("NA"); RRA.setErrorCode("NA");
	 * RRA.setInstrumentId("NA"); } catch (Exception e) { logger.
	 * error("DataManager.java ::: getRefundTxnDetails() :: Error Occurred while fetching Data for Txn Id '"
	 * + txnid + "' And MID '" + merchantid + "' : "), (Throwable)e);
	 * RRA.setTxnId(txnid); RRA.setMerchantId(merchantid); RRA.setId("NA");
	 * RRA.setDateTime("NA"); RRA.setTransStatus("NA"); RRA.setAmount("NA");
	 * RRA.setRespMessage("Error Occurred while fetching Data.");
	 * RRA.setServiceRRN("NA"); RRA.setReturn_url("NA"); RRA.setErrorCode("NA");
	 * RRA.setInstrumentId("NA"); try { if (rs != null) { rs.close(); rs = null; }
	 * if (callableStatement != null) { callableStatement.close(); callableStatement
	 * = null; } if (conn != null) { conn.close(); conn = null; } } catch
	 * (SQLException se) { logger.
	 * error("DataManager.java ::: getRefundTxnDetails() :: Error Occurred while closing Connection : "
	 * , (Throwable)se); } try { if (rs != null) { rs.close(); rs = null; } if
	 * (callableStatement != null) { callableStatement.close(); callableStatement =
	 * null; } if (conn != null) { conn.close(); conn = null; } } catch
	 * (SQLException se2) { logger.
	 * error("DataManager.java ::: getRefundTxnDetails() :: Error Occurred while closing Connection : "
	 * , (Throwable)se2); } return RRA; } finally { try { if (rs != null) {
	 * rs.close(); rs = null; } if (callableStatement != null) {
	 * callableStatement.close(); callableStatement = null; } if (conn != null) {
	 * conn.close(); conn = null; } } catch (SQLException se3) { logger.
	 * error("DataManager.java ::: getRefundTxnDetails() :: Error Occurred while closing Connection : "
	 * , (Throwable)se3); } } try { if (rs != null) { rs.close(); rs = null; } if
	 * (callableStatement != null) { callableStatement.close(); callableStatement =
	 * null; } if (conn != null) { conn.close(); conn = null; } } catch
	 * (SQLException se3) { logger.
	 * error("DataManager.java ::: getRefundTxnDetails() :: Error Occurred while closing Connection : "
	 * , (Throwable)se3); } try { if (rs != null) { rs.close(); rs = null; } if
	 * (callableStatement != null) { callableStatement.close(); callableStatement =
	 * null; } if (conn != null) { conn.close(); conn = null; } } catch
	 * (SQLException se4) { logger.
	 * error("DataManager.java ::: getRefundTxnDetails() :: Error Occurred while closing Connection : "
	 * , (Throwable)se4); } return RRA; }
	 * 
	 * public String getRefundStatus(final String mid, final String txnId) {
	 * Connection conn = null; ResultSet rs = null; CallableStatement
	 * callableStatement = null; JSONObject jo = null; JSONArray report = null; try
	 * { conn = DBConnectionHandler.getConnection(); final String query =
	 * "{call pro_getRefundStatus(?,?)}"; callableStatement =
	 * conn.prepareCall("{call pro_getRefundStatus(?,?)}");
	 * callableStatement.setString(1, mid); callableStatement.setString(2, txnId);
	 * callableStatement.execute(); rs = callableStatement.getResultSet(); report =
	 * new JSONArray(); while (rs.next()) { jo = new JSONObject(); if
	 * (rs.getString("error_code").equalsIgnoreCase("RS000")) { jo.put("pgRefNo",
	 * rs.getString("pg_ref_no")); jo.put("txnId", txnId); jo.put("merchantId",
	 * mid); jo.put("refundAmount", rs.getString("RefundAmt")); if
	 * (rs.getString("Refund_Type").equalsIgnoreCase("FR")) { jo.put("refundType",
	 * "Full Refund"); } else { jo.put("refundType", "Partial Refund"); } if
	 * (rs.getString("Refund_Status").equalsIgnoreCase("Processed")) {
	 * jo.put("refundStatus", rs.getString("Refund_Status")); jo.put("respMessage",
	 * rs.getString("respMessage")); jo.put("error_code",
	 * rs.getString("error_code")); jo.put("refundProcessDate",
	 * rs.getString("Refund_Process_date")); } else { jo.put("refundStatus",
	 * "Under Porcess"); jo.put("respMessage", "Refund under process");
	 * jo.put("error_code", "RF005"); } } else { jo.put("pgRefNo", "NA");
	 * jo.put("txnId", txnId); jo.put("merchantId", mid); jo.put("refundAmt", "NA");
	 * jo.put("refundStatus", "NA"); jo.put("refundType", "NA");
	 * jo.put("respMessage", rs.getString("respMessage")); jo.put("error_code",
	 * rs.getString("error_code")); } report.put(jo); } } catch (Exception e) {
	 * logger.error("DataManager.java getRefundStatus() Gerring Error   :::    ",
	 * (Throwable)e); try { if (rs != null) { rs.close(); rs = null; } if
	 * (callableStatement != null) { callableStatement.close(); callableStatement =
	 * null; } if (conn != null) { conn.close(); conn = null; } } catch
	 * (SQLException e2) {
	 * logger.error("DataManager.java getRefundStatus()Gerring Error   :::    ",
	 * (Throwable)e2); } try { if (rs != null) { rs.close(); rs = null; } if
	 * (callableStatement != null) { callableStatement.close(); callableStatement =
	 * null; } if (conn != null) { conn.close(); conn = null; } } catch
	 * (SQLException e3) {
	 * logger.error("DataManager.java getRefundStatus()Gerring Error   :::    ",
	 * (Throwable)e3); } return report.toString(); } finally { try { if (rs != null)
	 * { rs.close(); rs = null; } if (callableStatement != null) {
	 * callableStatement.close(); callableStatement = null; } if (conn != null) {
	 * conn.close(); conn = null; } } catch (SQLException e4) {
	 * logger.error("DataManager.java getRefundStatus()Gerring Error   :::    ",
	 * (Throwable)e4); } } try { if (rs != null) { rs.close(); rs = null; } if
	 * (callableStatement != null) { callableStatement.close(); callableStatement =
	 * null; } if (conn != null) { conn.close(); conn = null; } } catch
	 * (SQLException e4) {
	 * logger.error("DataManager.java getRefundStatus()Gerring Error   :::    ",
	 * (Throwable)e4); } try { if (rs != null) { rs.close(); rs = null; } if
	 * (callableStatement != null) { callableStatement.close(); callableStatement =
	 * null; } if (conn != null) { conn.close(); conn = null; } } catch
	 * (SQLException e5) {
	 * logger.error("DataManager.java getRefundStatus()Gerring Error   :::    ",
	 * (Throwable)e5); } return report.toString(); }
	 */

	public RefundRequestAPI getRefundTxnDetails(String merchantid, String txnid, String refundAmount, String atrn,
			String refundRequestId, String addedBy) {
		logger.info("merchantid=" + merchantid + " txnid=" + txnid + " refundAmount=" + refundAmount + " atrn=" + atrn
				+ " refundRequestId=" + refundRequestId + " addedBy=" + addedBy);

		RefundRequestAPI RRA = new RefundRequestAPI();
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement callableStatement = null;

		try {
			conn = DBConnectionHandler.getConnection();
			if (conn != null) {
				String query = "{call pro_refundApplyAPINew2(?,?,?,?,?)}";
				callableStatement = conn.prepareCall(query);
				callableStatement.setString(1, merchantid);
				callableStatement.setString(2, txnid);
				callableStatement.setString(3, refundAmount);
				callableStatement.setString(4, refundRequestId);
				callableStatement.setString(5, addedBy);
				callableStatement.execute();
				rs = callableStatement.getResultSet();
				if (rs != null && rs.next()) {
					if (!rs.getString("error_code").equalsIgnoreCase("RF001")) {
						RRA.setId(rs.getString("ID"));
						RRA.setTxnId(rs.getString("txn_Id"));
						RRA.setMerchantId(rs.getString("merchant_id"));
						RRA.setRefund_amount(rs.getString("irefundAmount"));
						RRA.setRespMessage(rs.getString("respMessage"));
						RRA.setErrorCode(rs.getString("error_code"));
						RRA.setRefund_type(rs.getString("refundType"));
						RRA.setAmount(rs.getString("txn_amount"));
						RRA.setServiceRRN(rs.getString("service_rrn"));
						RRA.setRefundRequestId(rs.getString("refundRequestId"));
						RRA.setUploadedBy(rs.getString("addedBy"));
					} else {
						RRA.setId(atrn);
						RRA.setTxnId(txnid);
						RRA.setMerchantId(merchantid);
						RRA.setRefund_amount(refundAmount);
						RRA.setRespMessage(rs.getString("respMessage"));
						RRA.setErrorCode(rs.getString("error_code"));
						RRA.setRefund_type(rs.getString("refundType"));
						RRA.setAmount("NA");
						RRA.setServiceRRN("NA");
						RRA.setRefundRequestId(refundRequestId);
						RRA.setUploadedBy(addedBy);
					}
				}
			}

		} catch (Exception var22) {
			logger.error("DataManager.java ::: getRefundTxnDetails() :: Error Occurred while fetching Data for Txn Id '"
					+ txnid + "' And MID '" + merchantid + "' : ", var22);

			RRA.setId("NA");
			RRA.setTxnId(txnid);
			RRA.setMerchantId(merchantid);
			RRA.setRefund_amount(refundAmount);
			RRA.setRespMessage("Error Occurred while fetching Data.");
			RRA.setErrorCode("NA");
			RRA.setRefund_type("NA");
			RRA.setAmount("NA");
			RRA.setServiceRRN("NA");
			RRA.setRefundRequestId(refundRequestId);
			RRA.setUploadedBy(addedBy);

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
			} catch (SQLException var20) {
				logger.error("DataManager.java ::: getRefundTxnDetails() :: Error Occurred while closing Connection : ",
						var20);
			}

		}

		return RRA;
	}

	/*
	 * public String getRefundStatus(String mid, String txnId, String
	 * refundRequestId) {
	 * 
	 * logger.info("getRefundStatus() mid="+mid+" txnId=="+txnId+" refundRequestd="
	 * +refundRequestId); Connection conn = null; ResultSet rs = null;
	 * CallableStatement callableStatement = null; JSONObject jo = null; JSONArray
	 * report = null; try { conn = DBConnectionHandler.getConnection(); String query
	 * = "{call pro_getRefundStatus(?,?,?)}"; callableStatement =
	 * conn.prepareCall(query); callableStatement.setString(1, mid);
	 * callableStatement.setString(2, txnId); callableStatement.setString(3,
	 * refundRequestId); //Added New callableStatement.execute(); rs =
	 * callableStatement.getResultSet();
	 * 
	 * for (report = new JSONArray(); rs.next(); report.put(jo)) { jo = new
	 * JSONObject(); String RefundId = rs.getString("RefundId"); String
	 * dbRefundRequestId = rs.getString("Refund_RequestId");
	 * 
	 * if (rs.getString("error_code").equalsIgnoreCase("RS000")) {
	 * jo.put("RefundId", RefundId); jo.put("pgRefNo", rs.getString("pg_ref_no"));
	 * jo.put("txnId", txnId); jo.put("merchantId", mid); jo.put("refundAmount",
	 * rs.getString("RefundAmt")); jo.put("refundRequestId", dbRefundRequestId);
	 * 
	 * if (rs.getString("Refund_Type").equalsIgnoreCase("FR")) jo.put("refundType",
	 * "Full Refund"); else { jo.put("refundType", "Partial Refund"); }
	 * 
	 * if (rs.getString("Refund_Status").equalsIgnoreCase("Processed")) {
	 * jo.put("refundStatus", rs.getString("Refund_Status")); jo.put("respMessage",
	 * rs.getString("respMessage")); jo.put("error_code",
	 * rs.getString("error_code")); jo.put("refundProcessDate",
	 * rs.getString("Refund_Process_date")); } else { jo.put("refundStatus",
	 * "Under Porcess"); jo.put("respMessage", "Refund under process");
	 * jo.put("error_code", "RF005"); } } else { jo.put("RefundId", RefundId);
	 * jo.put("pgRefNo", "NA"); jo.put("txnId", txnId); jo.put("merchantId", mid);
	 * jo.put("refundAmt", "NA"); jo.put("refundRequestId", dbRefundRequestId);
	 * jo.put("refundStatus", "NA"); jo.put("refundType", "NA");
	 * jo.put("respMessage", rs.getString("respMessage")); jo.put("error_code",
	 * rs.getString("error_code")); } } } catch (Exception var17) {
	 * logger.error("DataManager.java getRefundStatus() Gerring Error   :::    ",
	 * var17); try { if (rs != null) { rs.close(); rs = null; }
	 * 
	 * if (callableStatement != null) { callableStatement.close(); callableStatement
	 * = null; }
	 * 
	 * if (conn != null) { conn.close(); conn = null; } } catch (SQLException var16)
	 * { logger.error("DataManager.java getRefundStatus()Gerring Error   :::    ",
	 * var16); } } finally { try { if (rs != null) { rs.close(); rs = null; }
	 * 
	 * if (callableStatement != null) { callableStatement.close(); callableStatement
	 * = null; }
	 * 
	 * if (conn != null) { conn.close(); conn = null; } } catch (SQLException var16)
	 * { logger.error("DataManager.java getRefundStatus()Gerring Error   :::    ",
	 * var16); }
	 * 
	 * }
	 * 
	 * return report.toString(); }
	 */

	public JSONObject getRefundStatus(String mid, String txnId, String refundRequestId) {
		logger.info("mid======" + mid + "txnId============" + txnId + "refundRequestId=============" + refundRequestId);
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement callableStatement = null;

		JSONObject jo = new JSONObject();

		try {
			conn = DBConnectionHandler.getConnection();

			if (conn != null) {
				String query = "{call pro_getRefundStatus(?,?,?)}";
				callableStatement = conn.prepareCall(query);
				callableStatement.setString(1, mid);
				callableStatement.setString(2, txnId);
				callableStatement.setString(3, refundRequestId);

				callableStatement.execute();
				rs = callableStatement.getResultSet();

				if (rs.next()) {

					jo = RefundStatus.refundStatusResponse(rs.getString("error_code"), rs.getString("respMessage"),
							rs.getString("iatrnId"), rs.getString("itxnId"), rs.getString("imerchantId"),
							rs.getString("refAmt"), rs.getString("itxn_amount"), rs.getString("rrn"),
							rs.getString("refundType"), rs.getString("addedBy"), rs.getString("iarnNo"),
							rs.getString("irefundRequestId"), jo);
					String value = jo.toString();
					logger.info("value::::::::::::: " + value);
					logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					logger.info("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
					return jo;
				}
			}

		}

		catch (Exception e) {
			logger.error("DataManager.java getRefundStatus() Gerring Error   :::    ", e);
			jo = RefundStatus.refundStatusResponse("RS004", "Error While Fetching Records", "NA", txnId, mid, "NA",
					"NA", "NA", "NA", "NA", "NA", refundRequestId, jo);
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
			} catch (SQLException e) {
				logger.error("DataManager.java getRefundStatus()Gerring Error   :::    ", e);
				rs = null;
				callableStatement = null;
				conn = null;
			}
		}

		return jo;

	}

	/*
	 * public static void main(final String[] args) { final JSONObject json = new
	 * JSONObject(); json.put("udf5", "NA"); json.put("instrumentId", "NA");
	 * json.put("cardDetails", "NA"); json.put("cardType", "NA"); json.put("udf1",
	 * "3000308948_567"); json.put("udf2", "NA"); json.put("udf3", "NA");
	 * json.put("udf4", "NA"); json.put("txnType", "DIRECT"); json.put("returnURL",
	 * "http://localhost:42916/BillPayment_new/pgResponseHandling.aspx");
	 * json.put("productId", "DEFAULT"); json.put("isMultiSettlement", "0");
	 * json.put("merchantId", "M000257"); json.put("apiKey", "Hzxt0192Hzxt0192");
	 * json.put("txnId", "HDF1904247002116"); json.put("Amount", "567.00");
	 * json.put("dateTime", "2019-04-24 11:08:59"); json.put("custMobile",
	 * "9646130444"); json.put("custMail", "lakhvirsmrar@gmail.com");
	 * json.put("channelId", "0"); final String encryptedRequest =
	 * PGUtils.getEncData(json.toString(), "Hzxt0192Hzxt0192");
	 * logger.info("encryptedRequest : " + encryptedRequest)); }
	 */

	public List<String> getPageConfige(String merchantId)

	{

		logger.info(" Starts  " + new Timestamp(new Date().getTime()));

		logger.info("In getPageConfige() Method " + merchantId);

		List<String> list = null;

		Connection conn = null;

		PreparedStatement stmt = null;

		ResultSet rs = null;

		try

		{

			conn = DBConnectionHandler.getConnection();

			String sql = "select * from tbl_payment_constants where Merchant_id = ?";

			if (conn != null)

			{

				stmt = conn.prepareStatement(sql);

				stmt.setString(1, merchantId);

				rs = stmt.executeQuery();

				if (rs.next())

				{
					list = new ArrayList<>();

					list.add(rs.getString("Merchant_id"));

					list.add(rs.getString("Fee_conf"));

					list.add(rs.getString("Charge_Static_Field"));

					list.add(rs.getString("Other"));

					list.add(rs.getString("Channel_CC"));

					list.add(rs.getString("Channel_DC"));

					list.add(rs.getString("Channel_NB"));

					list.add(rs.getString("Channel_UPI"));

					list.add(rs.getString("Channel_EMI"));

					list.add(rs.getString("Save_Card"));

				}

				logger.info("List " + list);

			}

		}

		catch (Exception e)

		{

			logger.error("DataManager.java ::: getMerchant() :: Error Occurred : ", e);

		}

		finally

		{

			try

			{

				if (rs != null)

				{

					rs.close();

					rs = null;

				}

				if (stmt != null)

				{

					stmt.close();

					stmt = null;

				}

				if (conn != null)

				{

					conn.close();

					conn = null;

				}

			}

			catch (SQLException localSQLException4)

			{

				logger.error("DataManager.java ::: getMerchant() :: Error Occurred while closing Connection : ",
						localSQLException4);

			}

		}

		logger.info(" end  " + new Timestamp(new Date().getTime()));

		return list;

	}

	public ArrayList<String> getEMIBankList(String merchantId) {
		ArrayList<String> banklist = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnectionHandler.getConnection();

			String sql = "select distinct bank_name from tbl_emi_details where merchant_id = ?";
			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchantId);
				rs = stmt.executeQuery();

				banklist = new ArrayList<String>();

				while (rs.next()) {
					banklist.add(rs.getString("Bank_Name"));
				}

				logger.info("DataManager.java ::: getEMIBankList() :: Product Id List Size : " + banklist.size());
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getProductIdList() :: Error Occurred : ", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error("DataManager.java ::: getProductIdList() :: Error Occurred while closing Connection : ",
						localSQLException4);
			}
		}

		return banklist;

	}

	public ArrayList<String> getEMIDetails(String bankId) {
		ArrayList<String> banklist = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnectionHandler.getConnection();

			String sql = "select bank_name,tenture from tbl_emi_details where bank_id = ?";
			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, bankId);
				rs = stmt.executeQuery();

				banklist = new ArrayList<String>();

				while (rs.next()) {
					banklist.add(rs.getString("bank_name"));
					banklist.add(rs.getString("tenture"));
				}

				logger.info("DataManager.java ::: getEMIBankList() :: Product Id List Size : " + banklist.size());
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getProductIdList() :: Error Occurred : ", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error("DataManager.java ::: getProductIdList() :: Error Occurred while closing Connection : ",
						localSQLException4);
			}
		}

		return banklist;

	}

	/*
	 * public String getEmiDetails1(String amount , String bankName)
	 * 
	 * {
	 * 
	 * logger.info(" starts  "+new Timestamp(System.currentTimeMillis()));
	 * 
	 * logger.info("In getEmiDetails() Method "+amount);
	 * 
	 * 
	 * 
	 * Connection conn = null;
	 * 
	 * PreparedStatement stmt = null;
	 * 
	 * ResultSet rs = null;
	 * 
	 * JSONObject emidata=new JSONObject();
	 * 
	 * 
	 * try
	 * 
	 * {
	 * 
	 * conn = DBConnectionHandler.getConnection(); String sql =
	 * "select * from tbl_emi_details where bank_name='"+bankName+"' ";
	 * 
	 * if (conn != null)
	 * 
	 * { //float user_amount = Float.valueOf(amount); BigDecimal user_amount=new
	 * BigDecimal(amount);
	 * 
	 * stmt=conn.prepareStatement(sql); rs=stmt.executeQuery(); int count=0;
	 * while(rs.next()) { count=count+3; String Emp_Rate = rs.getString("rate");
	 * if(Emp_Rate !=null && !Emp_Rate.isEmpty()){
	 * 
	 * float rate = Float.valueOf(Emp_Rate); float time = count; float r = rate /
	 * (12 * 100); float t = time ;
	 * 
	 * //float emi= (float) ((user_amount*r*Math.pow(1+r,t))/(Math.pow(1+r,t)-1));
	 * BigDecimal emi=user_amount.multiply(new BigDecimal(r*Math.pow(1+r,t)));
	 * emi=emi.divide(new BigDecimal(Math.pow(1+r,t)-1),2, RoundingMode.HALF_UP);
	 * 
	 * 
	 * //float total_amount = emi*time; BigDecimal total_amount = emi.multiply(new
	 * BigDecimal(time));
	 * 
	 * //float bank_charge = total_amount - user_amount; BigDecimal bank_charge =
	 * total_amount .subtract(user_amount);
	 * 
	 * logger.info(" emi for "+count+" month "+convertToDecimalTwoPlaces(emi) +
	 * " amount "+convertToDecimalTwoPlaces(total_amount) +
	 * " bank_charge "+convertToDecimalTwoPlaces(bank_charge));
	 * 
	 * 
	 * JSONObject js=new JSONObject(); js.put("rate",convertToDecimalTwoPlaces(new
	 * BigDecimal(rate))); js.put("emi",convertToDecimalTwoPlaces(emi));
	 * js.put("total_amount",convertToDecimalTwoPlaces(total_amount));
	 * js.put("bank_charge",convertToDecimalTwoPlaces(bank_charge));
	 * js.put("bank_id", rs.getString("bank_id")); js.put("sp_id",
	 * rs.getString("sp_id"));
	 * logger.info("json object for "+count+" months"+js.toString());
	 * 
	 * emidata.put(rs.getString("month_duration"), js);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * //logger.info(emidata.toString());
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * catch (Exception e)
	 * 
	 * {
	 * 
	 * logger.error("DataManager.java ::: getEmiDetails1() :: Error Occurred : ",e);
	 * 
	 * }
	 * 
	 * finally
	 * 
	 * {
	 * 
	 * try
	 * 
	 * {
	 * 
	 * if(rs!=null)
	 * 
	 * {
	 * 
	 * rs.close();
	 * 
	 * rs = null;
	 * 
	 * }
	 * 
	 * if(stmt!=null)
	 * 
	 * {
	 * 
	 * stmt.close();
	 * 
	 * stmt = null;
	 * 
	 * }
	 * 
	 * if(conn != null)
	 * 
	 * { conn.close();
	 * 
	 * conn = null; }
	 * 
	 * }
	 * 
	 * catch (SQLException localSQLException4)
	 * 
	 * {
	 * 
	 * logger.
	 * error("DataManager.java ::: getEmiDetails1() :: Error Occurred while closing Connection : "
	 * ,localSQLException4);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * logger.info(" Ends  "+new Timestamp(System.currentTimeMillis()));
	 * 
	 * return emidata.toString();
	 * 
	 * }
	 */

	public String getEmiDetails1(String amount, String bankName)

	{

		logger.info(" starts  " + new Timestamp(new Date().getTime()));

		logger.info("In getEmiDetails() Method " + amount);

		Connection conn = null;

		PreparedStatement stmt = null;

		ResultSet rs = null;

		JSONObject emidata = new JSONObject();

		try

		{

			conn = DBConnectionHandler.getConnection();
			String sql = "select * from tbl_emi_details where bank_name=? ";

			if (conn != null)

			{
				// float user_amount = Float.valueOf(amount);
				BigDecimal user_amount = new BigDecimal(amount);

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, bankName);
				rs = stmt.executeQuery();
				int count = 0;
				while (rs.next()) {
					count = count + 3;
					String Emp_Rate = rs.getString("rate");
					if (Emp_Rate != null && !Emp_Rate.isEmpty()) {

						float rate = Float.valueOf(Emp_Rate);
						float time = count;
						float r = rate / (12 * 100);
						float t = time;

						// float emi= (float) ((user_amount*r*Math.pow(1+r,t))/(Math.pow(1+r,t)-1));
						BigDecimal emi = user_amount.multiply(new BigDecimal(r * Math.pow(1 + r, t)));
						emi = emi.divide(new BigDecimal(Math.pow(1 + r, t) - 1), 2, RoundingMode.HALF_UP);

						// float total_amount = emi*time;
						BigDecimal total_amount = emi.multiply(new BigDecimal(time));

						// float bank_charge = total_amount - user_amount;
						BigDecimal bank_charge = total_amount.subtract(user_amount);

						logger.info(" emi for " + count + " month " + convertToDecimalTwoPlaces(emi) + " amount "
								+ convertToDecimalTwoPlaces(total_amount) + " bank_charge "
								+ convertToDecimalTwoPlaces(bank_charge));

						JSONObject js = new JSONObject();
						js.put("rate", convertToDecimalTwoPlaces(new BigDecimal(rate)));
						js.put("emi", convertToDecimalTwoPlaces(emi));
						js.put("total_amount", convertToDecimalTwoPlaces(total_amount));
						js.put("bank_charge", convertToDecimalTwoPlaces(bank_charge));
						js.put("bank_id", rs.getString("bank_id"));
						js.put("sp_id", rs.getString("sp_id"));
						logger.info("json object for " + count + " months" + js.toString());

						emidata.put(count + "", js);

					}

				}

				// logger.info(emidata.toString());

			}

		}

		catch (Exception e)

		{

			logger.error("DataManager.java ::: getEmiDetails1() :: Error Occurred : ", e);

		}

		finally

		{

			try

			{

				if (rs != null)

				{

					rs.close();

					rs = null;

				}

				if (stmt != null)

				{

					stmt.close();

					stmt = null;

				}

				if (conn != null)

				{
					conn.close();

					conn = null;
				}

			}

			catch (SQLException localSQLException4)

			{

				logger.error("DataManager.java ::: getEmiDetails1() :: Error Occurred while closing Connection : ",
						localSQLException4);

			}

		}

		logger.info(" Ends  " + new Timestamp(new Date().getTime()));

		return emidata.toString();

	}

	private String convertToDecimalTwoPlaces(BigDecimal amt) {

		DecimalFormat df = new DecimalFormat("#.##");

		return df.format(amt);

	}

	public String binValidateBlockedCard(String cardNumber, String binType, String merchantId) {
		logger.info("cardNumber=" + cardNumber + " binType=" + binType + " merchantId=" + merchantId);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = "N";
		try {
			conn = DBConnectionHandler.getConnection();

			if (conn != null) {
				String sql = "select *  from others_cards_bin_validate  where bin_low like concat(?,'%') or bin_high like concat(?,'%')";
				if (conn != null) {
					logger.info("query=" + sql);
					ps = conn.prepareStatement(sql);
					ps.setString(1, cardNumber);
					ps.setString(2, cardNumber);

					rs = ps.executeQuery();

					if (rs.next()) {
						status = "B";

					} else {
						status = binValidateAllowedBins(cardNumber, merchantId);
					}
				}
			} else {
				logger.info("DataManager.java :: connection is null binValidateBlockedCard()");
			}
		} catch (Exception e) {
			status = e.getMessage();
			logger.error("DataManager.java ::: binValidateBlockedCard() :: Error Occurred : ", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error(
						"DataManager.java ::: binValidateBlockedCard() :: Error Occurred while closing Connection : ",
						localSQLException4);
			}
		}
		return status;
	}

	public String binValidateAllowedBins(String cardNumber, String merchantId) {
		logger.info("merchantId===>" + merchantId + " cardNumber==>" + cardNumber);
		Connection conn = null;
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null, rs1 = null;
		String status = "N";
		String allowedBins = null;
		try {
			conn = DBConnectionHandler.getConnection();

			if (conn != null) {
				String sql = "select is_allowedBins from tbl_mstmerchant where MerchantId=?";

				logger.info("query=" + sql);
				ps = conn.prepareStatement(sql);
				ps.setString(1, merchantId);
				rs = ps.executeQuery();

				while (rs.next()) {
					allowedBins = rs.getString("is_allowedBins");
				}

				if (allowedBins != null && allowedBins != "" && allowedBins.equalsIgnoreCase("Y")) {
					String sql1 = "select * from allowedbins  where merchant_id=? and is_deleted=? and (bin_low like concat(?,'%') or bin_high like concat(?,'%'))";

					logger.info("query=" + sql1);
					ps1 = conn.prepareStatement(sql1);
					ps1.setString(1, merchantId);
					ps1.setString(2, "N");
					ps1.setString(3, cardNumber);
					ps1.setString(4, cardNumber);

					rs1 = ps1.executeQuery();

					if (rs1.next()) {
						logger.info("allowed bin");
						status = "N";
					} else {
						logger.info("invalid bin for merchant not allowed");
						status = "MB";
					}
				} else {
					logger.info("is allowed bin flag is false no need to check the allowed bin");
					status = "N";
				}
			}

			else {
				logger.info("DataManager.java :: connection is null binValidateBlockedCard()");
			}
		} catch (Exception e) {
			status = e.getMessage();
			logger.info("DataManager.java ::: binValidateAllowedBins() :: Error Occurred : " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (rs1 != null) {
					rs1.close();
					rs1 = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}

				if (ps1 != null) {
					ps1.close();
					ps1 = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.info(
						"DataManager.java ::: binValidateAllowedBins()  :: Error Occurred while closing Connection : "
								+ localSQLException4.getMessage());
			}
		}
		return status;
	}

	public int insertTxnRetry(String merchantId, String txnId, String reqdata, String uploadedBy, String date_time) {
		logger.info("merchantId=" + merchantId + " txnId=" + txnId + " reqdata=" + reqdata + " uploadedBy=" + uploadedBy
				+ " date_time=" + date_time);
		Connection conn = null;
		PreparedStatement stmt = null;
		int status = 0;
		conn = DBConnectionHandler.getConnection();
		status = isTxnRetryPresent(txnId, conn);
		logger.info("isTxnRetryPresent status >>>>>>> " + status);
		if (status == 1) {
			logger.info("txnId>>>>" + txnId + " already inserted no need to insert");
			return 0;
		} else {
			try {
				String sql = "insert into tbl_txn_retry(merchant_id, txn_id, reqdata, uploded_by, date_time, modified_by,modified_on) "
						+ "values(?,?,?,?,?,?,?)";
				logger.info("query ====>" + sql);
				if (conn != null) {
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, merchantId);
					stmt.setString(2, txnId);
					stmt.setString(3, reqdata);
					stmt.setString(4, uploadedBy);
					stmt.setString(5, date_time);
					stmt.setString(6, "PGTransaction");
					stmt.setString(7, date_time);
					stmt.executeUpdate();
					status = 1;
					logger.info("DataManager.java ::: insertTxnRetry() :: status : " + status);
					return status;
				}
			}

			catch (Exception e) {
				logger.info("DataManager.java ::: insertTxnRetry() :: Error Occurred : " + e.getMessage());
				status = 0;
			}

			finally {
				try {

					if (stmt != null) {
						stmt.close();
						stmt = null;
					}
					if (conn != null) {
						conn.close();
						conn = null;
					}
				} catch (SQLException localSQLException4) {
					logger.error("DataManager.java ::: insertTxnRetry() :: Error Occurred while closing Connection : "
							+ localSQLException4.getMessage());
					stmt = null;
					conn = null;

				}
			}
			return status;
		}

	}

	public int insertCallBackResponseNPST(String Response) {
		logger.info("Response=" + Response);
		Connection conn = null;
		PreparedStatement stmt = null;
		int status = 0;
		String resp_date_Time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());

		conn = DBConnectionHandler.getConnection();
		try {
			String sql = "insert into tbl_npstcallback_response(response_date,response_data) values (?, ?)";
			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, resp_date_Time);
				stmt.setString(2, Response);
				stmt.executeUpdate();
				status = 1;
				logger.info("DataManager.java ::: InsertCallback() :: status : " + status);
				return status;
			}
		}

		catch (Exception e) {
			logger.info("DataManager.java ::: InsertCallback() :: Error Occurred : " + e.getMessage());
			status = 0;
		}

		finally {
			try {

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error("DataManager.java ::: InsertCallback() :: Error Occurred while closing Connection : "
						+ localSQLException4.getMessage());
				stmt = null;
				conn = null;

			}
		}
		return status;
	}

	public int insertAlertRiskLogs(String TransTime, String RiskStage, String MID, String RiskCode, String RiskFlag,
			String addedby, String AddedOn) {
		logger.info("TransTime=" + TransTime + " TransTime=" + TransTime + " TransTime=" + TransTime + " RiskStage="
				+ RiskStage);
		Connection conn = null;
		PreparedStatement stmt = null;
		int status = 0;
		conn = DBConnectionHandler.getConnection();
		try {
			String sql = "insert into tbl_riskalertlogs(TransTime,RiskStage,MID,RiskCode,RiskFlag,addedby,AddedOn) values(?,?,?,?,?,?,?)";
			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, TransTime);
				stmt.setString(2, RiskStage);
				stmt.setString(3, MID);
				stmt.setString(4, RiskCode);
				stmt.setString(5, RiskFlag);
				stmt.setString(6, addedby);
				stmt.setString(7, AddedOn);

				stmt.executeUpdate();
				status = 1;
				logger.info("DataManager.java ::: Risk AlertEntry() :: status : " + status);
				return status;
			}
		}

		catch (Exception e) {
			logger.info("DataManager.java ::: AlertEntry() :: Error Occurred : " + e.getMessage());
			status = 0;
		}

		finally {
			try {

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error("DataManager.java ::: insertTxnRetry() :: Error Occurred while closing Connection : "
						+ localSQLException4.getMessage());
				stmt = null;
				conn = null;

			}
		}
		return status;
	}

	public int isTxnRetryPresent(String txnId, Connection conn) {
		logger.info(" txnId=" + txnId);

		PreparedStatement stmt = null;
		ResultSet rs = null;

		int found = 0;
		try {
			conn = DBConnectionHandler.getConnection();

			String sql = "select * from  tbl_txn_retry where txn_id=?";

			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, txnId);
				rs = stmt.executeQuery();

				if (rs.next()) {
					found = 1;
					return found;
				}

			}
		} catch (Exception e) {
			logger.info("DataManager.java ::: isTxnRetryPresent() :: Error Occurred : " + e.getMessage());
			found = 0;
		} finally {
			try {

				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

			} catch (SQLException localSQLException4) {
				logger.info("DataManager.java ::: isTxnRetryPresent() :: Error Occurred while closing Connection : "
						+ localSQLException4.getMessage());
				rs = null;
				stmt = null;
			}
		}

		return found;

	}

	public TxnRetry getTxnRetry(String txnId) {
		logger.info(" txnId=" + txnId);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		TxnRetry txnRetry = null;

		try {
			conn = DBConnectionHandler.getConnection();

			String sql = "select * from  tbl_txn_retry where txn_id=?";

			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, txnId);
				rs = stmt.executeQuery();

				if (rs.next()) {
					txnRetry = new TxnRetry();
					txnRetry.setMerchantId(rs.getString("merchant_id"));
					txnRetry.setTxnId(rs.getString("txn_id"));
					txnRetry.setReqData(rs.getString("reqdata"));
					txnRetry.setUploadedBy(rs.getString("uploded_by"));
					txnRetry.setDateTime(rs.getString("date_time"));
					txnRetry.setModifiedBy(rs.getString("modified_by"));
					txnRetry.setModifiedOn(rs.getString("modified_on"));

					return txnRetry;
				}
			}
		} catch (Exception e) {
			logger.info("DataManager.java ::: insertTxnRetry() :: Error Occurred : " + e.getMessage());
			txnRetry = null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.info("DataManager.java ::: insertTxnRetry() :: Error Occurred while closing Connection : "
						+ localSQLException4.getMessage());
				rs = null;
				stmt = null;
				conn = null;
			}
		}

		return txnRetry;

	}

	public MerchantDTO getPushUrl(String mid) {

		MerchantDTO TM = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		logger.info("DataManager.java ::: getTxnMaster() :: fetch Data against the push_url '");

		try {
			conn = DBConnectionHandler.getConnection();
//			String sql = "select txn_id ,merchant_id,date_time,process_id,txn_amount,service_id,return_url,mobile_no,email_id ,udf1,udf2,id,trans_status,instrument_id,udf3,udf4,remote_ip,resp_date_time,udf5,resp_status,resp_message,service_rrn,bank_id,card_details,card_type,error_code,sp_error_code,rms_score,rms_reason,service_txn_id,sur_charge,UploadedBy from tbl_transactionmaster where txn_id = ?";
			String sql = "select push_url from tbl_mstmerchant where MerchantId = ?";

			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, mid);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					logger.info("mstmerchant.java ::: getTxnMaster() :: Inside rs ");
					TM = new MerchantDTO();
					TM.setPushUrl(rs.getString(1));
				} else {
					logger.info("DataManager.java ::: getTxnMaster() :: No Data found against the push_url '"
							+ rs.getString(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return TM;
	}

	public ArrayList<String> getPayzappDetails(final String merchantId, final String bankId) {
		logger.info("merchantId=" + merchantId + " bankId=" + bankId);
		String sProviderSQL = null;
		ArrayList<String> payzAppDetails = null;
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		try {
			sProviderSQL = "select merchant_AppId,Hash_Key from merchant_wallet where merchant_id = ? and bank_id = ?";

			logger.info("getSPMidKey() : " + sProviderSQL);
			if (con != null) {
				ps = con.prepareStatement(sProviderSQL);
				ps.setString(1, merchantId);
				ps.setString(2, bankId);
				rs = ps.executeQuery();
				if (rs.next()) {
					payzAppDetails = new ArrayList<String>();
					payzAppDetails.add(rs.getString(1));
					payzAppDetails.add(rs.getString(2));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.info("DataManager.java ::: getPayzappDetails() :: Error Occurred while closing Connection : "
						+ e4.getMessage());
			}
		}

		return payzAppDetails;
	}

	public TransactionMaster getTxnMasterByTxnId(String txnId) {

		TransactionMaster TM = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		logger.info("DataManager.java ::: getTxnMaster() :: fetch Data against the Txn Id '" + txnId + "'");

		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "select txn_id ,merchant_id,date_time,process_id,txn_amount,service_id,return_url,mobile_no,email_id ,udf1,udf2,id,trans_status,instrument_id,udf3,udf4,remote_ip,resp_date_time,udf5,resp_status,resp_message,service_rrn,bank_id,card_details,card_type,error_code,sp_error_code,rms_score,rms_reason,service_txn_id,sur_charge,UploadedBy,Modified_By,is_posted_back_res,is_verified_once,udf6 from tbl_transactionmaster where txn_id = ?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, txnId);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					logger.info("DataManager.java ::: getTxnMaster() :: Inside rs ");
					TM = new TransactionMaster();
					TM.setTxnId(rs.getString(1));
					TM.setMerchantId(rs.getString(2));
					TM.setDateTime(rs.getString(3));
					TM.setProcessId(rs.getString(4));
					TM.setAmount(rs.getString(5));
					TM.setProductId(rs.getString(6));
					TM.setReturn_url(rs.getString(7));
					TM.setCustMobile(rs.getString(8));
					TM.setCustMail(rs.getString(9));
					TM.setUdf1(rs.getString(10));
					TM.setUdf2(rs.getString(11));
					TM.setTransStatus(rs.getString(13));
					TM.setInstrumentId(rs.getString(14));
					TM.setUdf3(rs.getString(15));
					TM.setUdf4(rs.getString(16));
					TM.setId(rs.getString(12));
					TM.setHostAddress(rs.getString(17));
					TM.setRespDateTime(rs.getString(18));
					TM.setUdf5(rs.getString(19));
					TM.setRespStatus(rs.getString(20));
					TM.setRespMessage(rs.getString(21));
					TM.setServiceRRN(rs.getString(22));
					TM.setBankId(rs.getString(23));
					TM.setCardDetails(rs.getString(24));
					TM.setCardType(rs.getString(25));
					TM.setErrorCode(rs.getString(26));
					TM.setSpErrorCode(rs.getString(27));
					TM.setRmsScore(rs.getString(28));
					TM.setRmsReason(rs.getString(29));
					TM.setServiceTxnId(rs.getString(30));
					TM.setSurcharge(rs.getString(31));
					TM.setUploadedBy(rs.getString(32));
					TM.setModified_By(rs.getString("Modified_By"));
					TM.setIsPostedBackRes(rs.getString("is_posted_back_res"));
					TM.setIsVerifiedOnce(rs.getString("is_verified_once"));
					TM.setUdf6(rs.getString(36));
				} else {
					logger.info(
							"DataManager.java ::: getTxnMaster() :: No Data found against the Txn Id '" + txnId + "'");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return TM;
	}

	public ArrayList<String> getWalletList(String merchantid) {

		ArrayList<String> banklist = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "select  wallet_name from tbl_walletmaster as walletmaster,tbl_merchant_mdr as mdr where mdr.merchant_id=? and mdr.bank_id = walletmaster.bank_id ";

			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchantid);
				rs = stmt.executeQuery();
				banklist = new ArrayList<>();

				while (rs.next()) {
					banklist.add(rs.getString("wallet_name"));
				}

				logger.info("DataManager.java ::: getWalletList() :: Product Id List Size : " + banklist.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return banklist;
	}

	public ArrayList<String> getWalletList2(String merchantid) {

		ArrayList<String> banklist = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnectionHandler.getConnection();
//			String sql = "select  wallet_name from tbl_walletmaster as walletmaster,tbl_merchant_mdr as mdr where mdr.merchant_id=? and mdr.bank_id = walletmaster.bank_id ";
			String sql = "select  wallet_name from tbl_walletmaster as walletmaster,tbl_merchant_mdr as mdr where mdr.merchant_id=? and mdr.bank_id = walletmaster.bank_id and wallet_name not in('PayzApp')";

			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchantid);
				rs = stmt.executeQuery();
				banklist = new ArrayList<>();

				while (rs.next()) {
					banklist.add(rs.getString("wallet_name"));
				}

				logger.info("DataManager.java ::: getWalletList() :: Product Id List Size : " + banklist.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return banklist;
	}

	public ArrayList<String> getWalletList1(String merchantid) {

		ArrayList<String> banklist = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnectionHandler.getConnection();
//			String sql = "select  wallet_name from tbl_walletmaster as walletmaster,tbl_merchant_mdr as mdr where mdr.merchant_id=? and mdr.bank_id = walletmaster.bank_id ";
			String sql = "select  wallet_name from tbl_walletmaster as walletmaster,tbl_merchant_mdr as mdr where mdr.merchant_id=? and mdr.bank_id = walletmaster.bank_id and wallet_name ='PayzApp'";

			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchantid);
				rs = stmt.executeQuery();
				banklist = new ArrayList<>();

				while (rs.next()) {
					banklist.add(rs.getString("wallet_name"));
				}

				logger.info("DataManager.java ::: getWalletList() :: Product Id List Size : " + banklist.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return banklist;
	}

	public JSONObject getSBIErrorCode(String error_code_desc, String spId) {
		Connection conn = null;
		PreparedStatement stmt = null, stmt2 = null, stmt3 = null;
		JSONObject js = new JSONObject();
		ResultSet rs = null;
//		Random randNo=new Random();
//		int no=randNo.nextInt(1000);

		try {
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			;
			int no = secureRandom.nextInt(1000);
			String errorCode = "SBE0300" + no;
			String spErrorCode = "IPAY0300" + no;
			conn = DBConnectionHandler.getConnection();
			String sql = "select * from tbl_error_code_master where error_code_desc=?";
			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, error_code_desc);
				rs = stmt.executeQuery();

				if (rs.next()) {
					js.put("aggErrorCode", rs.getString("error_code"));
					js.put("aggErrorDesc", rs.getString("error_code_desc"));
				} else {
					sql = "insert into tbl_error_code_master values(?,?)";
					stmt2 = conn.prepareStatement(sql);
					stmt2.setString(1, errorCode);
					stmt2.setString(2, error_code_desc);
					stmt2.executeUpdate();

					sql = "insert into tbl_error_code_mapping values(?,?,?,?,?,?,?);";
					stmt2 = conn.prepareStatement(sql);
					stmt2.setString(1, spErrorCode);
					stmt2.setString(2, error_code_desc);
					stmt2.setString(3, errorCode);
					stmt2.setString(4, spId);
					stmt2.setString(5, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
					stmt2.setString(6, "Kiran");
					stmt2.setString(7, "N");
					stmt2.executeUpdate();

					js = getErrorCode(spErrorCode, spId);
				}

			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getWalletList() :: Error Occurred : ", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (stmt2 != null) {
					stmt2.close();
					stmt2 = null;
				}
				if (stmt3 != null) {
					stmt3.close();
					stmt3 = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				logger.error("DataManager.java ::: getWalletList() :: Error Occurred while closing Connection : ", e);
			}

		}

		return js;
	}

	public SbiAquiringDTO getSBIAquiringDetails(String merchantId) {
		logger.info("merchantId=" + merchantId);
		String sProviderSQL = null;
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		SbiAquiringDTO sbiaquiring = null;

		try {
			sProviderSQL = "select alias_name,key_path from sbiaquiring_details where merchant_id= ?";

			logger.info("getSPMidKey() : " + sProviderSQL);
			if (con != null) {
				ps = con.prepareStatement(sProviderSQL);
				ps.setString(1, merchantId);
				rs = ps.executeQuery();
				if (rs.next()) {
					sbiaquiring = new SbiAquiringDTO();
					sbiaquiring.setAliasName(rs.getString(1));
					sbiaquiring.setKeyPath(rs.getString(2));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.info("DataManager.java ::: SBIAquiringDetails() :: Error Occurred while closing Connection : "
						+ e4.getMessage());
			}
		}

		return sbiaquiring;
	}

	public List<String> getTransactionAmountDetails(String bankId, String sInstrumentId, String Amount,
			String merchantid, String emioption, String txnid) {
		logger.info("getCalculateSurcharge() bankId= " + bankId + " sInstrumentId=" + sInstrumentId + " Amount="
				+ Amount + " merchantid=" + merchantid + " emioption=" + emioption + " txnid=" + txnid);
		String sClassFileLoader = null, spId = null;
		Double txnFee = Double.valueOf(0.0D);
		Double gst = Double.valueOf(0.0D);
		Connection con = null;
		CallableStatement cs = null;
		List<String> lstdata = null;
		int status = 0;

		try {
			logger.info("DataManager.java :: getTransactionAmountDetails() :: txnid=" + txnid + " bankId :: " + bankId
					+ " sInstrumentId :::" + sInstrumentId + " getAmount::" + Amount + "  getMerchantId ::" + merchantid
					+ "   emioption=" + emioption);
			con = DBConnectionHandler.getConnection();
			// String query = "{call pr_calculateSurcharge1(?,?,?,?,?,?,?)}";
			if (con != null) {
				String query = "{call pr_transactionAmountDetails(?,?,?,?,?,?,?,?)}";
				cs = con.prepareCall(query);
				cs.setString(1, bankId);
				cs.setString(2, sInstrumentId);
				cs.setString(3, merchantid);
				cs.setString(4, Amount);
				cs.registerOutParameter(5, 8);
				cs.registerOutParameter(6, 12);
				cs.registerOutParameter(7, 12);
				cs.registerOutParameter(8, 12);
				// cs.setString(8,emioption);
				status = cs.executeUpdate();

				logger.info("DataManager.java :: getTransactionAmountDetails() :: txnid=" + txnid + " Status >>>>>> "
						+ status);

				txnFee = Double.valueOf(cs.getDouble(5));
				sClassFileLoader = cs.getString(6);
				spId = cs.getString(7);
				gst = Double.valueOf(cs.getDouble(8));

				lstdata = new ArrayList<String>();
				lstdata.add(txnFee + "");
				lstdata.add(sClassFileLoader);
				lstdata.add(spId);
				lstdata.add(String.valueOf(gst));

				logger.info("DataManager.java :: getTransactionAmountDetails() :: txnid=" + txnid + " retamount :::: "
						+ txnFee + " sClassFileLoader :::: " + sClassFileLoader + " retspid  ::: " + spId);
				return lstdata;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DataManager.java :: getTransactionAmountDetails() :: txnid=" + txnid
					+ " :::while procedure pr_calculateSurcharge :::Getting Error ", e);

		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("DataManager.java :: getTransactionAmountDetails() :: txnid=" + txnid
						+ "  :::while procedure pr_calculateSurcharge :::Getting Error ", e);
			}
		}
		return lstdata;

	}

//	public AtompgDetails getAtompgDetails(String merchantid, String mid) {
//
//		AtompgDetails atompgDetails = null;
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//
//		try {
//			conn = DBConnectionHandler.getConnection();
//			String sql = "select id,merchant_id,mid,request_hash_key,response_hash_key,product_id from atom_pg_details where merchant_id = ? and mid = ?";
//
//			logger.info("query ====>" + sql);
//			if (conn != null) {
//				stmt = conn.prepareStatement(sql);
//				stmt.setString(1, merchantid);
//				stmt.setString(2, mid);
//				rs = stmt.executeQuery();
//				atompgDetails = new AtompgDetails();
//
//				while (rs.next()) {
//					atompgDetails.setId(rs.getInt("id"));
//					atompgDetails.setMerchantId(rs.getString("merchant_id"));
//					atompgDetails.setMid(rs.getString("mid"));
//					atompgDetails.setProductId(rs.getString("product_id"));
//					atompgDetails.setRequestHashkey(rs.getString("request_hash_key"));
//					atompgDetails.setResponseHashkey(rs.getString("response_hash_key"));
//				}
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//					rs = null;
//				}
//
//				if (stmt != null) {
//					stmt.close();
//					stmt = null;
//				}
//
//				if (conn != null) {
//					conn.close();
//					conn = null;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return atompgDetails;
//	}

	/*** Merchant catagory code Method ************/

	public static String getMerchantCategoryCode(String merchant_id) {
		logger.info("calling getMerchantCategoryCode method");
		Connection conn = null;
		PreparedStatement stmt = null;
		String result = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "select merchant_category_code from tbl_mstmerchant where MerchantId = ?";
			logger.info("DataManager.java ::: getMerchantCategoryCode() :: query=" + sql);
			if (conn != null) {

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchant_id);

				rs = stmt.executeQuery();
				if (rs.next()) {
					result = rs.getString("merchant_category_code");
				}
				logger.info("DataManager.java ::: getMerchantCategoryCode() :: result========>" + result);

			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.error("DataManager.java ::: getMerchantCategoryCode() :: Error Occurred : ", e);
		} finally {
			try {

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error(
						"DataManager.java ::: getMerchantCategoryCode() :: Error Occurred while closing Connection : ",
						localSQLException4);
			}
		}

		return result;

	}

	public static String getMerchantCategoryRisk(String merchant_id) {
		logger.info("calling getMerchantCategoryCode method");
		Connection conn = null;
		PreparedStatement stmt = null;
		String result = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "select mcc_code from merchant_master_data where MId = ?";
			logger.info("DataManager.java ::: getMerchantCategoryCode() :: query=" + sql);
			if (conn != null) {

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchant_id);

				rs = stmt.executeQuery();
				if (rs.next()) {
					result = rs.getString("mcc_code");
				}
				logger.info("DataManager.java ::: getMerchantCategoryCode() :: result========>" + result);

			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.error("DataManager.java ::: getMerchantCategoryCode() :: Error Occurred : ", e);
		} finally {
			try {

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException4) {
				logger.error(
						"DataManager.java ::: getMerchantCategoryCode() :: Error Occurred while closing Connection : ",
						localSQLException4);
			}
		}

		return result;

	}

	public boolean isHDFCCard(String cardNumber) {
		boolean isHdfc = false;
		logger.info("cardNumber=" + cardNumber);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DBConnectionHandler.getConnection();
			if (conn != null) {
				final String sql = "select * from tbl_hdfc_bin_validate where bin_code =  ?";

				ps = conn.prepareStatement(sql);
				ps.setString(1, cardNumber);
				rs = ps.executeQuery();
				if (rs.next()) {
					isHdfc = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DataManager.java ::: isHDFCCard() :: Error Occurred : ", (Throwable) e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				localSQLException6.printStackTrace();
				logger.error("DataManager.java ::: isHDFCCard() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}
		return isHdfc;
	}

	public List<String> getSurchargeForHDFCCards(String bankId, String sInstrumentId, String Amount, String merchantid,
			String emioption, String txnid) {
		logger.info("getSurchargeForHDFCCards() bankId= " + bankId + " sInstrumentId=" + sInstrumentId + " Amount="
				+ Amount + " merchantid=" + merchantid + " emioption=" + emioption + " txnid=" + txnid);
		int payout = 0;
		Double retamount = Double.valueOf(0.0D);
		Connection con = null;
		CallableStatement cs = null;
		List<String> lstdata = new ArrayList<String>();
		int status = 0;

		try {
			logger.info("DataManager.java :: getSurchargeForHDFCCards() :: txnid=" + txnid + " bankId :: " + bankId
					+ " sInstrumentId :::" + sInstrumentId + " getAmount::" + Amount + "  getMerchantId ::" + merchantid
					+ "   emioption=" + emioption);
			con = DBConnectionHandler.getConnection();
			// String query = "{call pr_calculateSurcharge1(?,?,?,?,?,?,?)}";
			if (con != null) {
				String query = "{call pr_calculateSurchargeInstrumentWiseHDFCCards(?,?,?,?,?,?)}";
				cs = con.prepareCall(query);
				cs.setString(1, bankId);
				cs.setString(2, sInstrumentId);
				cs.setString(3, merchantid);
				cs.setString(4, Amount);
				cs.registerOutParameter(5, 8);
				cs.registerOutParameter(6, 12);
				status = cs.executeUpdate();

				ResultSet executeQuery = cs.executeQuery();

				int isApplicable = 0;
				if (executeQuery != null && executeQuery.next()) {
					isApplicable = executeQuery.getInt("isApplicable");
				}

				logger.info("DataManager.java :: getTransactionAmountDetailsHDFCCards() :: txnid=" + txnid
						+ " isApplicable >>>>>> " + isApplicable);

				if (isApplicable == 1) {
					retamount = Double.valueOf(cs.getDouble(5));
					payout = cs.getInt(6);

					lstdata.add(retamount + "");
					lstdata.add(String.valueOf(payout));
				}

				return lstdata;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lstdata;

	}

	public List<String> getTransactionAmountDetailsHDFCCards(String bankId, String sInstrumentId, String Amount,
			String merchantid, String emioption, String txnid) {
		logger.info("getTransactionAmountDetailsHDFCCards() bankId= " + bankId + " sInstrumentId=" + sInstrumentId
				+ " Amount=" + Amount + " merchantid=" + merchantid + " emioption=" + emioption + " txnid=" + txnid);
		String sClassFileLoader = null, spId = null;
		Double txnFee = Double.valueOf(0.0D);
		Double gst = Double.valueOf(0.0D);
		Connection con = null;
		CallableStatement cs = null;
		List<String> lstdata = new ArrayList<String>();
		int status = 0;

		try {
			logger.info("DataManager.java :: getTransactionAmountDetailsHDFCCards() :: txnid=" + txnid + " bankId :: "
					+ bankId + " sInstrumentId :::" + sInstrumentId + " getAmount::" + Amount + "  getMerchantId ::"
					+ merchantid + "   emioption=" + emioption);
			con = DBConnectionHandler.getConnection();
			// String query = "{call pr_calculateSurcharge1(?,?,?,?,?,?,?)}";
			if (con != null) {
				String query = "{call pr_transactionAmountDetailsHDFCCards(?,?,?,?,?,?)}";
				cs = con.prepareCall(query);
				cs.setString(1, bankId);
				cs.setString(2, sInstrumentId);
				cs.setString(3, merchantid);
				cs.setString(4, Amount);
				cs.registerOutParameter(5, 8);
				cs.registerOutParameter(6, 8);
				ResultSet executeQuery = cs.executeQuery();

				logger.info("DataManager.java :: getTransactionAmountDetailsHDFCCards() :: txnid=" + txnid
						+ " Status >>>>>> " + status);

				int isApplicable = 0;
				if (executeQuery != null && executeQuery.next()) {
					isApplicable = executeQuery.getInt("isApplicable");
				}
				if (isApplicable == 1) {
					txnFee = Double.valueOf(executeQuery.getDouble("txnFee"));
					gst = Double.valueOf(executeQuery.getDouble("gst"));

					lstdata.add(txnFee + "");
					lstdata.add(String.valueOf(gst));
				}

				return lstdata;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DataManager.java :: getTransactionAmountDetailsHDFCCards() :: txnid=" + txnid
					+ " :::while procedure pr_calculateSurcharge :::Getting Error ", e);

		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("DataManager.java :: getTransactionAmountDetailsHDFCCards() :: txnid=" + txnid
						+ "  :::while procedure pr_calculateSurcharge :::Getting Error ", e);
			}
		}
		return lstdata;

	}

	public void setSpTimestamp(String id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		String query = "update tbl_transactionmaster set sp_call_timestamp = ?, inquiry_status = ? where Id = ?";
		try {
			conn = DBConnectionHandler.getConnection();
			if (conn != null) {
				stmt = conn.prepareStatement(query);
				stmt.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				stmt.setString(2, "0");
				stmt.setString(3, id);
				stmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void updateIsPostedBackRes(String id) {
		logger.info("DataManager.java :: updateIsPostedBackRes() :: id=" + id);
		Connection conn = null;
		PreparedStatement stmt = null;
		String query = "update tbl_transactionmaster set is_posted_back_res = ? where Id = ?";
		try {
			conn = DBConnectionHandler.getConnection();
			if (conn != null) {
				stmt = conn.prepareStatement(query);
				stmt.setString(1, "1");
				stmt.setString(2, id);
				int i = stmt.executeUpdate();
				logger.info("DataManager.java :: updateIsPostedBackRes() :: is_posted_back_res updated" + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void updateIsVerfiedOnce(String id) {
		logger.info("DataManager.java :: updateIsPostedBackRes() :: id=" + id);
		Connection conn = null;
		PreparedStatement stmt = null;
		String query = "update tbl_transactionmaster set is_verified_once = ? where Id = ?";
		try {
			conn = DBConnectionHandler.getConnection();
			if (conn != null) {
				stmt = conn.prepareStatement(query);
				stmt.setString(1, "1");
				stmt.setString(2, id);
				int i = stmt.executeUpdate();
				logger.info("DataManager.java :: updateIsPostedBackRes() :: is_posted_back_res updated" + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<TransactionMaster> getTxnMasterByMobileNo(String mobile, String merchantId) {
		logger.info("getTxnMasterByMobileNo() mobile=" + mobile);
		List<TransactionMaster> transactions = null;
		TransactionMaster TM = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select txn_amount,email_id,udf1,udf2,udf3,udf4,udf5 from tbl_transactionmaster use index (txn_by_mobile_no) "
					+ "where mobile_no = ? and merchant_id=? and trans_status = 'Ok' order by date_time desc";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, mobile);
				stmt.setString(2, merchantId);
				rs = stmt.executeQuery();
				transactions = new ArrayList<TransactionMaster>();
				while (rs.next()) {
					TM = new TransactionMaster();
					TM.setAmount(rs.getString("txn_amount"));
					TM.setCustMail(rs.getString("email_id"));
					TM.setUdf1(rs.getString("udf1"));
					TM.setUdf2(rs.getString("udf2"));
					TM.setUdf3(rs.getString("udf3"));
					TM.setUdf4(rs.getString("udf4"));
					TM.setUdf5(rs.getString("udf5"));
					transactions.add(TM);
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred : ", (Throwable) e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return transactions;
	}

	public Boolean isTransLimitBreached(String link, String merchantId, String todayDate, String todayEnd) {
		logger.info("Inside isTransLimitBreached()");
		logger.debug("earnmore :{}, merchantId :{}", link, merchantId);
		Boolean transLimitBreached = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select count(Id) as transCount from tbl_transactionmaster use index(FK_MST_MER_idx) where date_time between '"
					+ todayDate + "' and '" + todayEnd + "' and trans_status='Ok' and merchant_id =? and udf2=?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchantId);
				stmt.setString(2, link);
				logger.info("Statement", stmt);
				rs = stmt.executeQuery();
				if (rs.next()) {
					String transCount = rs.getString("transCount");
					int count = Integer.parseInt(transCount);
					if (count >= 1750 && (link.equalsIgnoreCase("earn-more1") || link.equalsIgnoreCase("earn-more2"))) {
						transLimitBreached = true;
					} else if (count >= 55000 && link.equalsIgnoreCase("blinkpay")) {
						transLimitBreached = true;
					} else {
						if (count >= 2500 && !link.equalsIgnoreCase("blinkpay")) {
							transLimitBreached = true;
						}
					}
					logger.info("trans count is :{} and earn more type is :{}", count, link);
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: isTransLimitBreached() :: Error Occurred : ", (Throwable) e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error(
						"DataManager.java ::: isTransLimitBreached() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}
		logger.info("End of isTransLimitBreached()");
		return transLimitBreached;
	}

	public ArrayList<String> getECMSBankList(String merchantid) {

		ArrayList<String> banklist = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnectionHandler.getConnection();
//			String sql = "select  ecms.bank_id from tbl_emcs_bank as ecms,tbl_merchant_mdr as mdr "
//					+ "where mdr.merchant_id=? and mdr.bank_id = ecms.bank_id and ecms.is_deleted=? ";
			String sql = "select  ecms.bank_id from tbl_emcs_bank as ecms,tbl_merchant_mdr as mdr "
					+ "where mdr.merchant_id=? and mdr.bank_id = ecms.bank_id and type_OP='ecms' and ecms.is_deleted=? ";

			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchantid);
				stmt.setString(2, "N");
				rs = stmt.executeQuery();
				banklist = new ArrayList<>();

				while (rs.next()) {
					banklist.add(rs.getString("bank_id"));
				}

				logger.info("DataManager.java ::: getECMSBankList() :: Product Id List Size : " + banklist.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return banklist;
	}

	public String getServiceProviderName(String spId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sp_name = null;
		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "select sp.sp_name from tbl_mstserviceprovider as sp where sp.sp_id = ?";

			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, spId);
				rs = stmt.executeQuery();

				while (rs.next()) {
					sp_name = rs.getString("sp_name");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sp_name;
	}

	public String getAlternateRUPresent(String mId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String alterRu = null;
		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "select * from tbl_mstmerchant_alterateurl where merchant_id=?";

			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, mId);
				rs = stmt.executeQuery();

				while (rs.next()) {
					alterRu = rs.getString("return_url");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return alterRu;
	}

	public void saveOtp(String mobile_number, String emailId, String otp, String txnId) {

		final long OTP_VALID_DURATION = 2 * 60 * 1000;
		long currentTimeInMillis = System.currentTimeMillis();
		long otpRequestedTimeInMillis = Long.sum(currentTimeInMillis, OTP_VALID_DURATION);

		Connection conn = null;
		CallableStatement callableStatement = null;
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "Call pro_saveOtp(?,?,?,?,?)";
			if (conn != null) {
				conn.setAutoCommit(false);
				callableStatement = conn.prepareCall(sql);
				callableStatement.setString(1, mobile_number);
				callableStatement.setString(2, emailId);

				callableStatement.setString(3, otp);
				callableStatement.setString(4, txnId);
				callableStatement.setLong(5, otpRequestedTimeInMillis);

				callableStatement.execute();
				conn.commit();
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: saveOtp() :: Error Occurred : ", (Throwable) e);

			return;
		} finally {
			try {
				if (callableStatement != null) {
					callableStatement.close();
					callableStatement = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: saveOtp() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

	}

	public String validateOtp(String mobileNo, String emailId, String otp) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		JSONObject json = new JSONObject();

		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "select mobile_number, otp, otp_exipry_time from tbl_otpmaster_savecards where mobile_number = ?";

			logger.info("query ====>" + sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, mobileNo);
				rs = stmt.executeQuery();

				while (rs.next()) {
					json = new JSONObject();
					json.put("otpExist", rs.getString("otp"));
					json.put("otp_exipry_time", rs.getString("otp_exipry_time"));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json.toString();
	}

	public String getDetailByJsessionId(String jsessionId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		JSONObject json = new JSONObject();

		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "select mobile_number, email_id from tbl_otpmaster_savecards where jsessionId = ?";

			logger.info("query ====> {} ", sql);
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, jsessionId);
				rs = stmt.executeQuery();
				if (rs.next()) {
					while (rs.next()) {
						json = new JSONObject();
						json.put("mobile", rs.getString("mobile_number"));
						json.put("email", rs.getString("email_id"));

					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json.toString();
	}

	public void insertIndianBankUPICallBackResp(String callbackResp) {
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "insert into tbl_indianbank_callback(txn_id,service_txn_id,service_rrn,amount,payer_vpa,status,callback_resp) values(?,?,?,?,?,?,?)";

			logger.info("DataManager.java ::: insertIndianBankUPICallBackResp() :: query = {} ", sql);

			if (conn != null) {

				JSONObject callbackDecResp = new JSONObject(callbackResp);

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, callbackDecResp.getString("OrderNo")); // TxnId PAY
				stmt.setString(2, callbackDecResp.getString("TransID")); // INB TxnId
				stmt.setString(3, callbackDecResp.getString("CustRefNo")); // RRN No NPCI
				stmt.setString(4, callbackDecResp.getString("Amount"));
				stmt.setString(5, callbackDecResp.getString("Payer_VPA"));
				stmt.setString(6, callbackDecResp.getString("Status"));
				stmt.setString(7, callbackResp);

				result = stmt.executeUpdate();
				logger.info("DataManager.java ::: insertIndianBankUPICallBackResp() :: result========> {} ", result);

			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.error("DataManager.java ::: insertIndianBankUPICallBackResp() :: Error Occurred : ", e);
		} finally {
			try {

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error(
						"DataManager.java ::: insertIndianBankUPICallBackResp() :: Error Occurred while closing Connection : ",
						e);
			}
		}

	}

	public int insertMstSubmerchantdata(String merchantId, String spId, String instrument, String status,
			JSONObject request, JSONObject response) {
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		try {
			conn = DBConnectionHandler.getConnection();
			String sql = "insert into tbl_mstsubmerchantdata (Merchant,sp_id,instrument,status,request,response) values(?,?,?,?,?,?)";
			logger.info("DataManager.java ::: insertMstSubmerchantdata() :: query = {} ", sql);
			if (conn != null) {

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchantId);
				stmt.setString(2, spId);
				stmt.setString(3, instrument);
				stmt.setString(4, status);
				stmt.setString(5, request + "");
				stmt.setString(6, response + "");
				// stmt.setObject(5, request);
				// stmt.setObject(6, response);

				result = stmt.executeUpdate();

				logger.info("DataManager.java ::: insertMstSubmerchantdata() :: result========> {} ", result);

			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.error("DataManager.java ::: insertMstSubmerchantdata() :: Error Occurred : ", e);
		} finally {
			try {

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error(
						"DataManager.java ::: insertMstSubmerchantdata() :: Error Occurred while closing Connection : ",
						e);
			}
		}

		return result;

	}

	public String IsEnableUpiorIntent(String mid, boolean type) {
		logger.info("getAccessKey() mid=::::::::::::::" + type + "----" + mid);
		String sProviderSQL = null;
		int count = 0;
		String transCount = null;
		PreparedStatement ps = null;
		Connection con = DBConnectionHandler.getConnection();
		ResultSet rs = null;
		try {
			if (type) {
				sProviderSQL = "select upi_intent as UpiAccess  from tbl_mstmerchant where merchantId = ?";
			} else {
				sProviderSQL = "select upi_collect as UpiAccess  from tbl_mstmerchant where merchantId = ?";
			}
			if (con != null) {
				ps = con.prepareStatement(sProviderSQL);
				ps.setString(1, mid);
				logger.info("getAceesKey() : " + sProviderSQL);
				rs = ps.executeQuery();
				if (rs.next()) {
					transCount = rs.getString("UpiAccess");
					logger.error("DataManager.java ::: Logger() Upi::" + transCount);

					/*
					 * rs.close(); ps.close(); con.close();
					 */
				}
			}
		} catch (SQLException e) {
			logger.error("DataManager.java ::: getSPKey() :: Error Occurred : ", (Throwable) e);

			return transCount;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e4) {
				logger.info("DataManager.java ::: getSPKey() :: Error Occurred while closing Connection : ",
						(Throwable) e4);
			}
		}

		return transCount;
	}

	public String gettxnID_MID(String custRefNum) {
		logger.info("getTxnMaster() custRefNum=" + custRefNum);
		String MerchantId=null;
		TransactionMaster TM = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		logger.info("DataManager.java ::: getTxnMaster() :: fetch Data against the Txn Id '" + custRefNum + "'");
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select merchant_id from tbl_transactionmaster use index (PRIMARY) where id = ?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, custRefNum);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					logger.info("DataManager.java ::: getTxnMaster() :: Inside rs " + custRefNum);
					TM = new TransactionMaster();
					/*
					 * TM.setTxnId(rs.getString(1)); TM.setMerchantId(rs.getString(2));
					 * TM.setDateTime(rs.getString(3)); TM.setProcessId(rs.getString(4));
					 * TM.setAmount(rs.getString(5)); TM.setProductId(rs.getString(6));
					 * TM.setReturn_url(rs.getString(7)); TM.setCustMobile(rs.getString(8));
					 * TM.setCustMail(rs.getString(9)); TM.setUdf1(rs.getString(10));
					 * TM.setUdf2(rs.getString(11)); TM.setTransStatus(rs.getString(13));
					 * TM.setInstrumentId(rs.getString(14)); TM.setUdf3(rs.getString(15));
					 * TM.setUdf4(rs.getString(16)); TM.setId(id);
					 * TM.setHostAddress(rs.getString(17)); TM.setRespDateTime(rs.getString(18));
					 * TM.setUdf5(rs.getString(19)); TM.setRespStatus(rs.getString(20));
					 * TM.setRespMessage(rs.getString(21)); TM.setServiceRRN(rs.getString(22));
					 * TM.setBankId(rs.getString(23)); TM.setCardDetails(rs.getString(24));
					 * TM.setCardType(rs.getString(25)); TM.setErrorCode(rs.getString(26));
					 * TM.setSpErrorCode(rs.getString(27)); TM.setRmsScore(rs.getString(28));
					 * TM.setRmsReason(rs.getString(29)); TM.setServiceTxnId(rs.getString(30));
					 * TM.setSurcharge(rs.getString(31)); TM.setModified_By(rs.getString(32));
					 */

					 MerchantId=rs.getString("merchant_id");
				
				} else {
					logger.info("DataManager.java ::: getTxnMaster() :: No Data found against the Txn Id '" + custRefNum + "'");
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred : ", (Throwable) e);

		
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return MerchantId;
	}

	public String getSPMid(String merchantId) {
		logger.info("getTxnMaster() custRefNum=" + merchantId);
		String Mid=null;
		TransactionMaster TM = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		logger.info("DataManager.java ::: getTxnMaster() :: fetch Data against the Txn Id '" + merchantId + "'");
		try {
			conn = DBConnectionHandler.getConnection();
			final String sql = "select mid FROM tbl_merchant_mdr where merchant_id= ?";
			if (conn != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, merchantId);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					logger.info("DataManager.java ::: getTxnMaster() :: Inside rs " + merchantId);
					TM = new TransactionMaster();
					

					 Mid =rs.getString("mid");
				
				} else {
					logger.info("DataManager.java ::: getTxnMaster() :: No Data found against the Txn Id '" + merchantId + "'");
				}
			}
		} catch (Exception e) {
			logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred : ", (Throwable) e);

		
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException localSQLException6) {
				logger.error("DataManager.java ::: getTxnMaster() :: Error Occurred while closing Connection : ",
						(Throwable) localSQLException6);
			}
		}

		return Mid;
	}
	
	public ServiceProviderDto getServiceProviderById(Long spId) {
        Connection conn = null;
        CallableStatement callableStatement = null;
        ResultSet rs = null;
        ServiceProviderDto serviceProvider = null;

        try {
            conn = DBConnectionHandler.getConnection();
            final String sql = "CALL pro_getServiceProviderById(?)";

            if (conn != null) {
                conn.setAutoCommit(false);
                callableStatement = conn.prepareCall(sql);
                callableStatement.setLong(1, spId);
                callableStatement.execute();
                conn.commit();
                rs = callableStatement.getResultSet();

                if (rs.next()) {
                    serviceProvider = new ServiceProviderDto();
                    serviceProvider.setSp_id(rs.getLong("sp_id"));
                    serviceProvider.setSp_name(rs.getString("sp_name"));
                    serviceProvider.setMaster_mid(rs.getString("master_mid"));
                    serviceProvider.setMaster_tid(rs.getString("master_tid"));
                    serviceProvider.setSp_class_invoker(rs.getString("sp_class_invoker"));
                    serviceProvider.setApi_key(rs.getString("api_key"));
                    serviceProvider.setUdf_1(rs.getString("udf_1"));
                    serviceProvider.setUdf_2(rs.getString("udf_2"));
                    serviceProvider.setUdf_3(rs.getString("udf_3"));
                    serviceProvider.setUdf_4(rs.getString("udf_4"));
                    serviceProvider.setUdf_5(rs.getString("udf_5"));
                    serviceProvider.setCreatedOn(rs.getTimestamp("CreatedOn"));
                    serviceProvider.setCreatedBy(rs.getString("CreatedBy"));
                    serviceProvider.setModifiedOn(rs.getTimestamp("ModifiedOn"));
                    serviceProvider.setModifiedBy(rs.getString("ModifiedBy"));
                    serviceProvider.setIsDeleted(rs.getString("IsDeleted").charAt(0));
                    serviceProvider.setRefund_processor(rs.getString("refund_processor"));
                    serviceProvider.setBankIds(rs.getString("bankIds"));
                    serviceProvider.setInstrumentIds(rs.getString("instrumentIds"));
                    serviceProvider.setIsRefundAPI(rs.getString("isRefundAPI").charAt(0));
                    serviceProvider.setCutoff(rs.getString("cutoff"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return serviceProvider;
    }
	
	
}
