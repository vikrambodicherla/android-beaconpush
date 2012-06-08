package com.markiv.beaconpush;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import com.markiv.beaconpush.NetworkConnection.Response;
import com.markiv.beaconpush.exception.BeaconPushApiException;

public class User {
	private String mUserName;
	private NetworkConnection mConn;
	private String mApiKey;
	
	public User(NetworkConnection connection, String userName){
		this.mUserName = userName;
		this.mConn = connection;
	}
	
	public int sendMessage(JSONObject json) throws IOException, BeaconPushApiException{
		final String url = BeaconPushUrls.getUserReferrantUrl(mApiKey, mUserName);
		final Response response = mConn.post(url, json.toString());
		if(response.getStatusCode() == HttpStatus.SC_OK){
			try {
				final JSONObject responseJson = response.getBodyAsJson();
				return responseJson.getInt("messages_sent");
			} 
			catch (JSONException e) {
				Log.e("Invalid response from " + url, e);
				throw new BeaconPushApiException("Invalid response from " + url);
			}
		}
		else
			throw new BeaconPushApiException(response.getBodyAsString());
	}
	
	public boolean isOnline() throws IOException, BeaconPushApiException{
		final String url = BeaconPushUrls.getUserReferrantUrl(mApiKey, mUserName);
		final Response response = mConn.get(url);
		if(response.getStatusCode() == HttpStatus.SC_OK)
			return true;
		else if(response.getStatusCode() == HttpStatus.SC_NOT_FOUND)
			return false;
		else
			throw new BeaconPushApiException(response.getBodyAsString());
	}
	
	public void forceLogout() throws IOException{
		final String url = BeaconPushUrls.getUserReferrantUrl(mApiKey, mUserName);
		mConn.delete(url);
	}
}