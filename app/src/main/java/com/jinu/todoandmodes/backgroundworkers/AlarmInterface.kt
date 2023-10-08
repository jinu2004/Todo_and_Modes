package com.jinu.todoandmodes.backgroundworkers

import android.content.Context

interface AlarmInterface {
	fun setAlarm(context: Context,time:Long,hashCode: Int,icon:Int,title:String)
	fun repeatingAlarm(context: Context,hashCode: Int,time:Long)
	fun cancelAlarm(context: Context,hashCode: Int)
}