package com.interactiveplus;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

import com.interactiveplus.model.PaymentHistoryResponse;
import com.interactiveplus.parser.MonthParser;
import com.interactiveplus.preferences.HideDecPreferences;
import com.interactiveplus.preferences.LoginPreferences;
import com.interactiveplus.util.ClubspendDBAdapter;
import com.interactiveplus.webservice.PaymentHistoryWS;

public class PaymentHistory extends TabActivity {

	private static final String TAG = "TabMain";

	private static final int ACTIVIY_LOGOUT = 1;

	private static final int SEARCH = Menu.FIRST;
	private static final int UPDATE = Menu.FIRST + 2;
	private static final int BACK_MONEY_MANAGER = Menu.FIRST + 3;
	private static final int LOGOUT = Menu.FIRST + 4;

	private TabHost mTabHost;
	private ListView listPosted;
	private ListView listPending;
	private ClubspendDBAdapter mDbHelper;
	private Cursor mBundyCursor;
	private SimpleCursorAdapter clients;
	private TextView totalBalance;
	private Bundle mBundle;
	private TabSpec tabPending;
	private TabSpec tabPosted;
	private int searchSelected;
	private boolean searchOn = false;
	private static final String POSTED = "POSTED";
	private static final String PENDING = "PENDING";

	private int mYear;
	private int mMonth;
	private String strMonth;
	private String strYear;
	private String mTransId;

	private String userId;
	private String totalBal;
	private Bundle b;

	private PaymentHistoryWS paymentHistoryWS;
	private List<PaymentHistoryResponse> listResponses;

