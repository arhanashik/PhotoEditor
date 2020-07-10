package com.workfort.photoeditor.util.helper

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.workfort.base.util.helper.FileUtil
import com.workfort.photoeditor.R
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.model.AspectRatio
import com.yalantis.ucrop.view.CropImageView
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.*


class ImageUtil {

    fun cropImage(activity: Activity, uri: Uri) {
        try {
            val id = Random().nextInt(10000)
            val desImg = FileUtil().createEmptyFile("Image_$id.JPEG")
            if (desImg.exists()) {
                if (desImg.delete()) {
                    Timber.e("Old cache cleared")
                }
            }

            val options = UCrop.Options()
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
            options.setCompressionQuality(100)
            options.setFreeStyleCropEnabled(true)
            options.setHideBottomControls(false)

            /*
            If you want to configure how gestures work for all UCropActivity tabs
            options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
            * */

            /*
            This sets max size for bitmap that will be decoded from source Uri.
            More size - more memory allocation, default implementation uses screen diagonal.
            options.setMaxBitmapSize(640);
            * */

            /*
            Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧
            options.setMaxScaleMultiplier(5);
            options.setImageToCropBoundsAnimDuration(666);
            options.setDimmedLayerColor(Color.CYAN);
            options.setCircleDimmedLayer(true);
            options.setShowCropFrame(false);
            options.setCropGridStrokeWidth(20);
            options.setCropGridColor(Color.GREEN);
            options.setCropGridColumnCount(2);
            options.setCropGridRowCount(1);
            options.setToolbarCropDrawable(R.drawable.your_crop_icon);
            options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);
            */

            options.setToolbarTitle(activity.getString(R.string.title_activity_crop_image))

            // Color palette
            options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
            options.setToolbarWidgetColor(ContextCompat.getColor(activity, android.R.color.white))
            options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
            options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
            //options.setActiveWidgetColor(ContextCompat.getColor(activity, R.color.colorPrimary))
            //options.setRootViewBackgroundColor(ContextCompat.getColor(activity, R.color.your_color_res))

            //val displayParams = AndroidUtil().getDisplayParams(activity)
            //val ratio = AspectRatio("Fixed", displayParams.width.toFloat(), displayParams.height.toFloat())
            //options.setAspectRatioOptions(0, ratio)

            options.setAspectRatioOptions(
                1,
                AspectRatio("WOW", 1f, 2f),
                AspectRatio("MUCH", 3f, 4f),
                AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
                AspectRatio("SO", 16f, 9f),
                AspectRatio("ASPECT", 1f, 1f)
            )

            UCrop.of(uri, Uri.fromFile(desImg))
                .withOptions(options)
                .start(activity)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getBackgroundGradient(color1: Int, color2: Int): GradientDrawable {
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(color1, color2, color1)
        )
    }

    fun adjustOpacity(bitmap: Bitmap, opacity: Int): Bitmap {
        val mutableBitmap = if (bitmap.isMutable)
            bitmap
        else
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val colour = opacity and 0xFF shl 24
        canvas.drawColor(colour, PorterDuff.Mode.DST_IN)
        return mutableBitmap
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun uriToBitmap(context: Context, selectedFileUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val parcelFileDescriptor = context.contentResolver
                .openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)

            parcelFileDescriptor.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

    fun getImageContentUri(context: Context, imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath), null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            cursor.close()
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                return context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                return null
            }
        }
    }

    fun getPath(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(
            uri, projection, null, null, null
        ) ?: return null
        val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(columnIndex)
        cursor.close()
        return s
    }

    fun load(imageView: ImageView, path: String) {
        val requestOptions = RequestOptions()
            .timeout(20000)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
        Glide
            .with(imageView.context)
            .load(path)
            .apply(requestOptions)
            .into(imageView)
    }
}