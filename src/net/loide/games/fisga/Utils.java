package net.loide.games.fisga;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Utils {

	private static SoundPool soundPool;
	private static HashMap<Integer, Integer> soundPoolMap;
	public static final int SOUND_EXPLOSION = 1;
	public static final int SOUND_YOU_WIN = 2;
	public static final int SOUND_YOU_LOSE = 3;
	

	static void initSounds() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(SOUND_EXPLOSION, soundPool.load(getContext(), R.raw.laser_1, 1));
	}
			
	public void playSound(int sound) {
		AudioManager mgr = (AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE);
		int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1, 0, 1f);
	}
}
