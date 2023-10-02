package com.jinu.todoandmodes.mvvm.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("TaskTable")
data class TaskData(
	@PrimaryKey
	val primaryKey:Int?=null,
	var taskName:String?=null,
	val startDate:Long?=null,
	val dueDate:Long?=null,
	val time: Long?=null,
	var taskStatus:Boolean?=null,
	var taskDoneDate:Long?=null,
	val notifyEnable:Boolean?=null,
	val notify: Long?=null,
	val repeatEnable:Boolean?=null,
	val repeatable:String?=null,
	val description:String?=null,
	var category:String?=null,
	var categoryId:Int?=null
)
