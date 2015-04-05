/*
* Copyright (C) 2015 Svyatozar <svyatozar2015@mail.ru> et al
* License: http://www.gnu.org/licenses/gpl-2.0.html GPL version 2
*/

package ru.narod.vr5.AviaClock;

import java.util.Calendar;

import ru.narod.vr5.AviaClock.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ClockPainter {
	protected Bitmap pm, hour_pm, minute_pm, seconds_pm,
    	fl_hour_pm, fl_minute_pm,
    	timer_pm, timer_sec_pm;
	
	protected Point hour_origin, minute_origin, seconds_origin,
	    fl_hour_origin, fl_minute_origin,
	    timer_origin, timer_sec_origin;

	boolean flightClockIsRunning = false, timerIsRunning = false;
	int flightStartTime, flightCurrentTime, timerStartTime, timerCurrentTime;

	float dispScale;
	Rect button1, button2;

	Handler timer = new Handler();

	public ClockPainter(Context context) {
		this(context, null);
	}

	public ClockPainter(Context context, AttributeSet attrs) {
		pm           = BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.watches1));
	    seconds_pm   = BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.seconds));
	    minute_pm    = BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.minute));
	    hour_pm      = BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.hour));
	    fl_minute_pm = BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.fl_minute));
	    fl_hour_pm   = BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.fl_hour));
	    timer_pm     = BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.timer));
	    timer_sec_pm = BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.timer_sec));

	    seconds_origin   = new Point( seconds_pm.getWidth()/2,   seconds_pm.getHeight() - 86);
	    minute_origin    = new Point( minute_pm.getWidth()/2,    minute_pm.getHeight() - minute_pm.getWidth()/2);
	    hour_origin      = new Point( hour_pm.getWidth()/2,      hour_pm.getHeight() - hour_pm.getWidth()/2);
	    fl_minute_origin = new Point( fl_minute_pm.getWidth()/2, fl_minute_pm.getHeight() - fl_minute_pm.getWidth()/2);
	    fl_hour_origin   = new Point( fl_hour_pm.getWidth()/2,   fl_hour_pm.getHeight() - fl_hour_pm.getWidth()/2);
	    timer_origin     = new Point( timer_pm.getWidth()/2,     timer_pm.getHeight() - timer_pm.getWidth()/2);
	    timer_sec_origin = new Point( timer_sec_pm.getWidth()/2, timer_sec_pm.getHeight() - timer_sec_pm.getWidth()/2);

	    flightStartTime = flightCurrentTime = (int)(System.currentTimeMillis() / 1000);
	    timerStartTime  = timerCurrentTime  = flightStartTime;

	    button1 = new Rect(   0, 331,       94, 331 + 94 );
	    button2 = new Rect( 479, 331, 479 + 94, 331 + 94 );
	}

	public void onDraw(Canvas c) {
		float ratioW = (float)c.getWidth()  / pm.getWidth(),
			  ratioH = (float)c.getHeight() / pm.getHeight();
		dispScale = ratioW < ratioH? ratioW: ratioH;

		c.scale( dispScale, dispScale );
		c.drawBitmap(pm, 0, 0, null);

	    Calendar t = Calendar.getInstance();

/* flight time */
	    int flightH =  (flightCurrentTime - flightStartTime) / 60 / 60,
            flightM = ((flightCurrentTime - flightStartTime) / 60) % 60;

		PointF center = new PointF( 286, 150 );

	    c.save();
	    c.translate( center.x, center.y );
	    c.rotate( 360.0f * ((float)flightH + (float)flightM/60) / 12 );
	    c.translate( -fl_hour_origin.x, -fl_hour_origin.y );
	    c.drawBitmap(fl_hour_pm, 0, 0, null );
	    c.restore();

	    c.save();
	    c.translate( center.x, center.y );
	    c.rotate( 360.0f * (float)flightM/60 );
	    c.translate( -fl_minute_origin.x, -fl_minute_origin.y );
	    c.drawBitmap( fl_minute_pm, 0, 0, null );
	    c.restore();

/* timer */
	    int seconds =  (timerCurrentTime - timerStartTime) % 60,
            minutes = ((timerCurrentTime - timerStartTime) / 60) % 60;

	    center = new PointF( 286, 352 );

	    c.save();
	    c.translate( center.x, center.y );
	    c.rotate( 360.0f * (float)minutes/60 );
	    c.translate( -timer_origin.x, -timer_origin.y );
	    c.drawBitmap(timer_pm, 0, 0, null );
	    c.restore();

	    c.save();
	    c.translate( center.x, center.y );
	    c.rotate( 360.0f * (float)seconds/60 );
	    c.translate( -timer_sec_origin.x, -timer_sec_origin.y );
	    c.drawBitmap( timer_sec_pm, 0, 0, null );
	    c.restore();

/* wall time */
	    center = new PointF( pm.getWidth()/2 + 1, pm.getHeight()/2 + 2 );

	    c.save();
	    c.translate( center.x, center.y );
	    c.rotate( 360.0f * ((float)t.get(Calendar.HOUR) + (float)t.get(Calendar.MINUTE)/60) / 12 );
	    c.translate( -hour_origin.x, -hour_origin.y );
	    c.drawBitmap(hour_pm, 0, 0, null );
	    c.restore();

	    c.save();
	    c.translate( center.x, center.y );
	    c.rotate( 360.0f * (float)t.get(Calendar.MINUTE)/60 );
	    c.translate( -minute_origin.x, -minute_origin.y );
	    c.drawBitmap( minute_pm, 0, 0, null );
	    c.restore();

	    c.save();
	    c.translate( center.x, center.y );
	    c.rotate( 360.0f * (float)t.get(Calendar.SECOND)/60 );
	    c.translate( -seconds_origin.x, -seconds_origin.y );
	    c.drawBitmap( seconds_pm, 0, 0, null );
	    c.restore();
	}
	
	public void advanceHands() {
	    int currentTime = (int)(System.currentTimeMillis() / 1000);

	    if ( flightClockIsRunning ) flightCurrentTime = currentTime;
	    if ( timerIsRunning )       timerCurrentTime  = currentTime;
	}
	
	public int hintWidth() {
		return pm.getWidth();
	}
	
	public int hintHeight() {
		return pm.getHeight();
	}

// singleton-like API
	static ClockPainter painter = null;
	
	static public Bitmap paint(Context context) {
        if ( painter == null ) painter = new ClockPainter(context);

        Bitmap bitmap = Bitmap.createBitmap(painter.hintWidth(), painter.hintHeight(), Config.ARGB_4444);
        painter.onDraw( new Canvas(bitmap) );

        return bitmap;
	}
}
