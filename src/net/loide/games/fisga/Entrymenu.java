package net.loide.games.fisga;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Entrymenu extends Activity implements OnClickListener {

	private static final String LOG_TAG = null;
	public String scannedText = "";
	public String scannedTextType = "";
	public static String serverIP = "";
	public static int serverPORT = 8169;
	public boolean sensorrunning;
	public static String phoneAngle = "";
	static SoundPool soundPool;
	static HashMap<Integer, Integer> soundPoolMap;
	public static final int SOUND_THROW = 1;
	public static final int SOUND_YOU_WIN = 2;
	public static final int SOUND_YOU_LOSE = 3;
	public static Vibrator vibrator;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.entrymenu);

		SensorManager mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
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
		
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.cancel();
		

		Button button1 = (Button) findViewById(R.id.startgameBtn);
		Button button2 = (Button) findViewById(R.id.scanserverBtn);
		Button button3 = (Button) findViewById(R.id.exitBtn);

		button1.setVisibility(View.INVISIBLE);

		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);

		// iniciar a animação daqui a 500mS
		h.sendEmptyMessageDelayed(0, 500);
		initSounds();

	}

	Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ImageView img = (ImageView) findViewById(R.id.fundo);
			AnimationDrawable frameAnimation = (AnimationDrawable) img
					.getBackground();
			frameAnimation.start();
		}
	};

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startgameBtn:
			// handle button A click;

			if (scannedText.compareTo("") == 0) {
				Toast.makeText(this,
						"Primeiro tem que ler o código de barras!",
						Toast.LENGTH_LONG).show();
				break;
			} else {
				Intent myIntent = new Intent(Entrymenu.this, Battlefield.class);
				Entrymenu.this.startActivity(myIntent);

				break;
			}

		case R.id.scanserverBtn:
			// Ler um QR_Code
			IntentIntegrator.initiateScan(this);

			//Intent myIntent = new Intent(Entrymenu.this, Battlefield.class);
			//Entrymenu.this.startActivity(myIntent);

			break;

		case R.id.exitBtn:
			// handle button B click;
			Entrymenu.this.finish();
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		boolean validIP = false;
		// testar o resultado da chamada ZXing BarCodeScanner
		if (resultCode == RESULT_OK) {

			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, intent);
			if (scanResult != null) {
				// handle scan result
				scannedTextType = scanResult.getFormatName().trim();

				
				if (scannedTextType.compareTo("QR_CODE") == 0) {
					scannedText = scanResult.getContents().trim();
					Log.d("Scan", "Scanned text was: "+scannedText);

					// validar se o que foi recebido é de facto um IP
					validIP = true;
					if (!scannedText
							.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
						validIP = false;
					} else {
						String[] splits = scannedText.split("\\.");
						for (int i = 0; i < splits.length; i++) {
							if (Integer.valueOf(splits[i]) > 255) {
								validIP = false;
							}
						}
					}
					if (validIP) {
						serverIP = scannedText;
						Button button1 = (Button) findViewById(R.id.startgameBtn);
						Button button2 = (Button) findViewById(R.id.scanserverBtn);

						button1.setVisibility(View.VISIBLE);
						button2.setVisibility(View.INVISIBLE);

					} else {
						Toast.makeText(this, "código inválido",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(this, "código inválido", Toast.LENGTH_LONG)
							.show();
				}
			}
		}
	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(LOG_TAG, ex.toString());
		}
		return null;
	}

	public SensorEventListener mySensorEventListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent event) {

			int y = Math.round(event.values[1]);
			phoneAngle = String.valueOf((int) (Math.abs(y)));
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// Auto-generated method stub
		}
	};
	
	public static void sendData(String data) {
		try {
			InetAddress serverAddr = InetAddress.getByName(serverIP);
			Socket socket = new Socket(serverAddr, serverPORT);
				try {
					Log.d("ClientActivity", "C: Sending command.");
					PrintWriter out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(
									socket.getOutputStream())), true);
					// where you issue the commands
					out.println(data);
					Log.d("ClientActivity", "C: Sent.");
				} catch (Exception e) {
					Log.e("ClientActivity", "S: Error", e);
				}
			socket.close();
		} catch (Exception e) {
			Log.e("ClientActivity", "C: Error", e);
		}
	}

	private void initSounds() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(SOUND_THROW,
				soundPool.load(getBaseContext(), R.raw.catapult, 1));
		soundPoolMap.put(SOUND_YOU_WIN,
				soundPool.load(getBaseContext(), R.raw.laser_1, 1));
		soundPoolMap.put(SOUND_YOU_LOSE,
				soundPool.load(getBaseContext(), R.raw.laser_1, 1));
	}

	public void playSound(int sound) {
		AudioManager mgr = (AudioManager) getBaseContext().getSystemService(
				Context.AUDIO_SERVICE);
		int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1,
				0, 1f);
	}
}
