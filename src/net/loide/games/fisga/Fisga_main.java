package net.loide.games.fisga;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Fisga_main extends Activity {
	private static SensorManager mySensorManager;
	private boolean sensorrunning;
	private Vibrator vibrator;
	private static SoundPool soundPool;
	private static HashMap<Integer, Integer> soundPoolMap;
	private static String message;
	private int mood_change;
	private Thread cThread;

	public static final int SOUND_EXPLOSION = 1;
	public static final int SOUND_YOU_WIN = 2;
	public static final int SOUND_YOU_LOSE = 3;

	private String serverIpAddress = "192.168.69.2";
	private int serverPort = 8169;
	private boolean connected = false;

	TextView textviewAzimuth, textviewPitch, textviewRoll, textviewMsg;
	float x, y, z;
	float last_x = 0, last_y = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		textviewAzimuth = (TextView) findViewById(R.id.textazimuth);
		textviewPitch = (TextView) findViewById(R.id.textpitch);
		textviewRoll = (TextView) findViewById(R.id.textroll);
		textviewMsg = (TextView) findViewById(R.id.dados);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.cancel();

		// Toast.makeText(this, "movement sensor tests",
		// Toast.LENGTH_LONG).show();

		initSounds();
		message = "0";
		mood_change = 10;

		serverIpAddress = Entrymenu.serverIP;
		
		mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> mySensors = mySensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);

		if (mySensors.size() > 0) {

			mySensorManager.registerListener(mySensorEventListener,
					mySensors.get(0), SensorManager.SENSOR_DELAY_GAME);
			// .get(0), SensorManager.SENSOR_DELAY_UI);

			sensorrunning = true;

		} else {
			Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG)
					.show();
			sensorrunning = false;
			finish();
		}

		if (!connected) {
			if (!serverIpAddress.equals("")) {
				cThread = new Thread(new ClientThread());
				cThread.setDaemon(true);
			}
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			// handle scan result
			connected = false;
			serverIpAddress = scanResult.getContents().trim();
			cThread.start();
			connected = true;

			Toast.makeText(this, "resultado:" + scanResult.getContents(),
					Toast.LENGTH_LONG).show();
		}
		// else continue with any other code you need in the method
		// ...
	}

	private SensorEventListener mySensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {

			x = Math.round(event.values[0]);
			y = Math.round(event.values[1]);
			z = Math.round(event.values[2]);

			if (last_y - y > mood_change) {
				vibrator.vibrate(10);
				playSound(SOUND_EXPLOSION);
			}
			last_y = y;

			message = String.valueOf((int) (Math.abs(y)));

			textviewAzimuth.setText("Azimuth: " + String.valueOf(x));
			textviewPitch.setText("Pitch: " + String.valueOf(y));
			// textviewRoll.setText("Roll: " + String.valueOf(z));
			textviewRoll.setText("server IP: " + serverIpAddress);
			textviewMsg.setText("Message: " + message);

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	public class ClientThread implements Runnable {

		public void run() {
			try {
				InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
				Log.d("ClientActivity", "C: Connecting...");
				Socket socket = new Socket(serverAddr, serverPort);
				connected = true;
				while (connected) {
					try {
						Log.d("ClientActivity", "C: Sending command.");
						PrintWriter out = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(
										socket.getOutputStream())), true);
						// where you issue the commands
						out.println(message);
						Log.d("ClientActivity", "C: Sent.");
					} catch (Exception e) {
						Log.e("ClientActivity", "S: Error", e);
					}
				}
				socket.close();
				Log.d("ClientActivity", "C: Closed.");
			} catch (Exception e) {
				Log.e("ClientActivity", "C: Error", e);
				connected = false;
			}
		}
	}

	private void initSounds() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(SOUND_EXPLOSION,
				soundPool.load(getBaseContext(), R.raw.laser_1, 1));
	}

	public void playSound(int sound) {
		AudioManager mgr = (AudioManager) getBaseContext().getSystemService(
				Context.AUDIO_SERVICE);
		int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1,
				0, 1f);
	}

	// Menu stuff
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.new_game:
			
			Intent myIntent = new Intent(Fisga_main.this, Battlefield.class);
			Fisga_main.this.startActivity(myIntent);
			
			// newGame();
			return true;
		case R.id.help:
			// showHelp();
			return true;
		case R.id.exit:
			this.finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {

		vibrator.vibrate(500);
		super.onDestroy();

		if (sensorrunning) {
			mySensorManager.unregisterListener(mySensorEventListener);
			Toast.makeText(Fisga_main.this, "unregisterListener",
					Toast.LENGTH_SHORT).show();
			sensorrunning = false;
			connected = false;
		}

	}

}