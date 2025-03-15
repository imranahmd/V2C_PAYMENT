package com.rew.payment.rms;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.router.PGTransactionV2;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.RiskGlobalDTO;

@WebServlet("/CheckIp")
public class RiskConfiguration extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(PGTransactionV2.class);
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ValidateGlobalRMS(request, response);

	}

	public int ValidateGlobalRMS(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataManager dm = new DataManager();
		int i = 0;
		String resp_date_Time = new Timestamp(new Date().getTime()) + "";

		String rid = null;
		String risklogid = null;
		String TransTime = null;
		String RiskStage = null;
		String MID = null;
		String RiskCode = null;
		String RiskFlag = null;
		String reason = null;
		String addedby = null;
		String AddedOn = null;
		try {

			String ip = (String) request.getAttribute("CustIp");
			String mid = (String) request.getAttribute("Mid");
			RiskGlobalDTO RiskGlobalDTO = dm.RiskGolbalIp(ip, mid);
			logger.info("Value of Rc ::::::::::: " + RiskGlobalDTO.getCustIp());

			logger.info("Value of Rc ::::::" + RiskGlobalDTO.getCustIp_RiskCode());
			int IsMatched = CheckValueMatched(ip, RiskGlobalDTO.getCustIp());
			if (IsMatched == 0) {
				Neturino_Api Neturino_Api = new Neturino_Api();

				String Url = "https://neutrinoapi.net/ip-info?ip=" + ip + "&reverse-lookup=false";
				String Response = Neturino_Api.httpServerCall(Url);
				JSONObject js = new JSONObject(Response);
				String country_code = js.getString("country-code3");
				logger.info("Value of country:::::::::::Coun " + RiskGlobalDTO.getCountry_code());
				int Country_CodeCheck = CheckValueMatched(country_code, RiskGlobalDTO.getCountry_Value());

				if (Country_CodeCheck == 0) {
					logger.info("Value of Rc :::::::::::Country code not blocked");
					//if()
					
					
					i = 1; // global country
				} else {
					logger.info(
							"RiskConfigure.java :::Country code::  not allowed " + RiskGlobalDTO.getCountry_Value());
					TransTime = resp_date_Time;
					RiskStage = "Global Level";
					MID = mid;
					RiskCode = RiskGlobalDTO.getCountry_coderiskcode();
					RiskFlag = "Country Blocked";
					addedby = "System";
					AddedOn = resp_date_Time;
					logger.info("RiskConfiguration.java ::::::::>>>>>>>>>>>>Country Code Blocked");
					int ReturnFlag = dm.insertAlertRiskLogs(TransTime, RiskStage, MID, RiskCode, RiskFlag, addedby,
							AddedOn);
					logger.info("RiskConfigure.java :::IP ::  not allowed" + ReturnFlag);
					i = 2; // country blocked
				}
			} else {
				logger.info("RiskConfiguration.java ::::::::>>>>>>>>>>>>IP Address Blocked");
				TransTime = resp_date_Time;
				RiskStage = "Globa Level";
				MID = mid;
				RiskCode = RiskGlobalDTO.getCustIp_RiskCode();
				RiskFlag = "IP Blocked";
				addedby = "System";
				AddedOn = resp_date_Time;

				int ReturnFlag = dm.insertAlertRiskLogs(TransTime, RiskStage, MID, RiskCode, RiskFlag, addedby,
						AddedOn);
				logger.info("RiskConfiguration.java ::::::::>>>>>>>>>>>>IP Address Blocked" + ReturnFlag);

				i = 3; // Ip Blocked
			}

		} catch (Exception e) {
			e.printStackTrace();
			i = 0;

		}
		logger.info("RiskConfigure.java reutn value::::: " + i);
		return 1;

	}

	public static int CheckValueMatched(String custIP, String BlockIp) {
		if (custIP != null || BlockIp != null) {
			List<String> list = Arrays.asList(BlockIp.split(","));
			if (list.contains(custIP)) {
				logger.info("Ip found int hr black list:: ");
				return 1;

			} else {
				logger.info("Ip not found in this list");
				return 0;

			}

		} else {
			logger.info("Ip value::::::::::::: ");

		}
		return 0;
	}

	public static void main(String[] args) throws ParseException {
		int i = CheckValueMatched("192.168.0.1", "192.168.80.1");
		System.out.print("::::::::::::::: " + i);

		String Url = "https://neutrinoapi.net/ip-info?ip=103.66.234.21&reverse-lookup=false";
		// String Response= Neturino_Api.httpServerCall(Url);
		// JSONObject js = new JSONObject(Response);
		// System.out.print("::::::"+js.getString("currency-code")+":::::::::
		// "+Response);

	}
}
