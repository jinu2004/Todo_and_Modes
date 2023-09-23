package com.jinu.todoandmodes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jinu.todoandmodes.databinding.ActivityMainBinding
import com.jinu.todoandmodes.recyclerview.BottomSheet

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val bottomSheet = BottomSheet()
		binding.addNewTask.setOnClickListener {
			bottomSheet.show(supportFragmentManager,bottomSheet.tag)
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