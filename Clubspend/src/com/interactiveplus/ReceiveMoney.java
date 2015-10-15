package com.interactiveplus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.interactiveplus.preferences.LoginPreferences;
import com.interactiveplus.util.ClubspendDBAdapter;
import com.interactiveplus.util.FormUtil;
import com.interactiveplus.webservice.ReceiveMoneyWS;

public class ReceiveMoney extends Activity {
	
	private static final int ACTIVIY_LOGOUT =1;
	private static final int BACK = Menu.FIRST;

	private static final int SEND_MONEY = Menu.FIRST + 2;
	private static final int PAYMENT_HISTORY = Menu.FIRST + 3;
	private static final int LOGOUT = Menu.FIRST + 4;

	private EditText txtControlNumber;
	private EditText txtEmailPayer;
	private Button btnSubmit;

	private FormUtil formUtil;
	private ProgressDialog pd;

	private ClubspendDBAdapter mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.receive_money);

		formUtil = new FormUtil();

		txtControlNumber = (EditText) findViewById(R.id.txtControlNumber);
		txtEmailPayer = (EditText) findViewById(R.id.txtEmailAddressPayer);

		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new OnClickListener() {

			

			@Override
			public void onClick(View v) {
				String controlCode = txtControlNumber.getText().toString();
				String emailPayer = txtEmailPayer.getText().toString();

				if (controlCode == null || controlCode.equals("")) {
					Toast.makeText(ReceiveMoney.this,
							"Please input Control Number", Toast.LENGTH_LONG)
							.show();
				} else if (emailPayer == null || emailPayer.equals("")) {
					Toast.makeText(ReceiveMoney.this,
							"Please input Email Payer", Toast.LENGTH_LONG)
							.show();
				} else {
					if (formUtil.validateEmail(emailPayer)) {
						String userId = LoginPreferences.getId();
						ReceiveMoneyWS ws = new ReceiveMoneyWS(emailPayer, controlCode,userId);
						ws.receive();
						if(ws.getReceiveMoneyResponse().getSuccess().equals("false")) {
							
							Toast.makeText(ReceiveMoney.this,
									ws.getReceiveMoneyResponse().getError(),
									Toast.LENGTH_LONG).show();
							
							setResult(RESULT_CANCELED);
							
						}else {
							Log.d("RECEIVE_MONEY", "Amount Received: "+ws.getReceiveMoneyResponse().getAmountReceived()+
									"\nDate Received: "+ws.getReceiveMoneyResponse().getDateReceived()+
									"\nCurrent Balance : "+ws.getReceiveMoneyResponse().getBalance()+
									"\nPayment Id: "+ws.getReceiveMoneyResponse().getPaymentId()+
									"\nCommission Fee Id: "+ws.getReceiveMoneyResponse().getCommissionFeeId());

								LoginPreferences.load(ReceiveMoney.this);
								LoginPreferences.setBalance(ws.getReceiveMoneyResponse().getBalance());
								LoginPreferences.save();
							Toast.makeText(ReceiveMoney.this,
									"Money Received!!!",
									Toast.LENGTH_LONG).show();		
						
						Intent i = new Intent(ReceiveMoney.this,PaymentHistory.class);
						mDbHelper = new ClubspendDBAdapter(ReceiveMoney.this);
						mDbHelper.open();
						mDbHelper.deleteAllDetails();
						mDbHelper.close();
						setResult(RESULT_OK);	
						
						startActivity(i);
						}
					} else {
						Toast.makeText(ReceiveMoney.this,
								"Email Address Invalid", Toast.LENGTH_LONG)
								.show();
					}
				}
				
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, BACK, 0, "Back").setIcon(R.drawable.back);
		menu.add(0, SEND_MONEY, 0, "Send Money").setIcon(R.drawable.send_money);
		menu.add(0,PAYMENT_HISTORY,0,"Payment History").setIcon(R.drawable.history_payment);
		menu.add(0, LOGOUT, 0, "Logout").setIcon(R.drawable.logout);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case BACK:
			finish();
			break;
		case SEND_MONEY:
			Intent main = new Intent(this, SendMoney.class);
			startActivity(main);
			break;
			
		case PAYMENT_HISTORY:
			Intent managerArea = new Intent(this,PaymentHistory.class);
			startActivity(managerArea);
			break;
		case LOGOUT:
			pd = ProgressDialog.show(this,null, "Logging out...", true,
					false);

			new Thread() {
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (Exception e) {
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Intent logOut = new Intent(ReceiveMoney.this, Login.class);
							logOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							// finish();
							startActivityForResult(logOut, ACTIVIY_LOGOUT);
						}
					});
					// Dismiss the Dialog

					pd.dismiss();
				}
			}.start();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
