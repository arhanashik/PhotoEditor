package com.workfort.photoeditor.app.ui.main.holder

import android.text.TextUtils
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.workfort.photoeditor.R
import com.workfort.photoeditor.app.data.local.image.ImageEntity
import com.workfort.photoeditor.app.ui.main.listener.ImageClickEvent
import com.workfort.photoeditor.databinding.ItemStaggeredImageBinding
import com.workfort.photoeditor.util.helper.ImageUtil


/*
*  ****************************************************************************
*  * Created by : Arhan Ashik on 12/27/2018 at 6:39 PM.
*  * Email : ashik.pstu.cse@gmail.com
*  * 
*  * Last edited by : Arhan Ashik on 12/27/2018.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/

class ImageStaggeredViewHolder(private val binding: ItemStaggeredImageBinding): RecyclerView.ViewHolder(binding.root) {

    fun bindView(image: ImageEntity, listener: ImageClickEvent?) {
        binding.tvTitle.text = image.title

        val set = ConstraintSet()
        val ratio = String.format("%d:%d", image.width, image.height)
        set.clone(binding.container)
        set.setDimensionRatio(binding.imgImage.id, ratio)
        set.applyTo(binding.container)

        binding.imgImage.setOnClickListener {
            listener?.onClickImage(image, adapterPosition)
        }

        if(!TextUtils.isEmpty(image.url)) ImageUtil().load(binding.imgImage, image.url!!)
    }
}