package cn.krisez.account.utils

import android.content.Context
import cn.krisez.account.App
import java.util.*

/**
 *Created by zhouchaoxing on 2019/10/11
 */
object SharePreferencesUtils {

    fun getUUID(): String {
        val uuid = UUID.randomUUID().toString()
        val sp = App.context.getSharedPreferences("install", Context.MODE_PRIVATE)
        val id = sp.getString("uuid", uuid)
        return if (id != uuid) {
            id!!
        } else {
            saveId(uuid)
            uuid
        }
    }

    private fun saveId(uuid: String) {
        val editor = App.context.getSharedPreferences("install", Context.MODE_PRIVATE).edit()
        editor.putString("uuid", uuid)
        editor.apply()
    }

    fun saveGroupGid(id: String) {
        val editor = App.context.getSharedPreferences("configure", Context.MODE_PRIVATE).edit()
        editor.putString("g_id", id)
        editor.apply()
    }

    fun getGid():String?{
        val sp = App.context.getSharedPreferences("configure", Context.MODE_PRIVATE)
        return sp.getString("g_id", "")
    }
}