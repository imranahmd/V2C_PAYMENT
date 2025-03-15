package com.rew.payment.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/downloadFile")
public class DownloadFile extends HttpServlet{

	static Logger log = LoggerFactory.getLogger(AjaxAction.class);
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		try {
			String merchantId = request.getParameter("merchant");
			boolean flag = validateMerchantId(merchantId);
			String fileName = null;
			if(flag) {
				fileName = "TermsAndConditions-"+merchantId+".pdf";
			}
			String filePath = "/home/Documents";
			log.info("filename=" + fileName);

			File file1 = new File(filePath + File.separator + fileName);
			FileInputStream fileInputStream = new FileInputStream(file1);

			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "");
			ServletOutputStream out = response.getOutputStream();
			byte[] bytes = new byte[(int) file1.length()];
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file1);
				fis.read(bytes);
			} finally {
				if (fis != null) {
					fis.close();
				}
			}
			out.write(bytes);
			fileInputStream.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean validateMerchantId(String merchantId) {
		return merchantId.matches("[A-Za-z0-9]+");
	}
}
