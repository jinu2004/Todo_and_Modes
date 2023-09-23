package com.jinu.todoandmodes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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

		val bottomSheet = BottomSheet()
		binding.addNewTask.setOnClickListener {
			bottomSheet.show(supportFragmentManager,bottomSheet.tag)
		}
		roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
		val list = arrayListOf<Category>()
		list.add(Category(0,R.drawable.briefcase_svgrepo_com,"office"))
		list.add(Category(1,R.drawable.round_notifications_active_24,"notification"))
		list.add(Category(2, R.drawable.round_person_24,"personal"))
		list.forEach {
			roomViewModel.addNewCategory(it)
		}





		binding.bottomNavigationView.setOnItemSelectedListener {
			when(it.itemId)
			{
				R.id.task->{
//					it.iconTintList = myList
				}
				R.id.modes->{
//					it.iconTintList = myList
				}
				R.id.mine->{
//					it.iconTintList = myList
				}
				R.id.calender->{
					//
				}
				else->{}
			}
			true
		}

	}
}