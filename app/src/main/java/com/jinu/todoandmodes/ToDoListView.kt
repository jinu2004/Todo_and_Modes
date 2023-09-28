package com.jinu.todoandmodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.databinding.FragmentToDoListBinding
import com.jinu.todoandmodes.mvvm.dataclass.Category
import com.jinu.todoandmodes.mvvm.dataclass.TaskData
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import com.jinu.todoandmodes.recyclerview.ChipAdapter
import com.jinu.todoandmodes.recyclerview.TodoListAdapter


class ToDoListView : Fragment() {
	private lateinit var binding: FragmentToDoListBinding
	private lateinit var roomViewModel: RoomViewModel
	private var currentItem = "All"

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
		val current = arguments?.getInt("select")


		val categoryList = arrayListOf<Category>()

		roomViewModel.allCategory.observe(viewLifecycleOwner) { it ->
			categoryList.clear()
			categoryList.add(0, Category(null, null, "All"))
			categoryList.addAll(it)
			val reAdapter = ChipAdapter(categoryList)
			binding.recycler.adapter = reAdapter
			if (current != null) {
				reAdapter.selectedItemPosition = current + 1
			}
			if (current != null) {
				reAdapter.notifyItemChanged(current + 1)
			}
			if (current == null) {
				roomViewModel.allTaskToDane.observe(viewLifecycleOwner) {
					val toAdapter = TodoListAdapter(it)
					binding.listTask.adapter = toAdapter
					toAdapter.setOnclickListener(object :
						TodoListAdapter.SetOnCheckedStateChangeListener {
						override fun onCheck(position: Int, status: Boolean) {
							binding.listTask.post {
								updateStatus(status, it[position])
								toAdapter.notifyItemChanged(position)
							}
						}

					})
				}
			} else {
				roomViewModel.allTaskToDane.observe(viewLifecycleOwner) { data ->
						val dataPro =
							data.filter { it.category == categoryList[current + 1].heading }
						val toAdapter = TodoListAdapter(dataPro)
						binding.listTask.adapter = toAdapter
						toAdapter.setOnclickListener(object :
							TodoListAdapter.SetOnCheckedStateChangeListener {
							override fun onCheck(position: Int, status: Boolean) {
								binding.listTask.post {
									updateStatus(status, dataPro[position])
									toAdapter.notifyItemChanged(position)
								}
							}

						})

				}
			}



			reAdapter.setOnclickListener(object : ChipAdapter.OnClickListener {
				override fun onClick(position: Int) {
					currentItem = categoryList[position].heading!!
					roomViewModel.allTaskToDane.observe(viewLifecycleOwner) { data ->

						val dataPro = data.filter { it.category == currentItem }
						if (currentItem != "All") {
							val toAdapter = TodoListAdapter(dataPro)
							binding.listTask.adapter = toAdapter
							toAdapter.setOnclickListener(object :
								TodoListAdapter.SetOnCheckedStateChangeListener {
								override fun onCheck(position: Int, status: Boolean) {
									binding.listTask.post {
										updateStatus(status, dataPro[position])
										toAdapter.notifyItemChanged(position)
									}
								}

							})
						} else {
							val toAdapter = TodoListAdapter(data)
							binding.listTask.adapter = toAdapter
							toAdapter.setOnclickListener(object :
								TodoListAdapter.SetOnCheckedStateChangeListener {
								override fun onCheck(position: Int, status: Boolean) {
									binding.listTask.post {
										updateStatus(status, data[position])
										toAdapter.notifyItemChanged(position)
									}
								}

							})
						}
					}
				}
			})


		}

		binding.materialToolbar2.setNavigationOnClickListener {
			view?.findNavController()
				?.navigate(R.id.action_task_fragment_to_home_fragment)
		}




		return binding.root
	}

	private fun updateStatus(status: Boolean, data: TaskData) {
		data.taskStatus = status
		roomViewModel.updateTask(data)
	}


}

