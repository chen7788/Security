package m2m.security;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hiflying.smartlink.ISmartLinker;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.hiflying.smartlink.v7.MulticastSmartLinker;

public class CustomizedActivity extends Activity implements OnSmartLinkListener {
	
	public static final String EXTRA_SMARTLINK_VERSION = "EXTRA_SMARTLINK_VERSION";
	
	private static final String TAG = "CustomizedActivity";

	protected EditText mSsidEditText;
	protected EditText mPasswordEditText;
	protected EditText mOthersEditText;
	protected Button mStartButton;
	private View mMixedTypeRow;
	private Spinner mMixedTypeSpinner;
	protected ISmartLinker mSmartLinker;
	private boolean mIsConncting = false;
	protected Handler mViewHandler = new Handler();
	protected ProgressDialog mWaitingDialog;
	private BroadcastReceiver mWifiChangedReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int smartLinkVersion = getIntent().getIntExtra(EXTRA_SMARTLINK_VERSION, 3);
		if(smartLinkVersion == 7) {
			mSmartLinker = MulticastSmartLinker.getInstance();	
		}else {
			mSmartLinker = SnifferSmartLinker.getInstance();
		}		
		
		mWaitingDialog = new ProgressDialog(this);
		mWaitingDialog.setMessage("hiflying_smartlinker_waiting");
		mWaitingDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		mWaitingDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {

				mSmartLinker.setOnSmartLinkListener(null);
				mSmartLinker.stop();
				mIsConncting = false;
			}
		});
		
		setContentView(R.layout.activity_customized);
		mSsidEditText = (EditText) findViewById(R.id.editText_hiflying_smartlinker_ssid);
		mPasswordEditText = (EditText) findViewById(R.id.editText_hiflying_smartlinker_password);
		mOthersEditText = (EditText) findViewById(R.id.editText_hiflying_smartlinker_others);
		mStartButton = (Button) findViewById(R.id.button_hiflying_smartlinker_start);
		mSsidEditText.setText(getSSid());
		
		if (smartLinkVersion == 7) {
			mMixedTypeRow.setVisibility(View.VISIBLE);
			mMixedTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
					
					int mixType = 0;
					if (position == 0) {
						mixType = MulticastSmartLinker.MIX_TYPE_SMART_LINK_V8;
					}else if (position == 1) {
						mixType = MulticastSmartLinker.MIX_TYPE_SMART_LINK_V3;
					}
					((MulticastSmartLinker)mSmartLinker).setMixType(mixType);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}

		mStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mIsConncting){

					//设置要配置的ssid 和pswd
					try {
						mSmartLinker.setOnSmartLinkListener(CustomizedActivity.this);
						mSmartLinker.setOthers(mOthersEditText.getText().toString().trim());
						//开始 smartLink
						mSmartLinker.start(getApplicationContext(), mPasswordEditText.getText().toString().trim(), 
								mSsidEditText.getText().toString().trim());
						mIsConncting = true;
						mWaitingDialog.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		mWifiChangedReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (networkInfo != null && networkInfo.isConnected()) {
					mSsidEditText.setText(getSSid());
					mPasswordEditText.requestFocus();
					mStartButton.setEnabled(true);
				}else {
					mSsidEditText.setText("hiflying_smartlinker_no_wifi_connectivity");
					mSsidEditText.requestFocus();
					mStartButton.setEnabled(false);
					if (mWaitingDialog.isShowing()) {
						mWaitingDialog.dismiss();
					}
				}
			}
		};
		registerReceiver(mWifiChangedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mSmartLinker.setOnSmartLinkListener(null);
		try {
			unregisterReceiver(mWifiChangedReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onLinked(final SmartLinkedModule module) {
		// TODO Auto-generated method stub
		
		Log.w(TAG, "onLinked");
		mViewHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), getString(5, module.getId(), module.getMac(), module.getModuleIP()), 
						Toast.LENGTH_SHORT).show();
			}
		});
	}


	@Override
	public void onCompleted() {
		
		Log.w(TAG, "onCompleted");
		mViewHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "hiflying_smartlinker_completed", 
						Toast.LENGTH_SHORT).show();
				mWaitingDialog.dismiss();
				mIsConncting = false;
			}
		});
	}


	@Override
	public void onTimeOut() {
		
		Log.w(TAG, "onTimeOut");
		mViewHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "hiflying_smartlinker_timeout", 
						Toast.LENGTH_SHORT).show();
				mWaitingDialog.dismiss();
				mIsConncting = false;
			}
		});
	}	

	private String getSSid(){

		WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
		if(wm != null){
			WifiInfo wi = wm.getConnectionInfo();
			if(wi != null){
				String ssid = wi.getSSID();

				if(ssid.length()>2 && ssid.startsWith("\"") && ssid.endsWith("\"")){
					return ssid.substring(1,ssid.length()-1);
				}else{
					return ssid;
				}
			}
		}

		return "";
	}

}
