package com.interactiveplus;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.interactiveplus.preferences.LoginPreferences;
import com.interactiveplus.util.ClubspendDBAdapter;
import com.interactiveplus.util.FormUtil;
import com.interactiveplus.webservice.SendMoneyWS;

public class SendMoney extends Activity {
	private static final int ACTIVIY_LOGOUT = 1;
	private static final int BACK = Menu.FIRST;

	private static final int RECEIVE_MONEY = Menu.FIRST + 2;
	private static final int PAYMENT_HISTORY = Menu.FIRST + 3;
	private static final int LOGOUT = Menu.FIRST + 4;

	private TextView mDateDisplay;
	private Button mPickDate;

	private int mYear;
	private int mMonth;
	private int mDay;

	static final int DATE_DIALOG_ID = 0;

	private TextView lblBalanceValue;
	private TextView lblDateDisplay;
	private EditText txtEmailAddress;
	private EditText txtAmount;
	private EditText txtMessage;
	private Button btnSendMoney;

	private StringBuilder sb;
	private String balance;
	private String email;
	private String amount;
	private String date;
	private String message;
	private SendMoneyWS sendMoneyWS;
	private FormUtil formUtil;
	private String userId;
	private String bal;

	private ClubspendDBAdapter mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_money);

		formUtil = new FormUtil();

		// capture our View elements
		mDateDisplay = (TextView) findViewById(R.id.lblDateDisplay);
		mPickDate = (Button) findViewById(R.id.pickDate);

		// add a click listener to the button
		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// display the current date
		updateDisplay();
		// load the preferences
		LoginPreferences.load(this);
			userId = LoginPreferences.getId();
			bal = LoginPreferences.getBalance();

		Log.d("SEND_MONEY","ID is: " + userId);		
	
		lblBalanceValue = (TextView) findViewById(R.id.lblBalanceValue);
		Log.d("SEND_MONEY","Balance after sending: "+bal);
		if(bal.startsWith("$")) {
			lblBalanceValue.setText(bal);
		}else {
			lblBalanceValue.setText("$ "+Double.parseDouble(bal));	
		}
		
		txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
		txtAmount = (EditText) findViewById(R.id.txtAmount);
		// not going to be null
		lblDateDisplay = (TextView) findViewById(R.id.lblDateDisplay); 
		txtMessage = (EditText) findViewById(R.id.txtMessage);

		btnSendMoney = (Button) findViewById(R.id.btnSendMoney);
		btnSendMoney.setOnClickListener(new OnClickListener() {

			private int emailStat;
			private int amountStat;
			

			@Override
			public void onClick(View v) {
				date = lblDateDisplay.getText().toString();
				balance = lblBalanceValue.getText().toString();
				message = txtMessage.getText().toString();
				amount = txtAmount.getText().toString();
				email = txtEmailAddress.getText().toString();

				if (email == null || email.equals("")) {
					emailStat = 1;
				} else if (formUtil.validateEmail(email)) {
					emailStat = 0;
					Log.d("SENDMONEY", "Valid Email");
				}

				if (amount == null || amount.equals("")) {
					amountStat = 1;
				} else {
					amountStat = 0;
					amount = String.valueOf(Double.parseDouble(amount));
				}

				if (message == null || message.equals("")) {
					message = "";
				}

				sb = new StringBuilder();
				sb.append("Send from current balance : \n"+balance+"\n\n");
				sb.append("Send to email address : \n" + email + "\n\n");
				sb.append("Amount: \n" + amount + "\n\n");
				sb.append("Eligible to receive on date : \n" + date+ "\n\n");
				sb.append("Message(optional) : \n" + message);

				if (emailStat == 1 && amountStat == 0) {
					Toast.makeText(SendMoney.this,
							email + " is an invalid email address.",
							Toast.LENGTH_SHORT).show();
				} else if (amountStat == 1 && emailStat == 0) {
					Toast.makeText(SendMoney.this,
							" Please input an amount to send.",
							Toast.LENGTH_SHORT).show();
				} else if (emailStat == 1 && amountStat == 1) {
					Toast.makeText(SendMoney.this,
							"Please input values",
							Toast.LENGTH_SHORT).show();
				} else {
					if(message.equals("")) {
						sendMoneyWS = new SendMoneyWS(userId, email, amount, date);
					}else {
						sendMoneyWS = new SendMoneyWS(userId, email, amount, date,message);
					}
								
						new AlertDialog.Builder(SendMoney.this).setTitle("Send Money Details")
						.setMessage(sb.toString())
						.setPositiveButton("Confirm and Send",
									new DialogInterface.OnClickListener() {
										
										public void onClick(DialogInterface dialog,
												int whichButton) {
											sendMoneyWS.send();
											System.out.println("Success is: "+sendMoneyWS.getSendMoneyResponse().getSuccess());
											if(sendMoneyWS.getSendMoneyResponse().getSuccess().equals("false")) {
												Toast.makeText(SendMoney.this,
														sendMoneyWS.getSendMoneyResponse().getError(),
														Toast.LENGTH_LONG).show();
												setResult(RESULT_CANCELED);
											}else {
												Log.d("SEND_MONEY", "Control code: "+sendMoneyWS.getSendMoneyResponse().getControlCode()+
														"\nPayment_Id: "+sendMoneyWS.getSendMoneyResponse().getPaymentId()+
														"\nTransaction_id: "+sendMoneyWS.getSendMoneyResponse().getTransactionId()+
														"\nCurrent Balance : "+sendMoneyWS.getSendMoneyResponse().getBalance());

									
													String balAfter = sendMoneyWS.getSendMoneyResponse().getBalance();
													Log.d("SEND_MONEY","BALANCE after Sending is: "+bal);
													LoginPreferences.setBalance(balAfter);
													LoginPreferences.save();												
													
													Toast.makeText(SendMoney.this,
															"Money successfully sent!!!",
															Toast.LENGTH_LONG).show();
																							
												
												Intent i = new Intent(SendMoney.this,PaymentHistory.class);
												mDbHelper = new ClubspendDBAdapter(SendMoney.this);
												mDbHelper.open();
												mDbHelper.deleteAllDetails();
												mDbHelper.close();
												setResult(RESULT_OK);	
												
												startActivity(i);
											}
																			
										}
									})
						.setNegativeButton("Back",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								setResult(RESULT_CANCELED);									
							}
						}).show();

					
				}

			}
		});
	}

		
	// updates the date we display in the TextView
	private void updateDisplay() {
		StringBuilder builder = new StringBuilder();
		// Month is 0 based so add 1

		String monthP = "" + String.valueOf(mMonth + 1);
		String dayP = "" + mDay;

		if (mMonth < 10) {
			monthP = "0" + String.valueOf(mMonth + 1);
		} else {
			monthP = String.valueOf(mMonth + 1);
		}

		if (mDay < 10) {
			dayP = "0" + String.valueOf(mDay);
		} else {
			dayP = String.valueOf(mDay);
		}
		builder.append(mYear);
		builder.append("-");
		builder.append(monthP);
		builder.append("-");
		builder.append(dayP);

		mDateDisplay.setText(builder.toString());
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};
	private ProgressDialog pd;

	// call back of showDialog
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, BACK, 0, "Back").setIcon(R.drawable.back);
		menu.add(0, RECEIVE_MONEY, 0, "Receive Money").setIcon(
				R.drawable.receive_money);
		menu.add(0, PAYMENT_HISTORY, 0, "Payment History").setIcon(
				R.drawable.history_payment);
		menu.add(0, LOGOUT, 0, "Logout").setIcon(R.drawable.logout);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case BACK:
			finish();
			break;
		case RECEIVE_MONEY:
			Intent receiveMoney = new Intent(this, ReceiveMoney.class);
			startActivity(receiveMoney);
			break;

		case PAYMENT_HISTORY:
			Intent managerArea = new Intent(this, PaymentHistory.class);
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
							Intent logOut = new Intent(SendMoney.this, Login.class);
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
