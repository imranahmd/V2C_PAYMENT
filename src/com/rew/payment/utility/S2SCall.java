package com.rew.payment.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S2SCall {
	private static Logger logger = LoggerFactory.getLogger(S2SCall.class);

	static {
		disableSslVerification();
	}

	public S2SCall() {
	}

	public static String secureServerCall(String sURL, String data) {
		String line = null;
		BufferedReader br = null;
		StringBuffer respString = null;

		logger.info("S2SCall.java ::: secureServerCall() :: Posting URL : " + sURL);

		try {
			URL obj = new URL(sURL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			con.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
			con.addRequestProperty("Content-Length", data.getBytes().length + "");
			con.setDoOutput(true);
			con.connect();

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

			wr.write(data);
			wr.flush();

			respString = new StringBuffer();

			if (con.getResponseCode() == 200) {
				logger.info("S2SCall.java ::: secureServerCall() :: HTTP OK");
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				while ((line = br.readLine()) != null) {
					logger.info("S2SCall.java ::: secureServerCall() :: Response : " + line);
					respString.append(line);
				}

				br.close();

				return respString.toString().trim();
			}
		} catch (Exception e) {
			logger.error("S2SCall.java ::: secureServerCall() :: Error Occurred while Processing Request : ", e);
		}

		return null;
	}

	public static String httpServerCall(String sURL, String data) {
		String line = null;
		BufferedReader br = null;
		StringBuffer respString = null;

		logger.info("S2SCall.java ::: httpServerCall() :: Posting URL : " + sURL);

		try {
			URL obj = new URL(sURL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("POST");

			con.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
			con.addRequestProperty("Content-Length", data.getBytes().length + "");
			con.setDoOutput(true);
			con.connect();

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

			wr.write(data);
			wr.flush();

			respString = new StringBuffer();

			System.out.print("Sysrem::::::::::::::::::: " + con.getResponseCode());

			if (con.getResponseCode() == 200) {
				logger.info("S2SCall.java ::: httpServerCall() :: HTTP OK");
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				while ((line = br.readLine()) != null) {
					logger.info("S2SCall.java ::: httpServerCall() :: Response : " + line);
					respString.append(line);
				}

				br.close();

				return respString.toString();
			}
		} catch (Exception e) {
			logger.error("S2SCall.java ::: secureServerCall() :: Error Occurred while Processing Request : ", e);
		}

		return null;
	}

	private static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (Exception e) {

			logger.error(
					"S2SCall.java ::: disableSslVerification() :: Error Occurred while disabling SSL Verification : ",
					e);
		}
	}

	public static JSONObject servertoServerPayFi(Map<String, String> params, String hash, String api_url) {
		logger.info("Starting servertoServerPayFi method");

		try {
			logger.info("Creating URL object from api_url: {}", api_url);

			URL url = new URL(api_url);
			logger.info("Opening connection to URL");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			logger.info("Setting request method to POST");
			conn.setRequestMethod("POST");
			logger.info("Setting request property Content-Type to application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			logger.info("Setting DoOutput to true");
			conn.setDoOutput(true);
			logger.info("Building post data");
			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String, String> param : params.entrySet()) {
				if (postData.length() != 0)
					postData.append('&');
				postData.append(param.getKey()).append('=').append(param.getValue());
			}
			postData.append("&hash=").append(hash);
			logger.info("Post data built: {}", postData.toString());

			logger.info("Writing post data to output stream");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = postData.toString().getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}
			logger.info("Getting response code from connection");
			int responseCode = conn.getResponseCode();
			logger.info("Response Code: {}", responseCode);

			logger.info("Reading response from input stream");
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			logger.info("Closing input stream");
			in.close();
			logger.info("Disconnecting connection");
			conn.disconnect();

			logger.info("Parsing JSON response");
			JSONObject jsonResponse = new JSONObject(content.toString());
			logger.info("JSON response"+ jsonResponse);
			return jsonResponse;
		} catch (Exception e) {
			logger.error("Exception occurred: ", e);
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * public static JSONObject servertoServerPayFi(Map<String, String> params,
	 * String hash, String api_url) {
	 * logger.info("Starting servertoServerPayFi method");
	 * 
	 * try { logger.info("Creating URL object from api_url: {}", api_url); URL url =
	 * new URL(api_url); HttpURLConnection conn = (HttpURLConnection)
	 * url.openConnection(); logger.info("Opening connection to URL");
	 * 
	 * conn.setRequestMethod("POST"); logger.info("Setting request method to POST");
	 * 
	 * conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	 * logger.
	 * info("Setting request property Content-Type to application/x-www-form-urlencoded"
	 * );
	 * 
	 * conn.setDoOutput(true); logger.info("Setting DoOutput to true");
	 * 
	 * logger.info("Building post data"); StringBuilder postData = new
	 * StringBuilder(); for (Map.Entry<String, String> param : params.entrySet()) {
	 * if (postData.length() != 0) postData.append('&');
	 * postData.append(param.getKey()).append('=').append(param.getValue()); }
	 * postData.append("&hash=").append(hash); logger.info("Post data built: {}",
	 * postData.toString());
	 * 
	 * logger.info("Writing post data to output stream"); try (OutputStream os =
	 * conn.getOutputStream()) { byte[] input =
	 * postData.toString().getBytes(StandardCharsets.UTF_8); os.write(input, 0,
	 * input.length); } catch (Exception e) {
	 * logger.error("Error writing to output stream: ", e); return null; }
	 * 
	 * logger.info("Getting response code from connection"); int responseCode =
	 * conn.getResponseCode(); logger.info("Response Code: {}", responseCode);
	 * 
	 * logger.info("Reading response from input stream"); StringBuilder content =
	 * new StringBuilder(); try (BufferedReader in = new BufferedReader(new
	 * InputStreamReader(conn.getInputStream()))) { String inputLine; while
	 * ((inputLine = in.readLine()) != null) { content.append(inputLine); } }
	 * 
	 * logger.info("Disconnecting connection"); conn.disconnect();
	 * 
	 * logger.info("Parsing JSON response"); JSONObject jsonResponse = new
	 * JSONObject(content.toString()); return jsonResponse.getJSONObject("data"); }
	 * catch (Exception e) { logger.error("Exception occurred: ", e); return null; }
	 * }
	 */

	/*
	 * public static JSONObject servertoServerPayFi(Map<String, String> params,
	 * String hash, String apiUrl) {
	 * logger.info("Starting servertoServerPayFi method");
	 * 
	 * try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
	 * logger.info("Creating HttpPost object for URL: {}", apiUrl); HttpPost
	 * httpPost = new HttpPost(apiUrl);
	 * 
	 * logger.info("Setting headers and building post data");
	 * httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
	 * 
	 * StringBuilder postData = new StringBuilder(); for (Map.Entry<String, String>
	 * param : params.entrySet()) { if (postData.length() != 0)
	 * postData.append('&');
	 * postData.append(param.getKey()).append('=').append(param.getValue()); }
	 * postData.append("&hash=").append(hash); logger.info("Post data built: {}",
	 * postData.toString());
	 * 
	 * httpPost.setEntity(new StringEntity(postData.toString(),
	 * ContentType.APPLICATION_FORM_URLENCODED));
	 * 
	 * logger.info("Executing request"); try (CloseableHttpResponse response =
	 * httpClient.execute(httpPost)) { int statusCode = response.getCode();
	 * logger.info("Response Code: {}", statusCode);
	 * 
	 * String responseBody = EntityUtils.toString(response.getEntity());
	 * logger.info("Response Body: {}", responseBody);
	 * 
	 * logger.info("Parsing JSON response"); JSONObject jsonResponse = new
	 * JSONObject(responseBody); return jsonResponse.getJSONObject("data"); } }
	 * catch (Exception e) { logger.error("Exception occurred: ", e); return null; }
	 * }
	 */
}
