package com.jinu.todoandmodes.roomdb.repository

import androidx.lifecycle.LiveData
import com.jinu.todoandmodes.roomdb.dao.Dao
import com.jinu.todoandmodes.roomdb.dataclass.Category
import com.jinu.todoandmodes.roomdb.dataclass.StepTask
import com.jinu.todoandmodes.roomdb.dataclass.TaskData

class RoomRepository(private val dao: Dao) {
	val allTask:LiveData<List<TaskData>> = dao.allTask()
	val allCategory:LiveData<List<Category>> =dao.allCategory()

	fun getListByStatus(status:Boolean):LiveData<List<TaskData>>{
		return dao.getListByStatus(status)
	}

	fun getByCategoryID(categoryId:Int):LiveData<List<TaskData>>{
		return dao.getByCategoryID(categoryId)
	}
	fun getAllStep(mainTaskId:Int):LiveData<List<StepTask>>{
		return dao.getAllStep(mainTaskId)
	}
	suspend fun addNewTask(taskData: TaskData){
		dao.addNewTask(taskData)
	}
	suspend fun addNewCategory(category: Category){
		dao.addNewCategory(category)
	}
	suspend fun addSub(stepTask: StepTask){
		dao.addSub(stepTask)
	}
}