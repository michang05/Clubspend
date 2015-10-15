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

import com.interactiveplus.model.ReceiveMoneyResponse;
import com.interactiveplus.parser.ReceiveMoneyWSParser;

/**
 * @author Michael Angelo
 * 
 */
public class ReceiveMoneyWS {
	private static final String URL = "/android/receive_money.php";

	private String email;
	private String controlCode;
	private String userId;
	
	private ReceiveMoneyResponse receiveMoneyResponse;

	private DefaultHttpClient client;
	private HttpHost host;
	private HttpPost postMethod;
	private HttpResponse response;
	private HttpEntity entityResponse;
	private List<NameValuePair> valuePairs;
	
	public ReceiveMoneyWS(String email,String controlCode,String userId) {

		this.email = email;
		this.controlCode = controlCode;
		this.userId = userId;

		client = new DefaultHttpClient();
	
		host = new HttpHost("clubspend.com", -1, "http");

		postMethod = new HttpPost(URL);
		postMethod.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		valuePairs = new ArrayList<NameValuePair>();		
		valuePairs.add(new BasicNameValuePair("sEmail", email));
		valuePairs.add(new BasicNameValuePair("sControlCode", controlCode));
		valuePairs.add(new BasicNameValuePair("iUserId", userId)); 
	}
	
	public void receive() {

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
				 ReceiveMoneyWSParser reader = new ReceiveMoneyWSParser();
				 reader.parse(entityResponse.getContent());
				 setReceiveMoneyResponse(reader.getReceiveMoneyResponse());

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getControlCode() {
		return controlCode;
	}

	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}

	public ReceiveMoneyResponse getReceiveMoneyResponse() {
		return receiveMoneyResponse;
	}

	public void setReceiveMoneyResponse(ReceiveMoneyResponse receiveMoneyResponse) {
		this.receiveMoneyResponse = receiveMoneyResponse;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
