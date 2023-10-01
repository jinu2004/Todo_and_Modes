package com.jinu.todoandmodes.recyclerview.drag_gesture

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import com.jinu.todoandmodes.recyclerview.SubAdapter


class DragHelper(private val adapter:SubAdapter): ItemTouchHelper.Callback() {

	override fun isLongPressDragEnabled(): Boolean {
		return true
	}

	override fun getMovementFlags(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
	): Int {
		//
		return makeMovementFlags(UP or DOWN, ACTION_STATE_IDLE)
	}


	override fun onMove(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder,
	): Boolean {
		val fromPosition = viewHolder.adapterPosition
		val toPosition = target.adapterPosition
		adapter.moveItem(fromPosition, toPosition)
		return true
	}

	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
		//
	}
}