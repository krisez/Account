package cn.krisez.account.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.krisez.account.R
import cn.krisez.account.bean.VersionBean
import cn.krisez.account.services.DownloadFileServices
import cn.krisez.account.utils.Logs
import cn.krisez.framework.utils.DensityUtil
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction

/**
 *Created by zhouchaoxing on 2019/10/31
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        QMUIStatusBarHelper.translucent(this)
        checkUpdate()
    }

    private fun checkUpdate() {
        val query = BmobQuery<VersionBean>()
        query.addWhereGreaterThan("version", DensityUtil.getAppVersionName(this))
        query.setLimit(1)
        query.findObjects(object : FindListener<VersionBean>() {
            override fun done(list: MutableList<VersionBean>?, e: BmobException?) {
                Logs.d(localClassName,"数据："+list.toString())
                Logs.e(localClassName,"error："+e.toString())
                if (e == null) {
                    if (list != null && list.isNotEmpty()) {
                        val bean = list[0]
                        val builder = QMUIDialog.MessageDialogBuilder(this@SplashActivity).setTitle("发现新版本")
                                .setMessage(bean.content)
                                .setCancelable(false)
                                .setCanceledOnTouchOutside(false)
                                .addAction("升级") { dialog, _ ->
                                    startService(Intent(this@SplashActivity, DownloadFileServices::class.java).putExtra("url", bean.url).putExtra("version", bean.version))
                                    dialog.dismiss()
                                }
                        if (!bean.isForce) {
                            builder.addAction(0,"下次再说",QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, _ ->
                                dialog.dismiss()
                            }
                        }
                        builder.show().setOnDismissListener{
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                    }else{
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        })
    }
}