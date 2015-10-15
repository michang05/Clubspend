package com.interactiveplus.parserbase;

import java.io.InputStream;

public abstract class BaseXmlParser implements ConnectionXmlParser {

	// names of the Login XML tags
	protected static final String NINEDOT = "NineDot";
	
	protected static final String ID = "Id";
	protected static final String NAME = "Name";
	protected static final String NOTICE = "Notice";
	protected static final String SUCCESS = "Success";
	protected static final String BALANCE_LOGIN="Balance";
	
	//Payment History
	protected static final String PENDING_PAYMENTS="PendingPayments";
	protected static final String PENDING_PAYMENT="PendingPayment";
	protected static final String POSTED_PAYMENTS="PostedPayments";
	protected static final String POSTED_PAYMENT="PostedPayment";
	
	protected static final String PAYMENT_HISTORY_ID="Id";
	protected static final String POSTED_DATE="PostedDate";
	protected static final String TRANSACTION="Transaction";
	protected static final String DEBIT="Debit";
	protected static final String CREDIT="Credit";
	protected static final String STATUS="Status";
	protected static final String BALANCE="Balance";
	protected static final String RUNNING_BALANCE="RunningBalance";

	//Send Money
	protected static final String RESPONSE_SEND_MONEY ="response";
	protected static final String TRANSACTION_ID ="transaction_id";
	protected static final String PAYMENT_ID ="payment_id";
	protected static final String CONTROL_CODE ="control_code";
	protected static final String SUCCESS_SEND_MONEY ="success";
	protected static final String BALANCE_SEND_MONEY="balance";
	protected static final String ERROR="error";
	
	//Receive Money
	protected static final String RESPONSE_RECEIVE_MONEY ="response";
	protected static final String SUCCESS_RECEIVE_MONEY ="success";
	protected static final String ERROR_RECEIVE_MONEY ="error";
	protected static final String AMOUNT_RECEIVED ="amount_received";
	protected static final String DATE_RECEIVED ="date_received";
	protected static final String BALANCE_RECEIVE_MONEY="balance";
	protected static final String PAYMENT_ID_RECEIVE_MONEY ="payment_id";
	protected static final String COMMISSION_FEE_ID ="commission_fee_id";
	
	
	private InputStream inputStream;

	protected BaseXmlParser(InputStream inputStream) {
		
		this.inputStream = inputStream;		
		
	}
	public InputStream getInputStream() {		
			return inputStream;		
	}
	
	protected BaseXmlParser() {
		
	}



}
