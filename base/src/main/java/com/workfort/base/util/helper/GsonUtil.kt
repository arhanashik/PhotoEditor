package com.workfort.base.util.helper

import com.google.gson.Gson

open class GsonUtil {
    fun toJson(obj: Any): String {
        return Gson().toJson(obj)
    }

    fun<T> fromJson(jsonStr: String, any: Class<T>): T {
        return Gson().fromJson(jsonStr, any)
    }
}