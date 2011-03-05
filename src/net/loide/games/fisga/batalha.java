package net.loide.games.fisga;

import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

public class batalha extends Activity {
	TextView textviewAzimuth, textviewPitch, textviewRoll;
	float x, y, z;
	
	float last_x = 0, last_y = 0;

	private static SensorManager mySensorManager;
	private boolean sensorrunning;
	private Vibrator vibrator;

	public static final int SOUND_EXPLOSION = 1;
	public static final int SOUND_YOU_WIN = 2;
	public static final int SOUND_YOU_LOSE = 3;
	
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		textviewAzimuth = (TextView) findViewById(R.id.textazimuth);
		textviewPitch = (TextView) findViewById(R.id.textpitch);
		textviewRoll = (TextView) findViewById(R.id.textroll);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		Toast.makeText(this, "movement sensor tests", Toast.LENGTH_LONG).show();
		
		vibrator.cancel();
		
		initSounds();

		mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> mySensors = mySensorManager
		.getSensorList(Sensor.TYPE_ORIENTATION);

		if (mySensors.size() > 0) {
			mySensorManager.registerListener(mySensorEventListener, mySensors
					.get(0), SensorManager.SENSOR_DELAY_NORMAL);
			sensorrunning = true;
		//	Toast.makeText(this, "Start ORIENTATION Sensor", Toast.LENGTH_LONG).show();
		} else {
		//	Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG).show();
			sensorrunning = false;
			finish();
		}

	}

	private SensorEventListener mySensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub

			x = Math.round(event.values[0]);
			y = Math.round(event.values[1]);
			z = Math.round(event.values[2]);
			
			if (last_y-y > 5) {
				vibrator.vibrate(100);
				playSound(SOUND_EXPLOSION);
			}
			last_y = y;

			textviewAzimuth.setText("Azimuth: " + String.valueOf(x));
			textviewPitch.setText("Pitch: " + String.valueOf(y));
			textviewRoll.setText("Roll: " + String.valueOf(z));

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};


	private void initSounds() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(SOUND_EXPLOSION, soundPool.load(getBaseContext(), R.raw.laser_1, 1));
	}
			
	public void playSound(int sound) {
		AudioManager mgr = (AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE);
		int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1, 0, 1f);
	}


	/*
	 * public void onClick(View v) { if (v == button) { vibrator.vibrate(500); }
	 * }
	 */
	@Override
	protected void onDestroy() {

		super.onDestroy();

		if (sensorrunning) {
			mySensorManager.unregisterListener(mySensorEventListener);
			Toast.makeText(batalha.this, "unregisterListener",
					Toast.LENGTH_SHORT).show();
			vibrator.vibrate(500);
			vibrator.cancel();
		}
	}

}
