package com.telpo.tps900.api.demo.printer;

import java.io.DataOutputStream;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import com.common.sdk.TelpoException;
import com.common.sdk.printer.ThermalPrinter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.telpo.tps900.api.demo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


//import com.google.zxing.BarcodeFormat;
////import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;

/**
 * 单位 广东天波信息技术股份有限公司 功能 打印测试的处理 Created by hjx on 14-6-24.
 */
public class PrinterActivity extends Activity {  

	private  String printVersion;
	// private final int PRINTIT = 1;
	private final int ENABLE_BUTTON = 2;
	private final int NOPAPER = 3;
	private final int LOWBATTERY = 4;
	private final int PRINTVERSION = 5;
	private final int PRINTBARCODE = 6;
	private final int PRINTQRCODE = 7;
	private final int PRINTPAPERWALK = 8;
	private final int PRINTCONTENT = 9;
	private final int CANCELPROMPT = 10;
	private final int PRINTERR = 11;
	private final int OVERHEAT = 12;
	private final int MAKER = 13;
	private final int PRINTPICTURE = 14;
	private final int EXECUTECOMMAND = 15;
	private final int PRINTMIX = 16;
	private final int GB2312TESTPRINT = 17;
	private final int ASCIITESTPRINT = 18;

	private boolean stop = false;
	private static final String TAG = "ConsoleTestActivity";

	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	private Lock g_lock;

	MyHandler handler;
	private EditText editTextLeftDistance;
	// private EditText editTextRightDistance;
	private EditText editTextCharSpace;
	private EditText editTextAlign;
	private EditText editTextLineDistance;
	private EditText editTextWordFont;
	private EditText editTextPrintGray;
	private EditText editTextBarcode;
	private EditText editTextQrcode;
	private EditText editTextPaperWalk;
	private EditText editTextContent;
	private EditText edittext_maker_direction;
	private EditText edittext_maker_search_distance;
	private EditText edittext_maker_walk_distance;
	private EditText edittext_input_command;
	private Button buttonBarcodePrint;
	private Button buttonPaperWalkPrint;
	private Button buttonContentPrint;
	private Button buttonQrcodePrint;
	private Button buttonGetExampleText;
	private Button buttonGetZhExampleText;
	private Button buttonClearText;
	private Button button_maker;
	private Button button_papercut;
	private Button button_print_mix;
	private Button button_print_picture;
	private Button button_ASCII_test;
	private Button button_GB2312_test;
	private Button button_execute_command;
	private LinearLayout linearLayout;
	private TextView textPrintVersion;
	private TextView textViewGray;
	// private int printting = 0;
	private String Result;
	private Boolean nopaper = false;
	private boolean LowBattery = false;
	private boolean isClose = false;// 关闭程序

	public  String barcodeStr;
	public  String qrcodeStr;
	public  int paperWalk;
	public  String printContent;
	private int leftDistance = 0;
	private int textAlign = 0;
	private int lineDistance;
	private int charSpace;
	private int wordFont;
	private int printGray;
	private ProgressDialog progressDialog;
	private final static int MAX_LEFT_DISTANCE = 255;
	ProgressDialog dialog;

