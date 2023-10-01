package com.jinu.todoandmodes

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.jinu.todoandmodes.databinding.ActivityDetailsOfTaskBinding
import com.jinu.todoandmodes.mvvm.dataclass.StepTask
import com.jinu.todoandmodes.mvvm.dataclass.TaskData
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import com.jinu.todoandmodes.recyclerview.DropDownAdapter
import com.jinu.todoandmodes.recyclerview.SubAdapter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class DetailsOfTask : AppCompatActivity() {
	private lateinit var binding: ActivityDetailsOfTaskBinding
	private lateinit var roomViewModel: RoomViewModel
	private var itemId: Int? = null
	private var listData:TaskData? =null

	@SuppressLint("NewApi", "SetTextI18n")
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
			listData = dataTo
			binding.editText.hint = dataTo.taskName
			binding.editText.setOnEditorActionListener { _, actionId, event ->
				if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {

					val execute = ContextCompat.getMainExecutor(this)

					execute.execute {
						dataTo.taskName = binding.editText.text.toString()
						roomViewModel.updateTask(dataTo)
					}

					return@setOnEditorActionListener true
				}
				return@setOnEditorActionListener false
			}
			roomViewModel.allCategory.observe(this){ cat ->
				val index = cat.indexOfFirst { it.primaryKey == dataTo.categoryId }
				val dropDownAdapter = DropDownAdapter(this, binding.root.id, cat)
				binding.dynamic.adapter = dropDownAdapter
				binding.dynamic.setSelection(index)
				binding.dynamic.onItemSelectedListener =
					object : AdapterView.OnItemSelectedListener {
						override fun onItemSelected(
							parent: AdapterView<*>?,
							view: View?,
							position: Int,
							id: Long,
						) {

								listData!!.category = cat[position].heading
								listData!!.categoryId = cat[position].primaryKey


						}

						override fun onNothingSelected(parent: AdapterView<*>?) {
						}
					}
			}


			binding.dateText.text = formatMillisecondsToDate(dataTo.dueDate!!)

			if (dataTo.time == null)
				binding.timeText.text = "no"
			else {
				binding.timeText.text = convertMillisecondsToTime(dataTo.time)
			}


		}


		/// end of activity
	}


	@Deprecated("Deprecated in Java")
	override fun onBackPressed() {
		itemId?.let { check ->
			roomViewModel.getAllStep(check).observe(this) { item ->
				val toDelete = item.filter { it.text.isNullOrBlank() }
				toDelete.forEach {
					roomViewModel.deleteStep(it)
				}
			}
		}
		val execute = ContextCompat.getMainExecutor(this@DetailsOfTask)
		execute.execute {
			roomViewModel.updateTask(listData!!)
		}
		@Suppress("DEPRECATION") super.onBackPressed()

	}

	@RequiresApi(Build.VERSION_CODES.O)
	private fun formatMillisecondsToDate(milliseconds: Long): String {
		val instant = Instant.ofEpochMilli(milliseconds)
		val zoneId = ZoneId.systemDefault() // You can change this to your desired time zone
		val formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy")
		// Define your desired date format
		return formatter.format(instant.atZone(zoneId))

	}

	private fun convertMillisecondsToTime(milliseconds: Long): String {
		val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
		val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
		val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60

		return String.format("%02d:%02d:%02d", hours, minutes, seconds)
	}

}