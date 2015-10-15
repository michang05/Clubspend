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
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.interactiveplus.model.LoginResponse;
import com.interactiveplus.parser.LoginWSParser;

public class LoginWS {
	private static final String TAG = "LOGIN";
	private static final String LOGIN_URL = "/android/login.php";

	private String username;
	private String password;
	private LoginResponse loginResponse;
	private String statusMessage;

	private DefaultHttpClient client;
	private HttpHost host;
	private HttpPost postMethod;
	private HttpResponse response;
	private HttpEntity entityResponse;
	private List<NameValuePair> valuePairs;
	private String cookieString;
	
	public LoginWS(String uname, String pass) {

		username = uname;
		password = pass;
		
		client = new DefaultHttpClient();
		
		host = new HttpHost("clubspend.com", -1, "http");
		
		CookieStore cookies = client.getCookieStore();
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.setCookieStore(cookies);	
		
		postMethod = new HttpPost(LOGIN_URL);
		postMethod.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("username", username));
		valuePairs.add(new BasicNameValuePair("password", password));
		
	}
	
	
	public void login() {

		try {

		postMethod.setEntity(new UrlEncodedFormEntity(valuePairs));

			response = client.execute(host, postMethod);
	
			Log.d(TAG, "Response: "
					+ response.getStatusLine().getReasonPhrase()
					+ " | username: " + username + " password: " + password);
			entityResponse = response.getEntity();

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_IMPLEMENTED) {
				System.err
						.println("The Post method is not implemented by this URI");
				// still consume the response body
				entityResponse.consumeContent();

			} else {
				
				// Initial Set of Cookies
				System.out.println("Initial set of cookies:");
				List<Cookie> cookies = client.getCookieStore().getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i).toString());
					}
				}				

				/*
				 * android.sax
				 */
				 LoginWSParser reader = new LoginWSParser();
				 reader.parse(entityResponse.getContent());
				 setLoginResponse(reader.getLoginResponse());

				
				 
				// Post logon cookies
					System.out.println("Post logon cookies:");
					cookies = client.getCookieStore().getCookies();

					if (cookies.isEmpty()) {
						System.out.println("None");
					} else {
						for (int i = 0; i < cookies.size(); i++) {
							System.out.println("- " + cookies.get(i).toString());

						}
					}
					// extract and save the session id to the variable
					for (Cookie cookie : cookies) {
						if (cookie.getName().equals("PHPSESSID")) {
							cookieString = cookie.getValue();
						}
					}
			}

		} catch (UnsupportedEncodingException e) {
			setStatusMessage("UEE: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			setStatusMessage("CPE: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			setStatusMessage("IOE: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	public String getCookieString() {
		return cookieString;
	}

	public void setCookieString(String cookieString) {
		this.cookieString = cookieString;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public LoginResponse getLoginResponse() {
		return loginResponse;
	}

	public void setLoginResponse(LoginResponse loginResponse) {
		this.loginResponse = loginResponse;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
