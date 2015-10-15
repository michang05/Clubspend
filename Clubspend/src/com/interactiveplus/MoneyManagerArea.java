package com.interactiveplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.interactiveplus.preferences.LoginPreferences;
import com.interactiveplus.util.ClubspendDBAdapter;

public class MoneyManagerArea extends Activity {
	private static final int ACTIVIY_LOGOUT = 1;
	private static final int ABOUT = Menu.FIRST;
	private static final int LOGOUT = Menu.FIRST + 1;

	private Button btnPaymentHistory;
	private Button btnSendMoney;
	private Button btnReceiveMoney;

	private TextView paymentInfo;
	private TextView sendMoneyInfo;
	private TextView receiveMoneyInfo;
	private String balance;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.money_manager);
		

		paymentInfo = (TextView) findViewById(R.id.lblPaymenInfo);
		paymentInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(MoneyManagerArea.this).setTitle(
						"Payment History Info").setMessage(
						getResources().getString(R.string.paymentHistory_desc))
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										setResult(RESULT_OK);
									}
								}).show();
			}
		});

		sendMoneyInfo = (TextView) findViewById(R.id.lblSendMoneyInfo);
		sendMoneyInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(MoneyManagerArea.this).setTitle(
						"Send Money Info").setMessage(
						getResources().getString(R.string.sendMoney_Desc))
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										setResult(RESULT_OK);
									}
								}).show();
			}
		});

		receiveMoneyInfo = (TextView) findViewById(R.id.lblReceiveMoneyInfo);
		receiveMoneyInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(MoneyManagerArea.this).setTitle(
						"Receive Money Info").setMessage(
						getResources().getString(R.string.requestMoney_desc))
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										setResult(RESULT_OK);
									}
								}).show();
			}
		});

		btnPaymentHistory = (Button) findViewById(R.id.btnPaymentHistory);
		btnPaymentHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				pd = ProgressDialog.show(MoneyManagerArea.this, "Please Wait", "This might take some time ...", true,
									false);

				new Thread() {
					public void run() {					
						try {
							Thread.sleep(5000);
							runOnUiThread(new Runnable() {
								private ClubspendDBAdapter mDbHelper;

								@Override
								public void run() {
									Intent tabMain = new Intent(MoneyManagerArea.this,
											PaymentHistory.class);
									
									mDbHelper = new ClubspendDBAdapter(MoneyManagerArea.this);
									mDbHelper.open();
									mDbHelper.deleteAllDetails();
									mDbHelper.close();
									
									Bundle b = MoneyManagerArea.this.getIntent().getExtras();
									if (b == null) {
										LoginPreferences.load(MoneyManagerArea.this);
										String balancePref = LoginPreferences.getBalance();
										tabMain.putExtra("BALANCE", balancePref);
									} else {
										balance = b.getString("BALANCE");
										tabMain.putExtra("BALANCE", balance);
									}

									startActivity(tabMain);
								}
							});
							
						} catch (Exception e) {
						}
						
						// Dismiss the Dialog

						pd.dismiss();
					}
				}.start();
				
				
				
				
			}
		});

		btnSendMoney = (Button) findViewById(R.id.btnSendMoney);
		btnSendMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent sendMoney = new Intent(MoneyManagerArea.this,
						SendMoney.class);

				Bundle b = MoneyManagerArea.this.getIntent().getExtras();
				if (b == null) {
					LoginPreferences.load(MoneyManagerArea.this);
					String balancePref = LoginPreferences.getBalance();
					sendMoney.putExtra("BALANCE", balancePref);
				} else {
					balance = b.getString("BALANCE");
					sendMoney.putExtra("BALANCE", balance);
				}

				startActivity(sendMoney);
			}
		});

		btnReceiveMoney = (Button) findViewById(R.id.btnReceiveMoney);
		btnReceiveMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent receiveMoney = new Intent(MoneyManagerArea.this,
						ReceiveMoney.class);
				Bundle b = MoneyManagerArea.this.getIntent().getExtras();
				if (b == null) {
					LoginPreferences.load(MoneyManagerArea.this);
					String balancePref = LoginPreferences.getBalance();
					receiveMoney.putExtra("BALANCE", balancePref);
				} else {
					balance = b.getString("BALANCE");
					receiveMoney.putExtra("BALANCE", balance);
				}
				startActivity(receiveMoney);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ABOUT, 0, "About").setIcon(R.drawable.about);
		menu.add(0, LOGOUT, 0, "Logout").setIcon(R.drawable.logout);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
		super.onBackPressed();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ABOUT:
			Intent about = new Intent(this, AboutClubSpend.class);
			startActivity(about);
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
							Intent logOut = new Intent(MoneyManagerArea.this, Login.class);
							logOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							// finish();
							startActivityForResult(logOut, ACTIVIY_LOGOUT);
						}
					});
					// Dismiss the Dialog

					pd.dismiss();
				}
			}.start();
		}
		return super.onOptionsItemSelected(item);
	}
}
