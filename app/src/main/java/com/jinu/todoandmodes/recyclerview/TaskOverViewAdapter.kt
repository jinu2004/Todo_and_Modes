package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.jinu.todoandmodes.databinding.TaskGroupLayoutBinding
import com.jinu.todoandmodes.roomdb.dataclass.Category
import com.jinu.todoandmodes.roomdb.viewmodel.RoomViewModel

class TaskOverViewAdapter(val list: List<Category>, private val roomViewModel: RoomViewModel) :
	RecyclerView.Adapter<TaskOverViewAdapter.ViewHolder>() {
	inner class ViewHolder(val binding: TaskGroupLayoutBinding) :
		RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int,
	): TaskOverViewAdapter.ViewHolder {
		val view = TaskGroupLayoutBinding.inflate(LayoutInflater.from(parent.context))
		return ViewHolder(view)
	}

	@SuppressLint("SetTextI18n")
	override fun onBindViewHolder(holder: TaskOverViewAdapter.ViewHolder, position: Int) {
		val data = list[position]
		holder.binding.nameGroup.text = data.heading
		holder.binding.logo.setImageResource(data.icon!!)
		roomViewModel.getByCategoryID(data.primaryKey!!).observeForever { it ->
			val filter =
				it.filter { it.taskStatus == false && it.dueDate == MaterialDatePicker.todayInUtcMilliseconds() }
			val filterForPer =
				it.filter { it.taskStatus == true && it.dueDate == MaterialDatePicker.todayInUtcMilliseconds() }
			val filteredCategory =
				it.filter { it.dueDate == MaterialDatePicker.todayInUtcMilliseconds() }
			val percentage = (filterForPer.size.toDouble() / filteredCategory.size) * 100
			holder.binding.count.text = "${filter.size} Tasks"
			holder.binding.groupProgress.progress = percentage.toInt()
			holder.binding.progressCount.text = percentage.toInt().toString()

		}
	}

	override fun getItemCount(): Int {
		return list.size
	}


}