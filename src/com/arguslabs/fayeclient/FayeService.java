package com.arguslabs.fayeclient;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class FayeService extends Service implements FayeClientListener{
	
	/*
	 * 05-13 02:11:03.714: ERROR/AndroidRuntime(1279): java.nio.channels.UnresolvedAddressException
05-13 02:11:03.714: ERROR/AndroidRuntime(1279):     at org.apache.harmony.nio.internal.SocketChannelImpl.validateAddress(SocketChannelImpl.java:623)
05-13 02:11:03.714: ERROR/AndroidRuntime(1279):     at org.apache.harmony.nio.internal.SocketChannelImpl.connect(SocketChannelImpl.java:249)
05-13 02:11:03.714: ERROR/AndroidRuntime(1279):     at com.arguslabs.whooping.WebSocket.run(WebSocket.java:340)
05-13 02:11:03.714: ERROR/AndroidRuntime(1279):     at java.lang.Thread.run(Thread.java:1102)

	 */
	final String TAG = getClass().getSimpleName();
	Resources res;
	FayeClient fayeClient;
	String mainChannel = "/1";
	
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
	@Override
	public void connectedToServer(FayeClient fc) {
		Log.i(TAG, "CONNECTED TO SERVER");
		fc.subscribeToChannel(mainChannel);
		Toast.makeText(getApplicationContext(), "Connected to faye server on channel: " + mainChannel , Toast.LENGTH_LONG).show();
	}


	@Override
	public void disconnectedFromServer(FayeClient fc) {
		Log.i(TAG, "DISCONNECTED FROM SERVER");
		Toast.makeText(getApplicationContext(), "Disconnected from faye server", Toast.LENGTH_LONG).show();
	}


	@Override
	public void messageReceieved(FayeClient fc, String msg) {
		Log.i(TAG, "MESSAGE FROM SERVER: " + msg);	
		
//		String fayeInput = "{\"poke\":{\"id\":6,\"to_user_id\":1,\"from_user\":{\"id\":8,\"phone_nr\":\"(881)035-3514\",\"first_name\":\"Delphine\",\"user_name\":\"rodolfo\",\"last_location_id\":null,\"updated_at\":\"2011-05-03T13:06:31Z\",\"last_name\":\"Swift\",\"birthdate\":\"1965-02-26T23:00:00Z\",\"gender\":2,\"created_at\":\"2011-05-03T13:03:27Z\",\"photo\":{\"url\":\"/uploads/userphotos/bcfc3e6473f52433622bf9a5e67eafa615c68891/female_2.png\"},\"disabled\":null},\"created_at\":\"2011-05-12T21:05:19Z\",\"from_user_id\":8,\"received\":null,\"updated_at\":\"2011-05-12T21:05:19Z\"}}";
//		MyNotificationManager notificationMgr = new MyNotificationManager(getApplicationContext());
//		notificationMgr.manageNotification(msg);
	}
	
	/**
	 * Public section
	 */
	final private static String FAYE_HOST = "http://argus4.arguslabs.be/faye";
	final private static String FAYE_PORT = "5555";
	private void startFaye() {
		String userId = "dummyid";
		mainChannel = String.format("/private/%s", userId);
		fayeClient = new FayeClient(FAYE_HOST + ":" + FAYE_PORT + "/faye", mainChannel);
        fayeClient.setListener(this);
        fayeClient.connectToServer();
        Toast.makeText(getApplicationContext(), "FayeService Started!", Toast.LENGTH_LONG).show();
	}
	public void stopFaye() {
		if(fayeClient.isWebSocketConnected()) fayeClient.disconnectFromServer();
		Toast.makeText(getApplicationContext(), "FayeService Stopped!", Toast.LENGTH_LONG).show();
	}

	
	
}

