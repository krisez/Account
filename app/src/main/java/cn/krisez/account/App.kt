package cn.krisez.account

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.core.app.ActivityCompat
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobUser
import cn.krisez.account.bean.User
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

        if (BmobUser.isLogin()) {
            user = BmobUser.getCurrentUser(User::class.java)
        }

        getAppInfo()
    }

    companion object {
        lateinit var context: Context
        var user = User()
    }

    private fun getAppInfo() {
        val pm = packageManager
        val packages = pm.getInstalledPackages(0)
        var s = ""
        var o = ""
        for(packageInfo in packages){
            if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) { //非系统应用
                s =  s + "label:" + packageInfo.applicationInfo.loadLabel(packageManager) + "\n" +
                        "packageName:" + packageInfo.packageName + "\n" +
                        "versionCode:" + packageInfo.versionCode + "\n" +
                        "versionName:" + packageInfo.versionName + "\n"
            } else { // 系统应用
                o = o + "label:" + packageInfo.applicationInfo.loadLabel(packageManager) + "\n" +
                        "packageName:" + packageInfo.packageName + "\n" +
                        "versionCode:" + packageInfo.versionCode + "\n" +
                "versionName:" + packageInfo.versionName + "\n"
            }
        }
        Log.d("asdasd",s)
        Log.d("asdasd3333",o)
    }

}