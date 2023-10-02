package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.jinu.todoandmodes.DetailsOfTask
import com.jinu.todoandmodes.TaskView
import com.jinu.todoandmodes.databinding.TaskRecyclerviewItemViewBinding
import com.jinu.todoandmodes.mvvm.dataclass.TaskData
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class TodoListAdapter(private val list: List<TaskData>, private val roomViewModel: RoomViewModel) :
	RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {
	inner class ViewHolder(val binding: TaskRecyclerviewItemViewBinding) :
		RecyclerView.ViewHolder(binding.root)



	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = TaskRecyclerviewItemViewBinding.inflate(LayoutInflater.from(parent.context))
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return list.size
	}

	@SuppressLint("ResourceType", "PrivateResource")
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val data = list[position]
		holder.binding.dueTime.text = formatMillisecondsToDate(data.dueDate!!)
		holder.binding.check.text = data.taskName
		holder.binding.check.isChecked = data.taskStatus!!
		holder.binding.check.setOnCheckedChangeListener { _, checked ->
			val execute = ContextCompat.getMainExecutor(holder.itemView.context)
			execute.execute {
				updateStatus(checked,data)
			}

		}
		if (data.dueDate < MaterialDatePicker.todayInUtcMilliseconds() && data.taskStatus == false) {
			if (isDarkMode(holder.binding.root.context)) {
				val color = ContextCompat.getColor(
					holder.binding.root.context,
					R.color.error_color_material_dark
				)
				holder.binding.dueTime.setTextColor(color)
				holder.binding.imageView.setColorFilter(color)
			}
			else{
				val color = ContextCompat.getColor(
					holder.binding.root.context,
					R.color.error_color_material_light
				)
				holder.binding.dueTime.setTextColor(color)
				holder.binding.imageView.setColorFilter(color)
			}
		}

		holder.binding.card.setOnClickListener{
			val intent = Intent(holder.binding.root.context, DetailsOfTask::class.java)
			intent.putExtra(TaskView.PASSING_PRIMARY_KEY,list[position].primaryKey)
			startActivity(holder.binding.root.context,intent, Bundle())

		}

		if (data.taskStatus!!){
			if (isDarkMode(holder.itemView.context)){
				val color = ContextCompat.getColor(holder.itemView.context,R.color.m3_sys_color_dark_outline)
				val colorList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context,R.color.m3_sys_color_dark_outline))
				holder.binding.check.buttonTintList = colorList
				holder.binding.check.setTextColor(color)
				holder.binding.cardView2.setCardBackgroundColor(color)
				holder.binding.imageView.setColorFilter(color)
				holder.binding.dueTime.setTextColor(color)
				holder.binding.star.setColorFilter(color)

			}
			else{
				val colorList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context,R.color.m3_sys_color_light_outline))
				val color = ContextCompat.getColor(holder.itemView.context,R.color.m3_sys_color_light_outline)
				holder.binding.check.buttonTintList = colorList
				holder.binding.check.setTextColor(color)
				holder.binding.cardView2.setCardBackgroundColor(color)
				holder.binding.imageView.setColorFilter(color)
				holder.binding.dueTime.setTextColor(color)
				holder.binding.star.setColorFilter(color)

			}

		}

		roomViewModel.allCategory.observeForever { it ->
			val filter = it.filter { it.primaryKey == data.categoryId }
			val currentItemCategory = filter.first()
			holder.binding.categoryImage.setImageResource(currentItemCategory.icon!!)
		}






	}

	@SuppressLint("NewApi")
	fun formatMillisecondsToDate(milliseconds: Long): String {
		val instant = Instant.ofEpochMilli(milliseconds)
		val zoneId = ZoneId.systemDefault() // You can change this to your desired time zone
		val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") // Define your desired date format
		return formatter.format(instant.atZone(zoneId))
	}

	private fun isDarkMode(context: Context): Boolean {
		val currentNightMode =
			context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
		return currentNightMode == Configuration.UI_MODE_NIGHT_YES
	}
	private fun updateStatus(status: Boolean, data: TaskData) {
		data.taskStatus = status
		if (status) data.taskDoneDate = MaterialDatePicker.todayInUtcMilliseconds()
		else data.taskDoneDate = null
		roomViewModel.updateTask(data)
	}
}