package com.workfort.photoeditor.app.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.workfort.photoeditor.app.data.local.image.ImageEntity

class ImageDiffCallback(
        private val old: List<ImageEntity>,
        private val new: List<ImageEntity>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition].id == new[newPosition].id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition].url.equals(new[newPosition].url)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
