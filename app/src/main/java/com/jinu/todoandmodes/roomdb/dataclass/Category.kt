package com.jinu.todoandmodes.roomdb.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("category")
data class Category(
	@PrimaryKey
	val primaryKey:Int,
	val icon:Int,
	val heading:String)
