package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.R
import com.jinu.todoandmodes.databinding.SubTaskDoneBinding
import com.jinu.todoandmodes.mvvm.dataclass.StepTask
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel

class SubAdapter(private val sublist: ArrayList<StepTask>, private val roomViewModel: RoomViewModel) :
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

	@SuppressLint("NewApi")
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val data = sublist[position]

		if (data.text?.isNotEmpty() == true) holder.binding.text.hint = data.text


		if (data.text?.isEmpty() == true) holder.binding.close.visibility = View.INVISIBLE
		else holder.binding.close.visibility = View.INVISIBLE

		holder.binding.radioButton.isChecked = data.state

		if (data.state){
			val background = ContextCompat.getDrawable(holder.binding.root.context, R.drawable.crossline)
			holder.binding.text.background = background
		}


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

		holder.binding.text.post {
			holder.binding.text.requestFocus()
			val imm = holder.binding.text.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			imm.showSoftInput(holder.binding.text, InputMethodManager.SHOW_IMPLICIT)
			holder.binding.text.selectAll()
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
	fun moveItem(fromPosition: Int, toPosition: Int) {
		val item = sublist.removeAt(fromPosition)
		sublist.add(toPosition, item)
		notifyItemMoved(fromPosition, toPosition)
	}
}