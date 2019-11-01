package cn.krisez.account.bean

import cn.bmob.v3.BmobObject

/**
 *Created by zhouchaoxing on 2019/10/31
 */
class VersionBean :BmobObject(){
    var version: String = ""
    var content: String = ""
    var url: String = ""
    var isForce:Boolean = false
}