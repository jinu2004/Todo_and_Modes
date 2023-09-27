package com.jinu.todoandmodes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinu.todoandmodes.databinding.ActivityMainBinding
import com.jinu.todoandmodes.recyclerview.BottomSheet
import com.jinu.todoandmodes.roomdb.dataclass.Category
import com.jinu.todoandmodes.roomdb.viewmodel.RoomViewModel

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding
	private lateinit var roomViewModel: RoomViewModel
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

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

	}

}
