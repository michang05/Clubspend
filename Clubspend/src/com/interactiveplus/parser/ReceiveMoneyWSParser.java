/**
 * 
 */
package com.interactiveplus.parser;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.interactiveplus.model.ReceiveMoneyResponse;
import com.interactiveplus.parserbase.BaseXmlParser;

/**
 * @author Michael Angelo
 *
 */
public class ReceiveMoneyWSParser extends BaseXmlParser{

	private ReceiveMoneyResponse receiveMoneyResponse;
	private Vector<ReceiveMoneyResponse> responseVector;
	/**
	 * 
	 */
	public ReceiveMoneyWSParser() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see com.interactiveplus.parserbase.ConnectionXmlParser#parse(java.io.InputStream)
	 */
	@Override
	public Vector<ReceiveMoneyResponse> parse(InputStream inputStream) {
		
		receiveMoneyResponse = new ReceiveMoneyResponse();
		responseVector = new Vector<ReceiveMoneyResponse>();
		
		RootElement response = new RootElement(RESPONSE_RECEIVE_MONEY);
		
		Element success = response.getChild(SUCCESS_RECEIVE_MONEY);
		success.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String body) {	
				receiveMoneyResponse.setSuccess(body);
			}
		});
		Element amountReceived = response.getChild(AMOUNT_RECEIVED);
		amountReceived.setEndTextElementListener(new EndTextElementListener() {
			
			@Override
			public void end(String body) {
			receiveMoneyResponse.setAmountReceived(body);	
			}
		});
		Element dateReceived = response.getChild(DATE_RECEIVED);
		dateReceived.setEndTextElementListener(new EndTextElementListener() {
			
			@Override
			public void end(String body) {
				receiveMoneyResponse.setDateReceived(body);
			}
		});
		Element balance = response.getChild(BALANCE_RECEIVE_MONEY);
		balance.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String body) {
				receiveMoneyResponse.setBalance(body);

			}
		});
		Element paymentId = response.getChild(PAYMENT_ID_RECEIVE_MONEY);
		paymentId.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String body) {
				receiveMoneyResponse.setPaymentId(body);

			}
		});
		Element commissionFeeId = response.getChild(COMMISSION_FEE_ID);
		commissionFeeId.setEndTextElementListener(new EndTextElementListener() {
			
			@Override
			public void end(String body) {
			receiveMoneyResponse.setCommissionFeeId(body);	
			}
		});
		Element error = response.getChild(ERROR_RECEIVE_MONEY);
		error.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String body) {
				receiveMoneyResponse.setError(body);

			}
		});
		response.setEndElementListener(new EndElementListener() {

			@Override
			public void end() {
				responseVector.addElement(receiveMoneyResponse);
			}
		});
		try {
			Xml.parse(inputStream, Xml.Encoding.UTF_8, response.getContentHandler());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return responseVector;
	}

	/* (non-Javadoc)
	 * @see com.interactiveplus.parserbase.ConnectionXmlParser#parse()
	 */
	@Override
	public List<?> parse() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReceiveMoneyResponse getReceiveMoneyResponse() {
		return receiveMoneyResponse;
	}

	public void setReceiveMoneyResponse(ReceiveMoneyResponse receiveMoneyResponse) {
		this.receiveMoneyResponse = receiveMoneyResponse;
	}

	public Vector<ReceiveMoneyResponse> getResponseVector() {
		return responseVector;
	}

	public void setResponseVector(Vector<ReceiveMoneyResponse> responseVector) {
		this.responseVector = responseVector;
	}

	
}
