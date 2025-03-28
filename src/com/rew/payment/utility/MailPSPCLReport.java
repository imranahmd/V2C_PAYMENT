package com.rew.payment.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;
import com.rew.pg.db.DBConnectionHandler;

/**
 * Servlet implementation class PSPCLReport
 */
/*@WebServlet(name = "MailPSPCLReport", urlPatterns = { "/MailPSPCLReport" })*/
public class MailPSPCLReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(MailPSPCLReport.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MailPSPCLReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ResultSet rs= null;
		ResultSet rs2=null;
		//ResultSet rs1 = null;

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		//PreparedStatement ps1 = null;
		String filepath="/home/MailPSPCLReport";
		try {
			String MerchantId = PGUtils.getPropertyValue("pspclId");
			con = DBConnectionHandler.getConnection();
			logger.info(" <<<<<<<<<<<<<<<<<<<< PSPCL EOD report  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			String email = "skmpspcl@gmail.com";//
			if (con != null) {

				/* StringBuilder query = new StringBuilder("");
				StringBuilder query1 = new StringBuilder("");


				query1.append(" select email_id from tbl_mstmerchant where MerchantId='"+MerchantId+"'");
				ps1 = con.prepareStatement(query1.toString());

				logger.info(query1.toString());

				ps1.execute();
				rs1 = ps1.getResultSet();
				String email=null;
				if (rs1.next()) {

					email=rs1.getString("email_id");

				} 
				 */

				Calendar cal = Calendar.getInstance();
				logger.info("Today : " + cal.getTime());
				// Substract 30 days from the calendar
				cal.add(Calendar.DATE, -1);
				logger.info("30 days ago: " + cal.getTime());
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = dateFormat.format(cal.getTime());
				String yDate = formattedDate;

				String query = "select txn_Id as TransactionID,udf1 as CANo,DATE_FORMAT(date_time ,'%d-%m-%Y') as TransactionDate,DATE_FORMAT(date_time ,'%d-%m-%Y') as Posting,"
						+ "txn_amount as Amount,txn_amount  as TotalAmount,sur_charge,txn_amount+sur_charge as Total,trans_status,Id  from tbl_transactionmaster "
						+ "where merchant_id =? and trans_status=? and date_time >=? and date_time <=?";

				logger.info("query : " + query);
				ps = con.prepareStatement(query.toString());
				ps.setString(1, MerchantId);
				ps.setString(2, "Ok");
				ps.setString(3, yDate + " 00:00:00");
				ps.setString(4, yDate + " 23:59:59");
				ps.execute();
				rs = ps.getResultSet();

				ps2 = con.prepareStatement(query.toString());
				ps2.setString(1, MerchantId);
				ps2.setString(2, "Ok");
				ps2.setString(3, yDate + " 00:00:00");
				ps2.setString(4, yDate + " 23:59:59");
				ps2.execute();
				rs2 = ps2.getResultSet();

				//for csv file 

				File file = new File("PSPCL_MIS.csv");
				logger.info("file path===>"+file.getAbsolutePath());
				FileWriter outputfile = new FileWriter(file);

				CSVWriter writer = new CSVWriter(outputfile);

				List<String[]> data = new ArrayList<String[]>();
				data.add(new String[] { "Branch", "AgencyCode", "Transaction ID", "CA No", " Transaction Date",
						"Posting Date", "Amount", "Payment Mode", "Service Charge", "Total Amount", "Pg_Ref_Id",
				"Status" });

				while (rs.next()) {
					String s = "Failed";
					if (rs.getString("trans_status").equalsIgnoreCase("OK")) {
						s = "Success";
					}

					String ca_no = rs.getString("CANo");
					if (rs.getString("CANo").contains("_")) {
						ca_no = rs.getString("CANo").split("\\_")[0];
					}
					data.add(new String[] { "HDF", "11", rs.getString("TransactionID"), ca_no,
							rs.getString("TransactionDate"), rs.getString("TransactionDate"),
							rs.getString("Amount"), "CS", rs.getString("sur_charge"), rs.getString("Total"),
							rs.getString("Id"), s });
				}

				writer.writeAll(data);

				writer.close();

				// for xls file

				//File file1 = new File("PSPCL_MIS.xls"); 
				//FileWriter outputfile = new FileWriter(file1); 

				List<String[]> data1 = new ArrayList<String[]>();
				if (rs2 != null) {

					//data1.add(new String[] {"Branch","AgencyCode","Transaction ID","CA No"," Transaction Date","Posting Date","Amount","Payment Mode","Service Charge","Total Amount","Pg_Ref_Id","Status"}); 

					XSSFWorkbook workbook = new XSSFWorkbook();

					//Create a blank sheet
					XSSFSheet spreadsheet = workbook.createSheet("PSPCL_Report");

					//Create row object
					XSSFRow row;

					//This data needs to be written (Object[])
					Map<String, Object[]> empinfo = new TreeMap<String, Object[]>();
					empinfo.put("1",
							new Object[] { "Branch", "AgencyCode", "Transaction ID", "CA No", " Transaction Date",
									"Posting Date", "Amount", "Payment Mode", "Service Charge", "Total Amount",
									"Pg_Ref_Id", "Status" });

					int i = 2;
					while (rs2.next()) {
						String s = "Failed";
						if (rs2.getString("trans_status").equalsIgnoreCase("OK")) {
							s = "Success";
						}
						String ca_no = rs2.getString("CANo");
						if (rs2.getString("CANo").contains("_")) {
							ca_no = rs2.getString("CANo").split("\\_")[0];
						}

						empinfo.put(i + "",
								new Object[] { "HDF", "11", rs2.getString("TransactionID"), ca_no,
										rs2.getString("TransactionDate"), rs2.getString("TransactionDate"),
										rs2.getString("Amount"), "CS", rs2.getString("sur_charge"),
										rs2.getString("Total"), rs2.getString("Id"), s });

						i++;

					}

					Set<String> keyid = empinfo.keySet();
					int rowid = 0;

					for (String key : keyid) {
						row = spreadsheet.createRow(rowid++);
						Object[] objectArr = empinfo.get(key);
						int cellid = 0;

						for (Object obj : objectArr) {
							Cell cell = row.createCell(cellid++);
							cell.setCellValue((String) obj);
						}
					}
					//Write the workbook in file system

					File file1 = new File("PSPCL_MIS.xls");
					FileOutputStream out1 = new FileOutputStream(file1);

					workbook.write(out1);
					out1.close();
					workbook.close();

					int status = 0;
					String mailFrom = PGUtils.getPropertyValue("mailFrom");
					String mailCC = "vishwakarma3.mithilesh@gmail.com";//
					String mailCC1 = "vishwakarma3.mithilesh@gmail.com";	
					String mailCC2 = "vishwakarma3.mithilesh@gmail.com";//
					String mailCC3 = "vishwakarma3.mithilesh@gmail.com";//
					String mailCC4 = "vishwakarma3.mithilesh@gmail.com";
					String mailCC5 = "vishwakarma3.mithilesh@gmail.com";
					String mailCC6 = "vishwakarma3.mithilesh@gmail.com";

					String host = PGUtils.getPropertyValue("smtpHost");
					final String user1 = PGUtils.getPropertyValue("smtpUser");
					final String pass = PGUtils.getPropertyValue("smtpPass");
					String port = PGUtils.getPropertyValue("smtpPort");
					String startTls = PGUtils.getPropertyValue("smtpStartTLS");
					String sslEnable = PGUtils.getPropertyValue("smtpSSLEnable");
					String smtpAuth = PGUtils.getPropertyValue("smtpAuth");

					logger.info("user1====>"+user1);
					Properties properties = System.getProperties();
					properties.setProperty("mail.smtp.host", host);
					properties.setProperty("mail.smtp.user", user1);
					properties.setProperty("mail.smtp.password", pass);
					properties.put("mail.smtp.auth", smtpAuth);
					properties.put("mail.smtp.starttls.enable", startTls);
					properties.put("mail.smtp.ssl.enable", sslEnable);
					properties.put("mail.smtp.port", port);

					properties.put("mail.user", user1);

					properties.put("mail.password", pass);

					// Authorized the Session object.

					Session mailSession = Session.getInstance(properties, new javax.mail.Authenticator() {

						@Override

						protected PasswordAuthentication getPasswordAuthentication() {

							return new PasswordAuthentication(user1, pass);

						}

					});
					//Session mailSession = Session.getDefaultInstance(properties);

					/*
					javax.mail.Session s = javax.mail.Session.getDefaultInstance(properties, 
							new javax.mail.Authenticator(){
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									user1, pass);// Specify the Username and the PassWord
						}
					});
					 */
					try {

						MimeMessage message = new MimeMessage(mailSession);
						message.setFrom(new InternetAddress(mailFrom));
						message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
						message.addRecipient(Message.RecipientType.CC,new InternetAddress(mailCC));
						message.addRecipient(Message.RecipientType.CC,new InternetAddress(mailCC1));
						message.addRecipient(Message.RecipientType.CC,new InternetAddress(mailCC2));
						message.addRecipient(Message.RecipientType.CC,new InternetAddress(mailCC3));
						message.addRecipient(Message.RecipientType.CC,new InternetAddress(mailCC4));
						message.addRecipient(Message.RecipientType.CC,new InternetAddress(mailCC5));
						message.addRecipient(Message.RecipientType.CC,new InternetAddress(mailCC6));
						message.setSubject("PSPCL Report");
						logger.info("11111111111");
						StringBuilder sb=new StringBuilder();
						sb.append("Dear Team,\n\n");
						sb.append("Greeting from PAY!!!\n\n");
						sb.append("Please find enclosed transactions report for date "+yDate+"\n\n");
						sb.append("Do let us know if you need more information/clarification on same.");
						BodyPart messageBodyPart1 = new MimeBodyPart();
						messageBodyPart1.setText(sb.toString());
						logger.info("22222222");
						MimeBodyPart messageBodyPart2 = new MimeBodyPart();

						// String filename = "SendAttachment.java";
						DataSource source = new FileDataSource(file);
						messageBodyPart2.setDataHandler(new DataHandler(source));
						messageBodyPart2.setFileName("PSPCL_MIS.csv");
						logger.info("333333333");
						MimeBodyPart messageBodyPart3 = new MimeBodyPart();

						// String filename = "SendAttachment.java";
						DataSource source2 = new FileDataSource(file1);
						messageBodyPart3.setDataHandler(new DataHandler(source2));
						messageBodyPart3.setFileName("PSPCL_MIS.xls");
						logger.info("444444444");
						Multipart multipart = new MimeMultipart();
						multipart.addBodyPart(messageBodyPart1);
						multipart.addBodyPart(messageBodyPart2);
						multipart.addBodyPart(messageBodyPart3);
						logger.info("5555555555");
						message.setContent(multipart);
						logger.info("6666666");
						Transport.send(message);
						logger.info("7777777");
						logger.info("message sent....");
						status = 1;
					} catch (MessagingException ex) {
						logger.info(ex.getMessage());
						status=0;
					}

					
					logger.info("PSPCLReport.java ::: sendMail() :: Mail sent successfully and Status : " + status);

					// closing writer connection 
					if(status==1){
						try {
							file.delete(); // I delete the file
							file1.delete();
							logger.info("file deleted successfully");
						} catch (Exception e) {
							logger.info(e.getMessage());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			//session.close();
			try {
				if (rs != null)
					rs.close();
				if (rs2 != null)
					rs2.close();
				if (ps != null)
					ps.close();
				if (ps2 != null)
					ps2.close();

				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.info(e.getMessage(),e);
			}

		}

	}

}
