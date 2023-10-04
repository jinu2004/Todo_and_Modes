package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.databinding.HistoryItemMainGroupListBinding
import com.jinu.todoandmodes.mvvm.dataclass.HistoryData
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import com.jinu.todoandmodes.recyclerview.drag_gesture.DeleteDragHelper
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HistoryAdapter(private val list:List<HistoryData>,private val roomViewModel: RoomViewModel):RecyclerView.Adapter<HistoryAdapter.ViewHolder>()
{
	inner class ViewHolder(val binding: HistoryItemMainGroupListBinding):RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = HistoryItemMainGroupListBinding.inflate(LayoutInflater.from(parent.context))
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return list.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val data = list[position]
		holder.binding.date.text = formatMillisecondsToDate(data.date)
		holder.binding.recyclerView.layoutManager = GridLayoutManager(holder.binding.root.context,1)
		val adapter = HistoryItemAdapter(data.taskData,roomViewModel)
		holder.binding.recyclerView.adapter = adapter
		val itemTouchHelper = ItemTouchHelper(DeleteDragHelper(adapter))
		itemTouchHelper.attachToRecyclerView(holder.binding.recyclerView)




	}
	@SuppressLint("NewApi")
	private fun formatMillisecondsToDate(milliseconds: Long): String {
		val instant = Instant.ofEpochMilli(milliseconds)
		val zoneId = ZoneId.systemDefault() // You can change this to your desired time zone
		val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") // Define your desired date format
		return formatter.format(instant.atZone(zoneId))
	}

}