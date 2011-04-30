package net.loide.games.fisga;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Entrymenu extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
				
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.entrymenu);

		Button button1 = (Button) findViewById(R.id.startgameBtn);
		Button button2 = (Button) findViewById(R.id.exitBtn);

		button1.setOnClickListener(this);        
		button2.setOnClickListener(this);        
	
		h.sendEmptyMessageDelayed(0, 100);
		
	}
	
	Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ImageView img = (ImageView)findViewById(R.id.fundo);
			AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
			frameAnimation.start();
		}
	};	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startgameBtn:
			// handle button A click;
			Intent myIntent = new Intent(Entrymenu.this, Fisga_main.class);
			Entrymenu.this.startActivity(myIntent);
			break;

		case R.id.exitBtn:
			// handle button B click;
			Entrymenu.this.finish();
			break;
		}
	}


	
}
