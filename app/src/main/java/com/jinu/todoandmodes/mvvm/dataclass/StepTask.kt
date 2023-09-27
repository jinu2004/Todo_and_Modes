package com.jinu.todoandmodes.mvvm.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("StepTable")
data class StepTask(
	@PrimaryKey
	val primaryKey:Int,
	val id:Int,
	val text:String,
	val state:Boolean)
