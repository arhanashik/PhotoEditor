package com.workfort.photoeditor.app.ui.editor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.workfort.photoeditor.R
import com.workfort.photoeditor.app.ui.editor.holder.FilterViewHolder
import com.workfort.photoeditor.app.ui.editor.listener.FilterClickEvent
import com.workfort.photoeditor.databinding.ItemFilterViewBinding
import ja.burhanrashid52.photoeditor.PhotoFilter

class FilterAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mListOfFilterPairs = ArrayList<Pair<String, PhotoFilter>>()
    private var mListener: FilterClickEvent? = null

    init {
        setupFilters()
    }

    fun setListener(listener: FilterClickEvent) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemFilterViewBinding>(
            inflater, R.layout.item_filter_view, parent, false
        )

        return FilterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mListOfFilterPairs.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FilterViewHolder).bindView(mListOfFilterPairs[position], mListener)
    }

    private fun setupFilters() {
        mListOfFilterPairs.add(Pair("filters/original.jpg", PhotoFilter.NONE))
        mListOfFilterPairs.add(Pair("filters/auto_fix.png", PhotoFilter.AUTO_FIX))
        mListOfFilterPairs.add(Pair("filters/brightness.png", PhotoFilter.BRIGHTNESS))
        mListOfFilterPairs.add(Pair("filters/contrast.png", PhotoFilter.CONTRAST))
        mListOfFilterPairs.add(Pair("filters/documentary.png", PhotoFilter.DOCUMENTARY))
        mListOfFilterPairs.add(Pair("filters/dual_tone.png", PhotoFilter.DUE_TONE))
        mListOfFilterPairs.add(Pair("filters/fill_light.png", PhotoFilter.FILL_LIGHT))
        mListOfFilterPairs.add(Pair("filters/fish_eye.png", PhotoFilter.FISH_EYE))
        mListOfFilterPairs.add(Pair("filters/grain.png", PhotoFilter.GRAIN))
        mListOfFilterPairs.add(Pair("filters/gray_scale.png", PhotoFilter.GRAY_SCALE))
        mListOfFilterPairs.add(Pair("filters/lomish.png", PhotoFilter.LOMISH))
        mListOfFilterPairs.add(Pair("filters/negative.png", PhotoFilter.NEGATIVE))
        mListOfFilterPairs.add(Pair("filters/posterize.png", PhotoFilter.POSTERIZE))
        mListOfFilterPairs.add(Pair("filters/saturate.png", PhotoFilter.SATURATE))
        mListOfFilterPairs.add(Pair("filters/sepia.png", PhotoFilter.SEPIA))
        mListOfFilterPairs.add(Pair("filters/sharpen.png", PhotoFilter.SHARPEN))
        mListOfFilterPairs.add(Pair("filters/temprature.png", PhotoFilter.TEMPERATURE))
        mListOfFilterPairs.add(Pair("filters/tint.png", PhotoFilter.TINT))
        mListOfFilterPairs.add(Pair("filters/vignette.png", PhotoFilter.VIGNETTE))
        mListOfFilterPairs.add(Pair("filters/cross_process.png", PhotoFilter.CROSS_PROCESS))
        mListOfFilterPairs.add(Pair("filters/b_n_w.png", PhotoFilter.BLACK_WHITE))
        mListOfFilterPairs.add(Pair("filters/flip_horizental.png", PhotoFilter.FLIP_HORIZONTAL))
        mListOfFilterPairs.add(Pair("filters/flip_vertical.png", PhotoFilter.FLIP_VERTICAL))
        mListOfFilterPairs.add(Pair("filters/rotate.png", PhotoFilter.ROTATE))
    }
}