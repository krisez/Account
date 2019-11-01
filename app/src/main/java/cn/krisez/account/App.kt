package cn.krisez.account

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import cn.bmob.v3.Bmob
import cn.krisez.account.services.InstallationServices

/**
 *Created by zhouchaoxing on 2019/10/10
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Bmob.resetDomain(BuildConfig.OWN_URL)
        Bmob.initialize(this, BuildConfig.BMOB_KEY)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == 0) {
            startService(Intent(this, InstallationServices::class.java))
        }
    }

    companion object {
        lateinit var context: Context
    }

}