package com.interactiveplus;

import com.interactiveplus.util.ClubspendDBAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class TransactionDetails extends Activity{

	private static final int ACTIVIY_LOGOUT = 1;
	
	private static final int SEND_MONEY = Menu.FIRST;
	private static final int RECEIVE_MONEY = Menu.FIRST + 2;
	private static final int SEARCH= Menu.FIRST + 3;
	private static final int LOGOUT = Menu.FIRST + 4;
	
	private Bundle b;
	
	private TextView postedDate;
	private TextView transId;
	private TextView trans;
	private TextView debit;
	private TextView credit;
	private TextView balance;
	private TextView status;

	private ProgressDialog pd;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.trans_details);
		b = this.getIntent().getExtras();
		
		postedDate = (TextView)findViewById(R.id.transDateVal);
		postedDate.setText(b.getString(ClubspendDBAdapter.KEY_POSTED_DATE));
		
		transId = (TextView)findViewById(R.id.transIdVal);
		transId.setText(b.getString(ClubspendDBAdapter.KEY_TRANSACTION_ID));
		
		trans = (TextView)findViewById(R.id.transLabelVal);
		trans.setText(b.getString(ClubspendDBAdapter.KEY_TRANSACTION));
		
		debit = (TextView)findViewById(R.id.transDebitVal);
		debit.setText(b.getString(ClubspendDBAdapter.KEY_DEBIT));
		
		credit = (TextView)findViewById(R.id.transCreditVal);
		credit.setText(b.getString(ClubspendDBAdapter.KEY_CREDIT));
		
		balance = (TextView)findViewById(R.id.transBalVal);
		balance.setText(b.getString(ClubspendDBAdapter.KEY_BALANCE));
		
		status = (TextView)findViewById(R.id.transStatusVal);
		status.setText(b.getString(ClubspendDBAdapter.KEY_STATUS));
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SEARCH, 0, "Search").setIcon(R.drawable.search);
		menu.add(0, SEND_MONEY, 0, "Send Money").setIcon(R.drawable.send_money);
		menu.add(0, RECEIVE_MONEY, 0, "Receive Money").setIcon(
				R.drawable.receive_money);
		menu.add(0, LOGOUT, 0, "Logout").setIcon(R.drawable.logout);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SEARCH:
			Intent i = new Intent(this, SearchPayment.class);
			startActivity(i);
			break;
		case SEND_MONEY:
			Intent main = new Intent(this, SendMoney.class);
			startActivity(main);
			break;

		case RECEIVE_MONEY:
			Intent managerArea = new Intent(this, ReceiveMoney.class);
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
							Intent logOut = new Intent(TransactionDetails.this, Login.class);
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
