package com.telpo.tps900.api.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.telpo.tps900.api.demo.R;
import com.telpo.tps900.api.demo.iccard.PsamActivity;
import com.telpo.tps900.api.demo.iccard.SmarCardNewActivity;
import com.telpo.tps900.api.demo.idcard.IdCardActivity;
import com.telpo.tps900.api.demo.led.LedActivity;
import com.telpo.tps900.api.demo.megnetic.MegneticActivity;
import com.telpo.tps900.api.demo.nfc.NfcActivity;
import com.telpo.tps900.api.demo.printer.PrinterActivity;
import com.telpo.tps900.api.demo.wakeup.WakeupActivity;

public class MainActivity extends Activity {

	private int Oriental = -1;
	Button BnPrint, BnQRCode, psambtn, magneticCardBtn,
		rfidBtn, pcscBtn, identifyBtn, fingerBtn, hdmibtn,
		moneybox, irbtn, ledbtn, decodebtn, wakeBtn, nfcBtn,
		ledBtn,idcardBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (-1 == Oriental) {

			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Oriental = 0;
			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				Oriental = 1;
			}
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		BnPrint = (Button) findViewById(R.id.print_test);
		magneticCardBtn = (Button) findViewById(R.id.magnetic_card_btn);
		pcscBtn = (Button) findViewById(R.id.pcsc_btn);
		psambtn = (Button) findViewById(R.id.psam);
		wakeBtn = (Button) findViewById(R.id.wakeup_btn);
		nfcBtn  = (Button) findViewById(R.id.nfc_btn);
		ledBtn  = (Button) findViewById(R.id.led_btn);
		idcardBtn = (Button) findViewById(R.id.idcard_btn);
		BnPrint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, PrinterActivity.class);
				startActivity(intent);
			}
		});
		
		magneticCardBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, MegneticActivity.class);
				startActivity(intent);
			}
		});

		pcscBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SmarCardNewActivity.class);
				startActivity(intent);
			}
		});

		psambtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent psamIntent = new Intent(MainActivity.this, PsamActivity.class);
				startActivity(psamIntent);

			}
		});
		
		wakeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent psamIntent = new Intent(MainActivity.this, WakeupActivity.class);
				startActivity(psamIntent);
			}
		});
		
		nfcBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent nfcIntent = new Intent(MainActivity.this, NfcActivity.class);
				startActivity(nfcIntent);
			}
		});
		
		ledBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ledIntent = new Intent(MainActivity.this, LedActivity.class);
				startActivity(ledIntent);
			}
		});
		
		idcardBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ledIntent = new Intent(MainActivity.this, IdCardActivity.class);
				startActivity(ledIntent);
			}
		});
	}
		
	@Override
	protected void onResume() {
		super.onResume();
		setRequestedOrientation(Oriental);
	}
}