	private CheckBox chkHideDec;

	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_main);

		setTitle("Payment History for "+LoginPreferences.getUsername());
		
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH)+1;
	
		Log.d(TAG, "month: " + mMonth + " year: " + mYear);
			
		paymentHistoryWS = new PaymentHistoryWS();

		// load the preferences
		LoginPreferences.load(PaymentHistory.this);
		userId = LoginPreferences.getId();
		b = getIntent().getExtras();

		if (b == null) {
			totalBal = LoginPreferences.getBalance();
		} else {
			totalBal = b.getString("BALANCE");
		}
		Log.d(TAG, "ID is: " + userId);

		mDbHelper = new ClubspendDBAdapter(PaymentHistory.this);
		mDbHelper.open();
		
		
		// For getting all the list and saving to Database
		try {
		Log.d(TAG, "Row size is: "+mDbHelper.rowSize());
		
			if(mDbHelper.rowSize()==0) {
				processWebserviceValues();
			}
			
			totalBalance = (TextView) findViewById(R.id.totalBalance);
			totalBalance.setText(totalBalance.getText().toString()
					+ Double.parseDouble(totalBal));
			
			HideDecPreferences.load(PaymentHistory.this);
			
			mBundle = PaymentHistory.this.getIntent().getExtras();
			if (mBundle != null) {
				if (mBundle.getBoolean("searchOn") == true) {
					searchOn = mBundle.getBoolean("searchOn");
					searchSelected = HideDecPreferences.getSearchSelected();
				} else {
					searchOn = false;
				}
			}

			chkHideDec = (CheckBox) findViewById(R.id.chkHideDec);
			if (searchOn == true && searchSelected != 0) {
				chkHideDec.setChecked(HideDecPreferences.getChecked());
				Log.d(TAG, "SearchSelected: " + searchSelected);
				Log.d(TAG, "Search is On?: " + searchOn);
			} else {
				chkHideDec.setChecked(HideDecPreferences.getChecked());
				Log.d(TAG, "SearchSelected: " + searchSelected);
				Log.d(TAG, "Search is On?: " + searchOn);
			}

			chkHideDec.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {

					if (isChecked) {
						Log.d(TAG, "CHECK BOX is checked");
						HideDecPreferences.setChecked(true);
						HideDecPreferences.save();
						Intent i = new Intent(PaymentHistory.this,
								PaymentHistory.class);
						if (searchOn) {
							if (searchSelected == 1) {
								i.putExtra("SearchType", 1);
								i.putExtra("month", strMonth);
								i.putExtra("year", strYear);
							} else if (searchSelected == 2) {
								i.putExtra("SearchType", 2);
								i.putExtra("TransId", mTransId);
							} else if (searchSelected == 3) {
								i.putExtra("SearchType", 3);
							}
							i.putExtra("BALANCE", LoginPreferences.getBalance());
							i.putExtra("searchOn", true);
							startActivity(i);
						} else {
							startActivity(i);
						}

					} else {
						Log.d(TAG, "CHECK BOX is not checked");
						HideDecPreferences.setChecked(false);
						HideDecPreferences.save();
						Intent i = new Intent(PaymentHistory.this,
								PaymentHistory.class);
						if (searchOn) {
							if (searchSelected == 1) {
								i.putExtra("SearchType", 1);
								i.putExtra("month", strMonth);
								i.putExtra("year", strYear);
							} else if (searchSelected == 2) {
								i.putExtra("SearchType", 2);
								i.putExtra("TransId", mTransId);
							} else if (searchSelected == 3) {
								i.putExtra("SearchType", 3);
							}
							i.putExtra("BALANCE", LoginPreferences.getBalance());
							i.putExtra("searchOn", true);
							startActivity(i);
						} else {
							startActivity(i);
						}

					}
				}
			});
			populateTabs();
			LoginPreferences.setBalance(totalBal);
			LoginPreferences.save();
	
			
		} catch (NullPointerException e) {
			new AlertDialog.Builder(PaymentHistory.this).setTitle(
					"Connection Error").setMessage(
					"Connection was not established! Try again later")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									setResult(RESULT_OK);
								}
							}).show();
			finish();
		} catch (Exception e) {
			new AlertDialog.Builder(PaymentHistory.this).setTitle(
					"Connection Error").setMessage(
					"Connection was not established! Try again later")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									setResult(RESULT_OK);
								}
							}).show();
			finish();
		}

		
	}

	/**
	 * 
	 */
	private void processWebserviceValues() {
		listResponses = paymentHistoryWS.viewAll(userId, "all");
		
		Iterator<PaymentHistoryResponse> it = listResponses.iterator();
		Log.d("PH", String.valueOf(listResponses.size()));

		while (it.hasNext()) {
			PaymentHistoryResponse paymentHistoryResponse = it.next();

			if (paymentHistoryResponse.getType().equals("PENDING")) {
				String year = paymentHistoryResponse.getP_postedDate()
						.substring(0, 4);
				String month = paymentHistoryResponse.getP_postedDate()
						.substring(5, 7);
				mDbHelper.createDetails(paymentHistoryResponse.getP_id(),
						paymentHistoryResponse.getP_transaction(),
						paymentHistoryResponse.getP_debit(),
						paymentHistoryResponse.getP_credit(),
						paymentHistoryResponse.getP_balance(),
						paymentHistoryResponse.getStatus(),
						paymentHistoryResponse.getP_postedDate(),
						paymentHistoryResponse.getType(), year, month);
			} else {
				String year = paymentHistoryResponse.getPostedDate().substring(
						0, 4);
				String month = paymentHistoryResponse.getPostedDate()
						.substring(5, 7);
				mDbHelper.createDetails(paymentHistoryResponse.getTransId(),
						paymentHistoryResponse.getTransaction(),
						paymentHistoryResponse.getDebit(),
						paymentHistoryResponse.getCredit(),
						paymentHistoryResponse.getBalance(),
						paymentHistoryResponse.getStatus(),
						paymentHistoryResponse.getPostedDate(),
						paymentHistoryResponse.getType(), year, month);
			}
		}
	}

	/**
	 * 
	 */
	private void populateTabs() {
		// get the TABHOST

		mTabHost = getTabHost();

		tabPosted = mTabHost.newTabSpec("tab_posted");
		tabPosted.setIndicator(getString(R.string.PostedTab), getResources()
				.getDrawable(R.drawable.posted));
		tabPosted.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {

				LinearLayout ll = (LinearLayout) findViewById(R.id.postedPayment);
				listPosted = (ListView) findViewById(R.id.listPostedTransaction);

				if (searchOn == false) {
					if (chkHideDec.isChecked()) {
						fillDataDefault(listPosted, POSTED, mMonth, mYear,
								"APP");
					} else {
						fillDataDefault(listPosted, POSTED, mMonth, mYear);
					}
				} else {
					searchOn = true;
					searchSelected = mBundle.getInt("SearchType");
					HideDecPreferences.setSearchSelected(searchSelected);
					HideDecPreferences.save();
					Log.d(TAG, "case is: " + searchSelected);
					switch (searchSelected) {

					case 1:
						String month = mBundle.getString("month");
						strMonth = month;
						String year = mBundle.getString("year");
						strYear = year;
						Log.d(TAG, "MONTH : " + mBundle.getString("month")
								+ " | YEAR: " + mBundle.getString("year"));
						if (chkHideDec.isChecked()) {
							fillDataByPaymentPeriod(listPosted, POSTED, month,
									year, "APP");
						} else {
							fillDataByPaymentPeriod(listPosted, POSTED, month,
									year);
						}

						break;
					case 2:
						String transId = mBundle.getString("TransId");
						mTransId = transId;
						Log.d(TAG, "transId is: " + transId);
						if (chkHideDec.isChecked()) {
							fillDataByTransactionId(listPosted, POSTED,
									transId, "APP");
						} else {
							fillDataByTransactionId(listPosted, POSTED, transId);
						}
						break;
					case 3:
						if (chkHideDec.isChecked()) {
							fillDataAll(listPosted, POSTED, "APP");
						} else {
							fillDataAll(listPosted, POSTED);
						}
						break;
					}

				}

				listPosted.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Intent i = new Intent(PaymentHistory.this,
								TransactionDetails.class);

						Cursor c = mDbHelper.fetchDetail(parent
								.getItemIdAtPosition(position));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_POSTED_DATE,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_POSTED_DATE)));
						i.putExtra(ClubspendDBAdapter.KEY_ROWID, parent
								.getItemIdAtPosition(position));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_TRANSACTION_ID,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_TRANSACTION_ID)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_TRANSACTION,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_TRANSACTION)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_DEBIT,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_DEBIT)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_CREDIT,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_CREDIT)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_BALANCE,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_BALANCE)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_STATUS,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_STATUS)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_TRANSACTION_TYPE,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_TRANSACTION_TYPE)));

						startActivity(i);

					}
				});

				TextView postedMessage = new TextView(PaymentHistory.this);
				LayoutParams lpTextView = new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				postedMessage.setLayoutParams(lpTextView);
				postedMessage.setText(R.string.Posted_none);

				ll.addView(postedMessage);
			
				listPosted.setEmptyView(postedMessage);
				return ll;
			}
		});

		tabPending = mTabHost.newTabSpec("tab_pending");
		tabPending.setIndicator(getString(R.string.PendingTab), getResources()
				.getDrawable(R.drawable.pending));
		tabPending.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {

				LinearLayout ll = (LinearLayout) findViewById(R.id.pendingPayment);
				listPending = (ListView) findViewById(R.id.listPendingTransaction);

				if (searchOn == false) {
					if (chkHideDec.isChecked()) {
						fillDataDefault(listPosted, POSTED, mMonth, mYear,
								"APP");
					} else {
						fillDataDefault(listPosted, POSTED, mMonth, mYear);
					}
				} else {
					searchOn = true;
					Log.d(TAG, "case is: " + searchSelected);
					switch (searchSelected) {

					case 1:
						String month = mBundle.getString("month");
						strMonth = month;
						String year = mBundle.getString("year");
						strYear = year;

						if (chkHideDec.isChecked()) {
							fillDataByPaymentPeriod(listPending, PENDING,
									month, year, "APP");
						} else {
							fillDataByPaymentPeriod(listPending, PENDING,
									month, year);
						}
						break;
					case 2:
						String transId = mBundle.getString("TransId");
						mTransId = transId;
						Log.d(TAG, "transId is: " + transId);
						fillDataByTransactionId(listPending, PENDING, transId);
						break;
					case 3:
						if (chkHideDec.isChecked()) {
							fillDataAll(listPending, PENDING, "APP");
						} else {
							fillDataAll(listPending, PENDING);
						}
						break;
					}

				}

				listPending.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Intent i = new Intent(PaymentHistory.this,
								TransactionDetails.class);

						Cursor c = mDbHelper.fetchDetail(parent
								.getItemIdAtPosition(position));

						i.putExtra(ClubspendDBAdapter.KEY_POSTED_DATE,
										c.getString(c.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_POSTED_DATE)));
						i.putExtra(ClubspendDBAdapter.KEY_ROWID, parent
								.getItemIdAtPosition(position));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_TRANSACTION_ID,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_TRANSACTION_ID)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_TRANSACTION,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_TRANSACTION)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_DEBIT,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_DEBIT)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_CREDIT,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_CREDIT)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_BALANCE,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_BALANCE)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_STATUS,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_STATUS)));
						i
								.putExtra(
										ClubspendDBAdapter.KEY_TRANSACTION_TYPE,
										c
												.getString(c
														.getColumnIndexOrThrow(ClubspendDBAdapter.KEY_TRANSACTION_TYPE)));

						startActivity(i);

					}
				});

				TextView pendingMessage = new TextView(PaymentHistory.this);
				LayoutParams lpTextView = new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				pendingMessage.setLayoutParams(lpTextView);
				pendingMessage.setText(R.string.Pending_none);

				ll.addView(pendingMessage);
				listPending.setEmptyView(pendingMessage);
				return ll;

			}
		});

		mTabHost.addTab(tabPosted);
		mTabHost.addTab(tabPending);
		mTabHost.setCurrentTab(0);

	}

	private void fillDataByTransactionId(ListView list, String type,
			String transId) {

		Log.d("TABMAIN", transId);

		mBundyCursor = mDbHelper.searchDetailByTransactionId(transId, type);
		startManagingCursor(mBundyCursor);

		// Create an array to specify the fields we want to display in the list
		// (only Client and Date)
		String[] from = new String[] { ClubspendDBAdapter.KEY_TRANSACTION,
				ClubspendDBAdapter.KEY_POSTED_DATE };

		// and an array of the fields we want to bind those fields to
		int[] to = new int[] { R.id.transaction, R.id.postingDate };

		// Now create a simple cursor adapter and set it to display
		clients = new SimpleCursorAdapter(this, R.layout.transaction_row,
				mBundyCursor, from, to);

		list.setAdapter(clients);

	}

	private void fillDataByTransactionId(ListView list, String type,
			String transId, String status) {

		Log.d("TABMAIN", transId);

		mBundyCursor = mDbHelper.searchDetailByTransactionId(transId, type,
				status);
		startManagingCursor(mBundyCursor);

		// Create an array to specify the fields we want to display in the list
		// (only Client and Date)
		String[] from = new String[] { ClubspendDBAdapter.KEY_TRANSACTION,
				ClubspendDBAdapter.KEY_POSTED_DATE };

		// and an array of the fields we want to bind those fields to
		int[] to = new int[] { R.id.transaction, R.id.postingDate };

		// Now create a simple cursor adapter and set it to display
		clients = new SimpleCursorAdapter(this, R.layout.transaction_row,
				mBundyCursor, from, to);

		list.setAdapter(clients);

	}

	private void fillDataDefault(ListView list, String type, int month, int year) {

		mBundyCursor = mDbHelper.defaultSearch(month, year, type);

		startManagingCursor(mBundyCursor);

		// Create an array to specify the fields we want to display in the list
		// (only Client and Date)
		String[] from = new String[] { ClubspendDBAdapter.KEY_TRANSACTION,
				ClubspendDBAdapter.KEY_POSTED_DATE };

		// and an array of the fields we want to bind those fields to
		int[] to = new int[] { R.id.transaction, R.id.postingDate };

				
		// Now create a simple cursor adapter and set it to display
		clients = new SimpleCursorAdapter(this, R.layout.transaction_row,
				mBundyCursor, from, to);

		list.setAdapter(clients);

	}

	private void fillDataDefault(ListView list, String type, int month,
			int year, String status) {

		mBundyCursor = mDbHelper.defaultSearch(month, year, type, status);

		startManagingCursor(mBundyCursor);

		// Create an array to specify the fields we want to display in the list
		// (only Client and Date)
		String[] from = new String[] { ClubspendDBAdapter.KEY_TRANSACTION,
				ClubspendDBAdapter.KEY_POSTED_DATE };

		// and an array of the fields we want to bind those fields to
		int[] to = new int[] { R.id.transaction, R.id.postingDate };

		// Now create a simple cursor adapter and set it to display
		clients = new SimpleCursorAdapter(this, R.layout.transaction_row,
				mBundyCursor, from, to);

		list.setAdapter(clients);

	}

	private void fillDataByPaymentPeriod(ListView list, String type,
			String month, String year) {
		MonthParser parser = new MonthParser();
		String monthStr = parser.getMonthNumber(month);
		Log.d("TABMAIN", parser.getMonthNumber(month));

		mBundyCursor = mDbHelper.searchDetailByMonthYear(monthStr, year, type);
		startManagingCursor(mBundyCursor);

		// Create an array to specify the fields we want to display in the list
		// (only Client and Date)
		String[] from = new String[] { ClubspendDBAdapter.KEY_TRANSACTION,
				ClubspendDBAdapter.KEY_POSTED_DATE };

		// and an array of the fields we want to bind those fields to
		int[] to = new int[] { R.id.transaction, R.id.postingDate };

		// Now create a simple cursor adapter and set it to display
		clients = new SimpleCursorAdapter(this, R.layout.transaction_row,
				mBundyCursor, from, to);

		list.setAdapter(clients);

	}

	private void fillDataByPaymentPeriod(ListView list, String type,
			String month, String year, String status) {
		MonthParser parser = new MonthParser();
		String monthStr = parser.getMonthNumber(month);
		Log.d("TABMAIN", parser.getMonthNumber(month));

		mBundyCursor = mDbHelper.searchDetailByMonthYear(monthStr, year, type,
				status);
		startManagingCursor(mBundyCursor);

		// Create an array to specify the fields we want to display in the list
		// (only Client and Date)
		String[] from = new String[] { ClubspendDBAdapter.KEY_TRANSACTION,
				ClubspendDBAdapter.KEY_POSTED_DATE };

		// and an array of the fields we want to bind those fields to
		int[] to = new int[] { R.id.transaction, R.id.postingDate };

		// Now create a simple cursor adapter and set it to display
		clients = new SimpleCursorAdapter(this, R.layout.transaction_row,
				mBundyCursor, from, to);

		list.setAdapter(clients);

	}

	private void fillDataAll(ListView list, String type) {
		// Get all of the rows from the database and create the item list
		mBundyCursor = mDbHelper.fetchDetailByType(type);

		startManagingCursor(mBundyCursor);

		// Create an array to specify the fields we want to display in the list
		// (only Client and Date)
		String[] from = new String[] { ClubspendDBAdapter.KEY_TRANSACTION,
				ClubspendDBAdapter.KEY_POSTED_DATE };

		// and an array of the fields we want to bind those fields to
		int[] to = new int[] { R.id.transaction, R.id.postingDate };

		// Now create a simple cursor adapter and set it to display
		clients = new SimpleCursorAdapter(this, R.layout.transaction_row,
				mBundyCursor, from, to);

		list.setAdapter(clients);

	}

	private void fillDataAll(ListView list, String type, String status) {
		// Get all of the rows from the database and create the item list

		mBundyCursor = mDbHelper.fetchDetailByType(type, status);

		startManagingCursor(mBundyCursor);

		// Create an array to specify the fields we want to display in the list
		// (only Client and Date)
		String[] from = new String[] { ClubspendDBAdapter.KEY_TRANSACTION,
				ClubspendDBAdapter.KEY_POSTED_DATE };

		// and an array of the fields we want to bind those fields to
		int[] to = new int[] { R.id.transaction, R.id.postingDate };

		// Now create a simple cursor adapter and set it to display
		clients = new SimpleCursorAdapter(this, R.layout.transaction_row,
				mBundyCursor, from, to);

		list.setAdapter(clients);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SEARCH, 0, "Search").setIcon(R.drawable.search);
		menu.add(0, UPDATE, 0, "Update").setIcon(R.drawable.update);
		menu.add(0, BACK_MONEY_MANAGER, 0, "Money Manager").setIcon(
				R.drawable.manager);
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
		case UPDATE:
			
			pd = ProgressDialog.show(this,null, "Updating please wait...", true,
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
							Intent main = new Intent(PaymentHistory.this, PaymentHistory.class);
							mDbHelper.deleteAllDetails();
							startActivity(main);
						}
					});
					// Dismiss the Dialog

					pd.dismiss();
				}
			}.start();
			
			
			break;

		case BACK_MONEY_MANAGER:
			Intent managerArea = new Intent(this, MoneyManagerArea.class);
			startActivity(managerArea);
			break;

		case LOGOUT:
			pd = ProgressDialog.show(this, null, "Logging out...", true, false);

			new Thread() {
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (Exception e) {
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Intent logOut = new Intent(PaymentHistory.this,
									Login.class);
							logOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							// finish();
							startActivityForResult(logOut, ACTIVIY_LOGOUT);
						}
					});
					// Dismiss the Dialog

					pd.dismiss();
				}
			}.start();

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onDestroy() {
		if (mDbHelper != null && mDbHelper.isDbOpen()) {
			mDbHelper.deleteAllDetails();
			Log.d(TAG, "Row size onDestroy is: "+mDbHelper.rowSize());
			mDbHelper.close();
		}
		super.onDestroy();
	}
}
