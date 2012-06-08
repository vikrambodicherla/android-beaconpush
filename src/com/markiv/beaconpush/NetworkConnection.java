package com.markiv.beaconpush;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkConnection {
	private HttpClient mClient;
	private String mSecretKey;

	public NetworkConnection(String secretKey) {
		mClient = new DefaultHttpClient();
		mSecretKey = secretKey;
	}

	public Response get(String url) throws IOException {
		final HttpGet get = new HttpGet(url);
		get.addHeader("X-Beacon-Secret-Key", mSecretKey);
		return new Response(mClient.execute(get));
	}
	
	public Response post(String url, String body) throws IOException {
		final HttpPost post = new HttpPost(url);
		post.addHeader("X-Beacon-Secret-Key", mSecretKey);
		return new Response(mClient.execute(post));
	}

	public Response delete(String url) throws IOException{
		final HttpDelete delete = new HttpDelete(url);
		delete.addHeader("X-Beacon-Secret-Key", mSecretKey);
		return new Response(mClient.execute(delete));
	}
	
	public static class Response{
		private HttpResponse mResponse;
		
		public Response(HttpResponse response){
			mResponse = response;
		}
		
		public int getStatusCode(){
			return mResponse.getStatusLine().getStatusCode();
		}
		
		public String getBodyAsString() throws ParseException, IOException{
			return EntityUtils.toString(mResponse.getEntity());
		}
		
		public JSONObject getBodyAsJson() throws ParseException, JSONException, IOException{
			return new JSONObject(getBodyAsString());
		}
	}
}