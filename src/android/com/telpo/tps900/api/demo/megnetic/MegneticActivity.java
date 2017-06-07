package com.telpo.tps900.api.demo.megnetic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.common.sdk.TelpoException;
import com.common.sdk.TimeoutException;
import com.common.sdk.megnetic.MagneticCard;
import com.common.sdk.printer.ThermalPrinter;
import com.telpo.tps900.api.demo.R;
import com.telpo.tps900.api.demo.printer.PrinterActivity;

public class MegneticActivity extends Activity {
	private final String TAG = "MegneticActivity";
    EditText editText1;
    EditText editText2;
    EditText editText3;
	Button click;
	Button quit;
	Handler handler;
	Thread readThread;
	private final int SHOW_MSR_DATA     = 1;
	private final int CHECK_MSR_TIMEOUT = 2;
	private final int CANCLE_DIALOG     = 3;
	private ProgressDialog dialog;
	
	MagneticCard magneticCard = new MagneticCard(this);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.magnetic_main);
        editText1 = (EditText) findViewById(R.id.editText_track1);
        editText2 = (EditText) findViewById(R.id.editText_track2);
        editText3 = (EditText) findViewById(R.id.editText_track3);
        click = (Button) findViewById(R.id.button_open);
        quit = (Button) findViewById(R.id.button_quit);
        quit.setEnabled(false);
        
        dialog = new ProgressDialog(MegneticActivity.this);
        
        handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case SHOW_MSR_DATA: {
						String[] TracData = (String[])msg.obj;
			                for(int i=0; i<3; i++) {
			                    if(TracData[i] != null){
			                        switch (i)
			                        {
			                            case 0:
			                                editText1.setText(TracData[i]);
			                                break;
			                            case 1:
			                                editText2.setText(TracData[i]);
			                                break;
			                            case 2:
			                                editText3.setText(TracData[i]);
			                                break;
			                        }
			                    }
			                }
			                
			                try {
			        			magneticCard.close();
			        		} catch (TelpoException e) {
			        			e.printStackTrace();
			        		}
					}break;
					case CHECK_MSR_TIMEOUT: {
						dialog.cancel();
						Toast.makeText(MegneticActivity.this, "刷卡超时", Toast.LENGTH_LONG).show();
						quit.setEnabled(true);
						click.setEnabled(false);
						
						try {
		        			magneticCard.close();
		        		} catch (TelpoException e) {
		        			e.printStackTrace();
		        		}
					}break;
					case CANCLE_DIALOG: {
						dialog.cancel();
					}break;
					default:break;
				}
			}	
        };
        
        try {
        	magneticCard.open();
        } catch (Exception e) {
			// 打开磁条卡失败
			click.setEnabled(false);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(R.string.error);
            alertDialog.setMessage(R.string.error_open_magnetic_card);
            alertDialog.setPositiveButton(R.string.dialog_comfirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MegneticActivity.this.finish();
                }
            });
            alertDialog.show();
        }
        
        click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setTitle(getText(R.string.please));
				// 启动线程监控刷卡
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                
                dialog.setTitle(getText(R.string.waiting));
        		dialog.setMessage(getText(R.string.wait_for_msr));
        		dialog.setCancelable(false);
        		dialog.show();
                
				readThread = new ReadThread();
				readThread.start();
				click.setEnabled(false);
				quit.setEnabled(true);
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	click.setEnabled(true);
            	quit.setEnabled(false);
            	try {
				 	magneticCard.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
    }

    protected void onDestroy() {
    	try {
			magneticCard.close();
		} catch (TelpoException e) {
			e.printStackTrace();
		}
        super.onDestroy(); 
    }
    
    private class ReadThread extends Thread {
    	int status = -1;
    	String[] TracData = null;
    	
		@Override
		public void run() {
			try {
				status = magneticCard.check(10 * 1000); // 10s (超时时间)
				handler.sendMessage(handler.obtainMessage(CANCLE_DIALOG, null));
				if (0 == status) {
					Log.d(TAG, "Check MagneticCard...");
					TracData = magneticCard.read();
					handler.sendMessage(handler.obtainMessage(SHOW_MSR_DATA, TracData));
				}
			} catch (TelpoException e) {
				String exceptionStr = e.toString();
				if (exceptionStr.equals("com.common.sdk.TimeoutException")) {
					Log.d(TAG, "Check MagneticCard timeout...");
					handler.sendMessage(handler.obtainMessage(CHECK_MSR_TIMEOUT, null));
				}
				e.printStackTrace();
			}
		}
    }
}
