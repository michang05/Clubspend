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

import com.interactiveplus.model.SendMoneyResponse;
import com.interactiveplus.parserbase.BaseXmlParser;

/**
 * @author Michael Angelo
 * 
 */
public class SendMoneyWSParser extends BaseXmlParser {

	private SendMoneyResponse sendMoneyResponse;
	private Vector<SendMoneyResponse> responseVector;

	/**
	 * 
	 */
	public SendMoneyWSParser() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.interactiveplus.parserbase.ConnectionXmlParser#parse(java.io.InputStream
	 * )
	 */
	@Override
	public Vector<SendMoneyResponse> parse(InputStream inputStream) {
		sendMoneyResponse = new SendMoneyResponse();
		responseVector = new Vector<SendMoneyResponse>();

		RootElement response = new RootElement(RESPONSE_SEND_MONEY);

		Element success = response.getChild(SUCCESS_SEND_MONEY);
		success.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String body) {	
				sendMoneyResponse.setSuccess(body);
			}
		});

		
			Element transactionId = response.getChild(TRANSACTION_ID);
			transactionId
					.setEndTextElementListener(new EndTextElementListener() {

						@Override
						public void end(String body) {
							sendMoneyResponse.setTransactionId(body);

						}
					});

			Element paymentId = response.getChild(PAYMENT_ID);
			paymentId.setEndTextElementListener(new EndTextElementListener() {

				@Override
				public void end(String body) {
					sendMoneyResponse.setPaymentId(body);

				}
			});
			Element controlCode = response.getChild(CONTROL_CODE);
			controlCode.setEndTextElementListener(new EndTextElementListener() {

				@Override
				public void end(String body) {
					sendMoneyResponse.setControlCode(body);

				}
			});
			Element balance = response.getChild(BALANCE_SEND_MONEY);
			balance.setEndTextElementListener(new EndTextElementListener() {

				@Override
				public void end(String body) {
					sendMoneyResponse.setBalance(body);

				}
			});
		
			Element error = response.getChild(ERROR);
			error.setEndTextElementListener(new EndTextElementListener() {

				@Override
				public void end(String body) {
					sendMoneyResponse.setError(body);

				}
			});
		
		response.setEndElementListener(new EndElementListener() {

			@Override
			public void end() {
				responseVector.addElement(sendMoneyResponse);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interactiveplus.parserbase.ConnectionXmlParser#parse()
	 */
	@Override
	public List<?> parse() {
		// TODO Auto-generated method stub
		return null;
	}

	public SendMoneyResponse getSendMoneyResponse() {
		return sendMoneyResponse;
	}

	public void setSendMoneyResponse(SendMoneyResponse sendMoneyResponse) {
		this.sendMoneyResponse = sendMoneyResponse;
	}

	public Vector<SendMoneyResponse> getResponseVector() {
		return responseVector;
	}

	public void setResponseVector(Vector<SendMoneyResponse> responseVector) {
		this.responseVector = responseVector;
	}

}
