package cn.krisez.account.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.DownloadFileListener
import android.app.NotificationChannel
import android.annotation.TargetApi
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import cn.krisez.account.R
import java.io.File


/**
 *Created by zhouchaoxing on 2019/10/31
 */
class DownloadFileServices : Service() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var builder: NotificationCompat.Builder
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("0", "应用下载", IMPORTANCE_HIGH);
        }
        builder = NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("下载中")
                .setProgress(100, 0, false)
        notificationManager.notify(0, builder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra("url")
        val ver = intent?.getStringExtra("version")
        val bmobFile = BmobFile("account_$ver.apk", "", url)
        val file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        bmobFile.download(File(file, "account_$ver.apk"), object : DownloadFileListener() {

            override fun onProgress(progress: Int?, max: Long) {
                if (progress != null) {
                    builder.setProgress(100, progress, false)
                    builder.setContentTitle("下载：$progress%")
                    notificationManager.notify(0, builder.build())
                }
            }

            override fun done(path: String?, p1: BmobException?) {
                val i = Intent(Intent.ACTION_VIEW)
                i.setDataAndType(Uri.parse("file://$path"), "application/vnd.android.package-archive")
                if ((Build.VERSION.SDK_INT >= 24)) {//判读版本是否在7.0以上
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                notificationManager.cancel(0)
                stopSelf()
            }
        })
        return super.onStartCommand(intent, flags, startId)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? = null

}