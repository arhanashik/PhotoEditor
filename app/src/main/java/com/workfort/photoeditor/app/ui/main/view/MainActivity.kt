package com.workfort.photoeditor.app.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.workfort.base.util.helper.Toaster
import com.workfort.photoeditor.app.data.local.image.ImageEntity
import com.workfort.photoeditor.app.ui.main.listener.ImageClickEvent
import com.workfort.photoeditor.util.helper.StaggeredGridItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import com.fxn.pix.Pix
import android.content.pm.PackageManager
import com.fxn.utility.PermUtil
import com.fxn.pix.Options
import com.fxn.utility.ImageQuality
import com.workfort.photoeditor.R
import com.workfort.photoeditor.app.data.local.appconst.Const
import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.workfort.photoeditor.app.ui.editor.view.EditorActivity
import com.workfort.photoeditor.app.ui.main.adapter.ImageStaggeredAdapter
import com.workfort.photoeditor.app.ui.main.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: ImageStaggeredAdapter
    private lateinit var mViewModel: MainViewModel

    private var mCurrentEditPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        initView()
        addObserver()
        triggerMultipleImagePicker()
    }

    private fun initView() {
        rv_images.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_images.addItemDecoration(StaggeredGridItemDecoration(10, 2))

        mAdapter = ImageStaggeredAdapter()
        mAdapter.setListener(object: ImageClickEvent{
            override fun onClickImage(image: ImageEntity, position: Int) {
                mCurrentEditPosition = position

                val intent = Intent(this@MainActivity, EditorActivity::class.java)
                intent.putExtra(Const.Key.IMAGE_ENTITY, image)

                startActivityForResult(intent, Const.RequestCode.EDIT_PHOTO)
            }
        })

        rv_images.adapter = mAdapter

        swipe_refresh.setOnRefreshListener {
            Toaster(this).showToast("refresh!")
            swipe_refresh.isRefreshing = false
        }
    }

    private fun addObserver() {
        mViewModel.getImagesLiveData().observe(this, Observer {
            mAdapter.setImageList(it)
        })

        mViewModel.getImageUpdateLiveData().observe(this, Observer {
            mAdapter.updateImage(it, mCurrentEditPosition)
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    triggerMultipleImagePicker()
                } else {
                    Toast.makeText(this@MainActivity, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG)
                        .show()
                }
                return
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                Const.RequestCode.PIC_MULTIPLE_PHOTO -> {
                    if(data != null) {
                        mViewModel.setImages(data.getStringArrayListExtra(Pix.IMAGE_RESULTS))
                    }
                }
                Const.RequestCode.EDIT_PHOTO -> {
                    val image = data?.getParcelableExtra<ImageEntity>(Const.Key.IMAGE_ENTITY)
                    if(image != null) mViewModel.updateImage(image)
                }
                else -> {
                    Toaster(this).showToast("Who are you!!")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_pic_image -> triggerMultipleImagePicker()
            else -> { }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun triggerSingleImagePicker() {
        Pix.start(this, Options.init().setRequestCode(Const.RequestCode.PIC_SINGLE_PHOTO))
    }

    private fun triggerMultipleImagePicker() {
        val options = Options.init()
            .setRequestCode(Const.RequestCode.PIC_MULTIPLE_PHOTO)                //Request code for activity results
            .setCount(12)                                                         //Number of images to restict selection count
            .setFrontfacing(true)                                                //Front Facing camera on start
            .setImageQuality(ImageQuality.HIGH)                                  //Image Quality
            .setImageResolution(1024, 800)                            //Custom Resolution
            .setPreSelectedUrls(mViewModel.getImageUrls())                       //Pre selected Image Urls
            .setScreenOrientation(Options.SCREEN_ORIENTATION_REVERSE_PORTRAIT)   //Orientaion
            .setPath("/PhotoEditor/images")                                      //Custom Path For Image Storage

        Pix.start(this@MainActivity, options)
    }
}
