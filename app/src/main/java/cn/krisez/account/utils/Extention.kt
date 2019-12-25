package cn.krisez.account.utils

import android.content.Context
import android.view.View
import android.widget.Toast

/**
 *Created by zhouchaoxing on 2019/12/24
 */
class Extention {}
fun String.toast(context: Context){
    Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
}
fun Int.toDp(context:Context) = context.resources.displayMetrics.density / this + 0.5f
fun Float.toDp(context:Context) = context.resources.displayMetrics.density / this + 0.5f
