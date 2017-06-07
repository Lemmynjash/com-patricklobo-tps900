package com.telpo.tps900.api.demo.iccard;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.common.sdk.StringUtil;
import com.common.sdk.TelpoException;
import com.common.sdk.iccard.ICCard;
import com.telpo.tps900.api.demo.R;

public class PsamActivity extends Activity
{
	private Button readButton, buttonAPDU;
	private Button poweronButton;
	private Button poweroffButton;
	private Button openButton;
	private Button closeButton;
	private EditText mEditTextApdu;
	private TextView textReader;

	private Button psam1;
	private Button psam2;
	private Button psam3;
	
	private final String TAG = "PsamActivity";
	private static Boolean isDetect = false;
	Thread readThread;
	
	ICCard psamCard = new ICCard(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.smartcard);
		
		mEditTextApdu = (EditText) findViewById(R.id.editTextAPDU);
		textReader = (TextView) findViewById(R.id.textReader);
		psam1 = (Button) findViewById(R.id.psam1);
		psam2 = (Button) findViewById(R.id.psam2);
		psam3 = (Button) findViewById(R.id.psam3);

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
		
		isDetect = false;
		
	}
	
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.open_btn:
				new openTask().execute();
				break;

			case R.id.close_btn:
				try {
					psamCard.close();
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
					atrString = psamCard.getAtr();
				} catch (TelpoException e) {
					e.printStackTrace();
				}
				textReader.setText("ATR:" + (TextUtils.isEmpty(atrString.trim()) ? "null" : atrString));
				Toast.makeText(PsamActivity.this,
						getString(R.string.get_data_success), Toast.LENGTH_SHORT).show();
				break;

			case R.id.poweron_btn:
				if (isDetect) {
					try {
						psamCard.power_on();
					} catch (TelpoException e) {
						e.printStackTrace();
					}
					poweronButton.setEnabled(false);
					poweroffButton.setEnabled(true);
					readButton.setEnabled(true);
					buttonAPDU.setEnabled(true);
				} else {
					Toast.makeText(PsamActivity.this, 
							"Psam card power on failed", Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.poweroff_btn:
				try {
					psamCard.power_off();
				} catch (TelpoException e) {
					e.printStackTrace();
				}
				poweroffButton.setEnabled(false);
				poweronButton.setEnabled(true);
				readButton.setEnabled(false);
				buttonAPDU.setEnabled(false);
				break;
				
			case R.id.buttonAPDU:
				sendAPDUData();
				break;
				
			default:
				break;
			}
		}
	};

	public void changePsam(View view) {
		switch (view.getId()) {
		case R.id.psam1:
			psam1.setEnabled(false);
			psam2.setEnabled(true);
			try {
				psamCard.open(1);
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

		case R.id.psam2:
			psam1.setEnabled(true);
			psam2.setEnabled(false);
			try {
				psamCard.open(2);
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
		}
	}

	@Override
	protected void onDestroy() {
		try {
			psamCard.close();
		} catch (TelpoException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	private class openTask extends AsyncTask<Void, Integer, Boolean>
	{

		@Override
		protected Boolean doInBackground(Void... params)
		{
			int result = 0;
			
			try {
				psamCard.open(1);
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
				readThread = new ReadThread();
				readThread.start();
			} else {
				Toast.makeText(PsamActivity.this, 
						"Open reader failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void sendAPDUData() {
		byte[] pSendAPDU;
		byte[] result = null;
		int[] pRevAPDULen = new int[1];
		String apduStr;
		
		Log.d("sendAPDUkOnClick", "sendAPDUkOnClick");
		pRevAPDULen[0] = 300;
		apduStr = mEditTextApdu.getText().toString();
		pSendAPDU = toByteArray(apduStr);
		
		try {
			result = psamCard.transmit(pSendAPDU, pSendAPDU.length);
		} catch (TelpoException e) {
			e.printStackTrace();
		}
		
		textReader.setText(TextUtils.isEmpty(StringUtil.toHexString(result)) ? getString(R.string.send_APDU_fail) : getString(R.string.send_APDU_success) + StringUtil.toHexString(result));
		if (!TextUtils.isEmpty(StringUtil.toHexString(result))) {
			Toast.makeText(PsamActivity.this,
					getString(R.string.send_comm_success), Toast.LENGTH_SHORT).show();
		}
	}
	
	private String BCD2Str(byte[] data) {
		String string;
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i = 0; i < data.length; i++)
		{
			string = Integer.toHexString(data[i] & 0xFF);
			if (string.length() == 1)
			{
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
		if ((len % 2) == 1)
		{
			// 长度不为偶数，右补0
			str = s + "0";
			len = (len + 1) >> 1;
		}
		else 
		{
			str = s;
			len >>= 1;
		}
		
		byte[] bytes = new byte[len];
		byte high;
		byte low;
		
		for (int i = 0, j = 0; i < len; i++, j += 2)
		{
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
				status = psamCard.detect(10 * 1000); // 10s ( 超时时间)
				if (status == 0) {
					Log.d(TAG, "Check Psamcard...");
					isDetect = true;
				}
			} catch (TelpoException e) {
				e.printStackTrace();
			}
		}
    }
	
}
