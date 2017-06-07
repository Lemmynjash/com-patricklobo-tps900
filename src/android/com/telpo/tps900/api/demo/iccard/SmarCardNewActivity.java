package com.telpo.tps900.api.demo.iccard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.sdk.StringUtil;
import com.common.sdk.TelpoException;
import com.common.sdk.iccard.ICCard;
import com.telpo.tps900.api.demo.R;
import com.telpo.tps900.api.demo.megnetic.MegneticActivity;

public class SmarCardNewActivity extends Activity
{
	private Button readButton, buttonAPDU;
	private Button poweronButton;
	private Button poweroffButton;
	private Button openButton;
	private Button closeButton;
	private EditText mEditTextApdu;
	private TextView textReader;
	private LinearLayout psamlayout;
	Thread readThread;
	private final String TAG = "SmarCardNewActivity";
	private static Boolean isDetect = false;
	private static Boolean isGetAtrFail = false;
	private final int CHECK_ICCARD_TIMEOUT = 1;
	Handler handler;
	private ProgressDialog dialog;
	
	ICCard iccard = new ICCard(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.smartcard);
		
		mEditTextApdu = (EditText) findViewById(R.id.editTextAPDU);
		textReader = (TextView) findViewById(R.id.textReader);
		psamlayout = (LinearLayout) findViewById(R.id.select_psam);
		psamlayout.setVisibility(View.GONE);
		
