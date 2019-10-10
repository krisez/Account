package cn.krisez.account

import android.app.Application
import cn.bmob.v3.Bmob

/**
 *Created by zhouchaoxing on 2019/10/10
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Bmob.initialize(this, "a9fe93ffc254499038a17856b673241e")
    }
}