package com.markiv.beaconpush;

public class BeaconPushUrls {
	private static final String HOST = "api.beaconpush.com";
	private BeaconPushUrls() { }
	
	public static String getNumberOfUsersOnlineUrl(String apiKey){
		return HOST + "/1.0.0/" + apiKey + "/users";
	}
	
	public static String getUserReferrantUrl(String apiKey, String userName){
		return HOST + "/1.0.0/" + apiKey + "/users/" + userName;
	}
	
	public static String getChannelReferrantUrl(String apiKey, String channelName){
		return HOST + "/1.0.0/" + apiKey + "/channels/" + channelName;
	}
}
