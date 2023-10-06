package com.jinu.todoandmodes.backgroundworkers

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jinu.todoandmodes.R

class AlarmReceiver: BroadcastReceiver() {
	override fun onReceive(context: Context?, intent: Intent?) {
		showNotification(context)
	}
	@SuppressLint("MissingPermission")
	private fun showNotification(context: Context?) {
		createNotificationChannel(context)

		val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
			.setSmallIcon(R.drawable.notification_13_svgrepo_com)
			.setContentTitle("Notification Title")
			.setContentText("This is the notification content.")
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.build()

		val notificationManager = NotificationManagerCompat.from(context)
		notificationManager.notify(NOTIFICATION_ID, notification)
	}

	private fun createNotificationChannel(context: Context?) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				CHANNEL_ID,
				"My Notification Channel",
				NotificationManager.IMPORTANCE_DEFAULT
			)
			val notificationManager =
				context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			notificationManager.createNotificationChannel(channel)
		}
	}

	companion object {
		const val CHANNEL_ID = "channel_id"
		const val NOTIFICATION_ID = 1
	}
}