package com.workfort.photoeditor.app.ui.editor.holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import ja.burhanrashid52.photoeditor.PhotoFilter

import com.workfort.photoeditor.databinding.ItemFilterViewBinding
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.workfort.photoeditor.app.ui.editor.listener.FilterClickEvent
import java.io.IOException
import java.io.InputStream

class FilterViewHolder(private val binding: ItemFilterViewBinding): RecyclerView.ViewHolder(binding.root) {

    fun bindView(filterPair: Pair<String, PhotoFilter>, listener: FilterClickEvent?) {
        val bitmap = getBitmapFromAsset(binding.root.context, filterPair.first)
        if(bitmap != null) binding.imgFilterView.setImageBitmap(bitmap)
        binding.tvFilterName.text = filterPair.second.name.replace("_", " ")

        binding.imgFilterView.setOnClickListener { listener?.onClickFilter(filterPair.second) }
    }

    private fun getBitmapFromAsset(context: Context, strName: String): Bitmap? {
        val assetManager = context.assets
        var istr: InputStream? = null
        try {
            istr = assetManager.open(strName)
            return BitmapFactory.decodeStream(istr)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}