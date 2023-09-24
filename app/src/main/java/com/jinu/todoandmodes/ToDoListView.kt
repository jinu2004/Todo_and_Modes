package com.jinu.todoandmodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.databinding.FragmentToDoListBinding
import com.jinu.todoandmodes.recyclerview.ChipAdapter
import com.jinu.todoandmodes.recyclerview.TodoListAdapter
import com.jinu.todoandmodes.roomdb.dataclass.Category
import com.jinu.todoandmodes.roomdb.viewmodel.RoomViewModel


class ToDoListView : Fragment() {
	private lateinit var binding: FragmentToDoListBinding
	private lateinit var roomViewModel: RoomViewModel
	private var currentSelectedItem = "All"

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		binding = FragmentToDoListBinding.inflate(inflater, container, false)
		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]

		roomViewModel.allCategory.observe(viewLifecycleOwner) { it ->
			val dataWithAll = arrayListOf<Category>()
			it.forEach {
				dataWithAll.add(it)
			}
			dataWithAll.add(0, Category(null, null, "All"))
			val adapters = ChipAdapter(dataWithAll)
			binding.recycler.apply {
				layoutManager =
					LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
				adapter = adapters
				adapters.setOnclickListener(object : ChipAdapter.OnClickListener {
					override fun onClick(position: Int) {
						val list = dataWithAll[position]
						currentSelectedItem = list.heading.toString()
					}
				})
			}
		}

		when (currentSelectedItem) {
			"All" -> {
				roomViewModel.allTask.observe(viewLifecycleOwner) {
					binding.listTask.layoutManager = LinearLayoutManager(
						binding.root.context,
						RecyclerView.VERTICAL,
						false
					)
					binding.listTask.adapter = TodoListAdapter(it, roomViewModel)
				}
			}

			else -> {
//								roomViewModel.getByCategoryID(list.primaryKey!!).observe(viewLifecycleOwner){
//									binding.listTask.layoutManager = LinearLayoutManager(
//										binding.root.context,
//										RecyclerView.VERTICAL,
//										false
//									)
//									binding.listTask.adapter = TodoListAdapter(it, roomViewModel)
//								}

			}
		}




	return binding.root
}

}