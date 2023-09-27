package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.databinding.TaskRecyclerviewItemViewBinding
import com.jinu.todoandmodes.mvvm.dataclass.TaskData
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class TodoListAdapter(private val list: List<TaskData>) :
	RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {
	inner class ViewHolder(val binding: TaskRecyclerviewItemViewBinding) :
		RecyclerView.ViewHolder(binding.root)
	private var onCheckedStateChangeListener:SetOnCheckedStateChangeListener?=null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = TaskRecyclerviewItemViewBinding.inflate(LayoutInflater.from(parent.context))
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return list.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val data = list[position]
		holder.binding.dueTime.text = formatMillisecondsToDate(data.dueDate!!)
		holder.binding.check.text = data.taskName
		holder.binding.check.isChecked = data.taskStatus!!
		holder.binding.check.setOnCheckedChangeListener{_,checked->
				onCheckedStateChangeListener?.onCheck(position, status = checked)
		}

	}

	@SuppressLint("NewApi")
	fun formatMillisecondsToDate(milliseconds: Long): String {
		val instant = Instant.ofEpochMilli(milliseconds)
		val zoneId = ZoneId.systemDefault() // You can change this to your desired time zone
		val formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy:hh-mm-ss") // Define your desired date format
		return formatter.format(instant.atZone(zoneId))
	}

	fun setOnclickListener(setOnCheckedStateChangeListener:SetOnCheckedStateChangeListener){
		this.onCheckedStateChangeListener = setOnCheckedStateChangeListener
	}
	interface SetOnCheckedStateChangeListener {
		fun onCheck(position: Int , status: Boolean)

	}
}