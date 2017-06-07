/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package org.apache.cordova.tps900;

import java.util.TimeZone;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings;

import com.common.sdk.printer.ThermalPrinter;
import com.common.sdk.TelpoException;
import android.util.Log;


public class Tps900 extends CordovaPlugin {
    public static final String TAG = "Tps900";
    public static String platform;                            // Tps900 OS
    public static String uuid;  
    public ThermalPrinter thermalprinter;     
    public JSONObject r;                         // Tps900 UUID
    public JSONArray params;                              // Tps900 UUID

    private static final String ANDROID_PLATFORM = "Android";
    private static final String AMAZON_PLATFORM = "amazon-fireos";
    private static final String AMAZON_DEVICE = "Amazon";

    /**
     * Constructor.
     */
    public Tps900() {
    }

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Tps900.uuid = getUuid();
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
       thermalprinter = new ThermalPrinter(webView.getContext());
        if ("imprime".equals(action)) {
            params = args.getJSONArray(0);
            Log.e(TAG, params.toString());
            r = new JSONObject();
            new Thread(new Runnable() {
					public void run() {
						try {
                            thermalprinter.start();
                            for (int i = 0; i < params.length(); ++i)
						{
							JSONObject obj = params.getJSONObject(i);
                            Log.e(TAG, obj.toString());
							if(obj.has("addString")){
								thermalprinter.addString(
                                    obj.getJSONObject("addString")
                                    .getString("value")
                                    );
							}
							if(obj.has("setAlgin")){
								thermalprinter.setAlgin(
                                    obj.getJSONObject("setAlgin")
                                    .getInt("value")
                                    );
							}
							if(obj.has("setBold")){
								thermalprinter.setBold(
                                    obj.getJSONObject("setBold")
                                    .getBoolean("value")
                                    );
							}
							if(obj.has("setCharSpace")){
								thermalprinter.setCharSpace(
                                    obj.getJSONObject("setCharSpace")
                                    .getInt("value")
                                    );
							}
							if(obj.has("setFontSize")){
								thermalprinter.setFontSize(
                                    obj.getJSONObject("setFontSize")
                                    .getInt("value")
                                    );
							}
							if(obj.has("setGray")){
								thermalprinter.setGray(
                                    obj.getJSONObject("setGray")
                                    .getInt("value")
                                    );
							}
							if(obj.has("setHighlight")){
								thermalprinter.setHighlight(
                                    obj.getJSONObject("setHighlight")
                                    .getBoolean("value")
                                    );
							}
							if(obj.has("setLineSpace")){
								thermalprinter.setLineSpace(
                                    obj.getJSONObject("setLineSpace")
                                    .getInt("value")
                                    );
							}
							if(obj.has("walkPaper")){
								thermalprinter.walkPaper(
                                    obj.getJSONObject("walkPaper")
                                    .getInt("value")
                                    );
							}
							if(obj.has("printStringAndWalk")){
                                JSONObject _obj =  obj.getJSONObject("printStringAndWalk");
								thermalprinter.printStringAndWalk(
                                    _obj.getInt("d"),
                                    _obj.getInt("m"),
                                    _obj.getInt("l")
                                    );
							}

							if(obj.has("textAsBitmap")){
                                JSONObject _obj =  obj.getJSONObject("textAsBitmap");
								thermalprinter.textAsBitmap(
                                    _obj.getString("texto"),
                                    _obj.getBoolean("fontGrande"),
                                    _obj.getBoolean("negrito"),
                                    _obj.getBoolean("invertido")
                                    );
							}

							if(obj.has("printQrcode")){
                                JSONObject _obj =  obj.getJSONObject("printQrcode");
								thermalprinter.printQrcode(
                                    _obj.getString("value"),
                                    _obj.getInt("w"),
                                    _obj.getInt("y")
                                    );
							}
							if(obj.has("enlargeFontSize")){
                                JSONObject _obj =  obj.getJSONObject("enlargeFontSize");
								thermalprinter.enlargeFontSize(
                                    _obj.getInt("w"),
                                    _obj.getInt("y")
                                    );
							}
							if(obj.has("printString")){
								thermalprinter.printString();
							}
							if(obj.has("clearString")){
								thermalprinter.clearString();
							}
							if(obj.has("reset")){
								thermalprinter.reset();
							}
							if(obj.has("imprime")){

							}
						}
                        thermalprinter.reset();
                        try {
                            r.put("sucesso", true);
                        } catch (Exception e) {
							e.printStackTrace();
						}
						} catch (Exception e) {
                            try {
                             r.put("sucesso", false);
                                } catch (Exception e) {
						        	e.printStackTrace();
						        }
                           
							e.printStackTrace();
						}
					}
				}).start();
                JSONObject resp = new JSONObject();
                if(r.getBoolean("sucesso")){
                    resp.put("data", "sucess");
                    callbackContext.success(resp);
                } else {
                    resp.put("data", "error");
                    callbackContext.error(resp);
                }
            
        }
        else {
            return false;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------

    /**
     * Get the OS name.
     *
     * @return
     */
    public String getPlatform() {
        String platform;
        if (isAmazonTps900()) {
            platform = AMAZON_PLATFORM;
        } else {
            platform = ANDROID_PLATFORM;
        }
        return platform;
    }

    

    /**
     * Get the tps900's Universally Unique Identifier (UUID).
     *
     * @return
     */
    public String getUuid() {
        String uuid = Settings.Secure.getString(this.cordova.getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return uuid;
    }

    public String getModel() {
        String model = android.os.Build.MODEL;
        return model;
    }

    public String getProductName() {
        String productname = android.os.Build.PRODUCT;
        return productname;
    }

    public String getManufacturer() {
        String manufacturer = android.os.Build.MANUFACTURER;
        return manufacturer;
    }

    public String getSerialNumber() {
        String serial = android.os.Build.SERIAL;
        return serial;
    }

    /**
     * Get the OS version.
     *
     * @return
     */
    public String getOSVersion() {
        String osversion = android.os.Build.VERSION.RELEASE;
        return osversion;
    }

    public String getSDKVersion() {
        @SuppressWarnings("deprecation")
        String sdkversion = android.os.Build.VERSION.SDK;
        return sdkversion;
    }

    public String getTimeZoneID() {
        TimeZone tz = TimeZone.getDefault();
        return (tz.getID());
    }

    /**
     * Function to check if the tps900 is manufactured by Amazon
     *
     * @return
     */
    public boolean isAmazonTps900() {
        if (android.os.Build.MANUFACTURER.equals(AMAZON_DEVICE)) {
            return true;
        }
        return false;
    }

    public boolean isVirtual() {
	return android.os.Build.FINGERPRINT.contains("generic") ||
	    android.os.Build.PRODUCT.contains("sdk");
    }

}
