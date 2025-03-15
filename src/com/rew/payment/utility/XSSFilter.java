package com.rew.payment.utility;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;

public class XSSFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(XSSFilter.class);

	public XSSFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httprequest = (HttpServletRequest) request;
		
		logger.info("XSSFilter.java ::: Filter Validation starts on : " + httprequest.getHeader("Referer"));
		String paramName = null;
		String paramValue = null;
		String retValue = null;
		
//		if ("OPTIONS".equalsIgnoreCase(httprequest.getMethod())) {
//		    chain.doFilter(request, response); // Allow preflight requests
//		    return;
//		}
		
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			paramName = (String) params.nextElement();
			paramValue = request.getParameter(paramName);
			/*
			 * if(paramName.contains("udf6") && paramValue!="NA" && paramValue!="") {
			 * logger.info("checking udf6::::::::::::::::"+paramValue+"***name"+paramName);
			 * try { new JSONObject(paramValue); } catch (Exception ex) {
			 * 
			 * logger.info("fail 1 json string check::::::::::::::::"); try { new
			 * JSONArray(paramValue); } catch (Exception ex1) {
			 * logger.info("fail 2 json array check break here now::::::::::::::::"); break;
			 * } } }
			 */
			if(paramName.contains("udf6")) {
				continue;
			}
		
			retValue = stripXSS(paramValue);
			if ((retValue == null) || (!retValue.equals(paramValue))) {
				logger.info("XSSFilter.java ::: Parameter named '" + paramName + "' contains malicious value--> "
						+ paramValue + " And Sanitized value is--> " + retValue);
				break;
			}
		}
		
	
		if ((retValue == null) || (!retValue.equals(paramValue))) {
			request.getRequestDispatcher("xssErrorPage.jsp").include(request, response);
		} else {
			chain.doFilter(request, response);
		}

	
	}

	public static Pattern[] patterns = {
			Pattern.compile("[^-a-zA-Z0-9 /?&{}+=_:,|.'@~]*[`!%;<>#$%^*>()\">]*"),
			Pattern.compile("<script>(.*?)</script>", 2), 
			Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", 42),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 42), 
			Pattern.compile("</script>", 2),
			Pattern.compile("<script(.*?)>", 42), 
			Pattern.compile("eval\\((.*?)\\)", 42),
			Pattern.compile("expression\\((.*?)\\)", 42), 
			Pattern.compile("javascript:", 2),
			Pattern.compile("vbscript:", 2), 
			Pattern.compile("onload(.*?)=", 42), 
			Pattern.compile("onerror(.*?)=", 42),
			Pattern.compile("window.", 2), 
			Pattern.compile("document.", 2), 
			Pattern.compile("location.", 2), 
			Pattern.compile("alert\\((.*?)\\)", 42) };

	private String stripXSS(String value) {
		String checkedValue = null;
		if (value != null) {
			for (Pattern scriptPattern : patterns) {
				checkedValue = scriptPattern.matcher(value).replaceAll("");
				if (!value.equals(checkedValue)) {
					logger.info("XSSFilter.java ::: Break Script Pattern :: " + scriptPattern);
					break;
				}
			}
		}
		return checkedValue;
	}
	
	public String stripXSSJSP(String value) {
		String checkedValue = null;
		if (value != null) {
			for (Pattern scriptPattern : patterns) {
				checkedValue = scriptPattern.matcher(value).replaceAll("");
				if (!value.equals(checkedValue)) {
					logger.info("XSSFilter.java ::: Break Script Pattern :: " + scriptPattern);
					break;
				}
			}
		}
		return checkedValue;
	}
	
	public String CheckWhiteListedIP(HttpServletRequest request)
	{
	        String ipAddress = request.getHeader("X-Forwarded-For");
	        
	        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	            ipAddress = request.getHeader("Proxy-Client-IP");
	        }
	        
	        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	            ipAddress = request.getHeader("WL-Proxy-Client-IP");
	        }
	        
	        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
	        }
	        
	        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	            ipAddress = request.getHeader("HTTP_X_FORWARDED");
	        }
	        
	        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	            ipAddress = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
	        }
	        
	        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	            ipAddress = request.getHeader("HTTP_CLIENT_IP");
	        }
	        
	        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	            ipAddress = request.getHeader("REMOTE_ADDR");
	        }
	        
	        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	            ipAddress = request.getRemoteAddr();
	        }
	        
	        return ipAddress;
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
	
	public void init(FilterConfig fConfig) throws ServletException {
	}
}
