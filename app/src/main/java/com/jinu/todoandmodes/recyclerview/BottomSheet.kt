package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.jinu.todoandmodes.R
import com.jinu.todoandmodes.databinding.FragmentBottomSheetBinding
import com.jinu.todoandmodes.databinding.TaskGroupSelectBinding
import com.jinu.todoandmodes.roomdb.dataclass.TaskData
import com.jinu.todoandmodes.roomdb.viewmodel.RoomViewModel

@Suppress("NAME_SHADOWING")
class BottomSheet : BottomSheetDialogFragment() {

	private lateinit var binding: FragmentBottomSheetBinding
	private lateinit var binding2: TaskGroupSelectBinding
	private lateinit var roomViewModel: RoomViewModel
	private var datePickerData: Long? = null
	private var timePickerData: Long? = null
	private var selectedCategory:String?=null
	private var selectedCategoryId:Int?=null

	@SuppressLint("InflateParams")
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
		binding2 = TaskGroupSelectBinding.inflate(layoutInflater)


		roomViewModel.allCategory.observe(this) {
			val dropDownAdapter = DropDownAdapter(requireContext(),binding2.root.id, it)
			binding.dynamic.adapter = dropDownAdapter
			binding.dynamic.onItemSelectedListener = object :
				AdapterView.OnItemSelectedListener {
				override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
					selectedCategory = it[position].heading
					selectedCategoryId = it[position].primaryKey
				}

				override fun onNothingSelected(parent: AdapterView<*>?) {
					// Handle nothing selected
				}
			}
		}


		val startDate = MaterialDatePicker.todayInUtcMilliseconds()
		binding.pickDateTime.setOnClickListener {
			datePickerFun()
		}
		binding.setAlarm.setOnClickListener {
			timePicker()
		}
		binding.floating.setOnClickListener {
			val text = binding.text.text.toString()

			if (text.isNotEmpty() && datePickerData != null && timePickerData != null){
				roomViewModel.addNewTask(
					TaskData(
						null,
						text,
						startDate,
						datePickerData,
						timePickerData,
						false,
						false,
						0,
						false,
						"",
						"",
						category = selectedCategory,
						categoryId =selectedCategoryId
					)
				)
				binding.text.text.clear()
		}
			else {
				Toast.makeText(context, "please enter the task name", Toast.LENGTH_SHORT).show()
				Log.e("date","$datePickerData")
				Log.e("time","$timePickerData")
				Log.e("msg", text)
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
			datePickerData = it

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
			val hoursInMilliseconds =
				timePicker.hour * 60 * 60 * 1000 // Convert hours to milliseconds (1 hour = 60 minutes * 60 seconds * 1000 milliseconds)
			val minutesInMilliseconds =
				timePicker.minute * 60 * 1000 // Convert minutes to milliseconds (1 minute = 60 seconds * 1000 milliseconds)

			timePickerData = (hoursInMilliseconds + minutesInMilliseconds).toLong()
		}
	}
}