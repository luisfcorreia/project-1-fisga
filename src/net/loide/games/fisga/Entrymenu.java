package net.loide.games.fisga;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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

		// Load the ImageView that will host the animation and
		// set its background to our AnimationDrawable XML resource.
		ImageView img = (ImageView)findViewById(R.id.fundo);
		img.setBackgroundResource(R.drawable.background);

		// Get the background, which has been compiled to an AnimationDrawable object.
		AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

		// Start the animation (looped playback by default).
		frameAnimation.start();
		
		Button button1 = (Button) findViewById(R.id.startgameBtn);
		Button button2 = (Button) findViewById(R.id.exitBtn);

		button1.setOnClickListener(this);        
		button2.setOnClickListener(this);        
	
	}

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
