package com.jinu.todoandmodes.roomdb.dataclass

import androidx.room.Entity

@Entity("StepTable")
data class StepTask(val id:Int,val text:String,val state:Boolean)
