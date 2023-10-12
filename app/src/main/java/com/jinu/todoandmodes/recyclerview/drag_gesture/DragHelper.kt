package com.jinu.todoandmodes.recyclerview.drag_gesture

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.recyclerview.SubAdapter


class DragHelper(private val adapter: SubAdapter) : ItemTouchHelper.Callback() {

	override fun isLongPressDragEnabled(): Boolean {
		return true
	}

	override fun getMovementFlags(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
	): Int {
		//
		return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.RIGHT)
	}


	override fun onMove(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder,
	): Boolean {
		return true
	}

	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
		adapter.delete(viewHolder.adapterPosition)
		adapter.notifyItemChanged(viewHolder.adapterPosition)

	}
}