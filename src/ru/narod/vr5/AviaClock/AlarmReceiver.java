/*
* Copyright (C) 2015 Svyatozar <svyatozar2015@mail.ru> et al
* License: http://www.gnu.org/licenses/gpl-2.0.html GPL version 2
*/

package ru.narod.vr5.AviaClock;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.RemoteViews;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AviaClock");
		wl.acquire();

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		views.setImageViewBitmap(R.id.imageView1, ClockPainter.paint(context));

		ComponentName thiswidget = new ComponentName(context, Widget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
       	manager.updateAppWidget(thiswidget, views);

		wl.release();
	}
}
