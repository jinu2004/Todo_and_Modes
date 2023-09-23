package com.jinu.todoandmodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.jinu.todoandmodes.databinding.FragmentToDoListBinding
import com.jinu.todoandmodes.recyclerview.ChipAdapter


class ToDoListView : Fragment()
{
	private lateinit var binding:FragmentToDoListBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentToDoListBinding.inflate(inflater,container,false)

		binding.recycler.apply {
			layoutManager = LinearLayoutManager(binding.root.context, HORIZONTAL,false)
			adapter = ChipAdapter(arrayListOf("data","boom","hello"))
		}




		return binding.root
	}

}