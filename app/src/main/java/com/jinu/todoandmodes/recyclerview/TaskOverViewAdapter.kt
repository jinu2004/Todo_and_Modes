package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.jinu.todoandmodes.databinding.TaskGroupLayoutBinding
import com.jinu.todoandmodes.mvvm.dataclass.Category
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel

class TaskOverViewAdapter(val list: List<Category>, private val roomViewModel: RoomViewModel) :
	RecyclerView.Adapter<TaskOverViewAdapter.ViewHolder>() {
	inner class ViewHolder(val binding: TaskGroupLayoutBinding) :
		RecyclerView.ViewHolder(binding.root)
	private var onClickListener: OnClickListener?=null

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

			val filterForPer =
				it.filter { it.taskStatus == true && it.taskDoneDate == MaterialDatePicker.todayInUtcMilliseconds()}
			val filterByDate =
				it.filter { it.startDate == MaterialDatePicker.todayInUtcMilliseconds() }
			val percentage = (filterForPer.size.toDouble() / filterByDate.size) * 100
			holder.binding.count.text = "${filterForPer.size}/${filterByDate.size} Tasks"
			holder.binding.groupProgress.progress = percentage.toInt()
			holder.binding.progressCount.text = "${percentage.toInt()}%"

		}
		holder.binding.card.setOnClickListener {
			if (onClickListener != null) {
				onClickListener!!.onClick(position)
			}
		}
	}

	override fun getItemCount(): Int {
		return list.size
	}

	fun setOnclickListener(onClickListener:OnClickListener){
		this.onClickListener = onClickListener
	}
	interface OnClickListener {
		fun onClick(position: Int)

	}


}