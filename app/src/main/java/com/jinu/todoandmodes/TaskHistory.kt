package com.jinu.todoandmodes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.jinu.todoandmodes.databinding.ActivityTaskHistoryBinding
import com.jinu.todoandmodes.mvvm.dataclass.HistoryData
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import com.jinu.todoandmodes.recyclerview.HistoryAdapter

class TaskHistory : AppCompatActivity() {
	private lateinit var binding: ActivityTaskHistoryBinding
	private lateinit var roomViewModel: RoomViewModel
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityTaskHistoryBinding.inflate(layoutInflater)
		setContentView(binding.root)

		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
		val arrayList = ArrayList<HistoryData>()
		val listOfDate = ArrayList<Long>()
		roomViewModel.allTask.observe(this){data->
			arrayList.clear()
			listOfDate.clear()
			val extractDate = data.filter { it.taskStatus == true }
			extractDate.forEach {item->
				listOfDate.add(item.taskDoneDate!!)
			}
			val removeCopy =HashSet(listOfDate)
			val distinctList = ArrayList(removeCopy)

			distinctList.forEach {item->
				arrayList.add(HistoryData(item, extractDate.filter { it.taskDoneDate!! == item }))
			}

			binding.historyRecycler.layoutManager = GridLayoutManager(this,1)
			binding.historyRecycler.adapter = HistoryAdapter(arrayList,roomViewModel)
		}
		binding.materialToolbar3.setNavigationOnClickListener { finish() }


	}
}