package com.jinu.todoandmodes.roomdb.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Time

@Entity("TaskTable")
data class TaskData(
	@PrimaryKey
	val primaryKey:Int,
	val taskName:String,
	val startDate: Date,
	val dueDate:Date,
	val time: Time,
	val taskStatus:Boolean,
	val notifyEnable:Boolean,
	val notify: Time,
	val repeatEnable:Boolean,
	val repeatable:String,
	val description:String,
	val category:String,
	val categoryId:Int
)
