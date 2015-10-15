package com.interactiveplus.parser;

public class MonthParser {

	private String month;
	

	public String getMonthNumber(String monthText) {
		month = monthText;
		if(monthText.equals("January")) {
			month = "01";
		}else if(monthText.equals("February")) {
			month = "02";
		}else if(monthText.equals("March")) {
			month = "03";
		}else if(monthText.equals("April")) {
			month = "04";
		}else if(monthText.equals("May")) {
			month = "05";
		}else if(monthText.equals("June")) {
			month = "06";
		}else if(monthText.equals("July")) {
			month = "07";
		}if(monthText.equals("August")) {
			month = "08";
		}else if(monthText.equals("September")) {
			month = "09";
		}else if(monthText.equals("October")) {
			month = "10";
		}else if(monthText.equals("November")) {
			month = "11";
		}else if(monthText.equals("December")) {
			month = "12";
		}
		
		return month;
	}
}
