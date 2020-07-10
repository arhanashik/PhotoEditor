package com.workfort.photoeditor.app.ui.editor.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.media.effect.EffectFactory
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.workfort.base.util.helper.Toaster
import com.workfort.photoeditor.R
import com.workfort.photoeditor.app.data.local.appconst.Const
import com.workfort.photoeditor.app.data.local.image.ImageEntity
import com.workfort.photoeditor.app.ui.editor.adapter.EmojiAdapter
import com.workfort.photoeditor.app.ui.editor.adapter.FilterAdapter
import com.workfort.photoeditor.app.ui.editor.listener.EmojiClickEvent
import com.workfort.photoeditor.app.ui.editor.listener.FilterClickEvent
import com.workfort.photoeditor.app.ui.editor.viewmodel.EditorViewModel
import com.workfort.photoeditor.util.helper.ImageUtil
import com.workfort.photoeditor.util.helper.KeyboardUtil
import com.yalantis.ucrop.UCrop
import ja.burhanrashid52.photoeditor.*
import kotlinx.android.synthetic.main.activity_editor.*
import timber.log.Timber
import java.io.File

class EditorActivity: AppCompatActivity() {

    private lateinit var mViewModel: EditorViewModel

    private var mImageEntity: ImageEntity? = null

    private var mPhotoEditor: PhotoEditor? = null
    private var mCurrentTextView: View? = null

    private var mBrushColor: Int = Color.WHITE
    private var mTextColor: Int = Color.WHITE

    private var mLeftIn: Animation? = null
    private var mLeftOut: Animation? = null
    private var mRightIn: Animation? = null
    private var mRightOut: Animation? = null
    private var mAnimateView: View? = null

    private object ColorPickerMood {
        const val BRUSH = 1
        const val TEXT = 2
    }
    private var mColorPickerMood: Int = ColorPickerMood.BRUSH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mImageEntity = intent.getParcelableExtra(Const.Key.IMAGE_ENTITY)
        if(mImageEntity == null || TextUtils.isEmpty(mImageEntity?.url)) finish()

        mViewModel = ViewModelProviders.of(this).get(EditorViewModel::class.java)

        ImageUtil().load(photo_editor_view.source, mImageEntity?.url!!)

        setupAnimations()

