//package com.rew.offline.ecms;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import org.json.JSONObject;
//
//import com.rew.pg.db.DBConnectionHandler;
//
//public class ChallanDao20200626 {
//
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
//					resp.put("ru", rs.getString("return_url"));
//
//				}
//				System.out.println("getPgParameters values " + resp.toString());
//
//			} else {
//				System.out.println("getPgParameters connection is null");
//			}
//		} catch (Exception e) {
//			System.out.println("Gerring Error   :::    " + e);
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
//				System.out.println("Gerring Error   :::    " + e);
//			}
//		}
//		return resp.toString();
//	}
//
//	/*public boolean saveChallan(List<String> list) throws SQLException {
//
//		System.out.println("inside Dao in method saveChallan :::    ");
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
//				sql = "insert into  student_challan_details(challan_date, bnf_name, acc_no, ifsc_code, bank, branch, amount, utr_no, customerName, payoption, txnMode, emailId, mobile, add_details_1,CreatedOn,CreatedBy,ModifiedOn,ModifiedBy,IsDeleted)"
//						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//				System.out.println("sql:::    " + sql);
//
//				ps = con.prepareStatement(sql);
//
//				System.out.println("ps :::    " + ps);
//				System.out.println("Value 1 challan_date = "+list.get(8) + time);
//				System.out.println("Value 2 bnf_name = "+list.get(1));
//				System.out.println("Value 3 acc_no = "+list.get(2));
//				System.out.println("Value 4 ifsc_code = "+list.get(3));
//				System.out.println("Value 5 bank = "+list.get(5));
//				System.out.println("Value 6 branch = "+list.get(4));
//				System.out.println("Value 7 amount = "+list.get(6));
//				System.out.println("Value 8 utr_no = "+list.get(11));
//				System.out.println("Value 9 customerName = "+list.get(7));
//				System.out.println("Value 10 payoption = "+"CHALLAN");//payoption
//				System.out.println("Value 11 txnMode = "+"OFFLINE");//txnMode
//				System.out.println("Value 12 emailId = "+(String) list.get(9));
//				System.out.println("Value 13 mobile = "+list.get(10));
//				System.out.println("Value 14 Reference_No = "+list.get(0));
//				System.out.println("Value 15 Reference_No = "+list.get(8) + time);
//				System.out.println("Value 16 ModifiedOn = "+list.get(8) + time);
//
//				ps.setString(1, list.get(8) + time);
//				ps.setString(2, list.get(1));
//				ps.setString(3, list.get(2));
//				ps.setString(4, list.get(3));
//				ps.setString(5, list.get(5));
//				ps.setString(6, list.get(4));
//				ps.setString(7, list.get(6));
//				ps.setString(8, list.get(11));
//				ps.setString(9, list.get(7));
//				ps.setString(10, "CHALLAN");//payoption
//				ps.setString(11, "OFFLINE");//txnMode
//				ps.setString(12, (String) list.get(9));
//				ps.setString(13, list.get(10));
//				ps.setString(14, list.get(0));
//				ps.setString(15, list.get(8) + time);
//				ps.setString(16, "");
//				ps.setString(17, list.get(8) + time);
//				ps.setString(18, "");
//				ps.setString(19, "N");
//
//				ps.executeUpdate();
//
//				status = true;
//				System.out.println("save challan first status :::    " + status);
//				return status;
//
//			}
//
//			else {
//
//				System.out.println("saveChallan.java connection is null");
//
//			}
//		}
//
//		catch (Exception e) {
//			status = true;
//			e.printStackTrace();
//			System.out.println("Gerring Error   :::    " + e);
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
//	}*/
//	
//	public boolean saveChallan(List<String> list, String challanType) throws SQLException {
//
//		System.out.println("inside Dao in method saveChallan :::    ");
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
//				sql = "insert into tbl_student_challan_details(Reference_No, challan_date, bnf_name, acc_no, ifsc_code, bank, branch, amount, add_details_1, add_details_2, add_details_3, add_details_4, add_details_5, debit_acc_no, utr_no, customerId, customerName, payoption, txnMode, emailId, mobile, CreatedOn,CreatedBy,ModifiedOn,ModifiedBy,IsDeleted)"
//						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//				System.out.println("sql:::    " + sql);
//
//				ps = con.prepareStatement(sql);
//
//				for(int i=0;i<list.size();i++) {					
//					System.out.println("List Data ==>> list.get("+i+") = "+list.get(i));
//				}
//
//
//				String challan_date = list.get(8) + " "+time;//txn_date
//				String bnf_name = list.get(1);//beneficiaryName
//				String acc_no = list.get(2);//
//				String ifsc_code = list.get(3);
//				String bank = list.get(5);
//				String branch = list.get(4);
//				String amount = list.get(6);
//				String cust_name = list.get(7);
//				String date = list.get(8);
//				String emailid = list.get(9);
//				String mobile = list.get(10);
//				String utr_no = list.get(11);
//				String instrumentId = list.get(12);				 
//				String customerName = list.get(7);
//				String payoption = challanType;//payoption
//				String txnMode = list.get(12); //"OFFLINE";//txnMode
//				String emailId = (String) list.get(9);
//				String Reference_No = list.get(0);
//				String CreatedOn = list.get(8) + " "+time;
//				String ModifiedOn = list.get(8) + " "+time;
//				String add_details_1 = "";
//				String add_details_2 = "";
//				String add_details_3 = "";
//				String add_details_4 = "";
//				String add_details_5 = "";
//				String debit_acc_no = acc_no;
//				String customerId = list.get(13);
//				
//				System.out.println("--------------------------------START INSERT DATA FOR CHALLAN ------------------------");
//				
//				System.out.println("Value 1 challan_date = "+list.get(8) + time);
//				System.out.println("Value 2 bnf_name = "+list.get(1));
//				System.out.println("Value 3 acc_no = "+list.get(2));
//				System.out.println("Value 4 ifsc_code = "+list.get(3));
//				System.out.println("Value 5 bank = "+list.get(5));
//				System.out.println("Value 6 branch = "+list.get(4));
//				System.out.println("Value 7 amount = "+list.get(6));
//				System.out.println("Value 8 cust_name = "+list.get(7));
//				System.out.println("Value 9 date = "+list.get(8));
//				System.out.println("Value 10 emailid = "+list.get(9));
//				System.out.println("Value 11 mobile = "+list.get(10));
//				System.out.println("Value 12 utr_no = "+list.get(11));
//				System.out.println("Value 13 instrumentId = "+list.get(12));
//				
//				System.out.println("Value 14 customerName = "+list.get(7));
//				System.out.println("Value 15 payoption = "+"OL");//payoption
//				System.out.println("Value 11 txnMode = "+"OFFLINE");//txnMode
//				System.out.println("Value 12 emailId = "+(String) list.get(9));
//				System.out.println("Value 13 mobile = "+list.get(10));
//				System.out.println("Value 14 Reference_No = "+list.get(0));
//				System.out.println("Value 15 CreatedOn = "+list.get(8) + time);
//				System.out.println("Value 16 ModifiedOn = "+list.get(8) + time);
//
//				ps.setString(1,Reference_No);
//				ps.setString(2,challan_date);
//				ps.setString(3,bnf_name);
//				ps.setString(4,acc_no);
//				ps.setString(5,ifsc_code);
//				ps.setString(6,bank);
//				ps.setString(7,branch);
//				ps.setString(8,amount);
//				ps.setString(9,add_details_1);
//				ps.setString(10,add_details_2);
//				ps.setString(11,add_details_3);
//				ps.setString(12,add_details_4);
//				ps.setString(13,add_details_5);
//				ps.setString(14,debit_acc_no);
//				ps.setString(15,utr_no);
//				ps.setString(16,customerId);
//				ps.setString(17,customerName);
//				ps.setString(18,payoption);
//				ps.setString(19,txnMode);
//				ps.setString(20,emailId);
//				ps.setString(21, mobile);
//				ps.setString(22,CreatedOn);
//				ps.setString(23,""); //CreatedBy
//				ps.setString(24,ModifiedOn);
//				ps.setString(25,""); //ModifiedBy
//				ps.setString(26,"N");
//
//				ps.executeUpdate();
//
//				status = true;
//				System.out.println("save challan first status :::    " + status);
//				return status;
//
//			}
//
//			else {
//
//				System.out.println("saveChallan.java connection is null");
//
//			}
//		}
//
//		catch (Exception e) {
//			status = true;
//			e.printStackTrace();
//			System.out.println("Gerring Error   :::    " + e);
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
//	public boolean updateTransactionStatus(String refNo, String id) {
//
//		PreparedStatement ps = null;
//		Connection con = null;
//		ResultSet rs = null;
//		boolean resp = false;
//		String query = null;
//		try {
//			con = DBConnectionHandler.getConnection();
//			if (con != null) {
//				query = " update tbl_transactionmaster set trans_status='Pending'  where Id = ? and txn_Id=?";
//				ps = con.prepareStatement(query);
//				ps.setString(1, id);
//				ps.setString(2, refNo);
//
//				ps.executeUpdate();
//
//				resp = true;
//
//				System.out.println("getPgParameters status updated ");
//				return resp;
//
//			} else {
//				System.out.println("getPgParameters connection is null");
//			}
//		} catch (Exception e) {
//			System.out.println("Gerring Error   :::    " + e);
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
//				System.out.println("Gerring Error   :::    " + e);
//			}
//		}
//		return resp;
//	}
//}
//
