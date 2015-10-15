package com.interactiveplus.parser;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.interactiveplus.model.LoginResponse;
import com.interactiveplus.parserbase.BaseXmlParser;

public class LoginWSParser extends BaseXmlParser{

	private LoginResponse loginResponse;
	private Vector<LoginResponse> responseVector;
	
	public LoginWSParser() {		
		super();
	}

	public LoginResponse getLoginResponse() {
		return loginResponse;
	}

	public void setLoginResponse(LoginResponse loginResponse) {
		this.loginResponse = loginResponse;
	}

	public Vector<LoginResponse> getResponseVector() {
		return responseVector;
	}

	public void setResponseVector(Vector<LoginResponse> responseVector) {
		this.responseVector = responseVector;
	}

	@Override
	public Vector<LoginResponse> parse(InputStream inputStream) {
		
		loginResponse = new LoginResponse();
		responseVector = new Vector<LoginResponse>();
		
		RootElement ninedot = new RootElement(NINEDOT);
		
		Element id = ninedot.getChild(ID);
		id.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				loginResponse.setId(body);
			}
		});
		Element name = ninedot.getChild(NAME);
		name.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				loginResponse.setName(body);
			}
		});
		Element notice = ninedot.getChild(NOTICE);
		notice.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				loginResponse.setNotice(body);
			}
		});
		Element success = ninedot.getChild(SUCCESS);
		success.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				loginResponse.setSuccess(Integer.parseInt(body));
			}
		});
		Element balance = ninedot.getChild(BALANCE_LOGIN);
		balance.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				loginResponse.setBalance(body);
			}
		});	
		ninedot.setEndElementListener(new EndElementListener() {			
			@Override
			public void end() {
				responseVector.addElement(loginResponse);				
			}
		});
		
		try {
			Xml.parse(inputStream, Xml.Encoding.UTF_8, ninedot.getContentHandler());
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

}
