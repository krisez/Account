package cn.krisez.account.bean

import cn.bmob.v3.BmobObject
import cn.krisez.account.utils.Utils

/**
 *Created by zhouchaoxing on 2019/10/30
 */
class ConsumerBean : BmobObject() {
    var money: String = ""
    var type: Int = 0
    var time: String = "00-00 00:00"//月-日 时:分
    var describe: String = ""

    fun getTypeName(): String = Utils.getTypeName(type)
}