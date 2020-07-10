package com.workfort.photoeditor.app.ui.editor.holder

import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import com.workfort.photoeditor.app.ui.editor.listener.EmojiClickEvent
import com.workfort.photoeditor.databinding.ItemEmojiBinding

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

class EmojiViewHolder(private val binding: ItemEmojiBinding): RecyclerView.ViewHolder(binding.root) {

    fun bindView(emojiUnicode: String, listener: EmojiClickEvent?) {
        val context = binding.root.context
        val mEmojiTypeFace = Typeface.createFromAsset(context.assets, "emojione-android.ttf")
        binding.tvEmoji.typeface = mEmojiTypeFace

        binding.tvEmoji.text = emojiUnicode

        binding.tvEmoji.setOnClickListener {
            listener?.onClickEmoji(emojiUnicode)
        }
    }
}