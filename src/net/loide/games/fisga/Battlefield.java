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

			// desenhar f
			mPaint.setColor(0xFFCDE3A1);
			canvas.drawRect(0, 80, 480, 720, mPaint);

			// desenhar linha
			mPaint.setColor(0xFF888888);
			canvas.drawLine(200, mY, 280, mY, mPaint);

			mPaint.setColor(0xFFFF0000);
			canvas.drawLine(0, 0, 200, mY, mPaint);
			canvas.drawLine(480, 0, 280, mY, mPaint);
		}

		private float mX, mY;
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
			//TODO send Y position to server
			mX = 0;
			mY = 0;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
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