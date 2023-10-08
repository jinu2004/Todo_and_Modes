package com.jinu.todoandmodes.backgroundworkers

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jinu.todoandmodes.R

class AlarmReceiver: BroadcastReceiver() {
	@SuppressLint("UnsafeProtectedBroadcastReceiver")
	override fun onReceive(context: Context?, intent: Intent?) {
		val  icon = intent?.getIntExtra(AlarmManagerClass.NOTIFY_ICON,R.drawable.notification_13_svgrepo_com)
		val  title = intent?.getStringExtra(AlarmManagerClass.TITLE_NOTIFY)
		if (icon != null && title != null) {
			showNotification(context,icon,title)
		}
	}

	@SuppressLint("MissingPermission")
	private fun showNotification(context: Context?,icon:Int,content:String) {
		createNotificationChannel(context)

		val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
			.setSmallIcon(icon)
			.setContentTitle("Todo and Modes")
			.setContentText(content)
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.extend(NotificationCompat.WearableExtender().setBridgeTag("tagOne"))
			.build()

		val notificationManager = NotificationManagerCompat.from(context)
		notificationManager.notify(NOTIFICATION_ID, notification)
		Log.e("alarm","triggered")
	}

	private fun createNotificationChannel(context: Context?) {
		val channel = NotificationChannel(
			CHANNEL_ID,
			"Todo and Modes",
			NotificationManager.IMPORTANCE_DEFAULT
		)
		val notificationManager =
			context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.createNotificationChannel(channel)
	}

	companion object {
		const val CHANNEL_ID = "channel_id"
		const val NOTIFICATION_ID = 1
	}
}