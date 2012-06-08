package com.markiv.beaconpush;

public class Log {
	public static final String TAG = "BeaconPushClient";
	
	public static void e(String message, Throwable tr){
		android.util.Log.e(TAG, message, tr);
	}
}