	/**
	 * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
	 * 
	 * @return 应用程序是/否获取Root权限
	 */
	public  boolean upgradeRootPermission(String pkgCodePath) {
		Process process = null;
		DataOutputStream os = null;
		try {
			String cmd = "chmod 777 " + pkgCodePath;
			process = Runtime.getRuntime().exec("su"); // 切换到root帐号
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

	ThermalPrinter thermalprinter = new ThermalPrinter(this);
	
	private class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			if (stop == true)
				return;
			switch (msg.what) {
			// case PRINTIT:
			// final ArrayList<String> rInfoList = new ArrayList<String>();
			// buttonBarcodePrint.setEnabled(false);
			// switch(printting){
			// case PRINTBARCODE:
			// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
			// new barcodePrintThread().start();
			// break;
			// case PRINTQRCODE:
			// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.D_barcode_loading),getString(R.string.generate_barcode_wait));
			// new qrcodePrintThread().start();
			// break;
			// case PRINTPAPERWALK:
			// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
			// new paperWalkPrintThread().start();
			// break;
			// case PRINTCONTENT:
			// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
			// new contentPrintThread().start();
			// break;
			// case MAKER:
			// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.maker),getString(R.string.printing_wait));
			// new MakerThread().start();
			// break;
			// case PRINTPICTURE:
			// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
			// new printPicture().start();
			// break;
			// }
			// break;
			// case ENABLE_BUTTON:
			// buttonBarcodePrint.setEnabled(true);
			// printting = 0;
			// break;
			case NOPAPER:
				noPaperDlg();
				break;
			case LOWBATTERY:
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						PrinterActivity.this);
				alertDialog.setTitle(R.string.operation_result);
				alertDialog.setMessage(getString(R.string.LowBattery));
				alertDialog.setPositiveButton(getString(R.string.dlg_ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
							}
						});
				alertDialog.show();
				break;
			case PRINTVERSION:
				dialog.dismiss();
				if (msg.obj.equals("1")) {
					textPrintVersion.setText(printVersion);
				} else {
					Toast.makeText(PrinterActivity.this,
							R.string.operation_fail, Toast.LENGTH_LONG).show();
				}
				break;
			case PRINTBARCODE:
				// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
				// printting = PRINTBARCODE;
				new barcodePrintThread().start();
				break;
			case PRINTQRCODE:
				// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.D_barcode_loading),getString(R.string.generate_barcode_wait));
				// printting = PRINTQRCODE;
				new qrcodePrintThread().start();
				break;
			case PRINTPAPERWALK:
				// printting = PRINTPAPERWALK;
				// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
				new paperWalkPrintThread().start();
				break;
			case PRINTCONTENT:
				// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
				// printting = PRINTCONTENT;
				new contentPrintThread().start();
				break;
			case MAKER:
				// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.maker),getString(R.string.printing_wait));
				// printting = MAKER;
				new MakerThread().start();
				break;
			case PRINTPICTURE:
				// progressDialog=ProgressDialog.show(PrinterActivity.this,getString(R.string.bl_dy),getString(R.string.printing_wait));
				// printting = PRINTPICTURE;
				new printPicture().start();
				break;
			case CANCELPROMPT:
				if (progressDialog != null
						&& !PrinterActivity.this.isFinishing()) {
					progressDialog.dismiss();
					progressDialog = null;
				}
				break;
			case EXECUTECOMMAND:
				new executeCommand().start();
				break;

			case PRINTMIX:
				new PintMixThread().start();
				break;
			case ASCIITESTPRINT:
				new ASCIITestPrint().start();
				break;
			case GB2312TESTPRINT:
				new  GB2312TestPrint().start();
				break;
			case OVERHEAT:
				AlertDialog.Builder overHeatDialog = new AlertDialog.Builder(
						PrinterActivity.this);
				overHeatDialog.setTitle(R.string.operation_result);
				overHeatDialog.setMessage(getString(R.string.overTemp));
				overHeatDialog.setPositiveButton(getString(R.string.dlg_ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
							}
						});
				overHeatDialog.show();
				break;
			default:
				if (msg.obj != null) {
					Toast.makeText(PrinterActivity.this, msg.obj.toString(),
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(PrinterActivity.this, "Print Error!",
							Toast.LENGTH_LONG).show();
				}

				break;
			}
		}
	}
	
	private void setRegion(final EditText edit, final int iMaxNum, final int iMinNum)
	{
		
		edit.addTextChangedListener(new TextWatcher() { 
	            @Override 
	            public void onTextChanged(CharSequence s, int start, int before, int count) { 
	                if (start > 1) 
	                { 
	                    if (iMinNum != -1 && iMaxNum != -1) 
	                    { 
	                      int num = Integer.parseInt(s.toString()); 
	                      if (num > iMaxNum) 
	                      { 
	                          s = String.valueOf(iMaxNum); 
	                          edit.setText(s); 
	                      } 
	                      else if(num < iMinNum) 
	                      {
	                          s = String.valueOf(iMinNum);
	                      }
	                      return; 
	                    } 
	                } 
	            }

	            @Override 
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) { 
	            } 

	            @Override 
	            public void afterTextChanged(Editable s) 
	            { 
	                if (s != null && !s.equals("")) 
	                { 
	                    if (iMinNum != -1 && iMaxNum != -1) 
	                    { 
	                         int markVal = 0; 
	                         try 
	                         { 
	                             markVal = Integer.parseInt(s.toString()); 
	                         } 
	                         catch (NumberFormatException e) 
	                         { 
	                             markVal = 0; 
	                         } 
	                         if (markVal > iMaxNum) 
	                         { 
	                             Toast.makeText(PrinterActivity.this,
	                            	getString(R.string.exceedMaxNum),
									Toast.LENGTH_SHORT).show();
	                             edit.setText(String.valueOf(iMaxNum)); 
	                         }
	                         if (markVal < iMinNum)
	                         {
	                             Toast.makeText(PrinterActivity.this,
	                            	getString(R.string.exceedMinNum),
									Toast.LENGTH_SHORT).show();	                        	 
	                         }
	                         return; 
	                    } 
	                 } 
	            } 
	        }); 
	}

	/**
	 * 检测设备是否正在充电(USB / AC)设备
	 * @return true / false
	 */
	private boolean checkChargeUsbOrAc()
	{
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = PrinterActivity.this.registerReceiver(null, ifilter);
		
		// 是否在充电
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		if ((status == BatteryManager.BATTERY_STATUS_CHARGING)
				|| (status == BatteryManager.BATTERY_STATUS_FULL))
		{
			if ((chargePlug == BatteryManager.BATTERY_PLUGGED_USB)
					|| (chargePlug == BatteryManager.BATTERY_PLUGGED_AC))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "ConsoleTestActivity====onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.print_main);
		setTitle("Serail Port Console");
		handler = new MyHandler();
		linearLayout = (LinearLayout) findViewById(R.id.makerLayout);
		if (Build.MODEL.equals("TPS550") || Build.MODEL.equals("TPS580")
				|| Build.MODEL.equals("TPS580C")
				|| Build.MODEL.equals("TPS586")
				|| Build.MODEL.equals("TPS550E")
				|| Build.MODEL.equals("TPS580D")
				|| Build.MODEL.equals("TPS390"))
			linearLayout.setVisibility(View.VISIBLE);
		button_papercut = (Button) findViewById(R.id.button_papercut);
		if (Build.MODEL.equals("TPS617") || Build.MODEL.equals("rk3288"))
			button_papercut.setVisibility(View.VISIBLE);
		buttonBarcodePrint = (Button) findViewById(R.id.print_barcode);
		textViewGray = (TextView) findViewById(R.id.textview_gray);
		preferences = getSharedPreferences("logoStorePreferences",
				Context.MODE_PRIVATE);
		editor = preferences.edit();

		IntentFilter pIntentFilter = new IntentFilter();
		pIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		// pIntentFilter.addAction(PRINT_VERSION_CHANGE);
		registerReceiver(printReceive, pIntentFilter);

		editTextLeftDistance = (EditText) findViewById(R.id.set_leftDistance);
		editTextCharSpace = (EditText) findViewById(R.id.set_charSpace);
		// editTextRightDistance =
		// (EditText)findViewById(R.id.set_rightDistance);
		editTextAlign = (EditText) findViewById(R.id.set_textAlign);
		
		editTextLineDistance = (EditText) findViewById(R.id.set_lineDistance);
		editTextWordFont = (EditText) findViewById(R.id.set_wordFont);
		editTextPrintGray = (EditText) findViewById(R.id.set_printGray);
		editTextBarcode = (EditText) findViewById(R.id.set_Barcode);
		editTextPaperWalk = (EditText) findViewById(R.id.set_paperWalk);
		editTextContent = (EditText) findViewById(R.id.set_content);
		textPrintVersion = (TextView) findViewById(R.id.print_version);
		editTextQrcode = (EditText) findViewById(R.id.set_Qrcode);
		edittext_maker_direction = (EditText) findViewById(R.id.edittext_maker_direction);
		edittext_maker_search_distance = (EditText) findViewById(R.id.edittext_maker_search_distance);
		edittext_maker_walk_distance = (EditText) findViewById(R.id.edittext_maker_walk_distance);
		edittext_input_command = (EditText) findViewById(R.id.edittext_input_command);
		buttonQrcodePrint = (Button) findViewById(R.id.print_qrcode);
		
		editTextLeftDistance.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRegion(editTextLeftDistance, 255, 0);
		
		editTextLineDistance.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRegion(editTextLineDistance, 255, 0);
		
		editTextWordFont.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRegion(editTextWordFont, 4, 1);
		
		editTextPrintGray.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRegion(editTextPrintGray, 7, 0);
		
		edittext_maker_direction.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRegion(edittext_maker_direction, 1, 0);
		
		edittext_maker_search_distance.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRegion(edittext_maker_search_distance, 255, 0);
		
		edittext_maker_walk_distance.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRegion(edittext_maker_walk_distance, 255, 0);
		
		editTextPaperWalk.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRegion(editTextPaperWalk, 255, 0);
		
		editTextAlign.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRegion(editTextAlign, 2, 0);
		
		editTextCharSpace.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRegion(editTextCharSpace, 255, 0);
		
		buttonQrcodePrint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String exditText = null;
				
				exditText = editTextPrintGray.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.gray_level)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				printGray = Integer.parseInt(exditText);
				
				qrcodeStr = editTextQrcode.getText().toString();
				if (qrcodeStr == null || qrcodeStr.length() == 0) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.input_print_data),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (LowBattery == true) {
					handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0,
							null));
				} else {
					if (!nopaper) {
						setTitle("QRcode Print");
						if (null == progressDialog)
						{
							progressDialog = ProgressDialog.show(
									PrinterActivity.this,
									getString(R.string.D_barcode_loading),
									getString(R.string.generate_barcode_wait));
							
						}
						handler.sendMessage(handler.obtainMessage(PRINTQRCODE,
								1, 0, null));
					} else {
						Toast.makeText(PrinterActivity.this,
								getString(R.string.ptintInit),
								Toast.LENGTH_LONG).show();
					}
				}

			}
		});

		buttonBarcodePrint.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String exditText = null;
				
				exditText = editTextPrintGray.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.gray_level)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				printGray = Integer.parseInt(exditText);
				barcodeStr = editTextBarcode.getText().toString();
				if (barcodeStr == null || barcodeStr.length() == 0) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.empty), Toast.LENGTH_LONG)
							.show();
					return;
				} else if (barcodeStr.length() < 11) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				if (LowBattery == true) {
					handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0,
							null));
				} else {

					if (!nopaper) {
						setTitle("Barcode Print");
						if (null == progressDialog)
						{
							progressDialog = ProgressDialog.show(
									PrinterActivity.this,
									getString(R.string.bl_dy),
									getString(R.string.printing_wait));
						}
						handler.sendMessage(handler.obtainMessage(PRINTBARCODE,
								1, 0, null));
					} else {
						Toast.makeText(PrinterActivity.this,
								getString(R.string.ptintInit),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		buttonPaperWalkPrint = (Button) findViewById(R.id.print_paperWalk);
		buttonPaperWalkPrint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String exditText;
				exditText = editTextPaperWalk.getText().toString();
				if (exditText == null || exditText.length() == 0) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.empty), Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (Integer.parseInt(exditText) < 1
						|| Integer.parseInt(exditText) > 255) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.walk_paper_intput_value),
							Toast.LENGTH_LONG).show();
					return;
				}
				paperWalk = Integer.parseInt(exditText);
				if (LowBattery == true) {
					handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0,
							null));
				} else {
					// progressDialog.setMessage(error_msg + "  打印中......");
					// new MainActivity.WriteThread(data).start();
					if (!nopaper) {
						setTitle("print character");
						if (null == progressDialog)
						{
							progressDialog = ProgressDialog.show(
									PrinterActivity.this,
									getString(R.string.bl_dy),
									getString(R.string.printing_wait));
						}
						handler.sendMessage(handler.obtainMessage(
								PRINTPAPERWALK, 1, 0, null));
					} else {
						Toast.makeText(PrinterActivity.this,
								getString(R.string.ptintInit),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		buttonClearText = (Button) findViewById(R.id.clearText);
		buttonClearText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editTextContent.setText("");
			}
		});
		buttonGetExampleText = (Button) findViewById(R.id.getPrintExample);
		buttonGetExampleText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = "Print Test:\n" + "Device Base Information\n"
						+ "-----------------------------\n"
						+ "Printer Version:\n" + "  V05.2.0.3\n"
						+ "Printer Gray: 3\n" + "Soft Version:\n"
						+ "  TPDemo.G50.0.Build140313\n"
						+ "Battery Level: 100%\n" + "CSQ Value: 24\n"
						+ "IMEI:86378902177527" + "\n"
						+ getString(R.string.PrintTemp1)
						+ "\n"
						+ getString(R.string.PrintTemp2)
						+ "\n"
						+ "\n\n"
						+ "Device Base Information\n"
						+ "--------------0---------------\n"
						+ "Printer Version:\n"
						+ "  V05.2.0.3\n"
						+ "Printer Gray: 3\n"
						+ "Soft Version:\n"
						+ "  TPDemo.G50.0.Build140313\n"
						+ "Battery Level: 100%\n"
						+ "CSQ Value: 24\n"
						+ "IMEI:86378902177527"
						+ "\n"
						+ getString(R.string.PrintTemp1)
						+ "\n"
						+ getString(R.string.PrintTemp2)
						+ "\n"
						+ "Device Base Information\n"
						+ "--------------1---------------\n"
						+ "Printer Version:\n"
						+ "  V05.2.0.3\n"
						+ "Printer Gray: 3\n"
						+ "Soft Version:\n"
						+ "  TPDemo.G50.0.Build140313\n"
						+ "Battery Level: 100%\n"
						+ "CSQ Value: 24\n"
						+ "IMEI:86378902177527"
						+ "\n"
						+ getString(R.string.PrintTemp1)
						+ "\n"
						+ getString(R.string.PrintTemp2)
						+ "\n"
						+ "\n\n"
						+ "Device Base Information\n"
						+ "--------------2---------------\n"
						+ "Printer Version:\n"
						+ "  V05.2.0.3\n"
						+ "Printer Gray: 3\n"
						+ "Soft Version:\n"
						+ "  TPDemo.G50.0.Build140313\n"
						+ "Battery Level: 100%\n"
						+ "CSQ Value: 24\n"
						+ "IMEI:86378902177527"
						+ "\n"
						+ getString(R.string.PrintTemp1)
						+ "\n"
						+ getString(R.string.PrintTemp2)
						+ "\n"
						+ "Device Base Information\n"
						+ "--------------3---------------\n";
				editTextContent.setText(str);
			}
		});

		buttonGetZhExampleText = (Button) findViewById(R.id.getZhPrintExample);
		buttonGetZhExampleText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String str = "             烧烤"
						+ "\n----------------------------"
						+ "\n日期：2015-01-01 16:18:20" + "\n卡号：12378945664"
						+ "\n单号：1001000000000529142"
						+ "\n----------------------------"
						+ "\n  项目    数量   单价  小计" + "\n秘制烤羊腿  1    56    56"
						+ "\n黯然牛排    2    24    48"
						+ "\n烤火鸡      2    50    100"
						+ "\n炭烧鳗鱼    1    40    40"
						+ "\n烤全羊      1    200   200"
						+ "\n荔枝树烧鸡  1    50    50" + "\n冰镇乳鸽    2    23    46"
						+ "\n秘制烤羊腿  1    56    56" + "\n黯然牛排    2    24    48"
						+ "\n烤火鸡      2    50    100"
						+ "\n炭烧鳗鱼    1    40    40"
						+ "\n烤全羊      1    200   200"
						+ "\n荔枝树烧鸡  1    50    50" + "\n冰镇乳鸽    2    23    46"
						+ "\n秘制烤羊腿  1    56    56" + "\n黯然牛排    2    24    48"
						+ "\n烤火鸡      2    50    100"
						+ "\n炭烧鳗鱼    1    40    40"
						+ "\n烤全羊      1    200   200"
						+ "\n荔枝树烧鸡  1    50    50" + "\n冰镇乳鸽    2    23    46"
						+ "\n秘制烤羊腿  1    56    56" + "\n黯然牛排    2    24    48"
						+ "\n烤火鸡      2    50    100"
						+ "\n炭烧鳗鱼    1    40    40"
						+ "\n烤全羊      1    200   200"
						+ "\n荔枝树烧鸡  1    50    50" + "\n冰镇乳鸽    2    23    46"
						+ "\n冰镇乳鸽    2    23    46" + "\n秘制烤羊腿  1    56    56"
						+ "\n黯然牛排    2    24    48"
						+ "\n烤火鸡      2    50    100"
						+ "\n炭烧鳗鱼    1    40    40"
						+ "\n烤全羊      1    200   200"
						+ "\n荔枝树烧鸡  1    50    50" + "\n冰镇乳鸽    2    23    46"
						+ "\n 合计：1000：00元" + "\n----------------------------"
						+ "\n本卡金额：10000.00" + "\n累计消费：1000.00"
						+ "\n本卡结余：9000.00" + "\n----------------------------"
						+ "\n 地址：广东省佛山市南海区桂城街道桂澜南路45号鹏瑞利广场A317.B-18号铺"
						+ "\n欢迎您的再次光临\n";
				editTextContent.setText(str);
			}
		});

		

		buttonContentPrint = (Button) findViewById(R.id.print_content);
		buttonContentPrint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String exditText;
				exditText = editTextLeftDistance.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.left_margin)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				leftDistance = Integer.parseInt(exditText);
				
				exditText = editTextLineDistance.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.row_space)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				lineDistance = Integer.parseInt(exditText);
				
				exditText = editTextCharSpace.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.char_space)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				charSpace = Integer.parseInt(exditText);
				
				exditText = editTextAlign.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.text_align)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				textAlign = Integer.parseInt(exditText);
				
				printContent = editTextContent.getText().toString();
				exditText = editTextWordFont.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.font_size)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				wordFont = Integer.parseInt(exditText);
				exditText = editTextPrintGray.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.gray_level)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				printGray = Integer.parseInt(exditText);
				if (leftDistance > MAX_LEFT_DISTANCE) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.outOfLeft), Toast.LENGTH_LONG)
							.show();
					return;
				} else if (lineDistance > 255) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.outOfLine), Toast.LENGTH_LONG)
							.show();
					return;
				} else if (wordFont > 4 || wordFont < 1) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.outOfFont), Toast.LENGTH_LONG)
							.show();
					return;
				} else if (printGray < 0 || printGray > 12) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.outOfGray), Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (printContent == null || printContent.length() == 0) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.empty), Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (LowBattery == true) {
					handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0,
							null));
				} else {
					if (!nopaper) {
						setTitle("print character");
						if (null == progressDialog)
						{
							progressDialog = ProgressDialog.show(
									PrinterActivity.this,
									getString(R.string.bl_dy),
									getString(R.string.printing_wait));
						}
						handler.sendMessage(handler.obtainMessage(PRINTCONTENT,
								1, 0, null));
					} else {
						Toast.makeText(PrinterActivity.this,
								getString(R.string.ptintInit),
								Toast.LENGTH_LONG).show();
					}
				}

			}
		});

		button_maker = (Button) findViewById(R.id.button_maker);
		button_maker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (edittext_maker_direction.getText().length() == 0
						|| edittext_maker_search_distance.getText().length() == 0
						|| edittext_maker_walk_distance.getText().length() == 0) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.maker_error), Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (Integer.parseInt(edittext_maker_direction.getText()
						.toString()) < 0
						|| Integer.parseInt(edittext_maker_direction.getText()
								.toString()) > 1) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.maker_error), Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (Integer.parseInt(edittext_maker_search_distance.getText()
						.toString()) < 0
						|| Integer.parseInt(edittext_maker_search_distance
								.getText().toString()) > 255) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.maker_error), Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (Integer.parseInt(edittext_maker_walk_distance.getText()
						.toString()) < 0
						|| Integer.parseInt(edittext_maker_walk_distance
								.getText().toString()) > 255) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.maker_error), Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (LowBattery == true) {
					handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0,
							null));
				} else {
					if (!nopaper) {
						setTitle("maker");
						if (null == progressDialog)
						{
							progressDialog = ProgressDialog.show(
									PrinterActivity.this,
									getString(R.string.maker),
									getString(R.string.printing_wait));
						}
						handler.sendMessage(handler.obtainMessage(MAKER, 1, 0,
								null));
					} else {
						Toast.makeText(PrinterActivity.this,
								getString(R.string.ptintInit),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		button_papercut.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						try {
							// ThermalPrinter.start();
							thermalprinter.reset();
							thermalprinter.paperCut();
							// ThermalPrinter.stop();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});

		button_print_picture = (Button) findViewById(R.id.button_print_picture);
		button_print_picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String exditText = null;
				
				exditText = editTextPrintGray.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.gray_level)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				printGray = Integer.parseInt(exditText);
				
				if (LowBattery == true) {
					handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0,
							null));
				} else {
					if (!nopaper) {
						setTitle("print picture");
						if (null == progressDialog)
						{
							progressDialog = ProgressDialog.show(
									PrinterActivity.this,
									getString(R.string.bl_dy),
									getString(R.string.printing_wait));
						}
						handler.sendMessage(handler.obtainMessage(PRINTPICTURE,
								1, 0, null));
					} else {
						Toast.makeText(PrinterActivity.this,
								getString(R.string.ptintInit),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
		button_ASCII_test = (Button) findViewById(R.id.ASCII_test);
		button_ASCII_test.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						PrinterActivity.this);
				alertDialog.setTitle(getString(R.string.ascii_dialog));
				alertDialog.setMessage(getString(R.string.ascii_dialog));
				alertDialog.setPositiveButton(getString(R.string.dlg_ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
								String exditText;
								exditText = editTextLeftDistance.getText()
										.toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.left_margin)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								leftDistance = Integer.parseInt(exditText);
								
								exditText = editTextLineDistance.getText()
										.toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.row_space)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								lineDistance = Integer.parseInt(exditText);
								
								exditText = editTextCharSpace.getText().toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.char_space)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								charSpace = Integer.parseInt(exditText);
								
								exditText = editTextAlign.getText().toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.text_align)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								textAlign = Integer.parseInt(exditText);
								
								printContent = editTextContent.getText()
										.toString();
								exditText = editTextWordFont.getText()
										.toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.font_size)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								wordFont = Integer.parseInt(exditText);
								exditText = editTextPrintGray.getText()
										.toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.gray_level)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								printGray = Integer.parseInt(exditText);
								if (LowBattery == true) {
									handler.sendMessage(handler.obtainMessage(
											LOWBATTERY, 1, 0, null));
								} else {
									if (!nopaper) {
										setTitle("print ascii");
										if (null == progressDialog)
										{
											progressDialog = ProgressDialog
													.show(PrinterActivity.this,
															getString(R.string.bl_dy),
															getString(R.string.printing_wait));
										}
										handler.sendMessage(handler
												.obtainMessage(ASCIITESTPRINT,
														1, 0, null));
									} else {
										Toast.makeText(PrinterActivity.this,
												getString(R.string.ptintInit),
												Toast.LENGTH_LONG).show();
									}
								}
							}
						});
				alertDialog.show();

			}
		});
		
		/*
		button_GB2312_test = (Button) findViewById(R.id.GB2312_test);
		button_GB2312_test.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						PrinterActivity.this);
				alertDialog.setTitle(getString(R.string.gb2312_dialog));
				alertDialog.setMessage(getString(R.string.gb2312_dialog));
				alertDialog.setPositiveButton(getString(R.string.dlg_ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
								String exditText;
								exditText = editTextLeftDistance.getText()
										.toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.left_margin)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								leftDistance = Integer.parseInt(exditText);
								exditText = editTextLineDistance.getText()
										.toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.row_space)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								lineDistance = Integer.parseInt(exditText);
								
								exditText = editTextCharSpace.getText().toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.char_space)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								charSpace = Integer.parseInt(exditText);
								
								exditText = editTextAlign.getText().toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.text_align)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								textAlign = Integer.parseInt(exditText);
								
								printContent = editTextContent.getText()
										.toString();
								exditText = editTextWordFont.getText()
										.toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.font_size)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								wordFont = Integer.parseInt(exditText);
								
								exditText = editTextPrintGray.getText()
										.toString();
								if (exditText == null || exditText.length() < 1) {
									Toast.makeText(
											PrinterActivity.this,
											getString(R.string.gray_level)
													+ getString(R.string.lengthNotEnougth),
											Toast.LENGTH_LONG).show();
									return;
								}
								printGray = Integer.parseInt(exditText);
								
								if (LowBattery == true) {
									handler.sendMessage(handler.obtainMessage(
											LOWBATTERY, 1, 0, null));
								} else {
									if (!nopaper) {
										setTitle("print GB2312");
										if (null == progressDialog)
										{
											progressDialog = ProgressDialog
													.show(PrinterActivity.this,
															getString(R.string.bl_dy),
															getString(R.string.printing_wait));
										}
										handler.sendMessage(handler
												.obtainMessage(GB2312TESTPRINT,
														1, 0, null));
									} else {
										Toast.makeText(PrinterActivity.this,
												getString(R.string.ptintInit),
												Toast.LENGTH_LONG).show();
									}
								}
							}
						});
				alertDialog.show();

			}
		});*/

		button_print_mix = (Button) findViewById(R.id.print_mix);
		button_print_mix.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String exditText;
				exditText = editTextLeftDistance.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.left_margin)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				leftDistance = Integer.parseInt(exditText);
				exditText = editTextLineDistance.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.row_space)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				lineDistance = Integer.parseInt(exditText);
				printContent = editTextContent.getText().toString();
				exditText = editTextWordFont.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.font_size)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				wordFont = Integer.parseInt(exditText);
				exditText = editTextPrintGray.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.gray_level)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				printGray = Integer.parseInt(exditText);
				
				exditText = editTextAlign.getText().toString();
				if (exditText == null || exditText.length() < 1) {
					Toast.makeText(
							PrinterActivity.this,
							getString(R.string.text_align)
									+ getString(R.string.lengthNotEnougth),
							Toast.LENGTH_LONG).show();
					return;
				}
				textAlign = Integer.parseInt(exditText);
				
				if (LowBattery == true) {
					handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0,
							null));
				} else {
					if (!nopaper) {
						setTitle("print GB2312");
						if (null == progressDialog)
						{
							progressDialog = ProgressDialog.show(
									PrinterActivity.this,
									getString(R.string.bl_dy),
									getString(R.string.printing_wait));
						}

						printContent = new String(
								"             烧烤"
										+ "\n----------------------------"
										+ "\n日期：2015-01-01 16:18:20"
										+ "\n卡号：12378945664"
										+ "\n单号：1001000000000529142"
										+ "\n----------------------------"
										+ "\n  项目    数量   单价  小计"
										+ "\n秘制烤羊腿  1    56    56"
										+ "\n黯然牛排    2    24    48"
										+ "\n烤火鸡      2    50    100"
										+ "\n炭烧鳗鱼    1    40    40"
										+ "\n烤全羊      1    200   200"
										+ "\n荔枝树烧鸡  1    50    50"
										+ "\n冰镇乳鸽    2    23    46"
										+ "\n----------------------------"
										+ "\nPrinter Version:\n"
										+ "  V05.2.0.3\n"
										+ "Printer Gray: 3\n"
										+ "Soft Version:\n"
										+ "  TPDemo.G50.0.Build140313\n"
										+ "Battery Level: 100%\n"
										+ "CSQ Value: 24\n"
										+ "IMEI:86378902177527"
										+ "\n"
										+ getString(R.string.PrintTemp1)
										+ "\n"
										+ getString(R.string.PrintTemp2)
										+ "\n"
										+ "\n\n"
										+ "\n广东天波信息技术股份有限公司是移动POS机、安卓POS机、台式POS终端、二维码验码终端、来访登记仪、电子渠道终端等产品专业生产加工的公司。 天波优势："
										+ "\n1、出口100多个国家和地区，并为1000多家客户提供个性化定制； "
										+ "\n2、国内集多种支付、多种应用的智能POS机研发与生产企业； "
										+ "\n3、百度O2O终端POS机的合作供应商； "
										+ "\n4、国内遍布30个省、近200个售后服务网点，24小时在线。"
										+ "\n5、在鱼龙混杂的市场，天波POS机获国内国际级别安全认证；"
										+ "\n6、天波安卓系统POS机销量列国内前三甲； 公司在发展过程中先后获得多项国家、省部级资质、荣誉： 2013年05月——成立");

						// barcodeStr = new String("01234567899");

						// qrcodeStr = new String("telpo");

						handler.sendMessage(handler.obtainMessage(PRINTMIX, 1,
								0, null));

					} else {
						Toast.makeText(PrinterActivity.this,
								getString(R.string.ptintInit),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		button_execute_command = (Button) findViewById(R.id.button_execute_command);
		button_execute_command.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (edittext_input_command.getText().toString() == null
						|| edittext_input_command.getText().toString().length() == 0) {
					Toast.makeText(PrinterActivity.this,
							getString(R.string.empty), Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (LowBattery == true) {
					handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0,
							null));
				} else {
					if (!nopaper) {
						setTitle("Execute Command");
						if (null == progressDialog)
						{
							progressDialog = ProgressDialog.show(
									PrinterActivity.this,
									getString(R.string.bl_dy),
									getString(R.string.printing_wait));
						}
//						handler.sendMessage(handler.obtainMessage(
//								EXECUTECOMMAND, 1, 0, null));
					} else {
						Toast.makeText(PrinterActivity.this,
								getString(R.string.ptintInit),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		dialog = new ProgressDialog(PrinterActivity.this);
		
		upgradeRootPermission(getPackageCodePath());
	}

	/* Called when the application resumes */
	@Override
	protected void onResume() {
		super.onResume();
		dialog.setTitle("Processing");
		dialog.setMessage("Please wait while checking driver version");
		dialog.setCancelable(false);
		dialog.show();
		
	
		g_lock = new ReentrantLock();// 锁对象 
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					thermalprinter.start();
					printVersion = thermalprinter.getVersion();
				} catch (TelpoException e) {
					e.printStackTrace();
				} finally {
					if (printVersion != null) {
						Message message = new Message();
						message.what = PRINTVERSION;
						message.obj = "1";
						handler.sendMessage(message);
					} else {
						Message message = new Message();
						message.what = PRINTVERSION;
						message.obj = "0";
						handler.sendMessage(message);
					}
				}
			}
		}).start();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try {
			thermalprinter.stop();
		} catch (TelpoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private BroadcastReceiver printReceive = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
						BatteryManager.BATTERY_STATUS_NOT_CHARGING);
				int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
				if (status != BatteryManager.BATTERY_STATUS_CHARGING) {
					if (level * 5 <= scale) {
						LowBattery = true;
					} else {
						LowBattery = false;
					}
				} else {
					LowBattery = false;
				}
			}
		}
	};

	private void noPaperDlg() {
		AlertDialog.Builder dlg = new AlertDialog.Builder(PrinterActivity.this);
		dlg.setTitle(getString(R.string.noPaper));
		dlg.setMessage(getString(R.string.noPaperNotice));
		dlg.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						// if(!nopaper) {
						// handler.sendMessage(handler.obtainMessage(PRINTIT, 1,
						// 0, null));
						// }else{
						// Toast.makeText(PrinterActivity.this,getString(R.string.ptintInit),Toast.LENGTH_LONG).show();
						// handler.sendMessage(handler.obtainMessage(ENABLE_BUTTON,
						// 1, 0, null));
						// }
					}
				});
		// dlg.setNegativeButton(R.string.dialog_cancel, new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialogInterface, int i) {
		// handler.sendMessage(handler.obtainMessage(ENABLE_BUTTON, 1, 0,
		// null));
		// }
		// });
		dlg.show();
	}

	private class paperWalkPrintThread extends Thread {
		public void run() {
			super.run();
			setName("paper walk Thread");
			try {
				g_lock.lock();
				// ThermalPrinter.start();
				thermalprinter.reset();
				thermalprinter.walkPaper(paperWalk);
			} catch (TelpoException e) {
				e.printStackTrace();
				Result = e.toString();
				if (Result.equals("com.common.sdk.thermalprinter.NoPaperException")) {
					nopaper = true;
					// return;
				} else if (Result.equals(" com.common.sdk.thermalprinter.OverHeatException")) {
					handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0,
							null));
				} else {
					handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0,
							e.getDescription()));
				}
			} finally {
				g_lock.unlock();
				handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0,
						null));
				if (nopaper)
					handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0,
							null));
				// ThermalPrinter.stop();
				nopaper = false;
				// PrinterActivity.this.sleep(1500);
				// if(progressDialog != null &&
				// !PrinterActivity.this.isFinishing() ){
				// progressDialog.dismiss();
				// progressDialog = null;
				// }
				Log.v(TAG, "The Print Progress End !!!");
				if (isClose) {
					// onDestroy();
					finish();
				}
			}
			// handler.sendMessage(handler
			// .obtainMessage(ENABLE_BUTTON, 1, 0, null));
		}
	}

	private class barcodePrintThread extends Thread {
		public void run() {
			super.run();
			setName("Barcode Print Thread");
			try {
				g_lock.lock();
				// ThermalPrinter.start();
				thermalprinter.reset();
//				ThermalPrinter.setGray(printGray);
				thermalprinter.setAlgin(ThermalPrinter.ALGIN_MIDDLE);
				thermalprinter.setGray(printGray);
				//int status = thermalprinter.checkStatus();
				printBarcode(barcodeStr);
				thermalprinter.addString(barcodeStr);
				thermalprinter.printString();
				thermalprinter.walkPaper(20);
				
			} catch (TelpoException e) {
				e.printStackTrace();
				Result = e.toString();
				if (Result
						.equals("com.common.sdk.thermalprinter.NoPaperException")) {
					nopaper = true;
					// return;
				} else if (Result
						.equals(" com.common.sdk.thermalprinter.OverHeatException")) {
					handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0,
							null));
				} else {
					handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0,
							 e.getDescription()));
				}
			} finally {
				g_lock.unlock();
				handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0,
						null));
				if (nopaper)
					handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0,
							null));
				// ThermalPrinter.stop();
				nopaper = false;
				// PrinterActivity.this.sleep(1500);
				// if(progressDialog != null &&
				// !PrinterActivity.this.isFinishing() ){
				// progressDialog.dismiss();
				// progressDialog = null;
				// }
				Log.v(TAG, "The Print Progress End !!!");
				if (isClose) {
					finish();
				}
			}
			// handler.sendMessage(handler
			// .obtainMessage(ENABLE_BUTTON, 1, 0, null));
		}
	}

	private class qrcodePrintThread extends Thread {
		public void run() {
			super.run();
			setName("Barcode Print Thread");
			try {
				g_lock.lock();
//				thermalprinter.start();
				thermalprinter.reset();
				thermalprinter.setGray(printGray);
				thermalprinter.setAlgin(ThermalPrinter.ALGIN_MIDDLE);
				printQrcode(qrcodeStr);
				thermalprinter.addString(qrcodeStr);
				thermalprinter.printString();
				thermalprinter.walkPaper(20);
			} catch (TelpoException e) {
				e.printStackTrace();
				Result = e.toString();
				if (Result
						.equals("com.common.sdk.thermalprinter.NoPaperException")) {
					nopaper = true;
					// return;
				} else if (Result
						.equals(" com.common.sdk.thermalprinter.OverHeatException")) {
					handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0,
							null));
				} else {
					handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0,
							null));
				}
			} finally {
				// lock.release();
				g_lock.unlock();
				handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0,
						null));
				if (nopaper)
					handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0,
							null));
				// ThermalPrinter.stop();
				nopaper = false;
				// PrinterActivity.this.sleep(1500);
				// if(progressDialog != null &&
				// !PrinterActivity.this.isFinishing() ){
				// progressDialog.dismiss();
				// progressDialog = null;
				// }
				Log.v(TAG, "The Print Progress End !!!");
				if (isClose) {
					finish();
				}
			}
			// handler.sendMessage(handler
			// .obtainMessage(ENABLE_BUTTON, 1, 0, null));
		}
	}

	private class contentPrintThread extends Thread {
		public void run() {
			super.run();
			setName("Content Print Thread");
			try {
				g_lock.lock();
				// ThermalPrinter.start();
				thermalprinter.reset();
				
				switch (textAlign)
				{
					case 0:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
					}break;
					case 1:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_MIDDLE);
					}break;
					case 2:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_RIGHT);
					}break;	
					default:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
					}break;
				}
				thermalprinter.setLeftIndent(leftDistance);
				thermalprinter.setLineSpace(lineDistance);
				thermalprinter.setCharSpace(charSpace);
				thermalprinter.setGray(5);
				if (wordFont == 4) {
					thermalprinter.setFontSize(2);
					//thermalprinter.enlargeFontSize(2, 2);
				} else if (wordFont == 3) {
					thermalprinter.setFontSize(1);
					//thermalprinter.enlargeFontSize(2, 2);
				} else if (wordFont == 2) {
					thermalprinter.setFontSize(2);
				} else if (wordFont == 1) {
					thermalprinter.setFontSize(1);
				}
				thermalprinter.setGray(printGray);
				thermalprinter.addString(printContent);
				thermalprinter.printString();
				thermalprinter.clearString();
				thermalprinter.walkPaper(20);
			} catch (TelpoException e) {
				e.printStackTrace();
				Result = e.toString();
				// if
				// (Result.equals("com.telpo.tps550.api.printer.NoPaperException")){
				if (Result
						.equals("com.common.sdk.thermalprinter.NoPaperException")) {
					nopaper = true;
					// return;
				} else if (Result
						.equals(" com.common.sdk.thermalprinter.OverHeatException")) {
					handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0,
							null));
				} else {
					handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0,
							e.getDescription()));
				}
			} finally {
				// lock.release();
				g_lock.unlock();
				handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0,
						null));
				if (nopaper)
					handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0,
							null));
				// ThermalPrinter.stop();
				nopaper = false;
				// PrinterActivity.this.sleep(1500);
				// if(progressDialog != null &&
				// !PrinterActivity.this.isFinishing() ){
				// progressDialog.dismiss();
				// progressDialog = null;
				// }
				Log.v(TAG, "The Print Progress End !!!");
				if (isClose) {
					// onDestroy();
					finish();
				}
			}
			// handler.sendMessage(handler
			// .obtainMessage(ENABLE_BUTTON, 1, 0, null));
		}
	}
	
	

	private class MakerThread extends Thread {

		@Override
		public void run() {
			super.run();
			setName("Maker Thread");
			try {
				g_lock.lock();
				// ThermalPrinter.start();
				thermalprinter.reset();
				thermalprinter.searchMark(
						Integer.parseInt(edittext_maker_direction.getText()
								.toString()), Integer
								.parseInt(edittext_maker_search_distance
										.getText().toString()), Integer
								.parseInt(edittext_maker_walk_distance
										.getText().toString()));
			} catch (TelpoException e) {
				e.printStackTrace();
				Result = e.toString();
				if (Result
						.equals("com.common.sdk.thermalprinter.NoPaperException")) {
					nopaper = true;
					// return;
				} else if (Result
						.equals(" com.common.sdk.thermalprinter.OverHeatException")) {
					handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0,
							null));
				} else {
					handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0,
							e.getDescription()));
				}
			} finally {
				g_lock.unlock();
				handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0,
						null));
				if (nopaper)
					handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0,
							null));
				// ThermalPrinter.stop();
				nopaper = false;
				// PrinterActivity.this.sleep(1500);
				// if(progressDialog != null &&
				// !PrinterActivity.this.isFinishing() ){
				// progressDialog.dismiss();
				// progressDialog = null;
				// }
				Log.v(TAG, "The Maker Progress End !!!");
				if (isClose) {
					finish();
				}
			}
			// handler.sendMessage(handler
			// .obtainMessage(ENABLE_BUTTON, 1, 0, null));
		}
	}

	private class printPicture extends Thread {

		@Override
		public void run() {
			super.run();
			setName("PrintPicture Thread");
			try {
				g_lock.lock();
				// ThermalPrinter.start();
				thermalprinter.reset();
				thermalprinter.setGray(printGray);
				File file = new File("/mnt/sdcard/111.bmp");
				if (file.exists()) {
					thermalprinter.printLogo(BitmapFactory
							.decodeFile("/mnt/sdcard/111.bmp"));
					// ThermalPrinter.printLogo(((BitmapDrawable)getResources().getDrawable(R.drawable.lashou)).getBitmap());
					thermalprinter.walkPaper(20);
				} else {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(PrinterActivity.this,
									getString(R.string.not_find_picture),
									Toast.LENGTH_LONG).show();
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
				Result = e.toString();
				if (Result
						.equals("com.common.sdk.thermalprinter.NoPaperException")) {
					nopaper = true;
					// return;
				} else if (Result
						.equals(" com.common.sdk.thermalprinter.OverHeatException")) {
					handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0,
							null));
				} else {
					handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0,
							e.getMessage()));
				}
			} finally {
				g_lock.unlock();
				handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0,
						null));
				if (nopaper)
					handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0,
							null));
				// ThermalPrinter.stop();
				nopaper = false;
				// PrinterActivity.this.sleep(1500);
				// if(progressDialog != null &&
				// !PrinterActivity.this.isFinishing() ){
				// progressDialog.dismiss();
				// progressDialog = null;
				// }
				Log.v(TAG, "The PrintPicture Progress End !!!");
				if (isClose) {
					finish();
				}
			}
			// handler.sendMessage(handler
			// .obtainMessage(ENABLE_BUTTON, 1, 0, null));
		}
	}

	
	private class ASCIITestPrint extends Thread {
		public void run() {
			super.run();
			setName("ASCII Test Print Thread");
			try {
				g_lock.lock();
				// ThermalPrinter.start();
				thermalprinter.reset();
				switch (textAlign)
				{
					case 0:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
					}break;
					case 1:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_MIDDLE);
					}break;
					case 2:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_RIGHT);
					}break;	
					default:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
					}break;
				}
				thermalprinter.setLeftIndent(leftDistance);
				thermalprinter.setLineSpace(lineDistance);
				thermalprinter.setCharSpace(charSpace);
				if (wordFont == 4) {
					thermalprinter.setFontSize(2);
					thermalprinter.enlargeFontSize(2, 2);
				} else if (wordFont == 3) {
					thermalprinter.setFontSize(1);
					thermalprinter.enlargeFontSize(2, 2);
				} else if (wordFont == 2) {
					thermalprinter.setFontSize(2);
				} else if (wordFont == 1) {
					thermalprinter.setFontSize(1);
				}
				thermalprinter.setGray(printGray);

				String strtmp;
				strtmp = new String("!\"#$%&"
								   +"'()*+,"
								   +"-./012"
								   +"345678"
								   +"9:;<=>"
								   +"?@ABCD"
								   +"EFGHIJ"
								   +"KLMNOP"
								   +"QRSTUV"
								   +"WXYZ[\\"
								   +"]^_`ab"
								   +"cdefgh"
								   +"ijklmn"
								   +"opqrst"
								   +"uvwxyz"
								   +"{|}~");
									thermalprinter.addString(strtmp);
									thermalprinter.printString();
									thermalprinter.clearString();
									thermalprinter.walkPaper(20);
								} catch (TelpoException e) {
									e.printStackTrace();
									Result = e.toString();
									if (Result
											.equals("com.common.sdk.thermalprinter.NoPaperException")) {
										nopaper = true;
										// return;
									} else if (Result
											.equals(" com.common.sdk.thermalprinter.OverHeatException")) {
										handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0,
												null));
									} else {
										handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0,
												e.getDescription()));
									}
								} finally {
									// lock.release();
									g_lock.unlock();
									handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0,
											null));
									if (nopaper)
										handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0,
												null));
									// ThermalPrinter.stop();
									nopaper = false;
									// PrinterActivity.this.sleep(1500);
									// if(progressDialog != null &&
									// !PrinterActivity.this.isFinishing() ){
									// progressDialog.dismiss();
									// progressDialog = null;
									// }
									Log.v(TAG, "The Print Progress End !!!");
									if (isClose) {
										// onDestroy();
										finish();
									}
								}
								// handler.sendMessage(handler
								// .obtainMessage(ENABLE_BUTTON, 1, 0, null));
							}
						}
			
			
		

	
	private class GB2312TestPrint extends Thread {
		public void run() {
			super.run();
			setName("GB2312 Test Print Thread");
			try {
				g_lock.lock();
				// ThermalPrinter.start();
				thermalprinter.reset();
				switch (textAlign)
				{
					case 0:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
					}break;
					case 1:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_MIDDLE);
					}break;
					case 2:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_RIGHT);
					}break;	
					default:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
					}break;
				}
				thermalprinter.setLeftIndent(leftDistance);
				thermalprinter.setLineSpace(lineDistance);
				thermalprinter.setCharSpace(charSpace);
				if (wordFont == 4) {
					thermalprinter.setFontSize(2);
					thermalprinter.enlargeFontSize(2, 2);
				} else if (wordFont == 3) {
					thermalprinter.setFontSize(1);
					thermalprinter.enlargeFontSize(2, 2);
				} else if (wordFont == 2) {
					thermalprinter.setFontSize(2);
				} else if (wordFont == 1) {
					thermalprinter.setFontSize(1);
				}
				thermalprinter.setGray(printGray);

				String strtmp;
				strtmp = new String("\n01 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 　 、 。 · ˉ ˇ ¨ 〃 々" + "\n１ — ～ ‖ … ‘ ’ “ ” 〔 〕"
						+ "\n２ 〈 〉 《 》 「 」 『 』 〖 〗" + "\n３ 【 】 ± × ÷ ∶ ∧ ∨ ∑ ∏"
						+ "\n４ ∪ ∩ ∈ ∷ √ ⊥ ∥ ∠ ⌒ ⊙" + "\n５ ∫ ∮ ≡ ≌ ≈ ∽ ∝ ≠ ≮ ≯"
						+ "\n６ ≤ ≥ ∞ ∵ ∴ ♂ ♀ ° ′ ″" + "\n７ ℃ ＄ ¤ ￠ ￡ ‰ § № ☆ ★"
						+ "\n８ ○ ● ◎ ◇ ◆ □ ■ △ ▲ ※"
						+ "\n９ → ← ↑ ↓ 〓               "
						+ "\n                                "
						+ "\n02 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 ⅰ ⅱ ⅲ ⅳ ⅴ ⅵ ⅶ ⅷ ⅸ" + "\n１ ⅹ       ⒈ ⒉ ⒊"
						+ "\n２ ⒋ ⒌ ⒍ ⒎ ⒏ ⒐ ⒑ ⒒ ⒓ ⒔" + "\n３ ⒕ ⒖ ⒗ ⒘ ⒙ ⒚ ⒛ ⑴ ⑵ ⑶"
						+ "\n４ ⑷ ⑸ ⑹ ⑺ ⑻ ⑼ ⑽ ⑾ ⑿ ⒀" + "\n５ ⒁ ⒂ ⒃ ⒄ ⒅ ⒆ ⒇ ① ② ③"
						+ "\n６ ④ ⑤ ⑥ ⑦ ⑧ ⑨ ⑩   ㈠" + "\n７ ㈡ ㈢ ㈣ ㈤ ㈥ ㈦ ㈧ ㈨ ㈩ "
						+ "\n８  Ⅰ Ⅱ Ⅲ Ⅳ Ⅴ Ⅵ Ⅶ Ⅷ Ⅸ"
						+ "\n９ Ⅹ Ⅺ Ⅻ                 "
						+ "\n                                "
						+ "\n03 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 ！ ＂ ＃ ￥ ％ ＆ ＇ （ ）" + "\n１ ＊ ＋ ， － ． ／ ０ １ ２ ３"
						+ "\n２ ４ ５ ６ ７ ８ ９ ： ； ＜ ＝" + "\n３ ＞ ？ ＠ Ａ Ｂ Ｃ Ｄ Ｅ Ｆ Ｇ"
						+ "\n４ Ｈ Ｉ Ｊ Ｋ Ｌ Ｍ Ｎ Ｏ Ｐ Ｑ" + "\n５ Ｒ Ｓ Ｔ Ｕ Ｖ Ｗ Ｘ Ｙ Ｚ ［"
						+ "\n６ ＼ ］ ＾ ＿ ｀ ａ ｂ ｃ ｄ ｅ" + "\n７ ｆ ｇ ｈ ｉ ｊ ｋ ｌ ｍ ｎ ｏ"
						+ "\n８ ｐ ｑ ｒ ｓ ｔ ｕ ｖ ｗ ｘ ｙ"
						+ "\n９ ｚ ｛ ｜ ｝ ￣               "
						+ "\n                                "
						+ "\n04 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 ぁ あ ぃ い ぅ う ぇ え ぉ" + "\n１ お か が き ぎ く ぐ け げ こ"
						+ "\n２ ご さ ざ し じ す ず せ ぜ そ" + "\n３ ぞ た だ ち ぢ っ つ づ て で"
						+ "\n４ と ど な に ぬ ね の は ば ぱ" + "\n５ ひ び ぴ ふ ぶ ぷ へ べ ぺ ほ"
						+ "\n６ ぼ ぽ ま み む め も ゃ や ゅ" + "\n７ ゆ ょ よ ら り る れ ろ ゎ わ"
						+ "\n８ ゐ ゑ を ん      "
						+ "\n９                    "
						+ "\n                                "
						+ "\n05 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 ァ ア ィ イ ゥ ウ ェ エ ォ" + "\n１ オ カ ガ キ ギ ク グ ケ ゲ コ"
						+ "\n２ ゴ サ ザ シ ジ ス ズ セ ゼ ソ" + "\n３ ゾ タ ダ チ ヂ ッ ツ ヅ テ デ"
						+ "\n４ ト ド ナ ニ ヌ ネ ノ ハ バ パ" + "\n５ ヒ ビ ピ フ ブ プ ヘ ベ ペ ホ"
						+ "\n６ ボ ポ マ ミ ム メ モ ャ ヤ ュ" + "\n７ ユ ョ ヨ ラ リ ル レ ロ ヮ ワ"
						+ "\n８ ヰ ヱ ヲ ン ヴ ヵ ヶ   "
						+ "\n９                    "
						+ "\n                                "
						+ "\n06 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 Α Β Γ Δ Ε Ζ Η Θ Ι" + "\n１ Κ Λ Μ Ν Ξ Ο Π Ρ Σ Τ"
						+ "\n２ Υ Φ Χ Ψ Ω     " + "\n３    α β γ δ ε ζ η"
						+ "\n４ θ ι κ λ μ ν ξ ο π ρ" + "\n５ σ τ υ φ χ ψ ω   "
						+ "\n６     ︵ ︶ ︹ ︺ ︿ ﹀" + "\n７ ︽ ︾ ﹁ ﹂ ﹃ ﹄   ︻ ︼"
						+ "\n８ ︷ ︸ ︱  ︳ ︴    "
						+ "\n９                    "
						+ "\n                                "
						+ "\n07 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 А Б В Г Д Е Ё Ж З" + "\n１ И Й К Л М Н О П Р С"
						+ "\n２ Т У Ф Х Ц Ч Ш Щ Ъ Ы" + "\n３ Ь Э Ю Я      "
						+ "\n４          а" + "\n５ б в г д е ё ж з и й"
						+ "\n６ к л м н о п р с т у" + "\n７ ф х ц ч ш щ ъ ы ь э"
						+ "\n８ ю я        "
						+ "\n９                    "
						+ "\n                                "
						+ "\n08 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 ā á ǎ à ē é ě è ī" + "\n１ í ǐ ì ō ó ǒ ò ū ú ǔ"
						+ "\n２ ù ǖ ǘ ǚ ǜ ü ê ɑ  ń" + "\n３ ň  ɡ     ㄅ ㄆ ㄇ"
						+ "\n４ ㄈ ㄉ ㄊ ㄋ ㄌ ㄍ ㄎ ㄏ ㄐ ㄑ" + "\n５ ㄒ ㄓ ㄔ ㄕ ㄖ ㄗ ㄘ ㄙ ㄚ ㄛ"
						+ "\n６ ㄜ ㄝ ㄞ ㄟ ㄠ ㄡ ㄢ ㄣ ㄤ ㄥ" + "\n７ ㄦ ㄧ ㄨ ㄩ      "
						+ "\n８          "
						+ "\n９                    "
						+ "\n                                "
						+ "\n09 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　    ─ ━ │ ┃ ┄ ┅" + "\n１ ┆ ┇ ┈ ┉ ┊ ┋ ┌ ┍ ┎ ┏"
						+ "\n２ ┐ ┑ ┒ ┓ └ ┕ ┖ ┗ ┘ ┙" + "\n３ ┚ ┛ ├ ┝ ┞ ┟ ┠ ┡ ┢ ┣"
						+ "\n４ ┤ ┥ ┦ ┧ ┨ ┩ ┪ ┫ ┬ ┭" + "\n５ ┮ ┯ ┰ ┱ ┲ ┳ ┴ ┵ ┶ ┷"
						+ "\n６ ┸ ┹ ┺ ┻ ┼ ┽ ┾ ┿ ╀ ╁" + "\n７ ╂ ╃ ╄ ╅ ╆ ╇ ╈ ╉ ╊ ╋"
						+ "\n８          "
						+ "\n９                    "
						+ "\n                                "
						+ "\n10-15区为空                     "
						+ "\n                                "
						+ "\n16 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 啊 阿 埃 挨 哎 唉 哀 皑 癌" + "\n１ 蔼 矮 艾 碍 爱 隘 鞍 氨 安 俺"
						+ "\n２ 按 暗 岸 胺 案 肮 昂 盎 凹 敖" + "\n３ 熬 翱 袄 傲 奥 懊 澳 芭 捌 扒"
						+ "\n４ 叭 吧 笆 八 疤 巴 拔 跋 靶 把" + "\n５ 耙 坝 霸 罢 爸 白 柏 百 摆 佰"
						+ "\n６ 败 拜 稗 斑 班 搬 扳 般 颁 板" + "\n７ 版 扮 拌 伴 瓣 半 办 绊 邦 帮"
						+ "\n８ 梆 榜 膀 绑 棒 磅 蚌 镑 傍 谤"
						+ "\n９ 苞 胞 包 褒 剥               "
						+ "\n                                "
						+ "\n17 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 薄 雹 保 堡 饱 宝 抱 报 暴" + "\n１ 豹 鲍 爆 杯 碑 悲 卑 北 辈 背"
						+ "\n２ 贝 钡 倍 狈 备 惫 焙 被 奔 苯" + "\n３ 本 笨 崩 绷 甭 泵 蹦 迸 逼 鼻"
						+ "\n４ 比 鄙 笔 彼 碧 蓖 蔽 毕 毙 毖" + "\n５ 币 庇 痹 闭 敝 弊 必 辟 壁 臂"
						+ "\n６ 避 陛 鞭 边 编 贬 扁 便 变 卞" + "\n７ 辨 辩 辫 遍 标 彪 膘 表 鳖 憋"
						+ "\n８ 别 瘪 彬 斌 濒 滨 宾 摈 兵 冰"
						+ "\n９ 柄 丙 秉 饼 炳               "
						+ "\n                                "
						+ "\n18 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 病 并 玻 菠 播 拨 钵 波 博" + "\n１ 勃 搏 铂 箔 伯 帛 舶 脖 膊 渤"
						+ "\n２ 泊 驳 捕 卜 哺 补 埠 不 布 步" + "\n３ 簿 部 怖 擦 猜 裁 材 才 财 睬"
						+ "\n４ 踩 采 彩 菜 蔡 餐 参 蚕 残 惭" + "\n５ 惨 灿 苍 舱 仓 沧 藏 操 糙 槽"
						+ "\n６ 曹 草 厕 策 侧 册 测 层 蹭 插" + "\n７ 叉 茬 茶 查 碴 搽 察 岔 差 诧"
						+ "\n８ 拆 柴 豺 搀 掺 蝉 馋 谗 缠 铲"
						+ "\n９ 产 阐 颤 昌 猖               "
						+ "\n                                "
						+ "\n19 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 场 尝 常 长 偿 肠 厂 敞 畅" + "\n１ 唱 倡 超 抄 钞 朝 嘲 潮 巢 吵"
						+ "\n２ 炒 车 扯 撤 掣 彻 澈 郴 臣 辰" + "\n３ 尘 晨 忱 沉 陈 趁 衬 撑 称 城"
						+ "\n４ 橙 成 呈 乘 程 惩 澄 诚 承 逞" + "\n５ 骋 秤 吃 痴 持 匙 池 迟 弛 驰"
						+ "\n６ 耻 齿 侈 尺 赤 翅 斥 炽 充 冲" + "\n７ 虫 崇 宠 抽 酬 畴 踌 稠 愁 筹"
						+ "\n８ 仇 绸 瞅 丑 臭 初 出 橱 厨 躇"
						+ "\n９ 锄 雏 滁 除 楚               ");
				thermalprinter.addString(strtmp);
				thermalprinter.printString();
				thermalprinter.clearString();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				strtmp = new String("\n                                "
						+ "\n20 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 础 储 矗 搐 触 处 揣 川 穿" + "\n１ 椽 传 船 喘 串 疮 窗 幢 床 闯"
						+ "\n２ 创 吹 炊 捶 锤 垂 春 椿 醇 唇" + "\n３ 淳 纯 蠢 戳 绰 疵 茨 磁 雌 辞"
						+ "\n４ 慈 瓷 词 此 刺 赐 次 聪 葱 囱" + "\n５ 匆 从 丛 凑 粗 醋 簇 促 蹿 篡"
						+ "\n６ 窜 摧 崔 催 脆 瘁 粹 淬 翠 村" + "\n７ 存 寸 磋 撮 搓 措 挫 错 搭 达"
						+ "\n８ 答 瘩 打 大 呆 歹 傣 戴 带 殆"
						+ "\n９ 代 贷 袋 待 逮               "
						+ "\n                                "
						+ "\n21 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 怠 耽 担 丹 单 郸 掸 胆 旦" + "\n１ 氮 但 惮 淡 诞 弹 蛋 当 挡 党"
						+ "\n２ 荡 档 刀 捣 蹈 倒 岛 祷 导 到" + "\n３ 稻 悼 道 盗 德 得 的 蹬 灯 登"
						+ "\n４ 等 瞪 凳 邓 堤 低 滴 迪 敌 笛" + "\n５ 狄 涤 翟 嫡 抵 底 地 蒂 第 帝"
						+ "\n６ 弟 递 缔 颠 掂 滇 碘 点 典 靛" + "\n７ 垫 电 佃 甸 店 惦 奠 淀 殿 碉"
						+ "\n８ 叼 雕 凋 刁 掉 吊 钓 调 跌 爹"
						+ "\n９ 碟 蝶 迭 谍 叠               "
						+ "\n                                "
						+ "\n22 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 丁 盯 叮 钉 顶 鼎 锭 定 订" + "\n１ 丢 东 冬 董 懂 动 栋 侗 恫 冻"
						+ "\n２ 洞 兜 抖 斗 陡 豆 逗 痘 都 督" + "\n３ 毒 犊 独 读 堵 睹 赌 杜 镀 肚"
						+ "\n４ 度 渡 妒 端 短 锻 段 断 缎 堆" + "\n５ 兑 队 对 墩 吨 蹲 敦 顿 囤 钝"
						+ "\n６ 盾 遁 掇 哆 多 夺 垛 躲 朵 跺" + "\n７ 舵 剁 惰 堕 蛾 峨 鹅 俄 额 讹"
						+ "\n８ 娥 恶 厄 扼 遏 鄂 饿 恩 而 儿"
						+ "\n９ 耳 尔 饵 洱 二               "
						+ "\n                                "
						+ "\n23 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 贰 发 罚 筏 伐 乏 阀 法 珐" + "\n１ 藩 帆 番 翻 樊 矾 钒 繁 凡 烦"
						+ "\n２ 反 返 范 贩 犯 饭 泛 坊 芳 方" + "\n３ 肪 房 防 妨 仿 访 纺 放 菲 非"
						+ "\n４ 啡 飞 肥 匪 诽 吠 肺 废 沸 费" + "\n５ 芬 酚 吩 氛 分 纷 坟 焚 汾 粉"
						+ "\n６ 奋 份 忿 愤 粪 丰 封 枫 蜂 峰" + "\n７ 锋 风 疯 烽 逢 冯 缝 讽 奉 凤"
						+ "\n８ 佛 否 夫 敷 肤 孵 扶 拂 辐 幅"
						+ "\n９ 氟 符 伏 俘 服               "
						+ "\n                                "
						+ "\n24 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 浮 涪 福 袱 弗 甫 抚 辅 俯" + "\n１ 釜 斧 脯 腑 府 腐 赴 副 覆 赋"
						+ "\n２ 复 傅 付 阜 父 腹 负 富 讣 附" + "\n３ 妇 缚 咐 噶 嘎 该 改 概 钙 盖"
						+ "\n４ 溉 干 甘 杆 柑 竿 肝 赶 感 秆" + "\n５ 敢 赣 冈 刚 钢 缸 肛 纲 岗 港"
						+ "\n６ 杠 篙 皋 高 膏 羔 糕 搞 镐 稿" + "\n７ 告 哥 歌 搁 戈 鸽 胳 疙 割 革"
						+ "\n８ 葛 格 蛤 阁 隔 铬 个 各 给 根"
						+ "\n９ 跟 耕 更 庚 羹               "
						+ "\n                                "
						+ "\n25 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 埂 耿 梗 工 攻 功 恭 龚 供" + "\n１ 躬 公 宫 弓 巩 汞 拱 贡 共 钩"
						+ "\n２ 勾 沟 苟 狗 垢 构 购 够 辜 菇" + "\n３ 咕 箍 估 沽 孤 姑 鼓 古 蛊 骨"
						+ "\n４ 谷 股 故 顾 固 雇 刮 瓜 剐 寡" + "\n５ 挂 褂 乖 拐 怪 棺 关 官 冠 观"
						+ "\n６ 管 馆 罐 惯 灌 贯 光 广 逛 瑰" + "\n７ 规 圭 硅 归 龟 闺 轨 鬼 诡 癸"
						+ "\n８ 桂 柜 跪 贵 刽 辊 滚 棍 锅 郭"
						+ "\n９ 国 果 裹 过 哈               "
						+ "\n                                "
						+ "\n26 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 骸 孩 海 氦 亥 害 骇 酣 憨" + "\n１ 邯 韩 含 涵 寒 函 喊 罕 翰 撼"
						+ "\n２ 捍 旱 憾 悍 焊 汗 汉 夯 杭 航" + "\n３ 壕 嚎 豪 毫 郝 好 耗 号 浩 呵"
						+ "\n４ 喝 荷 菏 核 禾 和 何 合 盒 貉" + "\n５ 阂 河 涸 赫 褐 鹤 贺 嘿 黑 痕"
						+ "\n６ 很 狠 恨 哼 亨 横 衡 恒 轰 哄" + "\n７ 烘 虹 鸿 洪 宏 弘 红 喉 侯 猴"
						+ "\n８ 吼 厚 候 后 呼 乎 忽 瑚 壶 葫"
						+ "\n９ 胡 蝴 狐 糊 湖               "
						+ "\n                                "
						+ "\n27 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 弧 虎 唬 护 互 沪 户 花 哗" + "\n１ 华 猾 滑 画 划 化 话 槐 徊 怀"
						+ "\n２ 淮 坏 欢 环 桓 还 缓 换 患 唤" + "\n３ 痪 豢 焕 涣 宦 幻 荒 慌 黄 磺"
						+ "\n４ 蝗 簧 皇 凰 惶 煌 晃 幌 恍 谎" + "\n５ 灰 挥 辉 徽 恢 蛔 回 毁 悔 慧"
						+ "\n６ 卉 惠 晦 贿 秽 会 烩 汇 讳 诲" + "\n７ 绘 荤 昏 婚 魂 浑 混 豁 活 伙"
						+ "\n８ 火 获 或 惑 霍 货 祸 击 圾 基"
						+ "\n９ 机 畸 稽 积 箕               "
						+ "\n                                "
						+ "\n28 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 肌 饥 迹 激 讥 鸡 姬 绩 缉" + "\n１ 吉 极 棘 辑 籍 集 及 急 疾 汲"
						+ "\n２ 即 嫉 级 挤 几 脊 己 蓟 技 冀" + "\n３ 季 伎 祭 剂 悸 济 寄 寂 计 记"
						+ "\n４ 既 忌 际 妓 继 纪 嘉 枷 夹 佳" + "\n５ 家 加 荚 颊 贾 甲 钾 假 稼 价"
						+ "\n６ 架 驾 嫁 歼 监 坚 尖 笺 间 煎" + "\n７ 兼 肩 艰 奸 缄 茧 检 柬 碱 硷"
						+ "\n８ 拣 捡 简 俭 剪 减 荐 槛 鉴 践"
						+ "\n９ 贱 见 键 箭 件               "
						+ "\n                                "
						+ "\n29 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 健 舰 剑 饯 渐 溅 涧 建 僵" + "\n１ 姜 将 浆 江 疆 蒋 桨 奖 讲 匠"
						+ "\n２ 酱 降 蕉 椒 礁 焦 胶 交 郊 浇" + "\n３ 骄 娇 嚼 搅 铰 矫 侥 脚 狡 角"
						+ "\n４ 饺 缴 绞 剿 教 酵 轿 较 叫 窖" + "\n５ 揭 接 皆 秸 街 阶 截 劫 节 桔"
						+ "\n６ 杰 捷 睫 竭 洁 结 解 姐 戒 藉" + "\n７ 芥 界 借 介 疥 诫 届 巾 筋 斤"
						+ "\n８ 金 今 津 襟 紧 锦 仅 谨 进 靳"
						+ "\n９ 晋 禁 近 烬 浸               "
						+ "\n                                ");
				thermalprinter.addString(strtmp);
				thermalprinter.printString();
				thermalprinter.clearString();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				strtmp = new String("\n30 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 尽 劲 荆 兢 茎 睛 晶 鲸 京" + "\n１ 惊 精 粳 经 井 警 景 颈 静 境"
						+ "\n２ 敬 镜 径 痉 靖 竟 竞 净 炯 窘" + "\n３ 揪 究 纠 玖 韭 久 灸 九 酒 厩"
						+ "\n４ 救 旧 臼 舅 咎 就 疚 鞠 拘 狙" + "\n５ 疽 居 驹 菊 局 咀 矩 举 沮 聚"
						+ "\n６ 拒 据 巨 具 距 踞 锯 俱 句 惧" + "\n７ 炬 剧 捐 鹃 娟 倦 眷 卷 绢 撅"
						+ "\n８ 攫 抉 掘 倔 爵 觉 决 诀 绝 均"
						+ "\n９ 菌 钧 军 君 峻               "
						+ "\n                                "
						+ "\n31 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 俊 竣 浚 郡 骏 喀 咖 卡 咯" + "\n１ 开 揩 楷 凯 慨 刊 堪 勘 坎 砍"
						+ "\n２ 看 康 慷 糠 扛 抗 亢 炕 考 拷" + "\n３ 烤 靠 坷 苛 柯 棵 磕 颗 科 壳"
						+ "\n４ 咳 可 渴 克 刻 客 课 肯 啃 垦" + "\n５ 恳 坑 吭 空 恐 孔 控 抠 口 扣"
						+ "\n６ 寇 枯 哭 窟 苦 酷 库 裤 夸 垮" + "\n７ 挎 跨 胯 块 筷 侩 快 宽 款 匡"
						+ "\n８ 筐 狂 框 矿 眶 旷 况 亏 盔 岿"
						+ "\n９ 窥 葵 奎 魁 傀               "
						+ "\n                                "
						+ "\n32 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 馈 愧 溃 坤 昆 捆 困 括 扩" + "\n１ 廓 阔 垃 拉 喇 蜡 腊 辣 啦 莱"
						+ "\n２ 来 赖 蓝 婪 栏 拦 篮 阑 兰 澜" + "\n３ 谰 揽 览 懒 缆 烂 滥 琅 榔 狼"
						+ "\n４ 廊 郎 朗 浪 捞 劳 牢 老 佬 姥" + "\n５ 酪 烙 涝 勒 乐 雷 镭 蕾 磊 累"
						+ "\n６ 儡 垒 擂 肋 类 泪 棱 楞 冷 厘" + "\n７ 梨 犁 黎 篱 狸 离 漓 理 李 里"
						+ "\n８ 鲤 礼 莉 荔 吏 栗 丽 厉 励 砾"
						+ "\n９ 历 利 傈 例 俐               "
						+ "\n                                "
						+ "\n33 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 痢 立 粒 沥 隶 力 璃 哩 俩" + "\n１ 联 莲 连 镰 廉 怜 涟 帘 敛 脸"
						+ "\n２ 链 恋 炼 练 粮 凉 梁 粱 良 两" + "\n３ 辆 量 晾 亮 谅 撩 聊 僚 疗 燎"
						+ "\n４ 寥 辽 潦 了 撂 镣 廖 料 列 裂" + "\n５ 烈 劣 猎 琳 林 磷 霖 临 邻 鳞"
						+ "\n６ 淋 凛 赁 吝 拎 玲 菱 零 龄 铃" + "\n７ 伶 羚 凌 灵 陵 岭 领 另 令 溜"
						+ "\n８ 琉 榴 硫 馏 留 刘 瘤 流 柳 六"
						+ "\n９ 龙 聋 咙 笼 窿               "
						+ "\n                                "
						+ "\n34 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 隆 垄 拢 陇 楼 娄 搂 篓 漏" + "\n１ 陋 芦 卢 颅 庐 炉 掳 卤 虏 鲁"
						+ "\n２ 麓 碌 露 路 赂 鹿 潞 禄 录 陆" + "\n３ 戮 驴 吕 铝 侣 旅 履 屡 缕 虑"
						+ "\n４ 氯 律 率 滤 绿 峦 挛 孪 滦 卵" + "\n５ 乱 掠 略 抡 轮 伦 仑 沦 纶 论"
						+ "\n６ 萝 螺 罗 逻 锣 箩 骡 裸 落 洛" + "\n７ 骆 络 妈 麻 玛 码 蚂 马 骂 嘛"
						+ "\n８ 吗 埋 买 麦 卖 迈 脉 瞒 馒 蛮"
						+ "\n９ 满 蔓 曼 慢 漫               "
						+ "\n                                "
						+ "\n35 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 谩 芒 茫 盲 氓 忙 莽 猫 茅" + "\n１ 锚 毛 矛 铆 卯 茂 冒 帽 貌 贸"
						+ "\n２ 么 玫 枚 梅 酶 霉 煤 没 眉 媒" + "\n３ 镁 每 美 昧 寐 妹 媚 门 闷 们"
						+ "\n４ 萌 蒙 檬 盟 锰 猛 梦 孟 眯 醚" + "\n５ 靡 糜 迷 谜 弥 米 秘 觅 泌 蜜"
						+ "\n６ 密 幂 棉 眠 绵 冕 免 勉 娩 缅" + "\n７ 面 苗 描 瞄 藐 秒 渺 庙 妙 蔑"
						+ "\n８ 灭 民 抿 皿 敏 悯 闽 明 螟 鸣"
						+ "\n９ 铭 名 命 谬 摸               "
						+ "\n                                "
						+ "\n36 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 摹 蘑 模 膜 磨 摩 魔 抹 末" + "\n１ 莫 墨 默 沫 漠 寞 陌 谋 牟 某"
						+ "\n２ 拇 牡 亩 姆 母 墓 暮 幕 募 慕" + "\n３ 木 目 睦 牧 穆 拿 哪 呐 钠 那"
						+ "\n４ 娜 纳 氖 乃 奶 耐 奈 南 男 难" + "\n５ 囊 挠 脑 恼 闹 淖 呢 馁 内 嫩"
						+ "\n６ 能 妮 霓 倪 泥 尼 拟 你 匿 腻" + "\n７ 逆 溺 蔫 拈 年 碾 撵 捻 念 娘"
						+ "\n８ 酿 鸟 尿 捏 聂 孽 啮 镊 镍 涅"
						+ "\n９ 您 柠 狞 凝 宁               "
						+ "\n                                "
						+ "\n37 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 拧 泞 牛 扭 钮 纽 脓 浓 农" + "\n１ 弄 奴 努 怒 女 暖 虐 疟 挪 懦"
						+ "\n２ 糯 诺 哦 欧 鸥 殴 藕 呕 偶 沤" + "\n３ 啪 趴 爬 帕 怕 琶 拍 排 牌 徘"
						+ "\n４ 湃 派 攀 潘 盘 磐 盼 畔 判 叛" + "\n５ 乓 庞 旁 耪 胖 抛 咆 刨 炮 袍"
						+ "\n６ 跑 泡 呸 胚 培 裴 赔 陪 配 佩" + "\n７ 沛 喷 盆 砰 抨 烹 澎 彭 蓬 棚"
						+ "\n８ 硼 篷 膨 朋 鹏 捧 碰 坯 砒 霹"
						+ "\n９ 批 披 劈 琵 毗               "
						+ "\n                                "
						+ "\n38 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 啤 脾 疲 皮 匹 痞 僻 屁 譬" + "\n１ 篇 偏 片 骗 飘 漂 瓢 票 撇 瞥"
						+ "\n２ 拼 频 贫 品 聘 乒 坪 苹 萍 平" + "\n３ 凭 瓶 评 屏 坡 泼 颇 婆 破 魄"
						+ "\n４ 迫 粕 剖 扑 铺 仆 莆 葡 菩 蒲" + "\n５ 埔 朴 圃 普 浦 谱 曝 瀑 期 欺"
						+ "\n６ 栖 戚 妻 七 凄 漆 柒 沏 其 棋" + "\n７ 奇 歧 畦 崎 脐 齐 旗 祈 祁 骑"
						+ "\n８ 起 岂 乞 企 启 契 砌 器 气 迄"
						+ "\n９ 弃 汽 泣 讫 掐               "
						+ "\n                                "
						+ "\n39 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 恰 洽 牵 扦 钎 铅 千 迁 签" + "\n１ 仟 谦 乾 黔 钱 钳 前 潜 遣 浅"
						+ "\n２ 谴 堑 嵌 欠 歉 枪 呛 腔 羌 墙" + "\n３ 蔷 强 抢 橇 锹 敲 悄 桥 瞧 乔"
						+ "\n４ 侨 巧 鞘 撬 翘 峭 俏 窍 切 茄" + "\n５ 且 怯 窃 钦 侵 亲 秦 琴 勤 芹"
						+ "\n６ 擒 禽 寝 沁 青 轻 氢 倾 卿 清" + "\n７ 擎 晴 氰 情 顷 请 庆 琼 穷 秋"
						+ "\n８ 丘 邱 球 求 囚 酋 泅 趋 区 蛆"
						+ "\n９ 曲 躯 屈 驱 渠               ");
				thermalprinter.addString(strtmp);
				thermalprinter.printString();
				thermalprinter.clearString();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				strtmp = new String("\n                                "
						+ "\n40 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 取 娶 龋 趣 去 圈 颧 权 醛" + "\n１ 泉 全 痊 拳 犬 券 劝 缺 炔 瘸"
						+ "\n２ 却 鹊 榷 确 雀 裙 群 然 燃 冉" + "\n３ 染 瓤 壤 攘 嚷 让 饶 扰 绕 惹"
						+ "\n４ 热 壬 仁 人 忍 韧 任 认 刃 妊" + "\n５ 纫 扔 仍 日 戎 茸 蓉 荣 融 熔"
						+ "\n６ 溶 容 绒 冗 揉 柔 肉 茹 蠕 儒" + "\n７ 孺 如 辱 乳 汝 入 褥 软 阮 蕊"
						+ "\n８ 瑞 锐 闰 润 若 弱 撒 洒 萨 腮"
						+ "\n９ 鳃 塞 赛 三 叁               "
						+ "\n                                "
						+ "\n41 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 伞 散 桑 嗓 丧 搔 骚 扫 嫂" + "\n１ 瑟 色 涩 森 僧 莎 砂 杀 刹 沙"
						+ "\n２ 纱 傻 啥 煞 筛 晒 珊 苫 杉 山" + "\n３ 删 煽 衫 闪 陕 擅 赡 膳 善 汕"
						+ "\n４ 扇 缮 墒 伤 商 赏 晌 上 尚 裳" + "\n５ 梢 捎 稍 烧 芍 勺 韶 少 哨 邵"
						+ "\n６ 绍 奢 赊 蛇 舌 舍 赦 摄 射 慑" + "\n７ 涉 社 设 砷 申 呻 伸 身 深 娠"
						+ "\n８ 绅 神 沈 审 婶 甚 肾 慎 渗 声"
						+ "\n９ 生 甥 牲 升 绳               "
						+ "\n                                "
						+ "\n42 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 省 盛 剩 胜 圣 师 失 狮 施" + "\n１ 湿 诗 尸 虱 十 石 拾 时 什 食"
						+ "\n２ 蚀 实 识 史 矢 使 屎 驶 始 式" + "\n３ 示 士 世 柿 事 拭 誓 逝 势 是"
						+ "\n４ 嗜 噬 适 仕 侍 释 饰 氏 市 恃" + "\n５ 室 视 试 收 手 首 守 寿 授 售"
						+ "\n６ 受 瘦 兽 蔬 枢 梳 殊 抒 输 叔" + "\n７ 舒 淑 疏 书 赎 孰 熟 薯 暑 曙"
						+ "\n８ 署 蜀 黍 鼠 属 术 述 树 束 戍"
						+ "\n９ 竖 墅 庶 数 漱               "
						+ "\n                                "
						+ "\n43 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 恕 刷 耍 摔 衰 甩 帅 栓 拴" + "\n１ 霜 双 爽 谁 水 睡 税 吮 瞬 顺"
						+ "\n２ 舜 说 硕 朔 烁 斯 撕 嘶 思 私" + "\n３ 司 丝 死 肆 寺 嗣 四 伺 似 饲"
						+ "\n４ 巳 松 耸 怂 颂 送 宋 讼 诵 搜" + "\n５ 艘 擞 嗽 苏 酥 俗 素 速 粟 僳"
						+ "\n６ 塑 溯 宿 诉 肃 酸 蒜 算 虽 隋" + "\n７ 随 绥 髓 碎 岁 穗 遂 隧 祟 孙"
						+ "\n８ 损 笋 蓑 梭 唆 缩 琐 索 锁 所"
						+ "\n９ 塌 他 它 她 塔               "
						+ "\n                                "
						+ "\n44 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 獭 挞 蹋 踏 胎 苔 抬 台 泰" + "\n１ 酞 太 态 汰 坍 摊 贪 瘫 滩 坛"
						+ "\n２ 檀 痰 潭 谭 谈 坦 毯 袒 碳 探" + "\n３ 叹 炭 汤 塘 搪 堂 棠 膛 唐 糖"
						+ "\n４ 倘 躺 淌 趟 烫 掏 涛 滔 绦 萄" + "\n５ 桃 逃 淘 陶 讨 套 特 藤 腾 疼"
						+ "\n６ 誊 梯 剔 踢 锑 提 题 蹄 啼 体" + "\n７ 替 嚏 惕 涕 剃 屉 天 添 填 田"
						+ "\n８ 甜 恬 舔 腆 挑 条 迢 眺 跳 贴"
						+ "\n９ 铁 帖 厅 听 烃               "
						+ "\n                                "
						+ "\n45 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 汀 廷 停 亭 庭 挺 艇 通 桐" + "\n１ 酮 瞳 同 铜 彤 童 桶 捅 筒 统"
						+ "\n２ 痛 偷 投 头 透 凸 秃 突 图 徒" + "\n３ 途 涂 屠 土 吐 兔 湍 团 推 颓"
						+ "\n４ 腿 蜕 褪 退 吞 屯 臀 拖 托 脱" + "\n５ 鸵 陀 驮 驼 椭 妥 拓 唾 挖 哇"
						+ "\n６ 蛙 洼 娃 瓦 袜 歪 外 豌 弯 湾" + "\n７ 玩 顽 丸 烷 完 碗 挽 晚 皖 惋"
						+ "\n８ 宛 婉 万 腕 汪 王 亡 枉 网 往"
						+ "\n９ 旺 望 忘 妄 威               "
						+ "\n                                "
						+ "\n46 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 巍 微 危 韦 违 桅 围 唯 惟" + "\n１ 为 潍 维 苇 萎 委 伟 伪 尾 纬"
						+ "\n２ 未 蔚 味 畏 胃 喂 魏 位 渭 谓" + "\n３ 尉 慰 卫 瘟 温 蚊 文 闻 纹 吻"
						+ "\n４ 稳 紊 问 嗡 翁 瓮 挝 蜗 涡 窝" + "\n５ 我 斡 卧 握 沃 巫 呜 钨 乌 污"
						+ "\n６ 诬 屋 无 芜 梧 吾 吴 毋 武 五" + "\n７ 捂 午 舞 伍 侮 坞 戊 雾 晤 物"
						+ "\n８ 勿 务 悟 误 昔 熙 析 西 硒 矽"
						+ "\n９ 晰 嘻 吸 锡 牺               "
						+ "\n                                "
						+ "\n47 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 稀 息 希 悉 膝 夕 惜 熄 烯" + "\n１ 溪 汐 犀 檄 袭 席 习 媳 喜 铣"
						+ "\n２ 洗 系 隙 戏 细 瞎 虾 匣 霞 辖" + "\n３ 暇 峡 侠 狭 下 厦 夏 吓 掀 锨"
						+ "\n４ 先 仙 鲜 纤 咸 贤 衔 舷 闲 涎" + "\n５ 弦 嫌 显 险 现 献 县 腺 馅 羡"
						+ "\n６ 宪 陷 限 线 相 厢 镶 香 箱 襄" + "\n７ 湘 乡 翔 祥 详 想 响 享 项 巷"
						+ "\n８ 橡 像 向 象 萧 硝 霄 削 哮 嚣"
						+ "\n９ 销 消 宵 淆 晓               "
						+ "\n                                "
						+ "\n48 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 小 孝 校 肖 啸 笑 效 楔 些" + "\n１ 歇 蝎 鞋 协 挟 携 邪 斜 胁 谐"
						+ "\n２ 写 械 卸 蟹 懈 泄 泻 谢 屑 薪" + "\n３ 芯 锌 欣 辛 新 忻 心 信 衅 星"
						+ "\n４ 腥 猩 惺 兴 刑 型 形 邢 行 醒" + "\n５ 幸 杏 性 姓 兄 凶 胸 匈 汹 雄"
						+ "\n６ 熊 休 修 羞 朽 嗅 锈 秀 袖 绣" + "\n７ 墟 戌 需 虚 嘘 须 徐 许 蓄 酗"
						+ "\n８ 叙 旭 序 畜 恤 絮 婿 绪 续 轩"
						+ "\n９ 喧 宣 悬 旋 玄               "
						+ "\n                                "
						+ "\n49 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 选 癣 眩 绚 靴 薛 学 穴 雪" + "\n１ 血 勋 熏 循 旬 询 寻 驯 巡 殉"
						+ "\n２ 汛 训 讯 逊 迅 压 押 鸦 鸭 呀" + "\n３ 丫 芽 牙 蚜 崖 衙 涯 雅 哑 亚"
						+ "\n４ 讶 焉 咽 阉 烟 淹 盐 严 研 蜒" + "\n５ 岩 延 言 颜 阎 炎 沿 奄 掩 眼"
						+ "\n６ 衍 演 艳 堰 燕 厌 砚 雁 唁 彦" + "\n７ 焰 宴 谚 验 殃 央 鸯 秧 杨 扬"
						+ "\n８ 佯 疡 羊 洋 阳 氧 仰 痒 养 样"
						+ "\n９ 漾 邀 腰 妖 瑶               ");
				thermalprinter.addString(strtmp);
				thermalprinter.printString();
				thermalprinter.clearString();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				strtmp = new String("\n                                "
						+ "\n50 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 摇 尧 遥 窑 谣 姚 咬 舀 药" + "\n１ 要 耀 椰 噎 耶 爷 野 冶 也 页"
						+ "\n２ 掖 业 叶 曳 腋 夜 液 一 壹 医" + "\n３ 揖 铱 依 伊 衣 颐 夷 遗 移 仪"
						+ "\n４ 胰 疑 沂 宜 姨 彝 椅 蚁 倚 已" + "\n５ 乙 矣 以 艺 抑 易 邑 屹 亿 役"
						+ "\n６ 臆 逸 肄 疫 亦 裔 意 毅 忆 义" + "\n７ 益 溢 诣 议 谊 译 异 翼 翌 绎"
						+ "\n８ 茵 荫 因 殷 音 阴 姻 吟 银 淫"
						+ "\n９ 寅 饮 尹 引 隐               "
						+ "\n                                "
						+ "\n51 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 印 英 樱 婴 鹰 应 缨 莹 萤" + "\n１ 营 荧 蝇 迎 赢 盈 影 颖 硬 映"
						+ "\n２ 哟 拥 佣 臃 痈 庸 雍 踊 蛹 咏" + "\n３ 泳 涌 永 恿 勇 用 幽 优 悠 忧"
						+ "\n４ 尤 由 邮 铀 犹 油 游 酉 有 友" + "\n５ 右 佑 釉 诱 又 幼 迂 淤 于 盂"
						+ "\n６ 榆 虞 愚 舆 余 俞 逾 鱼 愉 渝" + "\n７ 渔 隅 予 娱 雨 与 屿 禹 宇 语"
						+ "\n８ 羽 玉 域 芋 郁 吁 遇 喻 峪 御"
						+ "\n９ 愈 欲 狱 育 誉               "
						+ "\n                                "
						+ "\n52 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 浴 寓 裕 预 豫 驭 鸳 渊 冤" + "\n１ 元 垣 袁 原 援 辕 园 员 圆 猿"
						+ "\n２ 源 缘 远 苑 愿 怨 院 曰 约 越" + "\n３ 跃 钥 岳 粤 月 悦 阅 耘 云 郧"
						+ "\n４ 匀 陨 允 运 蕴 酝 晕 韵 孕 匝" + "\n５ 砸 杂 栽 哉 灾 宰 载 再 在 咱"
						+ "\n６ 攒 暂 赞 赃 脏 葬 遭 糟 凿 藻" + "\n７ 枣 早 澡 蚤 躁 噪 造 皂 灶 燥"
						+ "\n８ 责 择 则 泽 贼 怎 增 憎 曾 赠"
						+ "\n９ 扎 喳 渣 札 轧               "
						+ "\n                                "
						+ "\n53 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 铡 闸 眨 栅 榨 咋 乍 炸 诈" + "\n１ 摘 斋 宅 窄 债 寨 瞻 毡 詹 粘"
						+ "\n２ 沾 盏 斩 辗 崭 展 蘸 栈 占 战" + "\n３ 站 湛 绽 樟 章 彰 漳 张 掌 涨"
						+ "\n４ 杖 丈 帐 账 仗 胀 瘴 障 招 昭" + "\n５ 找 沼 赵 照 罩 兆 肇 召 遮 折"
						+ "\n６ 哲 蛰 辙 者 锗 蔗 这 浙 珍 斟" + "\n７ 真 甄 砧 臻 贞 针 侦 枕 疹 诊"
						+ "\n８ 震 振 镇 阵 蒸 挣 睁 征 狰 争"
						+ "\n９ 怔 整 拯 正 政               "
						+ "\n                                "
						+ "\n54 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 帧 症 郑 证 芝 枝 支 吱 蜘" + "\n１ 知 肢 脂 汁 之 织 职 直 植 殖"
						+ "\n２ 执 值 侄 址 指 止 趾 只 旨 纸" + "\n３ 志 挚 掷 至 致 置 帜 峙 制 智"
						+ "\n４ 秩 稚 质 炙 痔 滞 治 窒 中 盅" + "\n５ 忠 钟 衷 终 种 肿 重 仲 众 舟"
						+ "\n６ 周 州 洲 诌 粥 轴 肘 帚 咒 皱" + "\n７ 宙 昼 骤 珠 株 蛛 朱 猪 诸 诛"
						+ "\n８ 逐 竹 烛 煮 拄 瞩 嘱 主 著 柱"
						+ "\n９ 助 蛀 贮 铸 筑               "
						+ "\n                                "
						+ "\n55 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 住 注 祝 驻 抓 爪 拽 专 砖" + "\n１ 转 撰 赚 篆 桩 庄 装 妆 撞 壮"
						+ "\n２ 状 椎 锥 追 赘 坠 缀 谆 准 捉" + "\n３ 拙 卓 桌 琢 茁 酌 啄 着 灼 浊"
						+ "\n４ 兹 咨 资 姿 滋 淄 孜 紫 仔 籽" + "\n５ 滓 子 自 渍 字 鬃 棕 踪 宗 综"
						+ "\n６ 总 纵 邹 走 奏 揍 租 足 卒 族" + "\n７ 祖 诅 阻 组 钻 纂 嘴 醉 最 罪"
						+ "\n８ 尊 遵 昨 左 佐 柞 做 作 坐 座"
						+ "\n９                    "
						+ "\n                                "
						+ "\n56 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 亍 丌 兀 丐 廿 卅 丕 亘 丞" + "\n１ 鬲 孬 噩 丨 禺 丿 匕 乇 夭 爻"
						+ "\n２ 卮 氐 囟 胤 馗 毓 睾 鼗 丶 亟" + "\n３ 鼐 乜 乩 亓 芈 孛 啬 嘏 仄 厍"
						+ "\n４ 厝 厣 厥 厮 靥 赝 匚 叵 匦 匮" + "\n５ 匾 赜 卦 卣 刂 刈 刎 刭 刳 刿"
						+ "\n６ 剀 剌 剞 剡 剜 蒯 剽 劂 劁 劐" + "\n７ 劓 冂 罔 亻 仃 仉 仂 仨 仡 仫"
						+ "\n８ 仞 伛 仳 伢 佤 仵 伥 伧 伉 伫"
						+ "\n９ 佞 佧 攸 佚 佝               "
						+ "\n                                "
						+ "\n57 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 佟 佗 伲 伽 佶 佴 侑 侉 侃" + "\n１ 侏 佾 佻 侪 佼 侬 侔 俦 俨 俪"
						+ "\n２ 俅 俚 俣 俜 俑 俟 俸 倩 偌 俳" + "\n３ 倬 倏 倮 倭 俾 倜 倌 倥 倨 偾"
						+ "\n４ 偃 偕 偈 偎 偬 偻 傥 傧 傩 傺" + "\n５ 僖 儆 僭 僬 僦 僮 儇 儋 仝 氽"
						+ "\n６ 佘 佥 俎 龠 汆 籴 兮 巽 黉 馘" + "\n７ 冁 夔 勹 匍 訇 匐 凫 夙 兕 亠"
						+ "\n８ 兖 亳 衮 袤 亵 脔 裒 禀 嬴 蠃"
						+ "\n９ 羸 冫 冱 冽 冼               "
						+ "\n                                "
						+ "\n58 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 凇 冖 冢 冥 讠 讦 讧 讪 讴" + "\n１ 讵 讷 诂 诃 诋 诏 诎 诒 诓 诔"
						+ "\n２ 诖 诘 诙 诜 诟 诠 诤 诨 诩 诮" + "\n３ 诰 诳 诶 诹 诼 诿 谀 谂 谄 谇"
						+ "\n４ 谌 谏 谑 谒 谔 谕 谖 谙 谛 谘" + "\n５ 谝 谟 谠 谡 谥 谧 谪 谫 谮 谯"
						+ "\n６ 谲 谳 谵 谶 卩 卺 阝 阢 阡 阱" + "\n７ 阪 阽 阼 陂 陉 陔 陟 陧 陬 陲"
						+ "\n８ 陴 隈 隍 隗 隰 邗 邛 邝 邙 邬"
						+ "\n９ 邡 邴 邳 邶 邺               "
						+ "\n                                "
						+ "\n59 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 邸 邰 郏 郅 邾 郐 郄 郇 郓" + "\n１ 郦 郢 郜 郗 郛 郫 郯 郾 鄄 鄢"
						+ "\n２ 鄞 鄣 鄱 鄯 鄹 酃 酆 刍 奂 劢" + "\n３ 劬 劭 劾 哿 勐 勖 勰 叟 燮 矍"
						+ "\n４ 廴 凵 凼 鬯 厶 弁 畚 巯 坌 垩" + "\n５ 垡 塾 墼 壅 壑 圩 圬 圪 圳 圹"
						+ "\n６ 圮 圯 坜 圻 坂 坩 垅 坫 垆 坼" + "\n７ 坻 坨 坭 坶 坳 垭 垤 垌 垲 埏"
						+ "\n８ 垧 垴 垓 垠 埕 埘 埚 埙 埒 垸"
						+ "\n９ 埴 埯 埸 埤 埝               ");
				thermalprinter.addString(strtmp);
				thermalprinter.printString();
				thermalprinter.clearString();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				strtmp = new String("\n                                "
						+ "\n60 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 堋 堍 埽 埭 堀 堞 堙 塄 堠" + "\n１ 塥 塬 墁 墉 墚 墀 馨 鼙 懿 艹"
						+ "\n２ 艽 艿 芏 芊 芨 芄 芎 芑 芗 芙" + "\n３ 芫 芸 芾 芰 苈 苊 苣 芘 芷 芮"
						+ "\n４ 苋 苌 苁 芩 芴 芡 芪 芟 苄 苎" + "\n５ 芤 苡 茉 苷 苤 茏 茇 苜 苴 苒"
						+ "\n６ 苘 茌 苻 苓 茑 茚 茆 茔 茕 苠" + "\n７ 苕 茜 荑 荛 荜 茈 莒 茼 茴 茱"
						+ "\n８ 莛 荞 茯 荏 荇 荃 荟 荀 茗 荠"
						+ "\n９ 茭 茺 茳 荦 荥               "
						+ "\n                                "
						+ "\n61 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 荨 茛 荩 荬 荪 荭 荮 莰 荸" + "\n１ 莳 莴 莠 莪 莓 莜 莅 荼 莶 莩"
						+ "\n２ 荽 莸 荻 莘 莞 莨 莺 莼 菁 萁" + "\n３ 菥 菘 堇 萘 萋 菝 菽 菖 萜 萸"
						+ "\n４ 萑 萆 菔 菟 萏 萃 菸 菹 菪 菅" + "\n５ 菀 萦 菰 菡 葜 葑 葚 葙 葳 蒇"
						+ "\n６ 蒈 葺 蒉 葸 萼 葆 葩 葶 蒌 蒎" + "\n７ 萱 葭 蓁 蓍 蓐 蓦 蒽 蓓 蓊 蒿"
						+ "\n８ 蒺 蓠 蒡 蒹 蒴 蒗 蓥 蓣 蔌 甍"
						+ "\n９ 蔸 蓰 蔹 蔟 蔺               "
						+ "\n                                "
						+ "\n62 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 蕖 蔻 蓿 蓼 蕙 蕈 蕨 蕤 蕞" + "\n１ 蕺 瞢 蕃 蕲 蕻 薤 薨 薇 薏 蕹"
						+ "\n２ 薮 薜 薅 薹 薷 薰 藓 藁 藜 藿" + "\n３ 蘧 蘅 蘩 蘖 蘼 廾 弈 夼 奁 耷"
						+ "\n４ 奕 奚 奘 匏 尢 尥 尬 尴 扌 扪" + "\n５ 抟 抻 拊 拚 拗 拮 挢 拶 挹 捋"
						+ "\n６ 捃 掭 揶 捱 捺 掎 掴 捭 掬 掊" + "\n７ 捩 掮 掼 揲 揸 揠 揿 揄 揞 揎"
						+ "\n８ 摒 揆 掾 摅 摁 搋 搛 搠 搌 搦"
						+ "\n９ 搡 摞 撄 摭 撖               "
						+ "\n                                "
						+ "\n63 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 摺 撷 撸 撙 撺 擀 擐 擗 擤" + "\n１ 擢 攉 攥 攮 弋 忒 甙 弑 卟 叱"
						+ "\n２ 叽 叩 叨 叻 吒 吖 吆 呋 呒 呓" + "\n３ 呔 呖 呃 吡 呗 呙 吣 吲 咂 咔"
						+ "\n４ 呷 呱 呤 咚 咛 咄 呶 呦 咝 哐" + "\n５ 咭 哂 咴 哒 咧 咦 哓 哔 呲 咣"
						+ "\n６ 哕 咻 咿 哌 哙 哚 哜 咩 咪 咤" + "\n７ 哝 哏 哞 唛 哧 唠 哽 唔 哳 唢"
						+ "\n８ 唣 唏 唑 唧 唪 啧 喏 喵 啉 啭"
						+ "\n９ 啁 啕 唿 啐 唼               "
						+ "\n                                "
						+ "\n64 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 唷 啖 啵 啶 啷 唳 唰 啜 喋" + "\n１ 嗒 喃 喱 喹 喈 喁 喟 啾 嗖 喑"
						+ "\n２ 啻 嗟 喽 喾 喔 喙 嗪 嗷 嗉 嘟" + "\n３ 嗑 嗫 嗬 嗔 嗦 嗝 嗄 嗯 嗥 嗲"
						+ "\n４ 嗳 嗌 嗍 嗨 嗵 嗤 辔 嘞 嘈 嘌" + "\n５ 嘁 嘤 嘣 嗾 嘀 嘧 嘭 噘 嘹 噗"
						+ "\n６ 嘬 噍 噢 噙 噜 噌 噔 嚆 噤 噱" + "\n７ 噫 噻 噼 嚅 嚓 嚯 囔 囗 囝 囡"
						+ "\n８ 囵 囫 囹 囿 圄 圊 圉 圜 帏 帙"
						+ "\n９ 帔 帑 帱 帻 帼               "
						+ "\n                                "
						+ "\n65 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 帷 幄 幔 幛 幞 幡 岌 屺 岍" + "\n１ 岐 岖 岈 岘 岙 岑 岚 岜 岵 岢"
						+ "\n２ 岽 岬 岫 岱 岣 峁 岷 峄 峒 峤" + "\n３ 峋 峥 崂 崃 崧 崦 崮 崤 崞 崆"
						+ "\n４ 崛 嵘 崾 崴 崽 嵬 嵛 嵯 嵝 嵫" + "\n５ 嵋 嵊 嵩 嵴 嶂 嶙 嶝 豳 嶷 巅"
						+ "\n６ 彳 彷 徂 徇 徉 後 徕 徙 徜 徨" + "\n７ 徭 徵 徼 衢 彡 犭 犰 犴 犷 犸"
						+ "\n８ 狃 狁 狎 狍 狒 狨 狯 狩 狲 狴"
						+ "\n９ 狷 猁 狳 猃 狺               "
						+ "\n                                "
						+ "\n66 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 狻 猗 猓 猡 猊 猞 猝 猕 猢" + "\n１ 猹 猥 猬 猸 猱 獐 獍 獗 獠 獬"
						+ "\n２ 獯 獾 舛 夥 飧 夤 夂 饣 饧 饨" + "\n３ 饩 饪 饫 饬 饴 饷 饽 馀 馄 馇"
						+ "\n４ 馊 馍 馐 馑 馓 馔 馕 庀 庑 庋" + "\n５ 庖 庥 庠 庹 庵 庾 庳 赓 廒 廑"
						+ "\n６ 廛 廨 廪 膺 忄 忉 忖 忏 怃 忮" + "\n７ 怄 忡 忤 忾 怅 怆 忪 忭 忸 怙"
						+ "\n８ 怵 怦 怛 怏 怍 怩 怫 怊 怿 怡"
						+ "\n９ 恸 恹 恻 恺 恂               "
						+ "\n                                "
						+ "\n67 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 恪 恽 悖 悚 悭 悝 悃 悒 悌" + "\n１ 悛 惬 悻 悱 惝 惘 惆 惚 悴 愠"
						+ "\n２ 愦 愕 愣 惴 愀 愎 愫 慊 慵 憬" + "\n３ 憔 憧 憷 懔 懵 忝 隳 闩 闫 闱"
						+ "\n４ 闳 闵 闶 闼 闾 阃 阄 阆 阈 阊" + "\n５ 阋 阌 阍 阏 阒 阕 阖 阗 阙 阚"
						+ "\n６ 丬 爿 戕 氵 汔 汜 汊 沣 沅 沐" + "\n７ 沔 沌 汨 汩 汴 汶 沆 沩 泐 泔"
						+ "\n８ 沭 泷 泸 泱 泗 沲 泠 泖 泺 泫"
						+ "\n９ 泮 沱 泓 泯 泾               "
						+ "\n                                "
						+ "\n68 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 洹 洧 洌 浃 浈 洇 洄 洙 洎" + "\n１ 洫 浍 洮 洵 洚 浏 浒 浔 洳 涑"
						+ "\n２ 浯 涞 涠 浞 涓 涔 浜 浠 浼 浣" + "\n３ 渚 淇 淅 淞 渎 涿 淠 渑 淦 淝"
						+ "\n４ 淙 渖 涫 渌 涮 渫 湮 湎 湫 溲" + "\n５ 湟 溆 湓 湔 渲 渥 湄 滟 溱 溘"
						+ "\n６ 滠 漭 滢 溥 溧 溽 溻 溷 滗 溴" + "\n７ 滏 溏 滂 溟 潢 潆 潇 漤 漕 滹"
						+ "\n８ 漯 漶 潋 潴 漪 漉 漩 澉 澍 澌"
						+ "\n９ 潸 潲 潼 潺 濑               "
						+ "\n                                "
						+ "\n69 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 濉 澧 澹 澶 濂 濡 濮 濞 濠" + "\n１ 濯 瀚 瀣 瀛 瀹 瀵 灏 灞 宀 宄"
						+ "\n２ 宕 宓 宥 宸 甯 骞 搴 寤 寮 褰" + "\n３ 寰 蹇 謇 辶 迓 迕 迥 迮 迤 迩"
						+ "\n４ 迦 迳 迨 逅 逄 逋 逦 逑 逍 逖" + "\n５ 逡 逵 逶 逭 逯 遄 遑 遒 遐 遨"
						+ "\n６ 遘 遢 遛 暹 遴 遽 邂 邈 邃 邋" + "\n７ 彐 彗 彖 彘 尻 咫 屐 屙 孱 屣"
						+ "\n８ 屦 羼 弪 弩 弭 艴 弼 鬻 屮 妁"
						+ "\n９ 妃 妍 妩 妪 妣               ");
				thermalprinter.addString(strtmp);
				thermalprinter.printString();
				thermalprinter.clearString();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				strtmp = new String("\n                                "
						+ "\n70 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 妗 姊 妫 妞 妤 姒 妲 妯 姗" + "\n１ 妾 娅 娆 姝 娈 姣 姘 姹 娌 娉"
						+ "\n２ 娲 娴 娑 娣 娓 婀 婧 婊 婕 娼" + "\n３ 婢 婵 胬 媪 媛 婷 婺 媾 嫫 媲"
						+ "\n４ 嫒 嫔 媸 嫠 嫣 嫱 嫖 嫦 嫘 嫜" + "\n５ 嬉 嬗 嬖 嬲 嬷 孀 尕 尜 孚 孥"
						+ "\n６ 孳 孑 孓 孢 驵 驷 驸 驺 驿 驽" + "\n７ 骀 骁 骅 骈 骊 骐 骒 骓 骖 骘"
						+ "\n８ 骛 骜 骝 骟 骠 骢 骣 骥 骧 纟"
						+ "\n９ 纡 纣 纥 纨 纩               "
						+ "\n                                "
						+ "\n71 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 纭 纰 纾 绀 绁 绂 绉 绋 绌" + "\n１ 绐 绔 绗 绛 绠 绡 绨 绫 绮 绯"
						+ "\n２ 绱 绲 缍 绶 绺 绻 绾 缁 缂 缃" + "\n３ 缇 缈 缋 缌 缏 缑 缒 缗 缙 缜"
						+ "\n４ 缛 缟 缡 缢 缣 缤 缥 缦 缧 缪" + "\n５ 缫 缬 缭 缯 缰 缱 缲 缳 缵 幺"
						+ "\n６ 畿 巛 甾 邕 玎 玑 玮 玢 玟 珏" + "\n７ 珂 珑 玷 玳 珀 珉 珈 珥 珙 顼"
						+ "\n８ 琊 珩 珧 珞 玺 珲 琏 琪 瑛 琦"
						+ "\n９ 琥 琨 琰 琮 琬               "
						+ "\n                                "
						+ "\n72 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 琛 琚 瑁 瑜 瑗 瑕 瑙 瑷 瑭" + "\n１ 瑾 璜 璎 璀 璁 璇 璋 璞 璨 璩"
						+ "\n２ 璐 璧 瓒 璺 韪 韫 韬 杌 杓 杞" + "\n３ 杈 杩 枥 枇 杪 杳 枘 枧 杵 枨"
						+ "\n４ 枞 枭 枋 杷 杼 柰 栉 柘 栊 柩" + "\n５ 枰 栌 柙 枵 柚 枳 柝 栀 柃 枸"
						+ "\n６ 柢 栎 柁 柽 栲 栳 桠 桡 桎 桢" + "\n７ 桄 桤 梃 栝 桕 桦 桁 桧 桀 栾"
						+ "\n８ 桊 桉 栩 梵 梏 桴 桷 梓 桫 棂"
						+ "\n９ 楮 棼 椟 椠 棹               "
						+ "\n                                "
						+ "\n73 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 椤 棰 椋 椁 楗 棣 椐 楱 椹" + "\n１ 楠 楂 楝 榄 楫 榀 榘 楸 椴 槌"
						+ "\n２ 榇 榈 槎 榉 楦 楣 楹 榛 榧 榻" + "\n３ 榫 榭 槔 榱 槁 槊 槟 榕 槠 榍"
						+ "\n４ 槿 樯 槭 樗 樘 橥 槲 橄 樾 檠" + "\n５ 橐 橛 樵 檎 橹 樽 樨 橘 橼 檑"
						+ "\n６ 檐 檩 檗 檫 猷 獒 殁 殂 殇 殄" + "\n７ 殒 殓 殍 殚 殛 殡 殪 轫 轭 轱"
						+ "\n８ 轲 轳 轵 轶 轸 轷 轹 轺 轼 轾"
						+ "\n９ 辁 辂 辄 辇 辋               "
						+ "\n                                "
						+ "\n74 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 辍 辎 辏 辘 辚 軎 戋 戗 戛" + "\n１ 戟 戢 戡 戥 戤 戬 臧 瓯 瓴 瓿"
						+ "\n２ 甏 甑 甓 攴 旮 旯 旰 昊 昙 杲" + "\n３ 昃 昕 昀 炅 曷 昝 昴 昱 昶 昵"
						+ "\n４ 耆 晟 晔 晁 晏 晖 晡 晗 晷 暄" + "\n５ 暌 暧 暝 暾 曛 曜 曦 曩 贲 贳"
						+ "\n６ 贶 贻 贽 赀 赅 赆 赈 赉 赇 赍" + "\n７ 赕 赙 觇 觊 觋 觌 觎 觏 觐 觑"
						+ "\n８ 牮 犟 牝 牦 牯 牾 牿 犄 犋 犍"
						+ "\n９ 犏 犒 挈 挲 掰               "
						+ "\n                                "
						+ "\n75 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 搿 擘 耄 毪 毳 毽 毵 毹 氅" + "\n１ 氇 氆 氍 氕 氘 氙 氚 氡 氩 氤"
						+ "\n２ 氪 氲 攵 敕 敫 牍 牒 牖 爰 虢" + "\n３ 刖 肟 肜 肓 肼 朊 肽 肱 肫 肭"
						+ "\n４ 肴 肷 胧 胨 胩 胪 胛 胂 胄 胙" + "\n５ 胍 胗 朐 胝 胫 胱 胴 胭 脍 脎"
						+ "\n６ 胲 胼 朕 脒 豚 脶 脞 脬 脘 脲" + "\n７ 腈 腌 腓 腴 腙 腚 腱 腠 腩 腼"
						+ "\n８ 腽 腭 腧 塍 媵 膈 膂 膑 滕 膣"
						+ "\n９ 膪 臌 朦 臊 膻               "
						+ "\n                                "
						+ "\n76 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 臁 膦 欤 欷 欹 歃 歆 歙 飑" + "\n１ 飒 飓 飕 飙 飚 殳 彀 毂 觳 斐"
						+ "\n２ 齑 斓 於 旆 旄 旃 旌 旎 旒 旖" + "\n３ 炀 炜 炖 炝 炻 烀 炷 炫 炱 烨"
						+ "\n４ 烊 焐 焓 焖 焯 焱 煳 煜 煨 煅" + "\n５ 煲 煊 煸 煺 熘 熳 熵 熨 熠 燠"
						+ "\n６ 燔 燧 燹 爝 爨 灬 焘 煦 熹 戾" + "\n７ 戽 扃 扈 扉 礻 祀 祆 祉 祛 祜"
						+ "\n８ 祓 祚 祢 祗 祠 祯 祧 祺 禅 禊"
						+ "\n９ 禚 禧 禳 忑 忐               "
						+ "\n                                "
						+ "\n77 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 怼 恝 恚 恧 恁 恙 恣 悫 愆" + "\n１ 愍 慝 憩 憝 懋 懑 戆 肀 聿 沓"
						+ "\n２ 泶 淼 矶 矸 砀 砉 砗 砘 砑 斫" + "\n３ 砭 砜 砝 砹 砺 砻 砟 砼 砥 砬"
						+ "\n４ 砣 砩 硎 硭 硖 硗 砦 硐 硇 硌" + "\n５ 硪 碛 碓 碚 碇 碜 碡 碣 碲 碹"
						+ "\n６ 碥 磔 磙 磉 磬 磲 礅 磴 礓 礤" + "\n７ 礞 礴 龛 黹 黻 黼 盱 眄 眍 盹"
						+ "\n８ 眇 眈 眚 眢 眙 眭 眦 眵 眸 睐"
						+ "\n９ 睑 睇 睃 睚 睨               "
						+ "\n                                "
						+ "\n78 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 睢 睥 睿 瞍 睽 瞀 瞌 瞑 瞟" + "\n１ 瞠 瞰 瞵 瞽 町 畀 畎 畋 畈 畛"
						+ "\n２ 畲 畹 疃 罘 罡 罟 詈 罨 罴 罱" + "\n３ 罹 羁 罾 盍 盥 蠲 钅 钆 钇 钋"
						+ "\n４ 钊 钌 钍 钏 钐 钔 钗 钕 钚 钛" + "\n５ 钜 钣 钤 钫 钪 钭 钬 钯 钰 钲"
						+ "\n６ 钴 钶 钷 钸 钹 钺 钼 钽 钿 铄" + "\n７ 铈 铉 铊 铋 铌 铍 铎 铐 铑 铒"
						+ "\n８ 铕 铖 铗 铙 铘 铛 铞 铟 铠 铢"
						+ "\n９ 铤 铥 铧 铨 铪               "
						+ "\n                                "
						+ "\n79 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 铩 铫 铮 铯 铳 铴 铵 铷 铹" + "\n１ 铼 铽 铿 锃 锂 锆 锇 锉 锊 锍"
						+ "\n２ 锎 锏 锒 锓 锔 锕 锖 锘 锛 锝" + "\n３ 锞 锟 锢 锪 锫 锩 锬 锱 锲 锴"
						+ "\n４ 锶 锷 锸 锼 锾 锿 镂 锵 镄 镅" + "\n５ 镆 镉 镌 镎 镏 镒 镓 镔 镖 镗"
						+ "\n６ 镘 镙 镛 镞 镟 镝 镡 镢 镤 镥" + "\n７ 镦 镧 镨 镩 镪 镫 镬 镯 镱 镲"
						+ "\n８ 镳 锺 矧 矬 雉 秕 秭 秣 秫 稆"
						+ "\n９ 嵇 稃 稂 稞 稔               "
						+ "\n                                ");
				thermalprinter.addString(strtmp);
				thermalprinter.printString();
				thermalprinter.clearString();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				strtmp = new String("\n80 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 稹 稷 穑 黏 馥 穰 皈 皎 皓" + "\n１ 皙 皤 瓞 瓠 甬 鸠 鸢 鸨 鸩 鸪"
						+ "\n２ 鸫 鸬 鸲 鸱 鸶 鸸 鸷 鸹 鸺 鸾" + "\n３ 鹁 鹂 鹄 鹆 鹇 鹈 鹉 鹋 鹌 鹎"
						+ "\n４ 鹑 鹕 鹗 鹚 鹛 鹜 鹞 鹣 鹦 鹧" + "\n５ 鹨 鹩 鹪 鹫 鹬 鹱 鹭 鹳 疒 疔"
						+ "\n６ 疖 疠 疝 疬 疣 疳 疴 疸 痄 疱" + "\n７ 疰 痃 痂 痖 痍 痣 痨 痦 痤 痫"
						+ "\n８ 痧 瘃 痱 痼 痿 瘐 瘀 瘅 瘌 瘗"
						+ "\n９ 瘊 瘥 瘘 瘕 瘙               "
						+ "\n                                "
						+ "\n81 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 瘛 瘼 瘢 瘠 癀 瘭 瘰 瘿 瘵" + "\n１ 癃 瘾 瘳 癍 癞 癔 癜 癖 癫 癯"
						+ "\n２ 翊 竦 穸 穹 窀 窆 窈 窕 窦 窠" + "\n３ 窬 窨 窭 窳 衤 衩 衲 衽 衿 袂"
						+ "\n４ 袢 裆 袷 袼 裉 裢 裎 裣 裥 裱" + "\n５ 褚 裼 裨 裾 裰 褡 褙 褓 褛 褊"
						+ "\n６ 褴 褫 褶 襁 襦 襻 疋 胥 皲 皴" + "\n７ 矜 耒 耔 耖 耜 耠 耢 耥 耦 耧"
						+ "\n８ 耩 耨 耱 耋 耵 聃 聆 聍 聒 聩"
						+ "\n９ 聱 覃 顸 颀 颃               "
						+ "\n                                "
						+ "\n82 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 颉 颌 颍 颏 颔 颚 颛 颞 颟" + "\n１ 颡 颢 颥 颦 虍 虔 虬 虮 虿 虺"
						+ "\n２ 虼 虻 蚨 蚍 蚋 蚬 蚝 蚧 蚣 蚪" + "\n３ 蚓 蚩 蚶 蛄 蚵 蛎 蚰 蚺 蚱 蚯"
						+ "\n４ 蛉 蛏 蚴 蛩 蛱 蛲 蛭 蛳 蛐 蜓" + "\n５ 蛞 蛴 蛟 蛘 蛑 蜃 蜇 蛸 蜈 蜊"
						+ "\n６ 蜍 蜉 蜣 蜻 蜞 蜥 蜮 蜚 蜾 蝈" + "\n７ 蜴 蜱 蜩 蜷 蜿 螂 蜢 蝽 蝾 蝻"
						+ "\n８ 蝠 蝰 蝌 蝮 螋 蝓 蝣 蝼 蝤 蝙"
						+ "\n９ 蝥 螓 螯 螨 蟒               "
						+ "\n                                "
						+ "\n83 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 蟆 螈 螅 螭 螗 螃 螫 蟥 螬" + "\n１ 螵 螳 蟋 蟓 螽 蟑 蟀 蟊 蟛 蟪"
						+ "\n２ 蟠 蟮 蠖 蠓 蟾 蠊 蠛 蠡 蠹 蠼" + "\n３ 缶 罂 罄 罅 舐 竺 竽 笈 笃 笄"
						+ "\n４ 笕 笊 笫 笏 筇 笸 笪 笙 笮 笱" + "\n５ 笠 笥 笤 笳 笾 笞 筘 筚 筅 筵"
						+ "\n６ 筌 筝 筠 筮 筻 筢 筲 筱 箐 箦" + "\n７ 箧 箸 箬 箝 箨 箅 箪 箜 箢 箫"
						+ "\n８ 箴 篑 篁 篌 篝 篚 篥 篦 篪 簌"
						+ "\n９ 篾 篼 簏 簖 簋               "
						+ "\n                                "
						+ "\n84 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 簟 簪 簦 簸 籁 籀 臾 舁 舂" + "\n１ 舄 臬 衄 舡 舢 舣 舭 舯 舨 舫"
						+ "\n２ 舸 舻 舳 舴 舾 艄 艉 艋 艏 艚" + "\n３ 艟 艨 衾 袅 袈 裘 裟 襞 羝 羟"
						+ "\n４ 羧 羯 羰 羲 籼 敉 粑 粝 粜 粞" + "\n５ 粢 粲 粼 粽 糁 糇 糌 糍 糈 糅"
						+ "\n６ 糗 糨 艮 暨 羿 翎 翕 翥 翡 翦" + "\n７ 翩 翮 翳 糸 絷 綦 綮 繇 纛 麸"
						+ "\n８ 麴 赳 趄 趔 趑 趱 赧 赭 豇 豉"
						+ "\n９ 酊 酐 酎 酏 酤               "
						+ "\n                                "
						+ "\n85 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 酢 酡 酰 酩 酯 酽 酾 酲 酴" + "\n１ 酹 醌 醅 醐 醍 醑 醢 醣 醪 醭"
						+ "\n２ 醮 醯 醵 醴 醺 豕 鹾 趸 跫 踅" + "\n３ 蹙 蹩 趵 趿 趼 趺 跄 跖 跗 跚"
						+ "\n４ 跞 跎 跏 跛 跆 跬 跷 跸 跣 跹" + "\n５ 跻 跤 踉 跽 踔 踝 踟 踬 踮 踣"
						+ "\n６ 踯 踺 蹀 踹 踵 踽 踱 蹉 蹁 蹂" + "\n７ 蹑 蹒 蹊 蹰 蹶 蹼 蹯 蹴 躅 躏"
						+ "\n８ 躔 躐 躜 躞 豸 貂 貊 貅 貘 貔"
						+ "\n９ 斛 觖 觞 觚 觜               "
						+ "\n                                "
						+ "\n86 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 觥 觫 觯 訾 謦 靓 雩 雳 雯" + "\n１ 霆 霁 霈 霏 霎 霪 霭 霰 霾 龀"
						+ "\n２ 龃 龅 龆 龇 龈 龉 龊 龌 黾 鼋" + "\n３ 鼍 隹 隼 隽 雎 雒 瞿 雠 銎 銮"
						+ "\n４ 鋈 錾 鍪 鏊 鎏 鐾 鑫 鱿 鲂 鲅" + "\n５ 鲆 鲇 鲈 稣 鲋 鲎 鲐 鲑 鲒 鲔"
						+ "\n６ 鲕 鲚 鲛 鲞 鲟 鲠 鲡 鲢 鲣 鲥" + "\n７ 鲦 鲧 鲨 鲩 鲫 鲭 鲮 鲰 鲱 鲲"
						+ "\n８ 鲳 鲴 鲵 鲶 鲷 鲺 鲻 鲼 鲽 鳄"
						+ "\n９ 鳅 鳆 鳇 鳊 鳋               "
						+ "\n                                "
						+ "\n87 ０ １ ２ ３ ４ ５ ６ ７ ８ ９"
						+ "\n０ 　 鳌 鳍 鳎 鳏 鳐 鳓 鳔 鳕 鳗" + "\n１ 鳘 鳙 鳜 鳝 鳟 鳢 靼 鞅 鞑 鞒"
						+ "\n２ 鞔 鞯 鞫 鞣 鞲 鞴 骱 骰 骷 鹘" + "\n３ 骶 骺 骼 髁 髀 髅 髂 髋 髌 髑"
						+ "\n４ 魅 魃 魇 魉 魈 魍 魑 飨 餍 餮" + "\n５ 饕 饔 髟 髡 髦 髯 髫 髻 髭 髹"
						+ "\n６ 鬈 鬏 鬓 鬟 鬣 麽 麾 縻 麂 麇" + "\n７ 麈 麋 麒 鏖 麝 麟 黛 黜 黝 黠"
						+ "\n８ 黟 黢 黩 黧 黥 黪 黯 鼢 鼬 鼯"
						+ "\n９ 鼹 鼷 鼽 鼾 齄               ");
				thermalprinter.addString(strtmp);
				thermalprinter.printString();
				thermalprinter.clearString();
				thermalprinter.walkPaper(20);
			} catch (TelpoException e) {
				e.printStackTrace();
				Result = e.toString();
				if (Result
						.equals("com.common.sdk.thermalprinter.NoPaperException")) {
					nopaper = true;
					// return;
				} else if (Result.equals(" com.common.sdk.thermalprinter.OverHeatException")) {
					handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0,
							null));
				} else {
					handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0,
							e.getDescription()));
				}
			} finally {
				// lock.release();
				g_lock.unlock();
				handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0,
						null));
				if (nopaper)
					handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0,
							null));
				// ThermalPrinter.stop();
				nopaper = false;
				// PrinterActivity.this.sleep(1500);
				// if(progressDialog != null &&
				// !PrinterActivity.this.isFinishing() ){
				// progressDialog.dismiss();
				// progressDialog = null;
				// }
				Log.v(TAG, "The Print Progress End !!!");
				if (isClose) {
					// onDestroy();
					finish();
				}
			}
			// handler.sendMessage(handler
			// .obtainMessage(ENABLE_BUTTON, 1, 0, null));
		}
	}

	private class PintMixThread extends Thread {
		public void run() {
			super.run();
			setName("PintMixThread Thread");
			try {
				g_lock.lock();
				// ThermalPrinter.start();
				thermalprinter.reset();

				switch (textAlign)
				{
					case 0:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
					}break;
					case 1:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_MIDDLE);
					}break;
					case 2:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_RIGHT);
					}break;	
					default:
					{
						thermalprinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
					}break;
				}
				
				thermalprinter.setLeftIndent(leftDistance);
				thermalprinter.setLineSpace(lineDistance);
				if (wordFont == 4) {
					thermalprinter.setFontSize(2);
					thermalprinter.enlargeFontSize(2, 2);
				} else if (wordFont == 3) {
					thermalprinter.setFontSize(1);
					thermalprinter.enlargeFontSize(2, 2);
				} else if (wordFont == 2) {
					thermalprinter.setFontSize(2);
				} else if (wordFont == 1) {
					thermalprinter.setFontSize(1);
				}
				thermalprinter.setGray(printGray);

				thermalprinter.printLogo(BitmapFactory.decodeResource(
						getResources(), R.drawable.telpo), true);

				thermalprinter.addString(printContent);
				thermalprinter.printLogo(BitmapFactory.decodeResource(
						getResources(), R.drawable.lashou), true);
				thermalprinter.printLogo(BitmapFactory.decodeResource(
						getResources(), R.drawable.test_2d), true);

				/*
				 * printQrcode(qrcodeStr); ThermalPrinter.addString(qrcodeStr);
				 * 
				 * ThermalPrinter.printBarcode(barcodeStr);
				 * ThermalPrinter.addString(barcodeStr);
				 */

				thermalprinter.printString();
				thermalprinter.clearString();

				thermalprinter.walkPaper(20);
			} catch (TelpoException e) {
				e.printStackTrace();
				Result = e.toString();
				if (Result
						.equals("com.common.sdk.thermalprinter.NoPaperException")) {
					nopaper = true;
					// return;
				} else if (Result
						.equals(" com.common.sdk.thermalprinter.OverHeatException")) {
					handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0,
							null));
				} else {
					handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0,
							e.getDescription()));
				}
			} finally {
				g_lock.unlock();
				handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0,
						null));
				if (nopaper)
					handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0,
							null));
				// ThermalPrinter.stop();
				nopaper = false;
				// PrinterActivity.this.sleep(1500);
				// if(progressDialog != null &&
				// !PrinterActivity.this.isFinishing() ){
				// progressDialog.dismiss();
				// progressDialog = null;
				// }
				Log.v(TAG, "The Print Progress End !!!");
				if (isClose) {
					// onDestroy();
					finish();
				}
			}
		}
	}

	private class executeCommand extends Thread {

		@Override
		public void run() {
			super.run();
			setName("ExecuteCommand Thread");
			try {
				g_lock.lock();
				// ThermalPrinter.start();
				thermalprinter.reset();
//				thermalprinter.sendCommand(edittext_input_command.getText()
//						.toString());
			} catch (TelpoException e) {
				e.printStackTrace();
				Result = e.toString();
				if (Result
						.equals("com.common.sdk.thermalprinter.NoPaperException")) {
					nopaper = true;
					// return;
				} else if (Result
						.equals(" com.common.sdk.thermalprinter.OverHeatException")) {
					handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0,
							null));
				} else {
					handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0,
							e.getDescription()));
				}
			} finally {
				g_lock.unlock();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0,
						null));
				if (nopaper)
					handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0,
							null));
				// ThermalPrinter.stop();
				nopaper = false;
				// PrinterActivity.this.sleep(1500);
				// if(progressDialog != null &&
				// !PrinterActivity.this.isFinishing() ){
				// progressDialog.dismiss();
				// progressDialog = null;
				// }
				Log.v(TAG, "The ExecuteCommand Progress End !!!");
				if (isClose) {
					finish();
				}
			}
		}

	}

	// private void sleep(int ms) {
	//
	// try {
	// Thread.sleep(ms);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
