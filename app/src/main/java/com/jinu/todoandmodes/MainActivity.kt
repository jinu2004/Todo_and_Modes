package com.jinu.todoandmodes

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinu.todoandmodes.backgroundworkers.AlarmReceiver
import com.jinu.todoandmodes.databinding.ActivityMainBinding
import com.jinu.todoandmodes.mvvm.dataclass.Category
import com.jinu.todoandmodes.mvvm.viewmodel.RoomViewModel
import com.jinu.todoandmodes.recyclerview.BottomSheet

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding
	private lateinit var roomViewModel: RoomViewModel
	private var isNotificationGranted = false
	private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
				permission->
			isNotificationGranted = permission[Manifest.permission.POST_NOTIFICATIONS]?:isNotificationGranted
		}
		requestPermissions()

		val navController = findNavController(R.id.fragmentContainerView2)
		val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
		bottomNavView.setupWithNavController(navController)


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

		roomViewModel.allTask.observeForever {tasks->
			val taskToAlarm = tasks.filter { it.time != null && it.taskStatus != true }
			taskToAlarm.forEach { item->
				setAlarm(this,item.time!!,item.primaryKey!!)
			}

		}



	}
	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	private fun requestPermissions(){
		isNotificationGranted = ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED



		val permissionRequest:MutableList<String> = ArrayList()

		if (!isNotificationGranted)
			permissionRequest.add(Manifest.permission.POST_NOTIFICATIONS)

		if(permissionRequest.isNotEmpty())permissionLauncher.launch(permissionRequest.toTypedArray())

	}

	private fun setAlarm(context:Context,time:Long,id:Int) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, AlarmReceiver::class.java)
		val pendingIntent = PendingIntent.getBroadcast(context, id,intent, PendingIntent.FLAG_MUTABLE)
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,pendingIntent)
	}
}
