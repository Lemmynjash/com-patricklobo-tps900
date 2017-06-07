package com.telpo.tps900.api.demo.wakeup;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.telpo.tps900.api.demo.R;

public class WakeupActivity extends Activity {
	private final String TAG = "WakeupActivity";
	private Button wakeBtn = null;
	private Button sleepBtn = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.wakeup_main);
       
        sleepBtn = (Button) findViewById(R.id.sleep_btn);
        wakeBtn = (Button) findViewById(R.id.wake_btn);
        
        sleepBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UsbSwitch.sleep();
			}
		});
        
        wakeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UsbSwitch.wakeup();
			}
		});
    }
}
