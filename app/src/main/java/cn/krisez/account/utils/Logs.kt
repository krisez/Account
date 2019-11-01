package cn.krisez.account.utils

import android.util.Log
import cn.krisez.account.BuildConfig

/**
 *Created by zhouchaoxing on 2019/10/31
 */
object Logs {

    fun d(tag: String, msg: String) {
        if (BuildConfig.logs) {
            Log.d(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.logs) {
            Log.e(tag, msg)
        }
    }
}