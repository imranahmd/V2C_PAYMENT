package com.apps.lbps.mware.processor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.pg.db.DBConnectionHandler;

public class CRUDService {
    private static Logger logger = LoggerFactory.getLogger(CRUDService.class);

	public void insertPaymentLinkDetails(String invoice_no, String merchant_id, float amount, Timestamp date_time, String valid_upto, String cust_name, String remarks, String email_id, String contact_number,
			String link, String status, Timestamp createdOn, String createdBy, Timestamp modifiedOn, String modifiedBy) 
	{

		Connection con = DBConnectionHandler.getConnection();
		String Query=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		String date = new SimpleDateFormat("yyyy-DD-mm HH:mm:ss").format(new Date());
		try {

			if(con != null)
			{
				Query = "insert into tbl_mstibps (invoice_no, merchant_id, amount, date_time, valid_upto, cust_name, email_id, contact_number, "
						+ "link, remarks, status, CreatedOn, CreatedBy, ModifiedOn, ModifiedBy) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				logger.info("query ===>",Query);
				ps=con.prepareStatement(Query);
				ps.setString(1, invoice_no);
				ps.setString(2, merchant_id);
				ps.setString(3, amount+"");
				ps.setString(4, date);
				ps.setString(5, valid_upto);
				ps.setString(6, cust_name);
				ps.setString(7, email_id);
				ps.setString(8, contact_number);
				ps.setString(9, link);
				ps.setString(10, remarks);
				ps.setString(11, "0");
				ps.setString(12, date);
				ps.setString(13, createdBy);
				ps.setString(14, date);
				ps.setString(15, modifiedBy);
				ps.executeUpdate();
				logger.info("recored inserted successfully");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(rs!=null)
				{
					rs.close();
					rs = null;
				}

				if(ps!=null)
				{
					ps.close();
					ps = null;
				}
				if(con!=null)
				{
					con.close();
					con = null;
				}

			}
			catch (SQLException e) 
			{
				logger.error("Error Occurred while closing Connection : "+e);
			}
		}

	}

	public List<String> getPymentData(String invoiceNo)
	{

		List<String> reportList = null;	
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		try 
		{

			con = DBConnectionHandler.getConnection();

			if(con != null)
			{
				StringBuilder query = new StringBuilder("");

				query.append("select merchant_id,invoice_no,amount,date_time,cust_name,email_id,contact_number,vendorId,cardId from tbl_mstibps where invoice_no=?");

				logger.debug("query : "+query);
				ps = con.prepareStatement(query.toString());
				ps.setString(1, invoiceNo);
				ps.execute();
				rs = ps.getResultSet();

				while (rs.next()) 
				{
					reportList = new ArrayList<String>();

					reportList.add(rs.getString("invoice_no"));
					reportList.add(rs.getString("amount"));
					reportList.add(rs.getString("date_time"));
					reportList.add(rs.getString("cust_name"));
					reportList.add(rs.getString("email_id"));
					reportList.add(rs.getString("contact_number"));
					reportList.add(rs.getString("vendorId"));
					reportList.add(rs.getString("cardId"));					
					reportList.add(rs.getString("merchant_id"));//Merchant Id	

				}

			}
		}

		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{

			try 
			{
				if (rs != null) 
					rs.close();

				if (ps != null) 
					ps.close();

				if (con != null) 
					con.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}

		return reportList;

	}

	public String getStatus(String invoiceNo,String merchantId)
	{
		String status="2";
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		try 
		{

			con = DBConnectionHandler.getConnection();

			if(con != null)
			{
				StringBuilder query = new StringBuilder("");
				query.append("select status from tbl_mstibps where invoice_no=?");

				logger.debug("query :{} ",query);
				ps = con.prepareStatement(query.toString());
				ps.setString(1, invoiceNo);
				ps.setString(2, merchantId);
				ps.execute();
				rs = ps.getResultSet();

				while (rs.next()) 
				{
					status=rs.getString("status");					
				}

			}
		}

		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{

			try 
			{
				if (rs != null) 
					rs.close();

				if (ps != null) 
					ps.close();

				if (con != null) 
					con.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}

		return status;

	}

	public void updatePaymentStatus(String invoiceNo,String stts)
	{
		Connection conn=null;
		PreparedStatement stmt=null;
		try{

			conn = DBConnectionHandler.getConnection();

			if (conn!=null) {
				String sql = "update tbl_mstibps set status=? where invoice_no=?";

				logger.debug("query==>:{}",sql);
				stmt=conn.prepareStatement(sql);
				stmt.setString(1, stts);
				stmt.setString(2, invoiceNo.trim());
				stmt.executeUpdate(sql);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		finally {

			try
			{


				if(stmt!=null)
				{
					stmt.close();
					stmt = null;
				}
				if(conn!=null)
				{
					conn.close();
					conn = null;
				}
			}catch(Exception ee) {
				ee.printStackTrace();
			}

		}		
	}

	public boolean checkInvoiceNumber(String invoiceno,String merchantid)
	{

		Connection con = DBConnectionHandler.getConnection();

		boolean result=false;

		String Query=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {

			if(con != null)
			{
				Query = "select * from tbl_mstibps where invoice_no=?";
				ps=con.prepareStatement(Query);
				ps.setString(1, invoiceno);
				rs=ps.executeQuery();

				while(rs.next())
				{
					result=true;
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(rs!=null)
				{
					rs.close();
					rs = null;
				}

				if(ps!=null)
				{
					ps.close();
					ps = null;
				}
				if(con!=null)
				{
					con.close();
					con = null;
				}
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return result;		
	}
	
	
	public boolean isValidTxn(String invoiceno, String merchantid) {
		new DBConnectionHandler();
		Connection con = DBConnectionHandler.getConnection();
		boolean result = false;
		String Query = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (con != null) {
				Query = "SELECT Id FROM tbl_transactionmaster WHERE date_time >= DATE_SUB(NOW(), INTERVAL 2 HOUR) AND trans_status = 'To' AND "
						+ "resp_status not in ('99') and resp_message not in ('RES') and merchant_id = ? AND txn_Id LIKE ?";
				ps = con.prepareStatement(Query);
				ps.setString(1, merchantid);
				ps.setString(2, invoiceno + "%");
				rs = ps.executeQuery();

				while (rs.next()) {
					result = true;
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
