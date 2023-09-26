package com.jinu.todoandmodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.databinding.FragmentToDoListBinding
import com.jinu.todoandmodes.recyclerview.ChipAdapter
import com.jinu.todoandmodes.recyclerview.TodoListAdapter
import com.jinu.todoandmodes.roomdb.dataclass.TaskData
import com.jinu.todoandmodes.roomdb.viewmodel.RoomViewModel



class ToDoListView : Fragment() {
	private lateinit var binding: FragmentToDoListBinding
	private lateinit var roomViewModel: RoomViewModel
	private var currentItem:Int? =0

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {

		binding = FragmentToDoListBinding.inflate(inflater, container, false)
		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
		onStart()
		roomViewModel.allCategory.observe(viewLifecycleOwner) { it ->
			val adapters = ChipAdapter(it)
			binding.recycler.apply {
				layoutManager =
					LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
				adapter = adapters

				adapters.setOnclickListener(object : ChipAdapter.OnClickListener {
					override fun onClick(position: Int) {
						val list = it[position]
						currentItem = list.primaryKey!!
						Log.e("error happened",currentItem.toString())
						roomViewModel.getByCategoryID(currentItem!!)
							.observe(viewLifecycleOwner) {
								val adapt = TodoListAdapter(it)

								binding.listTask.apply {
									layoutManager = GridLayoutManager(context, 1)
									adapter = adapt
									adapt.setOnclickListener(object :
										TodoListAdapter.SetOnCheckedStateChangeListener {
										override fun onCheck(position: Int, status: Boolean) {
											updateStatus(status, it[position])
										}

									})

								}
							}
					}
				})
			}

		}


		return binding.root
	}

	private fun updateStatus(status: Boolean, data: TaskData) {
		data.taskStatus = status
		roomViewModel.updateTask(data)
	}

	override fun onStart() {
		super.onStart()
		roomViewModel.getByCategoryID(currentItem!!)
			.observe(viewLifecycleOwner) {
				val adapt = TodoListAdapter(it)

				binding.listTask.apply {
					layoutManager = GridLayoutManager(context, 1)
					adapter = adapt
					adapt.setOnclickListener(object :
						TodoListAdapter.SetOnCheckedStateChangeListener {
						override fun onCheck(position: Int, status: Boolean) {
							updateStatus(status, it[position])
						}

					})

				}
			}
	}


}

