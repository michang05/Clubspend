package com.interactiveplus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.interactiveplus.model.LoginResponse;
import com.interactiveplus.preferences.LoginPreferences;
import com.interactiveplus.preferences.RememberMePreferences;
import com.interactiveplus.webservice.LoginWS;

public class Login extends Activity implements Button.OnClickListener {

	private static final String TAG = "WSLogin";
	private static final int ACTIVIY_LOGOUT = 1;

	private CheckBox chkRemember;
	private EditText mUsername;
	private EditText mPassword;
	private Button mLogIn;
	private LoginWS loginWS;
	private ProgressDialog pd;
	private Bundle b;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mUsername = (EditText) findViewById(R.id.txtUserId);
	
		mPassword = (EditText) findViewById(R.id.txtPassword);

		chkRemember = (CheckBox) findViewById(R.id.chkRemember);

		// load the preferences
		RememberMePreferences.load(this);

		if (RememberMePreferences.getChecked()) {
			chkRemember.setChecked(true);
			mUsername.setText(RememberMePreferences.getUserName());
			mPassword.setText(RememberMePreferences.getPassword());
		}

		mUsername.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				chkRemember.setChecked(false);
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				chkRemember.setChecked(false);
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				chkRemember.setChecked(false);
				
			}
		});
		
		chkRemember.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks
				if (chkRemember.isChecked()) {

					String username = mUsername.getText().toString();
					String password = mPassword.getText().toString();

					Log.d(TAG, "u: " + username + " pass: " + password);

					if (username == null || username.equals("")) {
						username = "";
					}

					if (password == null || password.equals("")) {
						password = "";
					}

					RememberMePreferences.setUsername(username);
					RememberMePreferences.setPassword(password);
					RememberMePreferences.setChecked(true);
					RememberMePreferences.save();
					
				} else {
					
					RememberMePreferences.setChecked(false);
					RememberMePreferences.save();
				}
			}
		});
		mLogIn = (Button) findViewById(R.id.loginBtn);

		mLogIn.setOnClickListener(this);

	}

	
	private void processValues(String u, String p) {

		try {
			if (u == null || p == null || (u.equals("") && p.equals(""))) {
				Toast.makeText(this, "Username/Password cannot be blank",
						Toast.LENGTH_LONG).show();
			} else {

				loginWS = new LoginWS(u, p);
				loginWS.login();

				String cookieValue = loginWS.getCookieString();
				Log.d(TAG, "Cookie is: " + cookieValue);

				LoginResponse loginResponse = loginWS.getLoginResponse();

				Log.d(TAG, loginResponse.getNotice());

				if (loginResponse.getSuccess() == 1) {
					Toast.makeText(this, loginResponse.getNotice(),
							Toast.LENGTH_SHORT).show();
					validationAccepted(loginResponse.getId(), loginResponse
							.getBalance());
				} else if (loginResponse.getSuccess() == 0) {
					validationError(loginResponse.getNotice());
				} else {
					Toast.makeText(this, loginWS.getStatusMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		} catch (Exception e) {
			Toast.makeText(this, "Error in Connection", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}
	}

	private void validationAccepted(String id, String balance) {
		Log.d(TAG, "ID is: " + id);

		Intent homepageIntent = new Intent(this, MoneyManagerArea.class);
		b = new Bundle();
		b.putString("BALANCE", balance);
		homepageIntent.putExtras(b);

		LoginPreferences.load(this);
		LoginPreferences.setBalance(balance);
		LoginPreferences.setUsername(mUsername.getText().toString());
		LoginPreferences.setId(id);
		LoginPreferences.save();

		startActivity(homepageIntent);
	}

	private void validationError(String notice) {
		Toast.makeText(this, notice, Toast.LENGTH_LONG).show();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			pd.dismiss();

			Bundle b = msg.getData();
			processValues(b.getString("USER"), b.getString("PASS"));
		};
	};

	@Override
	public void onClick(View arg0) {
		Log.d(TAG, "CLICKED LOG IN");
		pd = ProgressDialog.show(this, "Authenticating", "Logging in...", true,
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
//						String u = mUsername.getText().toString();
//						String p = mPassword.getText().toString();
//
//						Message message = new Message();
//						Bundle data = new Bundle();
//						data.putString("USER", u);
//						data.putString("PASS", p);
//
//						message.setData(data);
//
//						handler.sendMessage(message);
						
						Intent mm = new Intent(Login.this,MoneyManagerArea.class);
						mm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(mm);
					}
				});
				// Dismiss the Dialog

				pd.dismiss();
			}
		}.start();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case ACTIVIY_LOGOUT:
			finish();
			break;
		}
	}
}