package com.workfort.photoeditor.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.workfort.photoeditor.R
import com.workfort.photoeditor.app.data.local.image.ImageEntity
import com.workfort.photoeditor.app.ui.main.holder.ImageStaggeredViewHolder
import com.workfort.photoeditor.app.ui.main.listener.ImageClickEvent
import com.workfort.photoeditor.databinding.ItemStaggeredImageBinding

/*
*  ****************************************************************************
*  * Created by : Arhan Ashik on 12/27/2018 at 7:12 PM.
*  * Email : ashik.pstu.cse@gmail.com
*  * 
*  * Last edited by : Arhan Ashik on 12/27/2018.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/

class ImageStaggeredAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mListOfImages = ArrayList<ImageEntity>()
    private var mListener: ImageClickEvent? = null

    fun setImageList(listOfImages: List<ImageEntity>) {
        val callback = ImageDiffCallback(mListOfImages.toList(), listOfImages)
        val result = DiffUtil.calculateDiff(callback)

        mListOfImages.clear()
        mListOfImages.addAll(listOfImages)
        result.dispatchUpdatesTo(this)
    }

    fun getImageList(): ArrayList<ImageEntity> {
        return mListOfImages
    }

    fun updateImage(image: ImageEntity, position: Int) {
        mListOfImages.set(position, image)
        notifyItemChanged(position)
    }

    fun setListener(listener: ImageClickEvent) {
        this.mListener = listener
    }

    fun clear() {
        mListOfImages.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemStaggeredImageBinding>(LayoutInflater.from(parent.context),
            R.layout.item_staggered_image, parent, false)

        return ImageStaggeredViewHolder(binding)
    }

    override fun getItemCount(): Int = mListOfImages.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val wallpaperViewHolder = viewHolder as ImageStaggeredViewHolder

        wallpaperViewHolder.bindView(mListOfImages[position], mListener)
    }
}