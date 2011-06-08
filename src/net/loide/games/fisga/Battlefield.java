package net.loide.games.fisga;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Battlefield extends Activity {

	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(new MyView(this));

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFFFF0000);
		mPaint.setStrokeWidth(12);

	}

	private Paint mPaint;

	public class MyView extends View {

		private Canvas mCanvas;
		private int fingerDown = 0;
		private int constFixed = 42;

		public MyView(Context c) {
			super(c);

		}

		public void onCreate() {
			Config conf = Bitmap.Config.ARGB_8888;
			Bitmap mBitmap = Bitmap.createBitmap(480, 800, conf);
			setmCanvas(new Canvas(mBitmap));
		}

		@Override
		protected void onDraw(Canvas canvas) {
			
			// desenhar topo e rodapé
			mPaint.setColor(0xFF92C957);
			canvas.drawRect(0, 0, 480, 80, mPaint);
			canvas.drawRect(0, 720, 480, 800, mPaint);

			// desenhar fundo
			mPaint.setColor(0xFFCDE3A1);
			canvas.drawRect(0, 80, 480, 720, mPaint);

			// desenhar linha
			mPaint.setColor(0xFF964514);
			canvas.drawLine(200, mY, 280, mY, mPaint);

			// desenhar linhas do elástico
			mPaint.setColor(0xFFFF0000);
			canvas.drawLine( 40,  40, 200, mY, mPaint);
			canvas.drawLine(440,  40, 280, mY, mPaint);

			// desenhar umas bolinhas para esconder a imperfeição das linhas
			mPaint.setColor(0xFF964514);
            canvas.drawCircle(200, mY, 8, mPaint);
            canvas.drawCircle(280, mY, 8, mPaint);
			
            // desenhar os postes da fisga
			mPaint.setColor(0xFF603311);
            canvas.drawCircle( 40, 40,30, mPaint);
            canvas.drawCircle(440, 40,30, mPaint);
			
		}

		private float mX = constFixed;
		private float mY = constFixed;
		private static final float TOUCH_TOLERANCE = 4;

		private void touch_start(float x, float y) {
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mX = x;
				mY = y;
			}
		}

		private void touch_up() {
			// TODO send Y position to server
			mX = constFixed;
			mY = constFixed;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// só apanhar a fisga em cima
				if (y <= 80) {
					fingerDown = 1;
					touch_start(x, y);
					invalidate();
				}
				break;

			case MotionEvent.ACTION_MOVE:
				if (fingerDown == 1) {
					touch_move(x, y);
					invalidate();
				}
				break;

			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				fingerDown = 0;
				break;
			}
			return true;
		}

		public void setmCanvas(Canvas mCanvas) {
			this.mCanvas = mCanvas;
		}

		public Canvas getmCanvas() {
			return mCanvas;
		}
	}
}