package com.interactiveplus.preferences;

public class LoginPreferences extends BasePreferences{
	
	 private static final String ID = "Id";
	 private static final String BALANCE ="Balance";
	 private static final String USERNAME="Username";

	 private static final String DEFAULT_ID = "000";
	 private static final String DEFAULT_BALANCE="0.00";
	 private static final String DEFAULT_USERNAME="";
	 
	 public static String getId() {
	        //String encodedUsername = new String(Base64.encode(DEFAULT_USERNAME.getBytes()));
	        String encodedId = settings.getString(ID, DEFAULT_ID);
	        if(encodedId == null || encodedId.equals("")) {
	            return "";
	        }
	        return encodedId;//new String(Base64.decode(encodedPort));
	    }

	    public static void setId(String id) {
//	        editor.putString(USERNAME, new String(Base64.encode(username
//	                .getBytes())));
	    	if(id==null || id.equals("")) {
	    		id = "";
	    	}
	    	editor.putString(ID, id);
	    }

		public static String getBalance() {
			 String encodedBalance = settings.getString(BALANCE, DEFAULT_BALANCE);
		        if(encodedBalance == null || encodedBalance.equals("")) {
		            return "";
		        }
		        return encodedBalance;
		}
	    
		 public static void setBalance(String bal) {
//		        editor.putString(USERNAME, new String(Base64.encode(username
//		                .getBytes())));
		    	if(bal==null || bal.equals("")) {
		    		bal = "";
		    	}
		    	editor.putString(BALANCE, bal);
		    }

		 public static String getUsername() {
			 String username = settings.getString(USERNAME, DEFAULT_USERNAME);
		        if(username == null || username.equals("")) {
		            return "";
		        }
		        return username;
		}
	    
		 public static void setUsername(String username) {

		    	if(username==null || username.equals("")) {
		    		username = "";
		    	}
		    	editor.putString(USERNAME, username);
		    }
		 
}
