/*
* Copyright (C) 2015 Svyatozar <svyatozar2015@mail.ru> et al
* License: http://www.gnu.org/licenses/gpl-2.0.html GPL version 2
*/

package ru.narod.vr5.AviaClock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider
{
	@Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids)
    {
    	RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setImageViewBitmap(R.id.imageView1, ClockPainter.paint(context));
       	manager.updateAppWidget(ids, views);
    }

// alarm manager
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000, 1000 , pi);
	}

	@Override
	public void onDisabled(Context context) {
		  Intent intent = new Intent(context, AlarmReceiver.class);
		  PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		  AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		  alarmManager.cancel(sender);
		  super.onDisabled(context);
	}
}
