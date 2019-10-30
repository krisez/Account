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
        Bmob.resetDomain(BuildConfig.OWN_URL)
        Bmob.initialize(this,BuildConfig.BMOB_KEY)
    }
}