<%@page import="java.io.FileOutputStream"%>
<%@page import="org.apache.poi.ss.usermodel.Cell"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.apache.poi.xssf.usermodel.XSSFRow"%>
<%@page import="org.apache.poi.xssf.usermodel.XSSFSheet"%>
<%@page import="org.apache.poi.xssf.usermodel.XSSFWorkbook"%>
<%@page import="javax.mail.Session"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="javax.mail.Transport"%>
<%@page import="javax.mail.internet.MimeMultipart"%>
<%@page import="javax.mail.Multipart"%>
<%@page import="javax.activation.DataHandler"%>
<%@page import="javax.activation.FileDataSource"%>
<%@page import="javax.activation.DataSource"%>
<%@page import="javax.mail.internet.MimeBodyPart"%>
<%@page import="javax.mail.BodyPart"%>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.internet.InternetAddress"%>
<%@page import="javax.mail.internet.MimeMessage"%>
<%@page import="javax.mail.PasswordAuthentication"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.opencsv.CSVWriter"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.File"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="com.rew.pg.db.DBConnectionHandler"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>


<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	starting service
	<%
	Logger log = Logger.getLogger("JSPS.pspcl.jsp");

	ResultSet rs= null;
	ResultSet rs2=null;
	//ResultSet rs1 = null;

	Connection con = null;
	PreparedStatement ps = null;
	PreparedStatement ps2 = null;
	//PreparedStatement ps1 = null;

	try {
		String MerchantId = PGUtils.getPropertyValue("pspclId");
		con = DBConnectionHandler.getConnection();
		log.info(" <<<<<<<<<<<<<<<<<<<< PSPCL EOD report  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		String email = "r@y.in";//"aobankingaccounts@yahoo.com";
		if (con != null) {
			
			 /* StringBuilder query = new StringBuilder("");
			StringBuilder query1 = new StringBuilder("");
			
			
			query1.append(" select email_id from tbl_mstmerchant where MerchantId='"+MerchantId+"'");
			ps1 = con.prepareStatement(query1.toString());
			
			log.info(query1.toString());
			
			ps1.execute();
			rs1 = ps1.getResultSet();
			String email=null;
			if (rs1.next()) {
				
				email=rs1.getString("email_id");
				
			} 
			 */
			
			Calendar cal = Calendar.getInstance();
			System.out.println("Today : " + cal.getTime());
			// Substract 30 days from the calendar
			cal.add(Calendar.DATE, -1);
			System.out.println("30 days ago: " + cal.getTime());
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = dateFormat.format(cal.getTime());
			String yDate = formattedDate;

			String query = "select txn_Id as TransactionID,udf1 as CANo,DATE_FORMAT(date_time ,'%d-%m-%Y') as TransactionDate,DATE_FORMAT(date_time ,'%d-%m-%Y') as Posting,txn_amount as Amount,txn_amount  as TotalAmount,sur_charge,txn_amount+sur_charge as Total,trans_status,Id  from tbl_transactionmaster where merchant_id =? and trans_status=? and date_time >=? and date_time <=?";

			log.info("query : " + query);
			ps = con.prepareStatement(query.toString());
			ps.setString(1, MerchantId);
			ps.setString(2, "Ok");
			ps.setString(3, yDate + " 00:00:00");
			ps.setString(4, yDate + " 23:59:59");
			ps.execute();
			rs = ps.getResultSet();
			
			ps2 = con.prepareStatement(query.toString());
			ps2.execute();
			rs2 = ps2.getResultSet();

			//for csv file 

			File file = new File("PSPCL_MIS.csv");
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

				int status = 0;
				String mailFrom ="";//PGUtils.getPropertyValue("mailFrom");
				String mailCC = "";//PGUtils.getPropertyValue("mailCC");
				String mailCC1 = "";	
	       		String mailCC2 = "";//skmpspcl@gmail.com";
	       		String mailCC3 = "";
	       		String mailCC4 = "";
	       		 
				String host = "";//PGUtils.getPropertyValue("smtpHost");
				final String user1 = "";//PGUtils.getPropertyValue("smtpUser");
				final String pass = "";//PGUtils.getPropertyValue("smtpPass");
				String port = "";//PGUtils.getPropertyValue("smtpPort");
				String startTls = "";//PGUtils.getPropertyValue("smtpStartTLS");
				String sslEnable = "";//PGUtils.getPropertyValue("smtpSSLEnable");
				String smtpAuth = "";//PGUtils.getPropertyValue("smtpAuth");

				log.info("user1====>"+user1);
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
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
					message.addRecipient(Message.RecipientType.CC,new InternetAddress(mailCC));
					message.addRecipient(Message.RecipientType.CC,new InternetAddress(mailCC1));
					message.addRecipient(Message.RecipientType.CC,new InternetAddress(mailCC2));
					message.setSubject("PSPCL Report");

					BodyPart messageBodyPart1 = new MimeBodyPart();
					messageBodyPart1.setText("Dear Admin,");

					MimeBodyPart messageBodyPart2 = new MimeBodyPart();
/* 
					// String filename = "SendAttachment.java";
					DataSource source = new FileDataSource(file);
					messageBodyPart2.setDataHandler(new DataHandler(source));
					messageBodyPart2.setFileName("PSPCL_MIS.csv");

					MimeBodyPart messageBodyPart3 = new MimeBodyPart();

					// String filename = "SendAttachment.java";
					DataSource source2 = new FileDataSource(file1);
					messageBodyPart3.setDataHandler(new DataHandler(source2));
					messageBodyPart3.setFileName("PSPCL_MIS.xls");

					Multipart multipart = new MimeMultipart();
					multipart.addBodyPart(messageBodyPart1);
					multipart.addBodyPart(messageBodyPart2);
					multipart.addBodyPart(messageBodyPart3); */

					//message.setContent(multipart);

					Transport.send(message);

					log.info("message sent....");
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				status = 1;
				log.info("PSPCLReport.java ::: sendMail() :: Mail sent successfully and Status : " + status);

				// closing writer connection 
				try {
					file.delete(); // I delete the file
					file1.delete();
					log.info("file deleted successfully");
				} catch (Exception e) {
					log.info(e.getMessage());
				}

			}
		}
	} catch (Exception e) {
		e.printStackTrace();
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
			log.info(e.getMessage());
		}

	}
%>

	
</body>
</html>