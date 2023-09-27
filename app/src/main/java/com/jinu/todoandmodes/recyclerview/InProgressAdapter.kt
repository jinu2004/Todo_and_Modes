package com.jinu.todoandmodes.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.databinding.InProgressViewBinding
import com.jinu.todoandmodes.roomdb.dataclass.TaskData
import com.jinu.todoandmodes.roomdb.viewmodel.RoomViewModel

class InProgressAdapter(private val list: List<TaskData>, private val roomViewModel: RoomViewModel) :
	RecyclerView.Adapter<InProgressAdapter.ViewHolder>() {
	private var onClickListener:OnClickListener?=null
	inner class ViewHolder(val binding: InProgressViewBinding) :
		RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = InProgressViewBinding.inflate(LayoutInflater.from(parent.context))
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return list.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val data = list[position]
		holder.binding.textView6.text = data.taskName
		holder.binding.category.text = data.category
		roomViewModel.allCategory.observeForever { it ->
			val category = it.filter { it.primaryKey == data.primaryKey }
			holder.binding.icon.setImageResource(category.first().icon!!)
		}
		roomViewModel.getAllStep(data.primaryKey!!).observeForever{ it ->
			val filterForProgress = it.filter { it.state }
			val progress = (filterForProgress.size.toDouble()/it.size)*100
			holder.binding.subProgress.progress = progress.toInt()
		}

		holder.binding.card.setOnClickListener {
			if (onClickListener != null) {
				onClickListener!!.onClick(position)
			}
		}

	}
	fun setOnclickListener(onClickListener:OnClickListener){
		this.onClickListener = onClickListener
	}
	interface OnClickListener {
		fun onClick(position: Int)

	}
}