package cn.krisez.account.utils

import android.content.Context
import cn.krisez.account.App
import cn.krisez.account.bean.Group
import com.google.gson.Gson
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

    //组队的id
    fun saveGroupGid(id: String) {
        val editor = App.context.getSharedPreferences("configure", Context.MODE_PRIVATE).edit()
        editor.putString("g_id", id)
        editor.apply()
    }

    fun getGid(): String? {
        val sp = App.context.getSharedPreferences("configure", Context.MODE_PRIVATE)
        return sp.getString("g_id", "")
    }

    //缓存组队内容，触发：自己创建，组队成功才会调用
    fun saveGroup(group: Group) {
        val editor = App.context.getSharedPreferences("configure", Context.MODE_PRIVATE).edit()
        editor.putString("group", group.toString())
        editor.apply()
    }

    fun getGroup(): Group? {
        val sp = App.context.getSharedPreferences("configure", Context.MODE_PRIVATE)
        return Gson().fromJson(sp.getString("group", ""),Group::class.java)
    }
}