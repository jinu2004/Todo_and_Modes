package com.jinu.todoandmodes.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.databinding.TaskRecyclerviewItemViewBinding
import com.jinu.todoandmodes.roomdb.dataclass.TaskData
import com.jinu.todoandmodes.roomdb.viewmodel.RoomViewModel


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

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val data = list[position]
		holder.binding.check.isChecked = data.taskStatus!!
		updateStatus(status = holder.binding.check.isChecked, id = data.primaryKey!!)


	}

	private fun updateStatus(status: Boolean, id: Int) {
		val list = roomViewModel.getByID(id)
		list.taskStatus = status
		roomViewModel.addNewTask(list)

	}
}