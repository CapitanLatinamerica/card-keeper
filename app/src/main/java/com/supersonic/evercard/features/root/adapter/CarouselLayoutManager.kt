package com.supersonic.evercard.features.root.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CarouselLayoutManager(
    context: Context
) : LinearLayoutManager(context, VERTICAL, false) {

    private val activeHeight = 180.dpToPx(context)
    private val inactiveHeight = 36.dpToPx(context)
    private val animationDuration = 100L

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        updateChildrenSizes()
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        val scrolled = super.scrollVerticallyBy(dy, recycler, state)
        updateChildrenSizes()
        return scrolled
    }

    private fun updateChildrenSizes() {
        val parentCenter = height / 2
        val maxDistance = height / 2

        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue

            val childTop = getDecoratedTop(child)
            val childBottom = getDecoratedBottom(child)
            val childCenter = (childTop + childBottom) / 2
            val distanceFromCenter = Math.abs(parentCenter - childCenter)

            val proximity = 1f - (distanceFromCenter.toFloat() / maxDistance.toFloat())
            val targetHeight = (activeHeight * proximity + inactiveHeight * (1 - proximity)).toInt()

            // Без анимации — просто меняем размер
            val params = child.layoutParams
            params.height = targetHeight
            child.layoutParams = params
        }
    }

    private fun Int.dpToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
}