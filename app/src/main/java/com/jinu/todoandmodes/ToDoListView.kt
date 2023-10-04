package com.jinu.todoandmodes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.jinu.todoandmodes.databinding.FragmentToDoListBinding
import com.jinu.todoandmodes.mvvm.dataclass.Category
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import com.jinu.todoandmodes.recyclerview.ChipAdapter
import com.jinu.todoandmodes.recyclerview.TodoListAdapter


class ToDoListView : Fragment() {
	private lateinit var binding: FragmentToDoListBinding
	private lateinit var roomViewModel: RoomViewModel
	private var currentItem = "All"

	companion object {
		var currentCategory = 0
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		binding = FragmentToDoListBinding.inflate(inflater, container, false)
		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
		binding.listTask.layoutManager = GridLayoutManager(context, 1)
		binding.recycler.layoutManager =
			LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
		val current = arguments?.getInt(TaskView.PASSING_SELECTED_CATEGORY)
		if (current != null) {
			currentCategory = current
		}
		var expandable = true
		binding.expand.setOnClickListener {
			Log.e("expand", expandable.toString())
			expandable = !expandable
			if (!expandable) {
				binding.historyRecycler.visibility = View.INVISIBLE
				binding.expand.setImageResource(R.drawable.baseline_arrow_drop_up_24)
			} else {
				binding.historyRecycler.visibility = View.VISIBLE
				binding.historyRecycler.layoutManager = GridLayoutManager(context, 1)
				binding.expand.setImageResource(R.drawable.baseline_arrow_drop_down_24)
			}
		}
		if (!expandable) {
			binding.historyRecycler.visibility = View.INVISIBLE
			binding.expand.setImageResource(R.drawable.baseline_arrow_drop_up_24)
		} else {
			binding.historyRecycler.visibility = View.VISIBLE
			binding.historyRecycler.layoutManager = GridLayoutManager(context, 1)
			binding.expand.setImageResource(R.drawable.baseline_arrow_drop_down_24)
		}


// task to be done
		roomViewModel.allCategory.observe(viewLifecycleOwner) { it ->
			val categoryList = arrayListOf<Category>()
			categoryList.clear()
			categoryList.add(0, Category(null, null, "All"))
			categoryList.addAll(it)
			val reAdapter = ChipAdapter(categoryList)
			binding.recycler.adapter = reAdapter

			/// updating the selected category that i passed to this fragment
			if (current != null) {
				reAdapter.selectedItemPosition = current + 1
				reAdapter.notifyItemChanged(current + 1)
			}
			// checking the current category
			if (current == null) {
				roomViewModel.allTask.observe(viewLifecycleOwner) { data ->
					val dataPro =
						data.filter { it.taskStatus == false }
					val dataHis =
						data.filter { it.taskStatus == true && it.taskDoneDate == MaterialDatePicker.todayInUtcMilliseconds() }
					val toAdapter = TodoListAdapter(dataPro, roomViewModel)
					val doneHistoryAdapter = TodoListAdapter(dataHis, roomViewModel)
					binding.listTask.adapter = toAdapter
					binding.historyRecycler.adapter = doneHistoryAdapter

					if (dataHis.isEmpty()) {
						binding.textView.visibility = View.INVISIBLE
						binding.expand.visibility = View.INVISIBLE
					} else {
						binding.textView.visibility = View.VISIBLE
						binding.expand.visibility = View.VISIBLE
					}
				}
			} else {
				roomViewModel.allTask.observe(viewLifecycleOwner) { data ->
					val dataPro =
						data.filter { it.category == categoryList[current + 1].heading && it.taskStatus == false }
					val dataHis =
						data.filter { it.category == categoryList[current + 1].heading && it.taskStatus == true && it.taskDoneDate == MaterialDatePicker.todayInUtcMilliseconds() }
					val toAdapter = TodoListAdapter(dataPro, roomViewModel)
					val doneHistoryAdapter = TodoListAdapter(dataHis, roomViewModel)

					binding.listTask.adapter = toAdapter


					binding.historyRecycler.adapter = doneHistoryAdapter

					if (dataHis.isEmpty()) {
						binding.textView.visibility = View.INVISIBLE
						binding.expand.visibility = View.INVISIBLE
					} else {
						binding.textView.visibility = View.VISIBLE
						binding.expand.visibility = View.VISIBLE
					}


				}
			}

			// The category click event and processing the data
			reAdapter.setOnclickListener(object : ChipAdapter.OnClickListener {
				override fun onClick(position: Int) {
					currentItem = categoryList[position].heading!!
					roomViewModel.allTask.observe(viewLifecycleOwner) { data ->


						if (currentItem != "All") {
							val dataPro =
								data.filter { it.category == currentItem && it.taskStatus == false }
							val dataHis =
								data.filter { it.category == currentItem && it.taskStatus == true && it.taskDoneDate == MaterialDatePicker.todayInUtcMilliseconds() }
							val doneHistoryAdapter = TodoListAdapter(dataHis, roomViewModel)
							val toAdapter = TodoListAdapter(dataPro, roomViewModel)
							binding.listTask.adapter = toAdapter
							currentCategory = position - 1
							binding.historyRecycler.adapter = doneHistoryAdapter

							if (dataHis.isEmpty()) {
								binding.textView.visibility = View.INVISIBLE
								binding.expand.visibility = View.INVISIBLE
							} else {
								binding.textView.visibility = View.VISIBLE
								binding.expand.visibility = View.VISIBLE
							}

						} else {
							val dataPro = data.filter { it.taskStatus == false }
							val dataHis =
								data.filter { it.taskStatus == true && it.taskDoneDate == MaterialDatePicker.todayInUtcMilliseconds() }
							val toAdapter = TodoListAdapter(dataPro, roomViewModel)
							binding.listTask.adapter = toAdapter

							val doneHistoryAdapter = TodoListAdapter(dataHis, roomViewModel)
							binding.historyRecycler.adapter = doneHistoryAdapter

							if (dataHis.isEmpty()) {
								binding.textView.visibility = View.INVISIBLE
								binding.expand.visibility = View.INVISIBLE
							} else {
								binding.textView.visibility = View.VISIBLE
								binding.expand.visibility = View.VISIBLE
							}

						}
					}
				}
			})


		}

		binding.materialToolbar2.setNavigationOnClickListener {
			view?.findNavController()
				?.navigate(R.id.action_task_fragment_to_home_fragment)
		}
		binding.materialToolbar2.setOnMenuItemClickListener {
			when (it.itemId) {
				R.id.history_all -> {
					startActivity(Intent(context,TaskHistory::class.java))
					true
				}
				else -> true
			}
		}




		return binding.root
	}


}

