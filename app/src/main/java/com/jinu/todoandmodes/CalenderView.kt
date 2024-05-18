package com.jinu.todoandmodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jinu.todoandmodes.databinding.FragmentCalenderViewBinding
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import com.jinu.todoandmodes.recyclerview.TodoListAdapter
import java.util.Calendar

class CalenderView : Fragment() {
	private lateinit var binding: FragmentCalenderViewBinding
	private lateinit var roomViewModel: RoomViewModel
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		binding = FragmentCalenderViewBinding.inflate(inflater, container, false)
		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]

		binding.materialToolbar4.setNavigationOnClickListener { findNavController().navigate(R.id.action_calenderView_to_home_fragment) }

		binding.imageView.visibility = View.INVISIBLE

		val newCalendar = Calendar.getInstance()
		newCalendar.timeInMillis = binding.calendarView.date
		newCalendar[Calendar.HOUR_OF_DAY] = 0
		newCalendar[Calendar.MINUTE] = 0
		newCalendar[Calendar.SECOND] = 0
		newCalendar[Calendar.MILLISECOND] = 0
		val date = newCalendar.timeInMillis

		Log.d("currentDate", "$date")
		binding.recyclerView.layoutManager = GridLayoutManager(context, 1)

		roomViewModel.allTask.observeForever { it ->
			val filterDate = it.filter { it.dueDate == date }
			binding.recyclerView.adapter = TodoListAdapter(filterDate, roomViewModel)
			binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
				val calendar = Calendar.getInstance()
				calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
				calendar[Calendar.MONTH] = month
				calendar[Calendar.YEAR] = year
				calendar[Calendar.HOUR_OF_DAY] = 0
				calendar[Calendar.MINUTE] = 0
				calendar[Calendar.SECOND] = 0
				calendar[Calendar.MILLISECOND] = 0
				val filter = it.filter { it.dueDate == calendar.timeInMillis }
				binding.recyclerView.adapter = TodoListAdapter(filter, roomViewModel)

				if (filter.isEmpty()) binding.imageView.visibility = View.VISIBLE
				else binding.imageView.visibility = View.INVISIBLE


			}

			if (filterDate.isEmpty()) binding.imageView.visibility = View.VISIBLE
			else binding.imageView.visibility = View.INVISIBLE


		}





		return binding.root
	}

}