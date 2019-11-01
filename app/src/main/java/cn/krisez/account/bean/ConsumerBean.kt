package cn.krisez.account.bean

import cn.bmob.v3.BmobObject
import cn.krisez.account.utils.Utils

/**
 *Created by zhouchaoxing on 2019/10/30
 */
class ConsumerBean : BmobObject() {
    var money: String = ""
    val type: Int = 0
    val time: String = "00-00 00:00"//月-日 时:分
    val describe: String = ""

    fun getTypeName(): String = Utils.getTypeName(type)
}