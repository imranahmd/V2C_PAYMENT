//package com.rew.offline.ecms;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.rew.pg.db.DBConnectionHandler;
//
//import org.json.JSONObject;
//
//public class ChallanDao {
//	static Logger log = LoggerFactory.getLogger(ChallanDao.class.getName());
//	public String getPgParameters(String id) {
//		PreparedStatement ps = null;
//		Connection con = null;
//		ResultSet rs = null;
//		JSONObject resp = new JSONObject();
//		String query = null;
//		try {
//			con = DBConnectionHandler.getConnection();
//			if (con != null) {
//				query = " select * from  tbl_transactionmaster where Id  = ? ";
//				ps = con.prepareStatement(query);
//				ps.setString(1, id);
//
//				rs = ps.executeQuery();
//
//				while (rs.next()) {
//
//					resp.put("refNo", rs.getString("txn_Id"));
//					resp.put("date_time", rs.getString("date_time"));
//					resp.put("mobile_no", rs.getString("mobile_no"));
//					resp.put("email_id", rs.getString("email_id"));
//					resp.put("amt", rs.getString("txn_amount"));
//					resp.put("udf1", rs.getString("udf1"));
//					resp.put("udf2", rs.getString("udf2"));
//					resp.put("udf3", rs.getString("udf3"));
//					resp.put("udf4", rs.getString("udf4"));
//					resp.put("udf5", rs.getString("udf5"));
//					resp.put("udf6", rs.getString("udf6"));
//					resp.put("ru", rs.getString("return_url"));
//
//				}
//				log.info("getPgParameters values " + resp.toString());
//
//			} else {
//				log.info("getPgParameters connection is null");
//			}
//		} catch (Exception e) {
//			log.info("Gerring Error   :::    " + e);
//		} finally {
//
//			try {
//				if (rs != null) {
//					rs.close();
//					rs = null;
//				}
//				if (ps != null) {
//					ps.close();
//					ps = null;
//				}
//				if (con != null) {
//					con.close();
//					con = null;
//				}
//			} catch (SQLException e) {
//				log.info("Gerring Error   :::    " + e);
//			}
//		}
//		return resp.toString();
//	}
//
//	
//	public boolean saveChallan(Map<String,String> map, String challanType) throws SQLException {
//
//		log.info("inside Dao in method saveChallan :::    ");
//		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//		
//		Connection con = null;
//
//		PreparedStatement ps = null;
//		String sql = null;
//		boolean status = false;
//
//		try {
//
//			con = DBConnectionHandler.getConnection();
//
//			if (con != null) {
//				// String utrNo="123"+System.currentTimeMillis();
//
//				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//
//				String time = dateFormat.format(new Date());
//
//				sql = "insert into tbl_student_challan_details(Reference_No, challan_date,Expiry_date,bnf_name, acc_no, ifsc_code, bank, branch, amount, add_details_1, add_details_2, add_details_3, add_details_4, add_details_5, debit_acc_no, utr_no, customerId, customerName, payoption, txnMode, emailId, mobile, CreatedOn,CreatedBy,ModifiedOn,ModifiedBy,IsDeleted)"
//						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//				log.info("sql:::    " + sql);
//
//				ps = con.prepareStatement(sql);
//
//				for(int i=0;i<map.size();i++) {					
//					log.info("List Data ==>> list.get("+i+") = "+map.get(i));
//				}
//
//
//				String challan_date = map.get("txn_date") + " "+time;//txn_date
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//				LocalDateTime dateTime = LocalDateTime.parse(challan_date, formatter);
//				String expiry_date = dateTime.plusDays(1).toString();
//				String bnf_name = map.get("beneficiaryName");//beneficiaryName
//				String acc_no = map.get("acc_no");//
//				String ifsc_code = map.get("ifsc");
//				String bank = map.get("bank");
//				String branch = map.get("branch");
//				String amount = map.get("amount");
//				String cust_name = map.get("cust_name");
//				String date = map.get("txn_date");
//				String emailid = map.get("email");
//				String mobile = map.get("phone");
//				String utr_no = map.get("utrNo");
//				String instrumentId = map.get("Op");				 
//				String customerName = map.get("cust_name");
//				String payoption = challanType;//payoption
//				String txnMode = map.get("Op"); //"OFFLINE";//txnMode
//				String emailId =  map.get("email");
//				String Reference_No = map.get("reference_no");
//				String CreatedOn = map.get("txn_date") + " "+time;
//				String ModifiedOn = map.get("txn_date") + " "+time;
//				String add_details_1 = "";
//				String add_details_2 = "";
//				String add_details_3 = "";
//				String add_details_4 = "";
//				String add_details_5 = "";
//				String debit_acc_no = acc_no;
//				String customerId = map.get("customerId");
//				
//				log.info("--------------------------------START INSERT DATA FOR CHALLAN ------------------------");
//				
//				log.info("Value 1 challan_date = "+date + time);
//				log.info("Value 2 bnf_name = "+bnf_name);
//				log.info("Value 3 acc_no = "+acc_no);
//				log.info("Value 4 ifsc_code = "+ifsc_code);
//				log.info("Value 5 bank = "+bank);
//				log.info("Value 6 branch = "+branch);
//				log.info("Value 7 amount = "+amount);
//				log.info("Value 8 cust_name = "+cust_name);
//				log.info("Value 9 date = "+date);
//				log.info("Value 10 emailid = "+emailid);
//				log.info("Value 11 mobile = "+mobile);
//				log.info("Value 12 utr_no = "+utr_no);
//				log.info("Value 13 instrumentId = "+instrumentId);
//				
//				log.info("Value 14 customerName = "+customerName);
//				log.info("Value 15 payoption = "+"OL");//payoption
//				log.info("Value 11 txnMode = "+"OFFLINE");//txnMode
//				log.info("Value 12 emailId = "+emailId);
//				log.info("Value 13 mobile = "+mobile);
//				log.info("Value 14 Reference_No = "+Reference_No);
//				log.info("Value 15 CreatedOn = "+CreatedOn + time);
//				log.info("Value 16 ModifiedOn = "+ModifiedOn + time);
//
//				ps.setString(1,Reference_No);
//				ps.setString(2,challan_date);
//				ps.setString(3,expiry_date);
//				ps.setString(4,bnf_name);
//				ps.setString(5,acc_no);
//				ps.setString(6,ifsc_code);
//				ps.setString(7,bank);
//				ps.setString(8,branch);
//				ps.setString(9,amount);
//				ps.setString(10,add_details_1);
//				ps.setString(11,add_details_2);
//				ps.setString(12,add_details_3);
//				ps.setString(13,add_details_4);
//				ps.setString(14,add_details_5);
//				ps.setString(15,debit_acc_no);
//				ps.setString(16,utr_no);
//				ps.setString(17,customerId);
//				ps.setString(18,customerName);
//				ps.setString(19,payoption);
//				ps.setString(20,txnMode);
//				ps.setString(21,emailId);
//				ps.setString(22, mobile);
//				ps.setString(23,CreatedOn);
//				ps.setString(24,""); //CreatedBy
//				ps.setString(25,ModifiedOn);
//				ps.setString(26,""); //ModifiedBy
//				ps.setString(27,"N");
//
//				ps.executeUpdate();
//
//				status = true;
//				log.info("save challan first status :::    " + status);
//				return status;
//
//			}
//
//			else {
//
//				log.info("saveChallan.java connection is null");
//
//			}
//		}
//
//		catch (Exception e) {
//			status = true;
//			e.printStackTrace();
//			log.info("Gerring Error   :::    " + e);
//
//		} finally {
//
//			if (ps != null) {
//				ps.close();
//				// ps=null;
//			}
//			if (con != null) {
//				con.close();
//				// con=null;
//			}
//
//		}
//
//		return status;
//
//	}
//
//  public boolean updateTransactionStatus(String txnId, String id) {
//
//    PreparedStatement ps = null;
//    Connection con = null;
//    ResultSet rs = null;
//    boolean resp = false;
//    String query = null;
//    try {
//      con = DBConnectionHandler.getConnection();
//      if (con != null) {
//        query =
//            " update tbl_transactionmaster set trans_status='Pending'  where Id = ? and txn_Id=?";
//        ps = con.prepareStatement(query);
//        ps.setString(1, id);
//        ps.setString(2, txnId);
//
//				ps.executeUpdate();
//
//				resp = true;
//
//        log.info("status updated ");
//        return resp;
//
//      } else {
//        log.error("connection is null");
//      }
//    } catch (Exception e) {
//      log.error("Getting Error   :::    " + e);
//    } finally {
//
//      try {
//        if (rs != null) {
//          rs.close();
//          rs = null;
//        }
//        if (ps != null) {
//          ps.close();
//          ps = null;
//        }
//        if (con != null) {
//          con.close();
//          con = null;
//        }
//      } catch (SQLException e) {
//        log.error("Getting Error   :::    " + e);
//      }
//    }
//    return resp;
//  }
//
//  public Optional<String> getMappedVirtualAcc(String walletID) {
//    PreparedStatement ps = null;
//    Connection con = null;
//    ResultSet rs = null;
//    String query = null;
//    String virtualAcc = null;
//    try {
//      con = DBConnectionHandler.getConnection();
//      if (con != null) {
//        query = " select ecms_acc_no from fasttag_wallet_ecms_mapping where fastag_wallet_id = ?";
//        log.info("Running query" + query);
//        ps = con.prepareStatement(query);
//        ps.setString(1, walletID);
//
//        rs = ps.executeQuery();
//
//        while (rs.next()) {
//          virtualAcc = rs.getString("ecms_acc_no");
//        }
//
//        return Optional.ofNullable(virtualAcc);
//
//      } else {
//        log.error("connection is null");
//      }
//    } catch (Exception e) {
//      log.error("Getting Error   :::    " + e);
//    } finally {
//
//      try {
//        if (rs != null) {
//          rs.close();
//          rs = null;
//        }
//        if (ps != null) {
//          ps.close();
//          ps = null;
//        }
//        if (con != null) {
//          con.close();
//          con = null;
//        }
//      } catch (SQLException e) {
//        log.error("Getting Error   :::    " + e);
//      }
//    }
//    return Optional.ofNullable(virtualAcc);
//  }
//
//  public Optional<String> getAccNoPrefix(String merchantId) {
//    PreparedStatement ps = null;
//    Connection con = null;
//    ResultSet rs = null;
//    String query = null;
//    String prefix = null;
//    try {
//      con = DBConnectionHandler.getConnection();
//      if (con != null) {
//        query = " select prefix from ecms_merchant_prefix_mapping where merchant_id = ?";
//        log.info("Running query" + query);
//        ps = con.prepareStatement(query);
//        ps.setString(1, merchantId);
//
//	        rs = ps.executeQuery();
//
//	        while (rs.next()) {
//	          prefix = rs.getString("prefix");
//	        }
//
//	        return Optional.ofNullable(prefix);
//
//	      } else {
//	        log.error("connection is null");
//	      }
//	    } catch (Exception e) {
//	      log.error("Getting Error   :::    " + e);
//	    } finally {
//
//	      try {
//	        if (rs != null) {
//	          rs.close();
//	          rs = null;
//	        }
//	        if (ps != null) {
//	          ps.close();
//	          ps = null;
//	        }
//	        if (con != null) {
//	          con.close();
//	          con = null;
//	        }
//	      } catch (SQLException e) {
//	        log.error("Getting Error   :::    " + e);
//	      }
//	    }
//	    return Optional.ofNullable(prefix);
//	  }
//
//  public boolean challanExists(String referenceNo) {
//    PreparedStatement ps = null;
//    Connection con = null;
//    ResultSet rs = null;
//    String query = null;
//    String prefix = null;
//    try {
//      con = DBConnectionHandler.getConnection();
//      if (con != null) {
//        query = " select Reference_No from tbl_student_challan_details where Reference_No = ?";
//        log.info("Running query" + query);
//        ps = con.prepareStatement(query);
//        ps.setString(1, referenceNo);
//
//        rs = ps.executeQuery();
//
//        while (rs.next()) {
//          prefix = rs.getString("Reference_No");
//        }
//
//        return Optional.ofNullable(prefix).isPresent();
//
//			} else {
//				log.info("getPgParameters connection is null");
//			}
//		} catch (Exception e) {
//			log.info("Gerring Error   :::    " + e);
//		} finally {
//
//			try {
//				if (rs != null) {
//					rs.close();
//					rs = null;
//				}
//				if (ps != null) {
//					ps.close();
//					ps = null;
//				}
//				if (con != null) {
//					con.close();
//					con = null;
//				}
//			} catch (SQLException e) {
//				log.info("Gerring Error   :::    " + e);
//			}
//		}
//		return false;
//	}
//}
//
