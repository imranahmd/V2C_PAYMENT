package com.rew.payment.utility;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataPost {
	private static Logger logger = LoggerFactory.getLogger(DataPost.class);

	private javax.net.ssl.SSLSocketFactory getSocketFactory() throws KeyManagementException, NoSuchAlgorithmException,KeyStoreException,CertificateException,IOException
	{
		final javax.net.ssl.TrustManager[] trustAll = new javax.net.ssl.TrustManager[]{new javax.net.ssl.X509TrustManager(){
			
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		
			public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws java.security.cert.CertificateException {
			}
		
		
			public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws java.security.cert.CertificateException {
			}
		
		}};
	
        
		final javax.net.ssl.SSLContext sslcontext = javax.net.ssl.SSLContext.getInstance("TLS");
		sslcontext.init(null, trustAll, new java.security.SecureRandom());
	
		final javax.net.ssl.SSLSocketFactory scoFactory = sslcontext.getSocketFactory();
	
		return scoFactory;
	}
	
	
	public int postData(String finalURL, String data,String txnId){

		String line = null;	
		StringBuffer response = new StringBuffer();
		int Status=0;
		HttpURLConnection con = null;
		OutputStreamWriter wr = null;
		BufferedReader br = null;
		logger.info("Inside if::::::"+txnId+":::: post data "+data);
		try {
			logger.info("DataPost.java::::::Posting Url"+finalURL);
			//------------------------------------
			URL url = new URL(finalURL);
			getSocketFactory();
			if(finalURL.startsWith("https"))
			{
				con = (HttpsURLConnection)url.openConnection();
				
				  ((HttpsURLConnection)con).setSSLSocketFactory(getSocketFactory());
					
					  ((HttpsURLConnection)con).setHostnameVerifier(new
					  javax.net.ssl.HostnameVerifier() {
					  
					  @Override public boolean verify(String hostname, javax.net.ssl.SSLSession
					  sslSession) { return true; } });
					 
				 
			}
			else
				con = (HttpURLConnection) url.openConnection();
			
			con.setConnectTimeout(10000); 
			con.setReadTimeout(1000*125); // read time out 25/07/2018 updated to 125 second from 300 second
			
			con.setRequestMethod("POST");
			con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			con.setDoOutput(true);
			con.connect();		
			
			wr = new OutputStreamWriter(con.getOutputStream());
			
		}
		catch(Exception e)
		{
			logger.info("[DataPost]Error while creating connection to {} for Posting data for txn id :{} with reason :{}",finalURL, txnId,  e.getMessage(),e);
			
			try{ 
				if(con != null) con.disconnect(); 
			}
			catch(Exception ez) { 
				 
			}
			return 0;
		}

		try {
			
			wr.write(data);
			wr.flush();
		
			logger.info("Request data posted to Url : {} for txn id : {} ",finalURL,txnId);
			try {
				if(wr != null) wr.close();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
			logger.info("Response Code from Url :{} for txn id :{} ConnectionResponseCode :{}", finalURL,txnId,con.getResponseCode());//+ " ConnectionResponseMessgae:" + con.getResponseMessage()))
			
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				logger.info("Inside if::::::Response code::200");
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				Status=1;
				while((line = br.readLine()) != null) {
					response.append(line);
				}
			}
			else{
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));

				while((line = br.readLine()) != null) {
					response.append(line);
				}
			}

			logger.info("Rsponse from Url :{} for txn id : {} is :{}",finalURL,txnId ,response.toString());
		}
		catch(Exception e)
		{
			logger.error("Error while Posting data to url :{} with txnId:{} reason :{} ",finalURL,txnId,e.getMessage(),e);
			Status=0;
		}
		finally {
			try {
				if(br != null) br.close();
				if(con != null) con.disconnect();
			}catch(Exception ee) {}
		}
		
		return Status;		
	}
	
	public static void main(String[] args) {
		
		String url = "https://testpaycallback.rummysite.in/INR/INR_EpaisaaPay/Collect_CallBack";
		String data = "TSET";
		//String stru=S2SCall.httpServerCall(url, data);
		
	
		new DataPost().postData(url,data, System.currentTimeMillis()+"");
	}
}