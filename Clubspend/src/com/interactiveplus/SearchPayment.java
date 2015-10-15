package com.interactiveplus;

import com.interactiveplus.preferences.LoginPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchPayment extends Activity {

	private Spinner monthSpin;
	private Spinner yearSpin;
	private Button btnDisplayPaymentPeriod;
	private String monthString;
	private String yearString;
	private int searchType;

	private EditText txtTransId;
	private Button btnGo;
	private Button btnDisplayAll;
	
	private String balance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		LoginPreferences.load(this);
		balance = LoginPreferences.getBalance();
		
		monthSpin = (Spinner) findViewById(R.id.cmbMonth);
		monthSpin.setPrompt("Select Month");
		SpinnerAdapter monthAdapter = ArrayAdapter.createFromResource(this,
				R.array.Month, android.R.layout.simple_spinner_item);
		monthSpin.setAdapter(monthAdapter);
		monthSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				monthString = parent.getSelectedItem().toString();
				Log.d("SELECTED", monthString);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		yearSpin = (Spinner) findViewById(R.id.cmbYear);
		yearSpin.setPrompt("Select Year");
		SpinnerAdapter yearAdapter = ArrayAdapter.createFromResource(this,
				R.array.Year, android.R.layout.simple_spinner_item);
		yearSpin.setAdapter(yearAdapter);
		yearSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				yearString = parent.getSelectedItem().toString();
				Log.d("SELECTED", yearString);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnDisplayPaymentPeriod = (Button) findViewById(R.id.btnDisplayPaymentPeriod);
		btnDisplayPaymentPeriod.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchType = 1;
				Intent paymentPeriod = new Intent(SearchPayment.this,
						PaymentHistory.class);
				paymentPeriod.putExtra("month", monthString);
				paymentPeriod.putExtra("year", yearString);
				paymentPeriod.putExtra("SearchType", searchType);
				paymentPeriod.putExtra("searchOn", true);
				paymentPeriod.putExtra("BALANCE", balance);
				
				startActivity(paymentPeriod);
			}
		});

		// By ID
		txtTransId = (EditText) findViewById(R.id.txtTransactionId);
		
		
		btnGo = (Button) findViewById(R.id.btnGo);
		btnGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchType = 2;
				String transId = txtTransId.getText().toString();
				Intent byId = new Intent(SearchPayment.this,
						PaymentHistory.class);
				Log.d("SEARCH 2", "Trans ID: "+transId);
				byId.putExtra("TransId", transId);
				byId.putExtra("SearchType", searchType);
				byId.putExtra("searchOn", true);
				byId.putExtra("BALANCE",balance);
				startActivity(byId);
			}
		});
		
		//Display All
		btnDisplayAll = (Button)findViewById(R.id.btnDisplayAll);
		btnDisplayAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchType = 3;
				Intent byAll = new Intent(SearchPayment.this,
						PaymentHistory.class);
		
				byAll.putExtra("SearchType", searchType);
				byAll.putExtra("searchOn", true);
				byAll.putExtra("BALANCE", balance);
			
				startActivity(byAll);
			}
		});
	}

}
