package com.jinu.todoandmodes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.jinu.todoandmodes.databinding.FragmentTaskViewBinding
import com.jinu.todoandmodes.recyclerview.InProgressAdapter
import com.jinu.todoandmodes.recyclerview.TaskOverViewAdapter
import com.jinu.todoandmodes.roomdb.viewmodel.RoomViewModel


class TaskView : Fragment() {
	private lateinit var binding: FragmentTaskViewBinding
	private lateinit var roomViewModel: RoomViewModel

	@SuppressLint("SetTextI18n")
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		binding = FragmentTaskViewBinding.inflate(inflater, container, false)
		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
		val currentDate = MaterialDatePicker.todayInUtcMilliseconds()
		binding.inProgress.layoutManager = LinearLayoutManager(
			context,
			RecyclerView.HORIZONTAL, false
		)
		binding.taskGroup.layoutManager = GridLayoutManager(context, 1)

		roomViewModel.allTask.observe(viewLifecycleOwner) { it ->
			val filter = it.filter { it.taskStatus == true && it.dueDate == currentDate }
			val filterByDate = it.filter { it.dueDate == currentDate }
			val percentage = (filter.size.toDouble() / filterByDate.size) * 100
			if (filter.isNotEmpty()) binding.percentText.text = percentage.toInt().toString()
			else binding.percentText.visibility = View.INVISIBLE
			binding.circularProgressIndicator.progress = percentage.toInt()
			if (percentage == 0.0) binding.addressingText.text = "You have to complete tasks!!"
			else if (percentage > 0 && percentage < 50) binding.addressingText.text = "keep it up!!"
			else if (percentage > 50 && percentage < 80) binding.addressingText.text =
				"you have to do a little more"
			else if (percentage.toInt() == 100) binding.addressingText.text = "Congratulation"


			val filterToDo = it.filter { it.taskStatus == false && it.dueDate == currentDate }
			val adapter = InProgressAdapter(filterToDo, roomViewModel)
			binding.inProgress.adapter = adapter

			binding.taskProgressCount.text = filterToDo.size.toString()
			if (filterToDo.isEmpty()) {
				binding.progressCard.visibility = View.INVISIBLE
			} else
				binding.progressCard.visibility = View.VISIBLE


		}

		roomViewModel.allCategory.observe(viewLifecycleOwner) {
			val adapter = TaskOverViewAdapter(it, roomViewModel)
			binding.taskGroup.adapter = adapter
			binding.groupCount.text = it.size.toString()
		}




		return binding.root
	}
}