		isDetect = false;
		isGetAtrFail = false;
		dialog = new ProgressDialog(SmarCardNewActivity.this);
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case CHECK_ICCARD_TIMEOUT: {
						Toast.makeText(SmarCardNewActivity.this, "卡片未插入超时", Toast.LENGTH_LONG).show();
						readButton.setEnabled(false);
						poweronButton.setEnabled(false);
						poweroffButton.setEnabled(false);
						closeButton.setEnabled(false);
						buttonAPDU.setEnabled(false);
						openButton.setEnabled(true);
						dialog.cancel();
						
						// 下电
						try {
							iccard.close();
						} catch (TelpoException e) {
							e.printStackTrace();
						}
					}
					default:break;
				}
			}	
        };
		
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.open_btn:
					new openTask().execute();
					break;
					
				case R.id.close_btn:
					try {
						iccard.close();
					} catch (TelpoException e) {
						e.printStackTrace();
					}
					closeButton.setEnabled(false);
					openButton.setEnabled(true);
					poweroffButton.setEnabled(false);
					poweronButton.setEnabled(false);
					readButton.setEnabled(false);
					buttonAPDU.setEnabled(false);
					textReader.setText("");
					break;
					
				case R.id.read_btn:
					String atrString = null;
					
					try {
						atrString = iccard.getAtr();
					} catch (TelpoException e) {
						e.printStackTrace();
					}
					if (TextUtils.isEmpty(atrString.trim())) {
						isGetAtrFail = true;
					} else {
						isGetAtrFail = false;
					}
					textReader.setText("ATR:" + (TextUtils.isEmpty(atrString.trim()) ? "null" : atrString));
					Toast.makeText(SmarCardNewActivity.this,
							getString(R.string.get_data_success), Toast.LENGTH_SHORT).show();
					break;
					
				case R.id.poweron_btn:
					if (isDetect) {
						boolean powerStatus = true;
						try {
							iccard.power_on();
						} catch (TelpoException e) {
							powerStatus = false;
							e.printStackTrace();
						}
						if (powerStatus) {
							poweronButton.setEnabled(false);
							poweroffButton.setEnabled(true);
							readButton.setEnabled(true);
							buttonAPDU.setEnabled(true);
						} else {
							Toast.makeText(SmarCardNewActivity.this, 
									"ICC power on failed", Toast.LENGTH_SHORT).show();
						}
						
					} else {
						Toast.makeText(SmarCardNewActivity.this, 
								"ICC power on failed", Toast.LENGTH_SHORT).show();
					}
					break;
					
				case R.id.poweroff_btn:
					try {
						iccard.power_off();
					} catch (TelpoException e) {
						e.printStackTrace();
					}
					poweroffButton.setEnabled(false);
					poweronButton.setEnabled(true);
					readButton.setEnabled(false);
					buttonAPDU.setEnabled(false);
					break;
					
				case R.id.buttonAPDU:
					if (isGetAtrFail) {
						Toast.makeText(SmarCardNewActivity.this,
								getString(R.string.send_comm_fail), Toast.LENGTH_SHORT).show();
					} else {
						sendAPDUData();
					}
					break;
				default:
					break;
				}
			}
		};		
		
		readButton = (Button)findViewById(R.id.read_btn);
		readButton.setOnClickListener(listener);
		readButton.setEnabled(false);
		poweronButton = (Button)findViewById(R.id.poweron_btn);
		poweronButton.setOnClickListener(listener);
		poweronButton.setEnabled(false);
		poweroffButton = (Button)findViewById(R.id.poweroff_btn);
		poweroffButton.setOnClickListener(listener);
		poweroffButton.setEnabled(false);
		openButton = (Button)findViewById(R.id.open_btn);
		openButton.setOnClickListener(listener);
		closeButton = (Button)findViewById(R.id.close_btn);
		closeButton.setOnClickListener(listener);
		closeButton.setEnabled(false);
		buttonAPDU = (Button) findViewById(R.id.buttonAPDU);
		buttonAPDU.setOnClickListener(listener);
		buttonAPDU.setEnabled(false);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			iccard.close();
		} catch (TelpoException e) {
			e.printStackTrace();
		}
	}

	private class openTask extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params)
		{
			int result = 0;
			
			try {
				iccard.open(0);
			} catch (TelpoException e) {
				result = -1;
				e.printStackTrace();
			}
			if (result == 0) {
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				openButton.setEnabled(false);
				closeButton.setEnabled(true);
				poweronButton.setEnabled(true);
				
				dialog.setTitle(getText(R.string.waiting));
        		dialog.setMessage(getText(R.string.wait_for_iccard));
        		dialog.setCancelable(false);
        		dialog.show();
        		
				readThread = new ReadThread();
				readThread.start();
			} else {
				Toast.makeText(SmarCardNewActivity.this, "Open reader failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void sendAPDUData() {
		byte[] pSendAPDU;
		byte[] result = null;
		int[] pRevAPDULen = new int[1];
		String apduStr;
		int iRet = 0;
		
		Log.d("sendAPDUkOnClick", "sendAPDUkOnClick");
		pRevAPDULen[0] = 300;
		apduStr = mEditTextApdu.getText().toString();
		pSendAPDU = toByteArray(apduStr);
		
		try {
			result = iccard.transmit(pSendAPDU, pSendAPDU.length);
		} catch (TelpoException e) {
			e.printStackTrace();
		}
		
		if (iRet < 0) {
			Toast.makeText(SmarCardNewActivity.this,
					getString(R.string.send_comm_success), Toast.LENGTH_SHORT).show();
		}
		
		textReader.setText(TextUtils.isEmpty(StringUtil.toHexString(result)) ? getString(R.string.send_APDU_fail) : getString(R.string.send_APDU_success) + StringUtil.toHexString(result));
		if (!TextUtils.isEmpty(StringUtil.toHexString(result))) {
			Toast.makeText(SmarCardNewActivity.this,
					getString(R.string.send_comm_success), Toast.LENGTH_SHORT).show();
		}
	}
	
	private String BCD2Str(byte[] data) {
		String string;
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i = 0; i < data.length; i++) {
			string = Integer.toHexString(data[i] & 0xFF);
			if (string.length() == 1) {
				stringBuilder.append("0");
			}
			stringBuilder.append(string.toUpperCase());
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}
	
	private byte[] str2BCD(String string) {
		int len;
		String str;
		String hexStr = "0123456789ABCDEF";
		String s = string.toUpperCase();
		
		len = s.length();
		if ((len % 2) == 1) {
			// 长度不为偶数，右补0
			str = s + "0";
			len = (len + 1) >> 1;
		} else {
			str = s;
			len >>= 1;
		}
		
		byte[] bytes = new byte[len];
		byte high;
		byte low;
		
		for (int i = 0, j = 0; i < len; i++, j += 2) {
			high = (byte)(hexStr.indexOf(str.charAt(j)) << 4);
			low = (byte)hexStr.indexOf(str.charAt(j + 1));
			bytes[i] = (byte)(high | low);
		}
		return bytes;
	}

	public static byte[] toByteArray(String hexString) {
		int hexStringLength = hexString.length();
		byte[] byteArray = null;
		int count = 0;
		char c;
		int i;

		// Count number of hex characters
		for (i = 0; i < hexStringLength; i++) {
			c = hexString.charAt(i);
			if (c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f') {
				count++;
			}
		}

		byteArray = new byte[(count + 1) / 2];
		boolean first = true;
		int len = 0;
		int value;
		for (i = 0; i < hexStringLength; i++) {
			c = hexString.charAt(i);
			if (c >= '0' && c <= '9') {
				value = c - '0';
			} else if (c >= 'A' && c <= 'F') {
				value = c - 'A' + 10;
			} else if (c >= 'a' && c <= 'f') {
				value = c - 'a' + 10;
			} else {
				value = -1;
			}

			if (value >= 0) {

				if (first) {
					byteArray[len] = (byte) (value << 4);
				} else {
					byteArray[len] |= value;
					len++;
				}
				first = !first;
			}
		}
		return byteArray;
	}
	
	private class ReadThread extends Thread {
    	int status = -1;
    	
		@Override
		public void run() {
			try {
				status = iccard.detect(10 * 1000); // 10s
				dialog.cancel();
				if (status == 0) {
					Log.d(TAG, "Check ICCard...");
					isDetect = true;
				}
			} catch (TelpoException e) {
				String exceptionStr = e.toString();
				if (exceptionStr.equals("com.common.sdk.TimeoutException")) {
					Log.d(TAG, "Check MagneticCard timeout...");
					handler.sendMessage(handler.obtainMessage(CHECK_ICCARD_TIMEOUT, null));
				}
				e.printStackTrace();
			}
		}
	}
}
