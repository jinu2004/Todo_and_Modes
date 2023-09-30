package com.jinu.todoandmodes

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.jinu.todoandmodes.databinding.ActivityDetailsOfTaskBinding
import com.jinu.todoandmodes.mvvm.dataclass.StepTask
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import com.jinu.todoandmodes.recyclerview.SubAdapter

class DetailsOfTask : AppCompatActivity() {
	private lateinit var binding: ActivityDetailsOfTaskBinding
	private lateinit var roomViewModel: RoomViewModel
	private var itemId: Int? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailsOfTaskBinding.inflate(layoutInflater)
		setContentView(binding.root)
		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
		val data = intent.getIntExtra(TaskView.PASSING_PRIMARY_KEY, 0)
		roomViewModel.getAllStep(data).observe(this) {
			val item = it
			val adapter = SubAdapter(item, roomViewModel)
			binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
			binding.recyclerView.adapter = adapter
			binding.materialCardView3.setOnClickListener {
				itemId = data
				roomViewModel.addSub(StepTask(null, data, null, false))
				adapter.notifyItemInserted(item.size)
			}
		}


		roomViewModel.allTask.observe(this) { it ->
			val list = it.filter { it.primaryKey == data }
			val dataTo = list.first()
			binding.editText.hint = dataTo.taskName
			binding.editText.setOnEditorActionListener { _, actionId, event ->
				if (actionId == EditorInfo.IME_ACTION_DONE ||
					(event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
				) {

					val execute = ContextCompat.getMainExecutor(this)

					execute.execute {
						dataTo.taskName = binding.editText.text.toString()
						roomViewModel.updateTask(dataTo)
					}

					return@setOnEditorActionListener true
				}
				return@setOnEditorActionListener false
			}
		}

	}

	@Deprecated("Deprecated in Java")
	override fun onBackPressed() {
		itemId?.let { check ->
			roomViewModel.getAllStep(check).observe(this){ item ->
				val toDelete = item.filter { it.text.isNullOrBlank() }
				toDelete.forEach {
					roomViewModel.deleteStep(it)
				}
			}
		}
		@Suppress("DEPRECATION")
		super.onBackPressed()
	}

}