package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.jinu.todoandmodes.databinding.TaskGroupSelectBinding
import com.jinu.todoandmodes.roomdb.dataclass.Category

class DropDownAdapter(
	context: Context,
	resource: Int,
	objects: List<Category>,
) : ArrayAdapter<Category>(context, resource, objects) {
	private lateinit var binding:TaskGroupSelectBinding

	@SuppressLint("ViewHolder")
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		binding = TaskGroupSelectBinding.inflate(LayoutInflater.from(context))

		val item = getItem(position)

		binding.textview.text = item?.heading
		binding.imageView.setImageResource(item!!.icon)

		return binding.root
	}

	override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
		return getView(position,convertView,parent)
	}
}