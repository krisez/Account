package cn.krisez.account

import android.app.Application
import android.content.Context
import cn.bmob.v3.Bmob

/**
 *Created by zhouchaoxing on 2019/10/10
 */
class App : Application() {

    private var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Bmob.resetDomain("http://sdk.krisez.cn/8/");
        Bmob.initialize(this, "a9fe93ffc254499038a17856b673241e")
    }
}