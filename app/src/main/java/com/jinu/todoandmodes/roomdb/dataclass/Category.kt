package com.jinu.todoandmodes.roomdb.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("category")
data class Category(
	@PrimaryKey
	val primaryKey:Int?=null,
	val icon:Int?=null,
	val heading:String?=null)
