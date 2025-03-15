package com.rew.payment.router;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.utility.PGUtils;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;
import com.rew.pg.dto.TransactionMaster;

public class RequestHandler {
	private static Logger logger = LoggerFactory.getLogger(RequestHandler.class);
	private TransactionMaster TM;
	private MerchantDTO merchDto;
	private boolean isValid;
	private String errorCode;
	private String errorMessage;

	public RequestHandler(TransactionMaster TM) {
		this.TM = TM;
	}

	public MerchantDTO getMerchDto() {
		return merchDto;
	}

	public void setMerchDto(MerchantDTO merchDto) {
		this.merchDto = merchDto;
	}

	public TransactionMaster getTM() {
		return TM;
	}

	public void setTM(TransactionMaster tm) {
		TM = tm;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	DataManager dm = new DataManager();

	public RequestHandler validateRequest(RequestHandler handler) {

		handler.setValid(false);
		BigDecimal amount = null;
		BigDecimal minTokenSize = null;
		BigDecimal maxTokenSize = null;
		boolean validClient = validClient();
		logger.info("isValid VAS -- > 2......");
		try {
			amount = new BigDecimal(TM.getAmount());
			minTokenSize = new BigDecimal(PGUtils.getPropertyValue("minTokenSize"));

			maxTokenSize = merchDto.getMaxTokenSize();

			logger.info("Bigdecimal amount=" + amount + " " + minTokenSize + " " + maxTokenSize);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
logger.info("lllldd  "+TM.getTxnType());
		if (!(TM.getTxnType().equalsIgnoreCase("DIRECT") || TM.getTxnType().equalsIgnoreCase("REDIRECT") || TM.getTxnType().equalsIgnoreCase("seamless") || TM.getTxnType().equalsIgnoreCase("nonseamless"))) {
			handler.setErrorCode("10019");
			handler.setErrorMessage("Invalid Transaction Type");
		} else if (!(TM.getIsMultiSettlement().equalsIgnoreCase("0")
				|| TM.getIsMultiSettlement().equalsIgnoreCase("1"))) {
			handler.setErrorCode("10020");
			handler.setErrorMessage("MultiPart Settlement filed must be Numeric and of length 1.");
		} else if (TM.getChannelId().length() != 1 || !StringUtils.isNumeric(TM.getChannelId())) {
			handler.setErrorCode("10021");
			handler.setErrorMessage("Channel Id must be Numeric and of length 1.");
		} else if (!PGUtils.isValidForamt("DATE", TM.getDateTime())) {
			handler.setErrorCode("10024");
			handler.setErrorMessage("Invalid Date format");
		} else if (!isValidAmount(TM.getAmount())) {
			handler.setErrorCode("10025");
			handler.setErrorMessage("Invalid Amount");
			// ADDED BY SANIDHYA 2.7.22
			// condition for op -- bqr -- emi on the requirement
		} else if (!TM.getInstrumentId().equals("NA") && !TM.getInstrumentId().equals("NB")
				&& !TM.getInstrumentId().equals("DC") && !TM.getInstrumentId().equals("CC")
				&& !TM.getInstrumentId().equals("UPI") && !TM.getInstrumentId().equals("WALLET")
				&& !TM.getInstrumentId().equals("OP") && !TM.getInstrumentId().equals("BQR")
				&& !TM.getInstrumentId().equals("EMI")) {

			handler.setErrorCode("10026");
			handler.setErrorMessage("Only 'NA/NB/DC/CC/UPI/WALLET/BQR/OP/EMI' values allowed in Insrument Id Field.");
		} else if (!(TM.getUdf1().length() > 1 && TM.getUdf1().length() < 201)) {
			handler.setErrorCode("10027");
			handler.setErrorMessage("UDF1 Field length must be between length 2 to 200.");
		} else if (!(TM.getUdf2().length() > 1 && TM.getUdf2().length() < 201)) {
			handler.setErrorCode("10028");
			handler.setErrorMessage("UDF2 Field length must be between length 2 to 200.");
		}
		/*
		 * else if(!(TM.getBankId().length() > 1 && TM.getBankId().length() < 66) ||
		 * !StringUtils.isAlphanumeric(TM.getBankId().replaceAll("\\|",
		 * "").replaceAll(" ", ""))) { handler.setErrorCode("10029");
		 * handler.setErrorMessage("UDF3 Field length must be between length 2 to 65.");
		 * }
		 */
		else if (!(TM.getCardType().length() > 1 && TM.getCardType().length() < 21)
				|| !StringUtils.isAlpha(TM.getCardType())) {
			handler.setErrorCode("10030");
			handler.setErrorMessage("Only Alphabets allowed in Card Type Field between length 2 to 20.");
		} else if (!validClient) {
			handler.setErrorCode("10031");
			handler.setErrorMessage("Invalid Merchant Id/Transaction Key");
		} else if ((merchDto.getIntegrationType().equals("0") && !TM.getTxnType().equalsIgnoreCase("DIRECT") && !TM.getTxnType().equalsIgnoreCase("nonseamless"))
				|| (merchDto.getIntegrationType().equals("1") && !TM.getTxnType().equalsIgnoreCase("REDIRECT") && !TM.getTxnType().equalsIgnoreCase("seamless"))) {
			handler.setErrorCode("BL0001");
			handler.setErrorMessage("Invalid Merchant Integration Type.");
		}
		// else if(Double.valueOf(TM.getAmount()) <
		// Double.valueOf(PGUtils.getPropertyValue("minTokenSize")) ||
		// Double.valueOf(TM.getAmount()) > merchDto.getMaxTokenSize())
		else if ((amount.compareTo(minTokenSize) == -1) || (amount.compareTo(maxTokenSize) == 1)) {
			handler.setErrorCode("BL0002");
			handler.setErrorMessage("Transaction Amount is not as per the Slab Limit defined to the Merchant.");
			// handler.setErrorMessage("Min Transaction Amount Allowed is Rs.
			// "+PGUtils.getPropertyValue("minTokenSize")+" and Max Transaction Amount
			// Allowed is Rs. "+merchDto.getMaxTokenSize()+" for Merchant
			// '"+TM.getMerchantId()+"'.");
		} else if (merchDto.getStatus().equals("1")) {
			handler.setErrorCode("10032");
			handler.setErrorMessage(
					"Merchant is Inactive.Please contact to your Relationship Manager for further details.");
		} else if (!isValidURL()) {
			handler.setErrorCode("10033");
			handler.setErrorMessage("Invalid Referrer.Return URL is not registered.");
		} else if (!isValidProductId()) {
			handler.setErrorCode("10034");
			handler.setErrorMessage(
					"Invalid Product Id.Product Id field is Either Missing/Null/Blank or not in required format.");
		}

		/* Validation for Redirect Txn type Added by Suraj starts here */
		else if (TM.getTxnType().equalsIgnoreCase("REDIRECT")) {
			if (!(TM.getInstrumentId().equalsIgnoreCase("NB") || TM.getInstrumentId().equals("DC")
					|| TM.getInstrumentId().equals("CC") || TM.getInstrumentId().equals("UPI")
					|| TM.getInstrumentId().equals("WALLET"))) {
				handler.setErrorCode("10035");
				handler.setErrorMessage(
						"For REDIRECT transaction type Instrument Id value should be NB/CC/DC/UPI/WALLET");
			} else if (TM.getInstrumentId().equalsIgnoreCase("NB")
					&& (TM.getBankId().length() != 4 || !StringUtils.isNumeric(TM.getBankId()))) {
				handler.setErrorCode("10036");
				handler.setErrorMessage(
						"For Redirect transaction type Card Details field must contains 4 digit Bank Id if Instrument Id is 'NB'.");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& (TM.getBankId().indexOf("|") < 0 || TM.getBankId().split("\\|").length < 4)) {
				handler.setErrorCode("10037");
				handler.setErrorMessage(
						"Incomplete Card Details found in Card Details field for REDIRECT transaction type");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& TM.getCardType().equalsIgnoreCase("NA")) {
				handler.setErrorCode("10038");
				handler.setErrorMessage("Invalid Card Type used for REDIRECT transaction type.Card Type '"
						+ TM.getCardType() + "' not allowed.");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& (TM.getBankId().split("\\|")[0].toString() == null
							|| TM.getBankId().split("\\|")[0].toString().isEmpty())) {
				handler.setErrorCode("10039");
				handler.setErrorMessage(
						"Card Name Parameter in Card Details field cannot be Null/Blank for REDIRECT transaction type");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& (!String.valueOf(TM.getBankId().charAt(TM.getBankId().length() - 1)).equalsIgnoreCase("Y"))
					&& (TM.getBankId().split("\\|")[1].toString() == null
							|| TM.getBankId().split("\\|")[1].toString().isEmpty())) {
				handler.setErrorCode("10040");
				handler.setErrorMessage(
						"Card Number Parameter in Card Details field cannot be Null/Blank for REDIRECT transaction type");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& (TM.getBankId().split("\\|")[2].toString() == null
							|| TM.getBankId().split("\\|")[2].toString().isEmpty())) {
				handler.setErrorCode("10041");
				handler.setErrorMessage(
						"CVV Parameter in Card Details field cannot be Null/Blank for REDIRECT transaction type");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& (TM.getBankId().split("\\|")[3].toString() == null
							|| TM.getBankId().split("\\|")[3].toString().isEmpty())) {
				handler.setErrorCode("10042");
				handler.setErrorMessage(
						"Expiry Date Parameter in Card Details field cannot be Null/Blank for REDIRECT transaction type");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& !StringUtils.isAlpha(TM.getBankId().split("\\|")[0].replaceAll(" ", ""))) {
				handler.setErrorCode("10043");
				handler.setErrorMessage("Only Alphabets Allowed in Card Name Parameter of Card Details field.");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& (!String.valueOf(TM.getBankId().charAt(TM.getBankId().length() - 1)).equalsIgnoreCase("Y"))
					&& !StringUtils.isNumeric(TM.getBankId().split("\\|")[1])
					&& !(TM.getBankId().split("\\|")[1].length() >= 12
							&& TM.getBankId().split("\\|")[1].length() <= 19)) {
				handler.setErrorCode("10044");
				handler.setErrorMessage(
						"Only Numbers upto length 12-19 Allowed in Card Number Parameter of Card Details field.");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& !StringUtils.isNumeric(TM.getBankId().split("\\|")[2])
					&& !(TM.getBankId().split("\\|")[2].length() == 3
							|| TM.getBankId().split("\\|")[2].length() == 4)) {
				handler.setErrorCode("10045");
				handler.setErrorMessage(
						"Only Numbers upto length 3 or 4 Allowed in CVV Parameter of Card Details field.");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& !(isValidExpiryDate(TM.getBankId().split("\\|")[3].toString()))) {
				handler.setErrorCode("10046");
				handler.setErrorMessage(
						"Expiry date should be in MMyyyy format & should not be less than Current Month & Year.");
			} else if ((TM.getInstrumentId().equalsIgnoreCase("CC") || TM.getInstrumentId().equalsIgnoreCase("DC"))
					&& (String.valueOf(TM.getBankId().charAt(TM.getBankId().length() - 1)).equalsIgnoreCase("Y"))
					&& (TM.getBankId().split("\\|")[1].toString() == null
							|| TM.getBankId().split("\\|")[1].toString().isEmpty())) {
				handler.setErrorCode("10047");
				handler.setErrorMessage(
						"Token Reference Id Parameter in Card Details field cannot be Null/Blank for REDIRECT transaction type");
			}else if (TM.getInstrumentId().equalsIgnoreCase("UPI")) // 06-01-2023 For UPI validation in case of
				// seamless
			{
				logger.info("IsIntent:::::::::card::::: " + TM.getCardDetails());

				boolean isIntent = (TM.getCardDetails() != null) ? TM.getCardDetails().equalsIgnoreCase("I") : false;
				logger.info("IsIntent:::::::::::::: " + isIntent);
	// query ();
				DataManager db = new DataManager();
				String queryResult = db.IsEnableUpiorIntent(TM.getMerchantId(), isIntent);// "1";// or "0"; //call dbv
				logger.info("queryResult:::::::::::::: " + queryResult);

				if (queryResult != null && queryResult.equals("0")) {
					handler.setErrorCode("10048");
					handler.setErrorMessage("Payment instrument not allowed");
				} else {
					handler.setValid(true);

				}

			}
			/*
			 * else if((TM.getInstrumentId().equalsIgnoreCase("CC") ||
			 * TM.getInstrumentId().equalsIgnoreCase("DC")) &&
			 * !(TM.getBankId().split("\\|")[1].length() >= 12 &&
			 * TM.getBankId().split("\\|")[1].length() <= 19)) {
			 * handler.setErrorCode("10030");
			 * handler.setErrorMessage("Invalid Card Number Length = "+TM.getBankId().split(
			 * "\\|")[1].toString().length()); } else
			 * if((TM.getInstrumentId().equalsIgnoreCase("CC") ||
			 * TM.getInstrumentId().equalsIgnoreCase("DC")) &&
			 * !(TM.getBankId().split("\\|")[2].toString().length() == 3 ||
			 * TM.getBankId().split("\\|")[2].toString().length() == 4)) {
			 * handler.setErrorCode("10031");
			 * handler.setErrorMessage("Invalid CVV Number Length = "+TM.getBankId().split(
			 * "\\|")[2].toString().length()); }
			 */
			else {
				handler.setValid(true);
			}
			// else
			/*
			 * { String sRaw =
			 * TM.getMerchantId()+"|"+TM.getTxnId()+"|"+TM.getDateTime()+"|"+TM.getAmount()+
			 * "|"+TM.getApiKey()+"|"+
			 * TM.getCustMobile()+"|"+TM.getCustMail()+"|"+TM.getChannelId()+"|"+TM.
			 * getTxnType();
			 * 
			 * String sCheckSum = new
			 * AesBase64Wrapper().encryptAndEncode(sRaw,TM.getApiKey());
			 * 
			 * if(sCheckSum.equals(TM.getChecksum())) { handler.setValid(true); } else {
			 * logger.
			 * info("RequestHandler.java ::: REDIRECT :: System's Generated Checksum --> "
			 * +sCheckSum+" and Checksum in Request --> "+TM.getChecksum()
			 * +" doesn't match.System's Checksum Validation String --> {"+sRaw+"}");
			 * handler.setValid(false); handler.setErrorCode("10047");
			 * handler.setErrorMessage("Invalid Checksum"); } }
			 */
		} 
		/* Validation for Redirect Txn type Added by Suraj ends here */
		else {
			handler.setValid(true);
		}
		/*
		 * else { String sRaw =
		 * TM.getMerchantId()+"|"+TM.getTxnId()+"|"+TM.getDateTime()+"|"+TM.getAmount()+
		 * "|"+TM.getApiKey()+"|"+
		 * TM.getCustMobile()+"|"+TM.getCustMail()+"|"+TM.getChannelId()+"|"+TM.
		 * getTxnType();
		 * 
		 * String sCheckSum = new
		 * AesBase64Wrapper().encryptAndEncode(sRaw,TM.getApiKey());
		 * 
		 * if(sCheckSum.equals(TM.getChecksum())) { handler.setValid(true); } else {
		 * logger.
		 * info("RequestHandler.java ::: DIRECT :: System's Generated Checksum --> "
		 * +sCheckSum+" and Checksum in Request --> "+TM.getChecksum()
		 * +" doesn't match.System's Checksum Validation String --> {"+sRaw+"}");
		 * handler.setValid(false); handler.setErrorCode("10047");
		 * handler.setErrorMessage("Invalid Checksum"); } }
		 */

		return handler;

	}

	private boolean validClient() {
		merchDto = dm.getMerchant(TM.getMerchantId());
		if (null != merchDto) {
			setMerchDto(merchDto);
			if (merchDto.getTransactionKey().equals(TM.getApiKey()))
				return true;
		}
		return false;
	}

	private boolean isValidURL() {
		if (null != merchDto) {
			if (null != merchDto.getMerchReturnURL()) {
				StringTokenizer st = new StringTokenizer(merchDto.getMerchReturnURL(), ",");

				while (st.hasMoreTokens()) {
					String regURL = st.nextToken();

					if (TM.getReturn_url().contains(regURL)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isValidAmount(String txn_amount) {
		String amtArray[] = txn_amount.split("\\.");

		if (amtArray.length == 2 && amtArray[0] != null && amtArray[1] != null && !amtArray[0].isEmpty()
				&& !amtArray[1].isEmpty()) {
			if (amtArray[0].length() > 0 && amtArray[0].length() < 11 && amtArray[1].length() == 2) {
				if (StringUtils.isNumeric(amtArray[0]) && StringUtils.isNumeric(amtArray[1])) {
					// Double d = Double.valueOf(txn_amount);
					return true;
				} else {
					logger.info(
							"RequestHandler.java ::: isValidAmount() :: Amount received in Request is Not Numeric : "
									+ txn_amount);
				}
			} else {
				logger.info("RequestHandler.java ::: isValidAmount() :: Length Before Decimal allowed upto 8 : "
						+ amtArray[0].length() + " OR Length After Decimal allowed upto 2 : " + amtArray[1].length());
			}
		} else {
			logger.info("RequestHandler.java ::: isValidAmount() :: Amount Array Length is not 2 : " + amtArray.length
					+ " OR one or more required Fields are missing/Null/Blank.");
		}

		return false;
	}

	/*
	 * private boolean isValidDate(String date_time) { try { Date format = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date_time); return true; }
	 * catch (Exception e) { logger.
	 * error("RequestHandler.java ::: isValidDate() :: DateTime received in Request is : '"
	 * +date_time+"' And Exception Occurred : ",e); } return false; }
	 */

	private boolean isValidExpiryDate(String expDate) {
		if (expDate.toString().length() == 6 && expDate.matches("^\\d+$")) {
			try {
				int expMonth = Integer.parseInt(expDate.substring(0, 2));
				int expYear = Integer.parseInt(expDate.substring(2));

				Calendar calendar = Calendar.getInstance();
				int currentYear = calendar.get(Calendar.YEAR);
				int currentMonth = calendar.get(Calendar.MONTH) + 1;

				if ((expMonth < 0 && expMonth > 12) || expYear > 2060 || expYear < currentYear
						|| (expYear == currentYear && expMonth < currentMonth)) {
					logger.info("RequestHandler.java ::: isValidExpiryDate() :: Expiry Month = " + expMonth
							+ " , Expiry Year = " + expYear + " And Current Year : " + currentYear
							+ " , Current Month : " + currentMonth);
					return false;
				}
				return true;
			} catch (Exception e) {
				logger.error("RequestHandler.java ::: isValidExpiryDate() :: Exception Occurred : ", e);
			}
		} else {
			logger.info("RequestHandler.java ::: isValidExpiryDate() :: Expiry Date IsNumber --> "
					+ expDate.matches("^\\d+$") + " , Expiry Date Length --> " + expDate.toString().length());
		}
		return false;

	}

	// Method to check Product Id received in Request added by Suraj A 04-Aug-2018
	private boolean isValidProductId() {
		logger.info("(TM.getIsMultiSettlement().equalsIgnoreCase(1))"
				+ ((this.TM.getIsMultiSettlement().equalsIgnoreCase("0"))
						&& (this.TM.getProductId().equals("DEFAULT"))));
		logger.info("(TM.getIsMultiSettlement().equalsIgnoreCase(0))"
				+ this.TM.getIsMultiSettlement().equalsIgnoreCase("0"));

		if (this.TM.getIsMultiSettlement().equalsIgnoreCase("0")) {
			logger.info("blank and null check" + ((this.TM.getProductId() == "") || (this.TM.getProductId() == null)));
			logger.info("default  check" + this.TM.getProductId().equals("DEFAULT"));

			if ((this.TM.getProductId() == "") || (this.TM.getProductId() == null)) {
				logger.info("RequestHandler.java ::: isValidProductId() :: Product Id Array is not configured");
				return false;
			}
			if (this.TM.getProductId().equals("DEFAULT")) {
				return true;
			}

			ArrayList prodList = dm.getProductIdList(this.TM.getMerchantId());

			logger.info("added prodList" + prodList.toString());
			boolean contains = prodList.contains(this.TM.getProductId());
			logger.info("added product id " + contains);
			if (contains) {
				return true;
			}

			logger.info("RequestHandler.java ::: isValidProductId() :: Product Id Array is not configured");
			return false;
		}

		if (this.TM.getIsMultiSettlement().equalsIgnoreCase("1")) {
			if (this.TM.getProductId().indexOf("|") > 2) {
				String[] productIdArray = this.TM.getProductId().split("\\|");

				if ((productIdArray != null) && (productIdArray.length > 1)) {
					try {
						ArrayList prodList = dm.getProductIdList(this.TM.getMerchantId());
						logger.info("added prodList" + prodList.toString());
						if ((prodList != null) && (prodList.size() > 1)) {
							logger.info("added prodList" + this.TM.getProductId());
							boolean contains = prodList.contains(this.TM.getProductId());
							logger.info("added product id " + contains);
							if (contains) {
								return true;
							} else {
								logger.info(
										"RequestHandler.java ::: isValidProductId() :: Product Id Array is not configured");
								return false;
							}

						}

					} catch (Exception e) {
						logger.error("RequestHandler.java ::: isValidProductId() :: Exception Occurred : ", e);
					}

				}

				logger.info(
						"RequestHandler.java ::: isValidProductId() :: Product Id Array is NULL or length is less than 2.");
			} else {
				logger.info("RequestHandler.java ::: isValidProductId() :: Invalid Pipe Position '"
						+ this.TM.getProductId().indexOf("|") + "' in Product Id field.");
			}
		} else {
			logger.info(
					"RequestHandler.java ::: isValidProductId() :: Invalid Value Received for Is Multi Settlement : "
							+ this.TM.getIsMultiSettlement() + " OR Product Id : " + this.TM.getProductId());
		}

		return false;
	}
}
