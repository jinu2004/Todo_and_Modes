package com.jinu.todoandmodes

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jinu.todoandmodes.backgroundworkers.AlarmReceiver
import com.jinu.todoandmodes.databinding.ActivityMainBinding
import com.jinu.todoandmodes.mvvm.dataclass.Category
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import com.jinu.todoandmodes.recyclerview.BottomSheet

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding
	private lateinit var roomViewModel: RoomViewModel
	private var isNotificationGranted = false
	private var isNotificationUseGranted = false
	private var isNotificationPostGranted = false
	private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		permissionLauncher =
			registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
				isNotificationGranted =
					permission[Manifest.permission.SCHEDULE_EXACT_ALARM] ?: isNotificationGranted
				isNotificationUseGranted =
					permission[Manifest.permission.USE_EXACT_ALARM] ?: isNotificationUseGranted
				isNotificationPostGranted =
					permission[Manifest.permission.POST_NOTIFICATIONS] ?: isNotificationPostGranted
			}
		requestPermissions()

		val navController = findNavController(R.id.fragmentContainerView2)
		binding.bottomNavigationView.setupWithNavController(navController)

		val receiver = ComponentName(this, AlarmReceiver::class.java)

		this.packageManager.setComponentEnabledSetting(
			receiver,
			PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
			PackageManager.DONT_KILL_APP
		)

		val bottomSheet = BottomSheet()
		binding.addNewTask.setOnClickListener {
			bottomSheet.show(supportFragmentManager, bottomSheet.tag)
		}
		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
		val list = arrayListOf<Category>()
		list.add(Category(0, R.drawable.briefcase_svgrepo_com, "office"))
		list.add(Category(1, R.drawable.baseline_school_24, "school"))
		list.add(Category(2, R.drawable.round_person_24, "personal"))
		list.forEach {
			roomViewModel.addNewCategory(it)
		}

	}

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	private fun requestPermissions() {
		isNotificationPostGranted = ContextCompat.checkSelfPermission(
			this,
			Manifest.permission.POST_NOTIFICATIONS
		) == PackageManager.PERMISSION_GRANTED
		isNotificationGranted = ContextCompat.checkSelfPermission(
			this,
			Manifest.permission.SCHEDULE_EXACT_ALARM
		) == PackageManager.PERMISSION_GRANTED
		isNotificationUseGranted = ContextCompat.checkSelfPermission(
			this,
			Manifest.permission.USE_EXACT_ALARM
		) == PackageManager.PERMISSION_GRANTED


		val permissionRequest: MutableList<String> = ArrayList()

		if (!isNotificationGranted)
			permissionRequest.add(Manifest.permission.SCHEDULE_EXACT_ALARM)
		if (!isNotificationUseGranted)
			permissionRequest.add(Manifest.permission.USE_EXACT_ALARM)
		if (!isNotificationPostGranted)
			permissionRequest.add(Manifest.permission.POST_NOTIFICATIONS)

		if (permissionRequest.isNotEmpty()) permissionLauncher.launch(permissionRequest.toTypedArray())

	}

}
