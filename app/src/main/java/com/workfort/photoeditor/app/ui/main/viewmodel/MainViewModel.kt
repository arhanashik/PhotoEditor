package com.workfort.photoeditor.app.ui.main.viewmodel

import android.text.TextUtils
import android.util.SparseArray
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.workfort.photoeditor.app.data.local.image.ImageEntity

class MainViewModel : ViewModel() {
    private val mSelectedImagesLiveData = MutableLiveData<ArrayList<ImageEntity>>()
    private val mImageUpdateLiveData = MutableLiveData<ImageEntity>()
    private val mSelectedImages = SparseArray<String>()

    fun getImagesLiveData(): MutableLiveData<ArrayList<ImageEntity>> {
        return mSelectedImagesLiveData
    }

    fun getImageUpdateLiveData(): MutableLiveData<ImageEntity> {
        return mImageUpdateLiveData
    }

    private fun getImages(): ArrayList<ImageEntity> {
        val images = ArrayList<ImageEntity>()
        val totalImage = mSelectedImages.size()
        for(i in 0 until totalImage) {
            if(!TextUtils.isEmpty(mSelectedImages.valueAt(i))) {
                images.add(
                    ImageEntity(
                        mSelectedImages.keyAt(i), "Image $i", mSelectedImages.valueAt(i), 100, 120
                    )
                )
            }
        }

        return images
    }

    fun setImages(images: ArrayList<String>) {
        if(images.isNullOrEmpty()) return

        var lastKey = mSelectedImages.size()
        images.forEach {
            if(sparseIndexOfValue(it) > -1) return@forEach

            mSelectedImages.append(lastKey, it)
            lastKey++
        }

        mSelectedImagesLiveData.postValue(getImages())
    }

    private fun sparseIndexOfValue(url: String): Int {
        val totalImage = mSelectedImages.size()

        for(i in 0 until totalImage) {
            if(mSelectedImages.valueAt(i) == url) {
                return i
            }
        }

        return -1
    }

    fun getImageUrls(): ArrayList<String> {
        val imageUrls = ArrayList<String>()
        val totalImage = mSelectedImages.size()
        for(i in 0 until totalImage) {
            imageUrls.add(mSelectedImages.valueAt(i))
        }

        return imageUrls
    }

    fun updateImage(image: ImageEntity) {
        mImageUpdateLiveData.postValue(image)

        if(mSelectedImages.indexOfKey(image.id) < 0) return

        mSelectedImages.setValueAt(image.id, image.url)

//        val changedImages = ArrayList<ImageEntity>()
//        changedImages.add(image)
    }
}
