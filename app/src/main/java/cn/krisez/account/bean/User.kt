package cn.krisez.account.bean

import cn.bmob.v3.BmobUser

/**
 *Created by zhouchaoxing on 2019/10/16
 */
class User : BmobUser() {
    var head: String = ""
    var nickname: String = ""

    override fun toString(): String {
        return "object:$objectId nickname:$nickname"
    }
}