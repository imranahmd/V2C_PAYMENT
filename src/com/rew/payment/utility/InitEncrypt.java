package com.rew.payment.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;

/**
 * Servlet implementation class InitEncrypt
 */
@WebServlet("/InitEncrypt")
public class InitEncrypt extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(InitEncrypt.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitEncrypt() {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.info("calling post method");
		PrintWriter out=response.getWriter();

		//System.out.println("inside  >>>>>>>> encryptdata" );
		JSONObject fields=new JSONObject();
		String line=null;
		try {
			BufferedReader reader = request.getReader();
			StringBuilder sbuffer=new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sbuffer.append(line);
			}

			fields = new JSONObject(sbuffer.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String merchantId=fields.getString("merchantId");
		String plainData=fields.getString("encData");
		String type=fields.getString("type");

		logger.info("merchantId====>"+merchantId);
		logger.info("plainData====>"+plainData);
		logger.info("type="+type);
		String confMerchantId = PGUtils.getPropertyValue("pspclId");
		DataManager dm=new DataManager();
		MerchantDTO merchantDTO = dm.getMerchant(merchantId);
		String encData=null;
		String decData=null;
		JSONObject js=new JSONObject();
		if(type!=null && type.equalsIgnoreCase("encrypt"))
		{

			if(merchantDTO.getTransactionKey().length()==32)
			{
				logger.info("AES 256 encryption");
				encData = PSPCLEncryptor.encrypt(merchantDTO.getTransactionKey(), merchantDTO.getTransactionKey().substring(0,16), plainData);
			}
			else
			{

				if (confMerchantId.equals(merchantId)) {
					logger.info("PSPCL ENC");
					encData= PSPCLEncryptor.encrypt(merchantDTO.getTransactionKey(),merchantDTO.getTransactionKey(),plainData);
				} else {
					logger.info("GENERAL ENC");
					encData=PGUtils.getEncData(plainData,merchantDTO.getTransactionKey());

				}
			}
			logger.info("encryptData.jsp  >>>>>>>> encryptdata " +encData);


			js.put("merchantId", merchantId);
			js.put("encData", encData);
			js.put("type", "ENCRYPT");
		}
		else
		{
			try
			{
				if(merchantDTO.getTransactionKey().length()==32)
				{
					logger.info("AES 256 DEcryption ");
					decData = PSPCLEncryptor.decrypt(merchantDTO.getTransactionKey(), merchantDTO.getTransactionKey().substring(0,16), plainData);
					
				}
				else
				{

					if (confMerchantId.equals(merchantId)) {
						logger.info("PSPCL dec ");
						decData= PSPCLEncryptor.decrypt(merchantDTO.getTransactionKey(),merchantDTO.getTransactionKey(),plainData);
					} else {
						logger.info("GENERAL dec");
						decData=PGUtils.getDecData(plainData,merchantDTO.getTransactionKey());

					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			logger.info("encryptData.jsp  >>>>>>>> decryptdata " +decData);


			js.put("merchantId", merchantId);
			js.put("encData", decData);
			js.put("type", "DECRYPT");
		}
		out.write(js.toString());

	}
	/*
	public static void main(String[] args) {
		String encData = PSPCLEncryptor.encrypt("ZEGs0367ZEGs0367ZEGs0367ZEGs0367", "ZEGs0367ZEGs0367ZEGs0367ZEGs0367".substring(0,16), "kiran");
		System.out.println("encData="+encData);
		String decData = PSPCLEncryptor.decrypt("ZEGs0367ZEGs0367ZEGs0367ZEGs0367", "ZEGs0367ZEGs0367ZEGs0367ZEGs0367".substring(0,16), "ohhVN/SAihoSXb9LjD1mKCvYqBrr274nBTljFTx9K30=");
		System.out.println(decData);
	}
	*/

}
