package com.jinu.todoandmodes.roomdb.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinu.todoandmodes.roomdb.database.RoomData
import com.jinu.todoandmodes.roomdb.dataclass.Category
import com.jinu.todoandmodes.roomdb.dataclass.StepTask
import com.jinu.todoandmodes.roomdb.dataclass.TaskData
import com.jinu.todoandmodes.roomdb.repository.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {

	private val repository: RoomRepository
	private val allTask: LiveData<List<TaskData>>
	private val allCategory: LiveData<List<Category>>

	init {
		val database = RoomData.getDatabase(application).dao()
		repository = RoomRepository(database)
		allCategory = repository.allCategory
		allTask = repository.allTask
	}

	fun getListByStatus(status: Boolean): LiveData<List<TaskData>> {
		return repository.getListByStatus(status)
	}

	fun getByCategoryID(categoryId: Int): LiveData<List<TaskData>> {
		return repository.getByCategoryID(categoryId)
	}

	fun getAllStep(mainTaskId: Int): LiveData<List<StepTask>> {
		return repository.getAllStep(mainTaskId)
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
}