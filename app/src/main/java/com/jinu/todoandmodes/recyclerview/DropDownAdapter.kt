package com.jinu.todoandmodes.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.jinu.todoandmodes.databinding.TaskGroupSelectBinding
import com.jinu.todoandmodes.mvvm.dataclass.Category

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
		if (isDarkMode(context)){
			val color = Color.parseColor("#00382C") // Replace with your desired color
			val mode = PorterDuff.Mode.SRC_ATOP
			val colorFilter = PorterDuffColorFilter(color, mode)
			binding.imageView.colorFilter = colorFilter
		}
		else{
			val color =Color.parseColor("#FFFFFF") // Replace with your desired color
			val mode = PorterDuff.Mode.SRC_ATOP
			val colorFilter = PorterDuffColorFilter(color, mode)
			binding.imageView.colorFilter = colorFilter
		}

		binding.textview.text = item?.heading
		binding.imageView.setImageResource(item?.icon!!)

		return binding.root
	}

	override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
		return getView(position,convertView,parent)
	}
	private fun isDarkMode(context: Context): Boolean {
		val currentNightMode =
			context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
		return currentNightMode == Configuration.UI_MODE_NIGHT_YES
	}
}