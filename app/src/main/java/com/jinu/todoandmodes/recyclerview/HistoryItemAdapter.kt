package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.R
import com.jinu.todoandmodes.databinding.TaskRecyclerviewItemViewBinding
import com.jinu.todoandmodes.mvvm.dataclass.TaskData
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HistoryItemAdapter(private val list: List<TaskData>,private val roomViewModel: RoomViewModel) :
	RecyclerView.Adapter<HistoryItemAdapter.ViewHolder>() {
	inner class ViewHolder(val binding: TaskRecyclerviewItemViewBinding) :
		RecyclerView.ViewHolder(binding.root)



	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
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
		holder.binding.check.isFocusable = false
		holder.binding.check.isClickable = false
//		holder.binding.card.setOnClickListener{
//			val intent = Intent(holder.binding.root.context, DetailsOfTask::class.java)
//			intent.putExtra(TaskView.PASSING_PRIMARY_KEY,list[position].primaryKey)
//			ContextCompat.startActivity(holder.binding.root.context, intent, Bundle())
//		}

		if (data.taskStatus!!){
			if (isDarkMode(holder.itemView.context)){
				val color = ContextCompat.getColor(holder.itemView.context, R.color.m3_sys_color_dark_outline)
				val colorList = ColorStateList.valueOf(
					ContextCompat.getColor(holder.itemView.context,
						R.color.m3_sys_color_dark_outline))
				holder.binding.check.buttonTintList = colorList
				holder.binding.check.setTextColor(color)
				holder.binding.cardView2.setCardBackgroundColor(color)
				holder.binding.imageView.setColorFilter(color)
				holder.binding.dueTime.setTextColor(color)
				holder.binding.star.setColorFilter(color)

			}
			else{
				val colorList = ColorStateList.valueOf(
					ContextCompat.getColor(holder.itemView.context,
						R.color.m3_sys_color_light_outline))
				val color = ContextCompat.getColor(holder.itemView.context, R.color.m3_sys_color_light_outline)
				holder.binding.check.buttonTintList = colorList
				holder.binding.check.setTextColor(color)
				holder.binding.cardView2.setCardBackgroundColor(color)
				holder.binding.imageView.setColorFilter(color)
				holder.binding.dueTime.setTextColor(color)
				holder.binding.star.setColorFilter(color)

			}

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

	fun delete(position: Int){
		roomViewModel.deleteTask(list[position])
		list[position].primaryKey?.let { it ->
			roomViewModel.getAllStep(it).observeForever { list ->
				list.forEach {
					roomViewModel.deleteStep(it)
				}
		} }
		notifyItemRemoved(position)
	}
}
