package com.jinu.todoandmodes.roomdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jinu.todoandmodes.roomdb.dao.Dao
import com.jinu.todoandmodes.roomdb.dataclass.Category
import com.jinu.todoandmodes.roomdb.dataclass.StepTask
import com.jinu.todoandmodes.roomdb.dataclass.TaskData

@Database(
	entities = [Category::class, TaskData::class, StepTask::class],
	version = 1,
	exportSchema = false
)
abstract class RoomData : RoomDatabase() {
	abstract fun  dao():Dao
	companion object{
		@Volatile
		private var INSTANCE:RoomData?=null

		fun getDatabase(context: Context):RoomData{
			val tempInstance = INSTANCE
			if (tempInstance != null) return tempInstance

			synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					RoomData::class.java,
					"TodoData"
				).build()

				INSTANCE = instance
				return instance
			}
		}

	}
}