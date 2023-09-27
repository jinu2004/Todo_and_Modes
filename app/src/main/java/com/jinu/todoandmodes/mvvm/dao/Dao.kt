package com.jinu.todoandmodes.mvvm.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jinu.todoandmodes.mvvm.dataclass.Category
import com.jinu.todoandmodes.mvvm.dataclass.StepTask
import com.jinu.todoandmodes.mvvm.dataclass.TaskData

@Dao
interface Dao {
	@Query("SELECT*FROM TaskTable")
	fun allTask():LiveData<List<TaskData>>
	@Query("SELECT*FROM TaskTable WHERE taskStatus == 0")
	fun allTaskDone():LiveData<List<TaskData>>
	@Query("SELECT*FROM category")
	fun allCategory():LiveData<List<Category>>

	@Query("select count(*) from TaskTable")
	fun count():Int


	@Query("SELECT*FROM TaskTable WHERE categoryId LIKE:categoryId")
	fun getByCategoryID(categoryId:Int):LiveData<List<TaskData>>
	@Query("SELECT*FROM TaskTable WHERE categoryId LIKE:categoryId and taskStatus == 1")
	fun getByCategoryIDDone(categoryId:Int):LiveData<List<TaskData>>

	@Query("SELECT*FROM TaskTable WHERE primaryKey LIKE:primaryKey")
	fun getByID(primaryKey:Int):TaskData

	@Query("SELECT*FROM TaskTable WHERE taskStatus LIKE:status")
	fun getListByStatus(status:Boolean):LiveData<List<TaskData>>

	@Query("SELECT*FROM StepTable WHERE id LIKE:mainTaskId")
	fun getAllStep(mainTaskId:Int):LiveData<List<StepTask>>

	@Query("SELECT COUNT(*) FROM TaskTable WHERE categoryId = :categoryId")
	fun getCountByCategory(categoryId:Int):Int


	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun addNewTask(taskData: TaskData)

	@Update
	suspend fun updateTask(taskData: TaskData)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun addNewCategory(category: Category)
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun addSub(stepTask: StepTask)



}