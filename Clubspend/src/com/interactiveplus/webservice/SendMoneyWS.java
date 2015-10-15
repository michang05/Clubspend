/**
 * 
 */
package com.interactiveplus.webservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.interactiveplus.model.SendMoneyResponse;
import com.interactiveplus.parser.SendMoneyWSParser;

/**
 * @author Michael Angelo
 *
 */
public class SendMoneyWS {
	private static final String URL = "/android/send_money.php";

	private String userId;
	private String email;
	private String amount;
	private String date;
	private String message;
	private SendMoneyResponse sendMoneyResponse;
	
	private DefaultHttpClient client;
	private HttpHost host;
	private HttpPost postMethod;
	private HttpResponse response;
	private HttpEntity entityResponse;
	private List<NameValuePair> valuePairs;
	
	
	/**
	 * 
	 */
	public SendMoneyWS(String userId, String email,String amount,String date) {

		this.userId = userId;
		this.email = email;
		this.amount=amount;
		this.date=date;

		client = new DefaultHttpClient();

		host = new HttpHost("clubspend.com", -1, "http");

		postMethod = new HttpPost(URL);
		postMethod.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("iUserId", userId));
		valuePairs.add(new BasicNameValuePair("sEmail", email));
		valuePairs.add(new BasicNameValuePair("mAmount", amount));
		valuePairs.add(new BasicNameValuePair("dDate",date));

	}
	
	/**
	 * @param userId
	 * @param email
	 * @param amount
	 * @param date
	 * @param message
	 */
	public SendMoneyWS(String userId, String email, String amount,
			String date, String message) {

		this.userId = userId;
		this.email = email;
		this.amount=amount;
		this.date=date;
		this.message=message;

		client = new DefaultHttpClient();
	
		host = new HttpHost("clubspend.com", -1, "http");

		postMethod = new HttpPost(URL);
		postMethod.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("iUserId", userId));
		valuePairs.add(new BasicNameValuePair("sEmail", email));
		valuePairs.add(new BasicNameValuePair("mAmount", amount));
		valuePairs.add(new BasicNameValuePair("dDate",date));
		valuePairs.add(new BasicNameValuePair("sMessage",message));
	}

	public void send() {

		try {

		postMethod.setEntity(new UrlEncodedFormEntity(valuePairs));

			response = client.execute(host, postMethod);
	
			entityResponse = response.getEntity();

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_IMPLEMENTED) {
				System.err
						.println("The Post method is not implemented by this URI");
				// still consume the response body
				entityResponse.consumeContent();

			} else {
							

				/*
				 * android.sax
				 */
				 SendMoneyWSParser reader = new SendMoneyWSParser();
				 reader.parse(entityResponse.getContent());
				 setSendMoneyResponse(reader.getSendMoneyResponse());

			}

		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	public SendMoneyResponse getSendMoneyResponse() {
		return sendMoneyResponse;
	}

	public void setSendMoneyResponse(SendMoneyResponse sendMoneyResponse) {
		this.sendMoneyResponse = sendMoneyResponse;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
