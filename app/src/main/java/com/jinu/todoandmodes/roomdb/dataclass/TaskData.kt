package com.jinu.todoandmodes.roomdb.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("TaskTable")
data class TaskData(
	@PrimaryKey
	val primaryKey:Int?=null,
	val taskName:String?=null,
	val startDate:Long?=null,
	val dueDate:Long?=null,
	val time: Long?=null,
	val taskStatus:Boolean?=null,
	val notifyEnable:Boolean?=null,
	val notify: Long?=null,
	val repeatEnable:Boolean?=null,
	val repeatable:String?=null,
	val description:String?=null,
	val category:String?=null,
	val categoryId:Int?=null
)
