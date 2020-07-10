package com.workfort.photoeditor.util.helper

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/*
*  ****************************************************************************
*  * Created by : Arhan Ashik on 1/2/2019 at 5:32 PM.
*  * Email : ashik.pstu.cse@gmail.com
*  * 
*  * Last edited by : Arhan Ashik on 1/2/2019.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/

class StaggeredGridItemDecoration (spacingPx: Int, spanCount: Int) : RecyclerView.ItemDecoration() {
    private var mSpacingPx: Int = spacingPx
    private var mSpanCount: Int = spanCount

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val frameWidth = ((parent.width - mSpacingPx.toFloat() * (mSpanCount - 1)) / mSpanCount).toInt()
        val padding = parent.width / mSpanCount - frameWidth
        val layoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val itemPosition = layoutParams.viewAdapterPosition

        var leftEdge =  padding
        var topEdge = mSpacingPx
        var rightEdge = padding
        var bottomEdge = mSpacingPx

        val spanIndex = layoutParams.spanIndex
        when (spanIndex) {
            0 -> {
                leftEdge = 0
            }
            (mSpanCount - 1) -> {
                rightEdge = 0
            }
        }

        if(itemPosition < (parent.adapter?.itemCount!! - mSpanCount)) bottomEdge = 0

        outRect.set(
            leftEdge,
            topEdge,
            rightEdge,
            bottomEdge
        )
    }
}