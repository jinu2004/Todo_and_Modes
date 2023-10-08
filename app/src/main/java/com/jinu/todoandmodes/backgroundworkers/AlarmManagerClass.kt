package com.jinu.todoandmodes.backgroundworkers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmManagerClass:AlarmInterface {
	@SuppressLint("ScheduleExactAlarm")
	override fun setAlarm(context: Context, time: Long, hashCode: Int)  {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, AlarmReceiver::class.java)
		val pendingIntent = PendingIntent.getBroadcast(context, hashCode,intent, PendingIntent.FLAG_MUTABLE)
		alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,pendingIntent)
		Log.e("dateTime","$time")
	}

	override fun repeatingAlarm(context: Context, hashCode: Int, time: Long) {
		//
	}

	override fun cancelAlarm(context: Context, hashCode: Int) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val pendingIntent = PendingIntent.getBroadcast(context, hashCode,Intent(), PendingIntent.FLAG_MUTABLE)
		alarmManager.cancel(pendingIntent)
	}

}