package net.loide.games.fisga;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Entrymenu extends Activity implements OnClickListener {

	private static final String LOG_TAG = null;
	public String scannedText = "";
	public String scannedTextType = "";
	public static String serverIP = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.entrymenu);

		Button button1 = (Button) findViewById(R.id.startgameBtn);
		Button button2 = (Button) findViewById(R.id.scanserverBtn);
		Button button3 = (Button) findViewById(R.id.exitBtn);

		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);

		h.sendEmptyMessageDelayed(0, 100);

		Toast.makeText(this, getLocalIpAddress(), Toast.LENGTH_LONG).show();

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

	@Override
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
				Intent myIntent = new Intent(Entrymenu.this, Fisga_main.class);
				Entrymenu.this.startActivity(myIntent);
				break;
			}

		case R.id.scanserverBtn:
			// handle button A click;
			IntentIntegrator.initiateScan(this);
			break;

		case R.id.exitBtn:
			// handle button B click;
			Entrymenu.this.finish();
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		boolean validIP = false;
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			// handle scan result
			scannedTextType = scanResult.getFormatName().trim();

			if (scannedTextType.compareTo("QR_CODE") == 0) {
				scannedText = scanResult.getContents().trim();

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
					Toast.makeText(this,
							"resultado:" + scannedText + "-" + scannedTextType,
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, "código inválido", Toast.LENGTH_LONG)
					.show();
				}
			} else {
				Toast.makeText(this, "código inválido", Toast.LENGTH_LONG)
						.show();
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

}
