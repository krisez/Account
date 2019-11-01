package cn.krisez.account.bean

import cn.bmob.v3.BmobObject
import cn.krisez.account.App

/**
 *Created by zhouchaoxing on 2019/11/1
 */
class Group : BmobObject() {
    var g_a: User = App.user
    var agree: Int = 0
    lateinit var g_b: User
    var g_id: String = ((Math.random() * 9 + 1) * 100000).toString().substring(0, 6)


}