        setClickEvents()
        setupColorPicker()
        setupEditor()
        setupFilters()
        setupEmojis()
    }

    private fun setClickEvents() {
        btn_brush.setOnClickListener {
            activateBrush(true)
            setBrushProperty(40f, 100)

            KeyboardUtil.hideKeyboard(this@EditorActivity, et_text)
            til_text.visibility = View.GONE

            mColorPickerMood = ColorPickerMood.BRUSH
            showColorPicker(true)
        }
        btn_text.setOnClickListener {
            til_text.visibility = View.VISIBLE
            et_text.requestFocus()
            KeyboardUtil.showKeyboard(this)

            mColorPickerMood = ColorPickerMood.TEXT
            showColorPicker(true)
        }
        btn_eraser.setOnClickListener { setBrushEraser() }
        btn_crop.setOnClickListener { crop() }
        btn_emoji.setOnClickListener { showEmojiPrompt(rv_emojis.visibility == View.INVISIBLE) }
        btn_sticker.setOnClickListener { }
        btn_filters.setOnClickListener { showFilters(rv_filters.visibility != View.VISIBLE) }
        et_text.setOnEditorActionListener (object: TextView.OnEditorActionListener{
            override fun onEditorAction(tv: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    KeyboardUtil.hideKeyboard(this@EditorActivity, et_text)
                    til_text.visibility = View.GONE
                    if(et_text.text != null) {
                        val text = et_text.text.toString()
                        if(!TextUtils.isEmpty(text)) {
                            if(mCurrentTextView == null) addText(text)
                            else editText(text)
                            et_text.setText("")
                        }
                    }
                    return true
                }

                return false
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            if(data != null) {
                val resultUri = UCrop.getOutput(data)
                if(resultUri != null) {
                    val imagePath = ImageUtil().getPath(this, resultUri)?: resultUri.path
                    mViewModel.addCropHistory(imagePath)
                    mImageEntity?.url = imagePath
                    ImageUtil().load(photo_editor_view.source, mImageEntity?.url!!)
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            if(data != null) Timber.e(UCrop.getError(data))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_editor, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_undo -> undo()
            R.id.action_redo -> redo()
            R.id.action_save -> {
                if(til_text.visibility == View.VISIBLE) {
                    KeyboardUtil.hideKeyboard(this@EditorActivity, et_text)
                    til_text.visibility = View.GONE
                    if(et_text.text != null) {
                        val text = et_text.text.toString()
                        if(!TextUtils.isEmpty(text)) {
                            addText(text)
                            et_text.setText("")
                        }
                    }
                } else {
                    if(rv_emojis.visibility == View.VISIBLE) showEmojiPrompt(false)
                    else {
                        val pathStr = "${Environment.getExternalStorageDirectory()}/PhotoEditor/Edited"
                        val path = File(pathStr)
                        if(!path.exists()) path.mkdirs()
                        save("${path.absolutePath}/${mImageEntity?.title}.jpg")
                    }
                }
            }
            else -> {}
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(til_text.visibility == View.VISIBLE) {
            KeyboardUtil.hideKeyboard(this@EditorActivity, et_text)
            til_text.visibility = View.GONE
            showColorPicker(false)
            return
        }

        if(rv_filters.visibility == View.VISIBLE) {
            showFilters(false)
            return
        }

        if(color_picker.visibility == View.VISIBLE) {
            showColorPicker(false)
            return
        }

        if(rv_emojis.visibility == View.VISIBLE) {
            showEmojiPrompt(false)
            return
        }

        super.onBackPressed()
    }

    override fun onDestroy() {
        mViewModel.clearCropHistory()
        super.onDestroy()
    }

    private fun setupColorPicker() {
        //color_picker.colors = intArrayOf(Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW)
        //color_picker.setSelectedColor(Color.RED)

        color_picker.setOnColorChangedListener { color ->
            //val colorStr = Integer.toHexString(color)
            when(mColorPickerMood) {
                ColorPickerMood.BRUSH -> {
                    mBrushColor = color
                    updateBrushColor(mBrushColor)
                }
                ColorPickerMood.TEXT -> mTextColor = color
                else -> { }
            }

            showColorPicker(false)
        }
    }

    private fun setupAnimations() {
        mLeftIn = AnimationUtils.loadAnimation(this, R.anim.left_in)
        mLeftOut = AnimationUtils.loadAnimation(this, R.anim.left_out)
        mRightIn = AnimationUtils.loadAnimation(this, R.anim.right_in)
        mRightOut = AnimationUtils.loadAnimation(this, R.anim.right_out)

        setupAnimationListener(mLeftIn, mLeftOut, mRightIn, mRightOut)
    }

    private fun setupAnimationListener(vararg animations: Animation?) {
        animations.forEach { animation ->
            animation?.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    if(animation == mLeftIn || animation == mRightIn) mAnimateView?.visibility = View.VISIBLE
                }
                override fun onAnimationEnd(p0: Animation?) {
                    if(animation == mLeftOut || animation == mRightOut) mAnimateView?.visibility = View.GONE
                }
                override fun onAnimationRepeat(p0: Animation?) {}
            })
        }
    }

    private fun showColorPicker(show: Boolean) {
        mAnimateView = color_picker
        color_picker.clearAnimation()
        if(show) {
            if(color_picker.visibility != View.VISIBLE) {
                color_picker.setSelectedColor(
                    if(mColorPickerMood == ColorPickerMood.BRUSH) mBrushColor else mTextColor
                )
                color_picker.startAnimation(mRightIn)
            }
        } else {
            if(color_picker.visibility == View.VISIBLE) color_picker.startAnimation(mRightOut)
        }
    }

    private fun setupEditor() {
        //Use custom font using latest support library
        val mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium)

        //loading font from assest
        val mEmojiTypeFace = Typeface.createFromAsset(assets, "emojione-android.ttf")

        mPhotoEditor = PhotoEditor.Builder(this, photo_editor_view)
            .setPinchTextScalable(true)
            .setDefaultTextTypeface(mTextRobotoTf)
            .setDefaultEmojiTypeface(mEmojiTypeFace)
            .build()

        mPhotoEditor?.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {}
            override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
                mCurrentTextView = rootView
                til_text.visibility = View.VISIBLE
                et_text.requestFocus()
                KeyboardUtil.showKeyboard(this@EditorActivity)

                mColorPickerMood = ColorPickerMood.TEXT
                showColorPicker(true)
            }
            override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {}
            override fun onStartViewChangeListener(viewType: ViewType?) {}
            override fun onStopViewChangeListener(viewType: ViewType?) {}
            override fun onRemoveViewListener(numberOfAddedViews: Int) {}
        })
    }

    private fun activateBrush(activate: Boolean) {
        mPhotoEditor?.setBrushDrawingMode(activate)
    }

    private fun setBrushProperty(brushSize: Float = 40f, opacity: Int = 100) {
        mPhotoEditor?.brushSize = brushSize
        mPhotoEditor?.setOpacity(opacity)
        mPhotoEditor?.brushColor = mBrushColor
    }

    private fun updateBrushColor(color: Int) {
        mPhotoEditor?.brushColor = color
    }

    private fun setBrushEraser() {
        mPhotoEditor?.brushEraser()
    }

    private fun setupFilters() {
        val adapter = FilterAdapter()
        adapter.setListener(object: FilterClickEvent {
            override fun onClickFilter(filter: PhotoFilter) {
                setFilter(filter)
            }
        })

        rv_filters.adapter = adapter
    }

    private fun showFilters(show: Boolean) {
        mAnimateView = rv_filters
        rv_filters.clearAnimation()

        rv_filters.startAnimation(if(show) mLeftIn else mLeftOut)
        btn_filters.text = getString(if(show) R.string.label_close else R.string.label_filters)
        btn_filters.setCompoundDrawablesRelativeWithIntrinsicBounds(
            if(show) R.drawable.ic_close else R.drawable.ic_filter, 0, 0, 0
        )
    }

    private fun setFilter(filter: PhotoFilter) {
        mPhotoEditor?.setFilterEffect(filter) //PhotoFilter.BRIGHTNESS
    }

    private fun setCustomFilter() {
        val customEffect = CustomEffect.Builder(EffectFactory.EFFECT_BRIGHTNESS)
            .setParameter("brightness", 0.5f)
            .build()
        mPhotoEditor?.setFilterEffect(customEffect)
    }

    private fun addText(inputText: String) {
        mPhotoEditor?.addText(inputText, mTextColor)
        //mPhotoEditor.addText(mTypeface,inputText, colorCode)
    }

    private fun editText(inputText: String) {
        if(mCurrentTextView == null) return

        mPhotoEditor?.editText(mCurrentTextView, inputText, mTextColor)
        mCurrentTextView = null
    }

    private fun crop() {
        val source = ImageUtil().getImageContentUri(this, File(mImageEntity?.url))
        if(source != null) ImageUtil().cropImage(this, source)
    }

    private fun getAvailableEmojis(): ArrayList<String> {
        return PhotoEditor.getEmojis(this)
    }

    private fun setupEmojis() {
        val adapter = EmojiAdapter()
        adapter.setEmojis(getAvailableEmojis())
        adapter.setListener(object: EmojiClickEvent {
            override fun onClickEmoji(emojiUnicode: String) {
                addEmoji(emojiUnicode)
                showEmojiPrompt(false)
            }
        })

        rv_emojis.layoutManager = GridLayoutManager(this, 5)
        rv_emojis.setHasFixedSize(true)
        rv_emojis.adapter = adapter
    }

    private fun showEmojiPrompt(show: Boolean) {
        rv_emojis.visibility = if(show) View.VISIBLE else View.INVISIBLE
    }

    private fun addEmoji(emojiUnicode: String) {
        mPhotoEditor?.addEmoji(emojiUnicode)
        //mPhotoEditor.addEmoji(mEmojiTypeface,emojiUnicode)
    }

    private fun addImageOrSticker(bitmap: Bitmap) {
        mPhotoEditor?.addImage(bitmap)
    }

    private fun undo() {
        mPhotoEditor?.undo()
    }

    private fun redo() {
        mPhotoEditor?.redo()
    }

    @SuppressLint("MissingPermission")
    private fun save(filePath: String) {
        val file = File(filePath)
        if(file.exists()) file.delete()

        mPhotoEditor?.saveAsFile(filePath, object : PhotoEditor.OnSaveListener {
            override fun onSuccess(imagePath: String) {
                Timber.e("Image Saved Successfully")
                mImageEntity?.url = imagePath

                val returnIntent = Intent()
                returnIntent.putExtra(Const.Key.IMAGE_ENTITY, mImageEntity)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }

            override fun onFailure(exception: Exception) {
                Timber.e("Failed to save Image")
                Toaster(this@EditorActivity).showToast("Failed to save Image")
            }
        })
    }
}