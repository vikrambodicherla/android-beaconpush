package com.markiv.beaconpush;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.markiv.beaconpush.NetworkConnection.Response;
import com.markiv.beaconpush.exception.BeaconPushApiException;

public class Channel {
	private String mApiKey;
	private NetworkConnection mConn;
	private String mChannelName;
	
	public Channel(NetworkConnection connection, String apiKey, String channelName){
		this.mConn = connection;
		this.mApiKey = apiKey;
		this.mChannelName = channelName;
	}
	
	public int sendMessage(JSONObject json) throws IOException, BeaconPushApiException{
		final String url = BeaconPushUrls.getChannelReferrantUrl(mApiKey, mChannelName);
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
	
	public List<User> getUsers() throws IOException, BeaconPushApiException{
		final String url = BeaconPushUrls.getChannelReferrantUrl(mApiKey, mChannelName);
		final Response response = mConn.get(url);
		if(response.getStatusCode() != HttpStatus.SC_OK){
			try {
				final JSONObject responseJson = response.getBodyAsJson();
				final JSONArray usersArray = responseJson.getJSONArray("users");
				if(usersArray != null){
					final int usersLength = usersArray.length();
					final List<User> users = new ArrayList<User>();

					for(int i = 0; i < usersLength; i++)
						users.add(new User(mConn, usersArray.getString(i)));
					
					return users;
				}
				else
					return null;
				
			} 
			catch (JSONException e) {
				Log.e("Invalid response from " + url, e);
				throw new BeaconPushApiException("Invalid response from " + url);
			}
		}
		else
			throw new BeaconPushApiException(response.getBodyAsString());
	}
}
