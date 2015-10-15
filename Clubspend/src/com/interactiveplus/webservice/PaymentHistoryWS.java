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
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.interactiveplus.model.PaymentHistoryResponse;
import com.interactiveplus.parser.PaymentHistoryWSParser;

public class PaymentHistoryWS {

	private static final String PAYMENT_HISTORY_URL = "/android/payment_history.php";

	private PaymentHistoryResponse paymentHistoryResponse;
	private PaymentHistoryWSParser reader; 
	private DefaultHttpClient client;
	private HttpHost host;
	private HttpPost postMethod;
	private HttpResponse response;
	private HttpEntity entityResponse;
	private List<NameValuePair> valuePairs;

	/**
	 * Method for viewing Payment History by Month / Year
	 * 
	 * @param userId
	 * @param month
	 * @param year
	 * @return
	 */
	public List<PaymentHistoryResponse> viewByMonthYear(String userId, String month,String year,String hideDecPayments) {
		List<PaymentHistoryResponse> list=null;
				
		client = new DefaultHttpClient();

		host = new HttpHost("clubspend.com", -1, "http");

		CookieStore cookies = client.getCookieStore();
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.setCookieStore(cookies);

		postMethod = new HttpPost(PAYMENT_HISTORY_URL);
		postMethod.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("USER_ID", userId));
		valuePairs.add(new BasicNameValuePair("strtMonth", month));
		if(hideDecPayments.equals("1")) {
			valuePairs.add(new BasicNameValuePair("hideDec", hideDecPayments));
		}
		valuePairs.add(new BasicNameValuePair("strtYear", year));

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
				 reader = new PaymentHistoryWSParser();								
				 list = reader.parse(entityResponse.getContent());
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
		return list;

	}
	
	/**
	 *  Method for viewing all the payment history
	 *  
	 * @param userId
	 * @param view
	 * @return
	 */
	public List<PaymentHistoryResponse> viewAll(String userId, String view,String hideDec) {
		List<PaymentHistoryResponse> list=null;
		String id = userId;
		String viewType = view;

		client = new DefaultHttpClient();

		host = new HttpHost("clubspend.com", -1, "http");

		CookieStore cookies = client.getCookieStore();
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.setCookieStore(cookies);

		postMethod = new HttpPost(PAYMENT_HISTORY_URL);
		postMethod.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("USER_ID", id));
		valuePairs.add(new BasicNameValuePair("view", viewType));
		if(hideDec.equals("1")) {
			valuePairs.add(new BasicNameValuePair("hideDec", hideDec));
		}
		
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
				 reader = new PaymentHistoryWSParser();								
				 list = reader.parse(entityResponse.getContent());
			}

		}catch (UnsupportedEncodingException e) {
			
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
		return list;

	}

	public List<PaymentHistoryResponse> viewAll(String userId, String view) {
		List<PaymentHistoryResponse> list=null;
		String id = userId;
		String viewType = view;

		client = new DefaultHttpClient();

		host = new HttpHost("clubspend.com", -1, "http");

		CookieStore cookies = client.getCookieStore();
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.setCookieStore(cookies);

		postMethod = new HttpPost(PAYMENT_HISTORY_URL);
		postMethod.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("USER_ID", id));
		valuePairs.add(new BasicNameValuePair("view", viewType));
	
		
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
				 reader = new PaymentHistoryWSParser();								
				 list = reader.parse(entityResponse.getContent());
			}

		}catch (UnsupportedEncodingException e) {
			
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
		return list;

	}

	public PaymentHistoryResponse getPaymentHistoryResponse() {
		return paymentHistoryResponse;
	}

	public void setPaymentHistoryResponse(
			PaymentHistoryResponse paymentHistoryResponse) {
		this.paymentHistoryResponse = paymentHistoryResponse;
	}

	public PaymentHistoryWSParser getReader() {
		return reader;
	}

	public void setReader(PaymentHistoryWSParser reader) {
		this.reader = reader;
	}

	
}
