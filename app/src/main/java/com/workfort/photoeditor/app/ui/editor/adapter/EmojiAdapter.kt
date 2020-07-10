package com.workfort.photoeditor.app.ui.editor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.workfort.photoeditor.R
import com.workfort.photoeditor.app.ui.editor.holder.EmojiViewHolder
import com.workfort.photoeditor.app.ui.editor.listener.EmojiClickEvent
import com.workfort.photoeditor.databinding.ItemEmojiBinding

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

class EmojiAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mListOfEmojis = ArrayList<String>()
    private var mListener: EmojiClickEvent? = null

    fun setEmojis(listOfEmojis: List<String>) {
        mListOfEmojis.clear()
        mListOfEmojis.addAll(listOfEmojis)
        notifyDataSetChanged()
    }

    fun getEmojis(): ArrayList<String> {
        return mListOfEmojis
    }

    fun setListener(listener: EmojiClickEvent) {
        this.mListener = listener
    }

    fun clear() {
        mListOfEmojis.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemEmojiBinding>(LayoutInflater.from(parent.context),
            R.layout.item_emoji, parent, false)

        return EmojiViewHolder(binding)
    }

    override fun getItemCount(): Int = mListOfEmojis.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val emojiViewHolder = viewHolder as EmojiViewHolder

        emojiViewHolder.bindView(mListOfEmojis[position], mListener)
    }
}