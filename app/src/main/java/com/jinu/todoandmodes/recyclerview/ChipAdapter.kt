package com.jinu.todoandmodes.recyclerview

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources.Theme
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.R
import com.jinu.todoandmodes.databinding.ChipRecyclerViewItemBinding

class ChipAdapter(private val list: List<String>) : RecyclerView.Adapter<ChipAdapter.ViewHolder>() {
	private var selectedItemPosition = 0

	inner class ViewHolder(val binding: ChipRecyclerViewItemBinding) :
		RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = ChipRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context))
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return list.size
	}


	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.binding.chip.text = list[position]
		holder.binding.chip.setOnClickListener {
			val previousSelectedItem = selectedItemPosition
			selectedItemPosition = holder.adapterPosition
			notifyItemChanged(previousSelectedItem)
			notifyItemChanged(selectedItemPosition)
		}
		if (selectedItemPosition == position) {
			if (isDarkMode(holder.binding.root.context))
				holder.binding.chip.setChipBackgroundColorResource(R.color.md_theme_dark_secondaryContainer)
			else
				holder.binding.chip.setChipBackgroundColorResource(R.color.md_theme_light_secondaryContainer)
		} else {
			holder.binding.chip.setChipBackgroundColorResource(R.color.transparent)
		}
	}

	private fun isDarkMode(context: Context): Boolean {
		val currentNightMode =
			context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
		return currentNightMode == Configuration.UI_MODE_NIGHT_YES
	}
}