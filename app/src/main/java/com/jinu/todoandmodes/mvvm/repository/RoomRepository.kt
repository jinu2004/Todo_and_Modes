package com.jinu.todoandmodes.mvvm.repository

import androidx.lifecycle.LiveData
import com.jinu.todoandmodes.mvvm.dao.Dao
import com.jinu.todoandmodes.mvvm.dataclass.Category
import com.jinu.todoandmodes.mvvm.dataclass.StepTask
import com.jinu.todoandmodes.mvvm.dataclass.TaskData

class RoomRepository(private val dao: Dao) {
	val allTask:LiveData<List<TaskData>> = dao.allTask()
	val allTaskToDone:LiveData<List<TaskData>> = dao.allTaskDone()
	val allCategory:LiveData<List<Category>> =dao.allCategory()

	fun getListByStatus(status:Boolean):LiveData<List<TaskData>>{
		return dao.getListByStatus(status)
	}




	fun getByCategoryID(categoryId:Int):LiveData<List<TaskData>>{
		return dao.getByCategoryID(categoryId)
	}
	suspend fun getByID(primaryKey:Int):TaskData{
		return dao.getByID(primaryKey)
	}

	suspend fun getCategoryById(primaryKey:Int):Category{
		return dao.getCategoryById(primaryKey)
	}

	fun getCountByCategory(categoryId:Int):Int{
		return dao.getCountByCategory(categoryId)
	}

	fun getAllStep(mainTaskId:Int):LiveData<List<StepTask>>{
		return dao.getAllStep(mainTaskId)
	}

	fun searchResult(query: String?):LiveData<List<TaskData>>{
		return dao.searchResult(query)
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
	suspend fun updateTask(taskData: TaskData){
		dao.updateTask(taskData)
	}
	suspend fun deleteStep(stepTask: StepTask){
		dao.deleteStep(stepTask)
	}
	suspend fun deleteTask(taskData: TaskData){
		dao.deleteTask(taskData)
	}
}