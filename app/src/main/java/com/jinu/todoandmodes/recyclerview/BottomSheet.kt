package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.jinu.todoandmodes.R
import com.jinu.todoandmodes.ToDoListView
import com.jinu.todoandmodes.backgroundworkers.AlarmManagerClass
import com.jinu.todoandmodes.databinding.FragmentBottomSheetBinding
import com.jinu.todoandmodes.databinding.TaskGroupSelectBinding
import com.jinu.todoandmodes.mvvm.dataclass.TaskData
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import java.util.Calendar

@Suppress("NAME_SHADOWING")
class BottomSheet : BottomSheetDialogFragment() {

	private lateinit var binding: FragmentBottomSheetBinding
	private lateinit var binding2: TaskGroupSelectBinding
	private lateinit var roomViewModel: RoomViewModel
	private var datePickerData: Long? = null
	private var timePickerData: Long? = null
	private var hour: Int? = null
	private var minute: Int? = null
	private var selectedCategory: String? = null
	private var selectedCategoryId: Int? = null
	private var selectedCategoryIcon: Int? = null
	private lateinit var alarm: AlarmManagerClass
	private var times: Long? = null
	private val alamId = Math.random().hashCode()

	@SuppressLint("InflateParams")
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
		binding2 = TaskGroupSelectBinding.inflate(layoutInflater)



		alarm = AlarmManagerClass()


		if (isDarkMode(requireContext())) {
			val color = Color.parseColor("#E1E3E0") // Replace with your desired color
			val mode = PorterDuff.Mode.SRC_ATOP
			val colorFilter = PorterDuffColorFilter(color, mode)
			binding.pickDateTime.colorFilter = colorFilter
			binding.setAlarm.colorFilter = colorFilter
			binding.repeat.colorFilter = colorFilter
		} else {
			val color = Color.parseColor("#191C1B") // Replace with your desired color
			val mode = PorterDuff.Mode.SRC_ATOP
			val colorFilter = PorterDuffColorFilter(color, mode)
			binding.pickDateTime.colorFilter = colorFilter
			binding.setAlarm.colorFilter = colorFilter
			binding.repeat.colorFilter = colorFilter
		}

		binding.text.post {
			binding.text.requestFocus()
			val imm =
				binding.text.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			imm.showSoftInput(binding.text, InputMethodManager.SHOW_IMPLICIT)
			binding.text.selectAll()
		}

		roomViewModel.allCategory.observe(this) {


			val dropDownAdapter = DropDownAdapter(requireContext(), binding2.root.id, it)
			binding.dynamic.adapter = dropDownAdapter
			binding.dynamic.setSelection(ToDoListView.currentCategory)
			binding.dynamic.onItemSelectedListener = object :
				AdapterView.OnItemSelectedListener {
				override fun onItemSelected(
					parent: AdapterView<*>?,
					view: View?,
					position: Int,
					id: Long,
				) {
					selectedCategory = it[position].heading
					selectedCategoryId = it[position].primaryKey
					selectedCategoryIcon = it[position].icon
				}

				override fun onNothingSelected(parent: AdapterView<*>?) {
					// Handle nothing selected
				}
			}
		}


//		val startDateCalender = Calendar.getInstance()
//		startDateCalender[Calendar.HOUR_OF_DAY] = 0
//		startDateCalender[Calendar.MINUTE] = 0
//		startDateCalender[Calendar.SECOND] = 0
//		startDateCalender[Calendar.MILLISECOND] = 0
		val startDate = MaterialDatePicker.todayInUtcMilliseconds()
		datePickerData = MaterialDatePicker.todayInUtcMilliseconds()



		binding.pickDateTime.setOnClickListener {
			datePickerFun()
		}
		binding.setAlarm.setOnClickListener {
			timePicker()
		}
		binding.floating.setOnClickListener {
			val text = binding.text.text.toString()

			if (text.isNotEmpty() && datePickerData != null) {
				roomViewModel.addNewTask(
					TaskData(
						primaryKey = null,
						taskName = text,
						startDate = startDate,
						dueDate = datePickerData,
						taskStatus = false,
						category = selectedCategory,
						categoryId = selectedCategoryId,
						time = timePickerData,
						alarmId = alamId
					)
				)
				binding.text.text.clear()
				Log.d("Date","$datePickerData")
			}
			else if(text.isNotEmpty() && datePickerData == null) {}
			else{
				Toast.makeText(context, "please enter the task name", Toast.LENGTH_SHORT).show()
				Log.e("date", "$datePickerData")
				Log.e("msg", text)
			}
			if (text.isNotEmpty() && hour != null && datePickerData != null) {
				val newCalendar = Calendar.getInstance()
				newCalendar[Calendar.HOUR_OF_DAY] = hour!!
				newCalendar[Calendar.MINUTE] = minute!!
				newCalendar[Calendar.SECOND] = 0
				newCalendar[Calendar.MILLISECOND] = 0
				alarm.setAlarm(requireContext(), (newCalendar.time.time), alamId, selectedCategoryIcon!!,text)
			}


		}
		binding.repeat.setOnClickListener {
			val builder = AlertDialog.Builder(requireContext())
			val inflater: LayoutInflater = layoutInflater
			val dialogLayout = inflater.inflate(R.layout.in_progress_view, null)

			with(builder) {
				setTitle("Remind me")
				setPositiveButton("OK") { _, _ ->
				}
				setNegativeButton("Cancel") { _, _ ->
				}
				setView(dialogLayout)
				show()
			}
		}





		return binding.root
	}

	private fun datePickerFun() {
		val datePicker =
			MaterialDatePicker.Builder.datePicker()
				.setTitleText("Select date")
				.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
				.build()
		datePicker.show(childFragmentManager, "")
		datePicker.addOnPositiveButtonClickListener {
			val newCalendar = Calendar.getInstance()
			newCalendar.timeInMillis = it
			newCalendar[Calendar.HOUR_OF_DAY] = 0
			newCalendar[Calendar.MINUTE] = 0
			newCalendar[Calendar.SECOND] = 0
			newCalendar[Calendar.MILLISECOND] = 0
			datePickerData = newCalendar.timeInMillis
		}

	}

	private fun timePicker() {
		val timePicker = MaterialTimePicker.Builder()
			.setTimeFormat(TimeFormat.CLOCK_12H)
			.setHour(12)
			.setMinute(10)
			.setTitleText("Select time")
			.build()
		timePicker.show(childFragmentManager, "")
		timePicker.addOnPositiveButtonClickListener {
			hour = timePicker.hour
			minute = timePicker.minute
			val newCalendar = Calendar.getInstance()
			newCalendar[Calendar.HOUR_OF_DAY] = hour!!
			newCalendar[Calendar.MINUTE] = minute!!
			newCalendar[Calendar.SECOND] = 0
			newCalendar[Calendar.MILLISECOND] = 0
			timePickerData = newCalendar.timeInMillis

		}
	}

	private fun isDarkMode(context: Context): Boolean {
		val currentNightMode =
			context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
		return currentNightMode == Configuration.UI_MODE_NIGHT_YES
	}


}

