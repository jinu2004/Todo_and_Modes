package com.jinu.todoandmodes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.jinu.todoandmodes.databinding.FragmentTaskViewBinding
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import com.jinu.todoandmodes.recyclerview.InProgressAdapter
import com.jinu.todoandmodes.recyclerview.TaskOverViewAdapter


class TaskView : Fragment() {
	private lateinit var binding: FragmentTaskViewBinding
	private lateinit var roomViewModel: RoomViewModel

	companion object{
		val PASSING_PRIMARY_KEY = "primary"
		val PASSING_SELECTED_CATEGORY = "current_category"
	}

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
			if (filter.isNotEmpty()) binding.percentText.text = " ${percentage.toInt()}%"
			binding.circularProgressIndicator.progress = percentage.toInt()
			if (percentage == 0.0) binding.addressingText.text = "You have to complete tasks!!"
			else if (percentage > 0 && percentage < 50) binding.addressingText.text = "keep it up!!"
			else if (percentage > 50 && percentage < 80) binding.addressingText.text =
				"you have to do a little more"
			else if (percentage.toInt() == 100) binding.addressingText.text = "Congratulation"


			val filterToDo = it.filter { it.taskStatus == false && it.dueDate == currentDate }
			val adapter = InProgressAdapter(filterToDo, roomViewModel)
			binding.inProgress.adapter = adapter
			adapter.setOnclickListener(object : InProgressAdapter.OnClickListener {
				override fun onClick(position: Int) {
					val intent = Intent(context, DetailsOfTask::class.java)
					intent.putExtra(PASSING_PRIMARY_KEY,filterToDo[position].primaryKey)
					startActivity(intent)
				}

			})

			binding.taskProgressCount.text = filterToDo.size.toString()
			if (filterToDo.isEmpty()) {
				binding.progressCard.visibility = View.INVISIBLE
			} else
				binding.progressCard.visibility = View.VISIBLE


		}

		roomViewModel.allCategory.observe(viewLifecycleOwner) {
			val adapter = TaskOverViewAdapter(it, roomViewModel)
			binding.taskGroup.adapter = adapter
			adapter.setOnclickListener(object : TaskOverViewAdapter.OnClickListener {
				override fun onClick(position: Int) {
					val bundle = bundleOf(PASSING_SELECTED_CATEGORY to position)
					view?.findNavController()
						?.navigate(R.id.action_home_fragment_to_task_fragment, bundle)
				}

			})
			binding.groupCount.text = it.size.toString()
		}

		binding.button.setOnClickListener {
			view?.findNavController()
				?.navigate(R.id.action_home_fragment_to_task_fragment)
		}

		return binding.root
	}
}