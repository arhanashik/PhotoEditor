package com.workfort.photoeditor.app.data.local.appconst;
/*
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 12/28/2018 at 4:28 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * Last edited by : Arhan Ashik on 12/28/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

object Const {
    object RequestCode {
        const val PIC_SINGLE_PHOTO = 100
        const val PIC_MULTIPLE_PHOTO = 101
        const val EDIT_PHOTO = 102
    }

    object Key {
        const val IMAGE_ENTITY = "IMAGE_ENTITY"
        const val URL = "URL"
    }

    // Prefix
    const val PREFIX_IMAGE = "IMG_"
    // Postfix
    const val SUFFIX_IMAGE = ".jpg"
}
