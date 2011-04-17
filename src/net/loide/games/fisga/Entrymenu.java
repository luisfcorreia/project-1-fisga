package net.loide.games.fisga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
	}

	@Override
    public void onClick(View v) 
    {
		switch(v.getId())
    	{
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
	
/*

if (yesButton.getId() == ((Button) v).getId()  ) 

{ // remainingNumber } 

else if (noButton.getId() == ((Button) v).getId()) { // it was the second button }





    @Override
    public void onClick(View src) {
      Intent i = new Intent(this, IntentA.class);
      startActivity(i);
    }







public void onClick(View v) {
}


private OnClickListener matrix = new OnClickListener() 
{
    public void onClick(View v) 
    {
    	finish();
    	Intent myIntent = new Intent(mainScreen.this, matrix.class);
    	mainScreen.this.startActivity(myIntent);
    }		    			    	
};



 * 
 * tipo isto:

Intent i = new Intent(..);
startActivity(i);

[pedro.n.veloso] no constructor, tens varios. o mais pratico para Activities 
é colocas o contexto da tua activity actual como 1º paramentro, e a class de onde queres ir no segundo tipo

i = new Intent(PrimeiraActivity.this,SegundaActivity.class);

[pedro.n.veloso] basta isso

 */

