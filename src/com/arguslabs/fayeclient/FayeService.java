package com.arguslabs.fayeclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class FayeService extends Service {
	
	final private String TAG = getClass().getSimpleName();
	
	final private static String FAYE_HOST = "ws://argus4.arguslabs.be";
	final private static String FAYE_PORT = "5556";
	final private static String authToken = "27328fa4afd993840427fb5c971766e1c035e1c221ca93a5ddbad201bb04df86";
	final private static String mainChannel = String.format("/private/%s", "4ed37ce10bd771759900002b");

	FayeClient fayeClient;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		stopFaye();
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		startFaye();
	}
	
	/**
	 * FayeClientListener
	 */
	FayeClientListener fayeClientListener = new FayeClientListener() {
		
		@Override
		public void messageReceieved(FayeClient fc, String msg) {
			Log.i(TAG, "MESSAGE FROM SERVER: " + msg);	
			Toast.makeText(getApplicationContext(), "Message from server: " + msg, Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void disconnectedFromServer(FayeClient fc) {
			Log.i(TAG, "DISCONNECTED FROM SERVER");
			Toast.makeText(getApplicationContext(), "Disconnected from faye server", Toast.LENGTH_LONG).show();
			fc.connectToServer(); //reconnect when disconnect
		}
		
		@Override
		public void connectedToServer(FayeClient fc) {
			Log.i(TAG, "CONNECTED TO SERVER");
			fc.subscribeToChannel(mainChannel);
			Toast.makeText(getApplicationContext(), "Connected to faye server on channel: " + mainChannel , Toast.LENGTH_LONG).show();
		}
	};
	
	
	/**
	 * Public section
	 */
	private void startFaye() {	
		fayeClient = new FayeClient(FAYE_HOST + ":" + FAYE_PORT + "/faye", authToken, mainChannel);
        fayeClient.setListener(fayeClientListener);
        fayeClient.connectToServer();
        Toast.makeText(getApplicationContext(), "FayeService Started!", Toast.LENGTH_LONG).show();
	}
	public void stopFaye() {
		if(fayeClient.isWebSocketConnected()) fayeClient.disconnectFromServer();
		Toast.makeText(getApplicationContext(), "FayeService Stopped!", Toast.LENGTH_LONG).show();
	}
}

