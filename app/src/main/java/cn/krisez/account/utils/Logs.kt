package cn.krisez.account.utils

import android.util.Log

/**
 *Created by zhouchaoxing on 2019/10/31
 */
object Logs{

    fun d(tag:String,msg:String){
        Log.d(tag,msg)
    }

    fun e(tag:String,msg:String){
        Log.e(tag,msg)
    }
}