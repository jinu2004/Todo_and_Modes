package com.jinu.todoandmodes.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jinu.todoandmodes.mvvm.database.RoomData
import com.jinu.todoandmodes.mvvm.dataclass.Category
import com.jinu.todoandmodes.mvvm.dataclass.StepTask
import com.jinu.todoandmodes.mvvm.dataclass.TaskData
import com.jinu.todoandmodes.mvvm.repository.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {

	private val repository: RoomRepository
	val allTask: LiveData<List<TaskData>>
	val allTaskToDane : LiveData<List<TaskData>>
	val allCategory: LiveData<List<Category>>

	init {
		val database = RoomData.getDatabase(application).dao()
		repository = RoomRepository(database)
		allCategory = repository.allCategory
		allTask = repository.allTask
		allTaskToDane = repository.allTaskToDone
	}

	fun getListByStatus(status: Boolean): LiveData<List<TaskData>> {
		return repository.getListByStatus(status)
	}

	fun getByCategoryID(categoryId: Int): LiveData<List<TaskData>> {
		return repository.getByCategoryID(categoryId)
	}
	fun getCountByCategory(categoryId:Int):Int{
		return repository.getCountByCategory(categoryId)
	}

	fun getAllStep(mainTaskId: Int): LiveData<List<StepTask>> {
		return repository.getAllStep(mainTaskId)
	}
	fun getByID(primaryKey:Int):TaskData?{
		var data:TaskData?=null
		viewModelScope.launch(Dispatchers.IO) {
		 data = repository.getByID(primaryKey)
		}
		return data
	}

	fun addNewTask(task: TaskData) {
		viewModelScope.launch(Dispatchers.IO) {
			repository.addNewTask(task)
		}
	}

	fun addNewCategory(category: Category) {
		viewModelScope.launch(Dispatchers.IO) { repository.addNewCategory(category) }
	}

	fun addSub(stepTask: StepTask) {
		viewModelScope.launch(Dispatchers.IO) { repository.addSub(stepTask) }
	}
	fun updateTask(taskData: TaskData){viewModelScope.launch(Dispatchers.IO) { repository.updateTask(taskData) }}

	fun deleteStep(stepTask: StepTask) {
		viewModelScope.launch(Dispatchers.IO) { repository.deleteStep(stepTask) }
	}
}