//		@Override
//		protected void onStart() {
//			try {
//				ThermalPrinter.start();
//			} catch (TelpoException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			super.onStart();
//		}
//		
//		@Override
//		protected void onStop() {
//			ThermalPrinter.stop();
//			super.onStop();
//		}

		protected void onDestroy() {
		if (progressDialog != null && !PrinterActivity.this.isFinishing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		stop = true;
		unregisterReceiver(printReceive);
		try {
			thermalprinter.stop();
		} catch (TelpoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 生成条码
	 *
	 * @param str
	 *            条码内容
	 * @param type
	 *            条码类型： AZTEC, CODABAR, CODE_39, CODE_93, CODE_128, DATA_MATRIX,
	 *            EAN_8, EAN_13, ITF, MAXICODE, PDF_417, QR_CODE, RSS_14,
	 *            RSS_EXPANDED, UPC_A, UPC_E, UPC_EAN_EXTENSION;
	 * @param bmpWidth
	 *            生成位图宽,宽不能大于384，不然大于打印纸宽度
	 * @param bmpHeight
	 *            生成位图高，8的倍数
	 */
	public Bitmap CreateCode(String str, com.google.zxing.BarcodeFormat type,
			int bmpWidth, int bmpHeight) throws WriterException {
		String utf8Str = null;
		// 生成二维矩阵,编码时要指定大小,不要生成了图片以后再进行缩放,以防模糊导致识别失败
		Map<EncodeHintType,Object> hints = new EnumMap<EncodeHintType,Object>(EncodeHintType.class);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix matrix = new MultiFormatWriter().encode(str, type, bmpWidth,
				bmpHeight, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组（一直横着排）
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				} else {
					pixels[y * width + x] = 0xffffffff;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * Function printBarcode
	 * 
	 * @return None
	 * @author zhouzy
	 * @date 20141223
	 * @note
	 */
	private void printQrcode(String str) throws TelpoException {
		// Bitmap bitmap=
		// BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/time1.bmp"));
		Bitmap bitmap = null;
		try {
			bitmap = CreateCode(str, BarcodeFormat.QR_CODE, 256, 256);
		} catch (WriterException e) {
			e.printStackTrace();
		} // BarcodeFormat.QR_CODE
		if (bitmap != null) {
			Log.v(TAG, "Find the Bmp");
			thermalprinter.printLogo(bitmap);
		}
	}
	
	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	public synchronized void printBarcode(String barcode) throws TelpoException {
		if (barcode == null || barcode.length() == 0 || barcode.length() > 12
				|| barcode.length() < 11) {
			throw new IllegalArgumentException();
		}
		if (!isNumeric(barcode)) {
			throw new IllegalArgumentException();
		}
		Bitmap bitmap = null;
		try {
			bitmap = CreateCode(barcode, BarcodeFormat.UPC_A, 360, 108);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		thermalprinter.printLogo(bitmap);
	}
}
