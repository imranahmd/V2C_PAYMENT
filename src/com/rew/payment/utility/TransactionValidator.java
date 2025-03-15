package com.rew.payment.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





public class TransactionValidator
{
    private static Logger logger = LoggerFactory.getLogger(TransactionValidator.class);

	public TransactionValidator() {}

	public String getValidatetrans(String URL) {
		String respStatus = null;
		try
		{
			String data = "";
			String line = null;
			URL url = new URL(URL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();

			con.setRequestMethod("GET");



			con.setDoOutput(true);
			con.connect();

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(data);
			wr.flush();
			StringBuffer resp = new StringBuffer();
			if (con.getResponseCode() == 200) {
				logger.info("HTTP OK....");
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream()));

				while ((line = br.readLine()) != null) {
					resp.append(line);
				}
				br.close();
			}
			else
			{
				logger.info("error code="+con.getResponseCode()+" error stream="+con.getErrorStream());
			}

			con.disconnect();
			resp.toString();
			respStatus = resp.toString();
		} catch (Exception e) {
			logger.error("TransactionValidator.java ::: ", e);
		}

		return respStatus;
	}
}
