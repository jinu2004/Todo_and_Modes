package com.jinu.todoandmodes.recyclerview

import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.databinding.SubTaskDoneBinding
import com.jinu.todoandmodes.mvvm.dataclass.StepTask
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel

class SubAdapter(private val sublist: List<StepTask>, private val roomViewModel: RoomViewModel) :
	RecyclerView.Adapter<SubAdapter.ViewHolder>() {
	inner class ViewHolder(val binding: SubTaskDoneBinding) : RecyclerView.ViewHolder(binding.root)

	@Suppress("DEPRECATION")
	private val handler = Handler()


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = SubTaskDoneBinding.inflate(LayoutInflater.from(parent.context))
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return sublist.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val data = sublist[position]

		if (data.text?.isNotEmpty() == true) holder.binding.text.hint = data.text


		if (data.text?.isEmpty() == true) holder.binding.close.visibility = View.INVISIBLE
		else holder.binding.close.visibility = View.INVISIBLE

		holder.binding.radioButton.isChecked = data.state

		if (data.text.isNullOrBlank()) {
			holder.binding.close.visibility = View.VISIBLE
			holder.binding.close.setOnClickListener {
				roomViewModel.deleteStep(data)
			}

		} else holder.binding.close.visibility = View.INVISIBLE


		holder.binding.text.setOnEditorActionListener { _, actionId, event ->
			if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
				handler.post {
					data.text = holder.binding.text.text.toString()
					roomViewModel.addSub(data)
				}

				return@setOnEditorActionListener true
			}
			return@setOnEditorActionListener false
		}






		holder.binding.radioButton.setOnCheckedChangeListener { _, checked ->
			data.state = checked
			handler.post {
				if (data.text?.isNotEmpty() == true) {
					roomViewModel.addSub(data)
					notifyItemChanged(position)
				}
			}
		}


	}
}