package cn.krisez.account.bean

import cn.bmob.v3.BmobObject

/**
 *Created by zhouchaoxing on 2019/10/31
 */
class InstanceBean : BmobObject() {
    var imei: String = ""
    val os: String = "android"
    var ver: Int = 5
    var model: String = ""

}