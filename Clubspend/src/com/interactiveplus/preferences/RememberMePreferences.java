package com.interactiveplus.preferences;

public class RememberMePreferences extends BasePreferences {

	// Sync preferences
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CHECKED= "checked";

    // Sync preferences default
    private static final String DEFAULT_USERNAME = "hipspend@ninedot.biz";
    private static final String DEFAULT_PASSWORD = "demodemo";
    private static final boolean DEFAULT_CHECKED = false;

    public static String getUserName() {
        //String encodedUsername = new String(Base64.encode(DEFAULT_USERNAME.getBytes()));
        String encodedUsername = settings.getString(USERNAME, DEFAULT_USERNAME);
        if(encodedUsername == null || encodedUsername.equals("")) {
            return "";
        }
        return encodedUsername;//new String(Base64.decode(encodedPort));
    }

    public static void setUsername(String username) {
//        editor.putString(USERNAME, new String(Base64.encode(username
//                .getBytes())));
    	if(username==null || username.equals("")) {
    		username = "";
    	}
    	editor.putString(USERNAME, username);
    }


    public static String getPassword() {
       
    	return settings.getString(PASSWORD, DEFAULT_PASSWORD);
    }

    public static void setPassword(String password) {
    	if(password==null || password.equals("")) {
    		password = "";
    	}
        editor.putString(PASSWORD, password);
    }
    
    public static void setChecked(boolean checked) {
    	editor.putBoolean(CHECKED,checked);
    }
    
    public static boolean getChecked() {
    	return settings.getBoolean(CHECKED,DEFAULT_CHECKED);
    }
}
