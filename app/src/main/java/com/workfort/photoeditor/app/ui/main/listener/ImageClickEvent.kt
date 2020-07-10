package com.workfort.photoeditor.app.ui.main.listener

import com.workfort.photoeditor.app.data.local.image.ImageEntity

/*
*  ****************************************************************************
*  * Created by : Arhan Ashik on 1/2/2019 at 4:25 PM.
*  * Email : ashik.pstu.cse@gmail.com
*  * 
*  * Last edited by : Arhan Ashik on 1/2/2019.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/

interface ImageClickEvent {
    fun onClickImage(image: ImageEntity, position: Int)
}