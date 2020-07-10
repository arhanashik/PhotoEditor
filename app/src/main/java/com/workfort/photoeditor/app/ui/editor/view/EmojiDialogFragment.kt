package com.workfort.photoeditor.app.ui.editor.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.workfort.photoeditor.R
import com.workfort.photoeditor.app.ui.editor.adapter.EmojiAdapter
import com.workfort.photoeditor.app.ui.editor.listener.EmojiClickEvent
import com.workfort.photoeditor.databinding.PromptEmojiBinding

class EmojiDialogFragment: DialogFragment() {

    companion object {
        private val mEmojis = ArrayList<String>()
        private var mListener: EmojiClickEvent? = null

        fun newInstance(emojis: ArrayList<String>, listener: EmojiClickEvent): EmojiDialogFragment {
            mEmojis.addAll(emojis)
            mListener = listener

            return EmojiDialogFragment()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mEmojis.clear()
        mListener = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return context.let { ctx ->
            AlertDialog.Builder(ctx)
                .setView(createView(ctx!!))
                .create()
        }
    }

    private fun createView(ctx: Context) : View {

        val binding = DataBindingUtil.inflate<PromptEmojiBinding>(
            LayoutInflater.from(ctx), R.layout.prompt_emoji, null, false
        )

        binding.rvEmojis.layoutManager = GridLayoutManager(ctx, 3)
        binding.rvEmojis.setHasFixedSize(true)

        val adapter = EmojiAdapter()
        adapter.setEmojis(mEmojis)
        adapter.setListener(object: EmojiClickEvent {
            override fun onClickEmoji(emojiUnicode: String) {
                mListener?.onClickEmoji(emojiUnicode)
            }
        })
        binding.rvEmojis.adapter = adapter

        return binding.root
    }
}