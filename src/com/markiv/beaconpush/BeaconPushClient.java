package com.markiv.beaconpush;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.markiv.beaconpush.NetworkConnection.Response;
import com.markiv.beaconpush.exception.BeaconPushApiException;

public class BeaconPushClient {
	private String mApiKey;
	private NetworkConnection mNetConnection;

	public BeaconPushClient(String secretKey, String apiKey) {
		this.mApiKey = apiKey;

		mNetConnection = new NetworkConnection(secretKey);
	}

	public int getOnlineUserCount() throws IOException, BeaconPushApiException {
		final Response response = mNetConnection.get(BeaconPushUrls.getNumberOfUsersOnlineUrl(mApiKey));
		if(response.getStatusCode() == HttpStatus.SC_OK){
			try {
				final JSONObject responseJson = response.getBodyAsJson();
				return responseJson.getInt("online");
			} catch (JSONException e) {
				Log.e("getOnlineUserCount failed: " + response, e);
				return Integer.MIN_VALUE;
			}
		}
		else
			throw new BeaconPushApiException(response.getBodyAsString());
	}

	public Channel newChannel(String name) {
		if (TextUtils.isEmpty(name))
			throw new IllegalArgumentException("Invalid name");

		return new Channel(mNetConnection, mApiKey, name);
	}

	public User newUser(String name) {
		if (TextUtils.isEmpty(name))
			throw new IllegalArgumentException("Invalid name");

		return new User(mNetConnection, name);
	}
}