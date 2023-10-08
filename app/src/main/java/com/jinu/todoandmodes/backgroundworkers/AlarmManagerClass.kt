package com.jinu.todoandmodes.backgroundworkers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmManagerClass:AlarmInterface {
	companion object{
		const val NOTIFY_ICON ="icon_notify"
		const val TITLE_NOTIFY = "Notify"
	}
	@SuppressLint("ScheduleExactAlarm")
	override fun setAlarm(context: Context, time: Long, hashCode: Int,icon:Int,title:String)  {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, AlarmReceiver::class.java)
		intent.putExtra(NOTIFY_ICON,icon)
		intent.putExtra(TITLE_NOTIFY,title)
		val pendingIntent = PendingIntent.getBroadcast(context, hashCode,intent, PendingIntent.FLAG_MUTABLE)
		alarmManager.setExact(AlarmManager.RTC_WAKEUP,time,pendingIntent)
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