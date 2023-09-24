package com.jinu.todoandmodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jinu.todoandmodes.databinding.FragmentTaskViewBinding


class TaskView : Fragment() {
	private lateinit var binding: FragmentTaskViewBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentTaskViewBinding.inflate(inflater,container,false)




		return binding.root
	}
}