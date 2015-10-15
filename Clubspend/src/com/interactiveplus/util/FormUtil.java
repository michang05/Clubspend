package com.interactiveplus.util;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormUtil {

	public boolean validateEmail(String email){
		   // Input the string for validation
		   // String email = "xyz@.com";
		   // Set the email pattern string
		   Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

		   // Match the given string with the pattern
		   Matcher m = p.matcher(email);

		   // check whether match is found
		   boolean matchFound = m.matches();

		   StringTokenizer st = new StringTokenizer(email, ".");
		   String lastToken = null;
		   while (st.hasMoreTokens()) {
		      lastToken = st.nextToken();
		   }

		   if (matchFound && lastToken.length() >= 2
		      && email.length() - 1 != lastToken.length()) {

		      // validate the country code
		      return true;
		   }
		   else return false;
		}

}
