package cn.krisez.account.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.krisez.account.bean.InstanceBean
import cn.krisez.account.utils.Logs
import cn.krisez.account.utils.SharePreferencesUtils
import cn.krisez.framework.utils.DensityUtil

/**
 *Created by zhouchaoxing on 2019/10/31
 */
class InstallationServices : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var imei = DensityUtil.getIMEI(this)
        if (imei.isEmpty()) {
            imei = SharePreferencesUtils.getUUID()
        }
        val bmobQuery = BmobQuery<InstanceBean>()
        bmobQuery.addWhereEqualTo("imei", imei)
        bmobQuery.findObjects(object : FindListener<InstanceBean>() {
            override fun done(list: MutableList<InstanceBean>?, e: BmobException?) {
                if (e == null) {
                    if (list == null || list.size == 0) {
                        val install = InstanceBean()
                        install.imei = imei
                        install.model = Build.MODEL
                        install.ver = Build.VERSION.SDK_INT
                        install.save(object : SaveListener<String>() {
                            override fun done(p0: String?, p1: BmobException?) {
                                stopSelf()
                            }
                        })
                    }
                }else{
                    stopSelf()
                    e.message?.let { Logs.e("GetIMEI", it) }
                }
            }
        })
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null

}