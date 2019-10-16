package cn.krisez.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_person.*

/**
 *Created by zhouchaoxing on 2019/10/16
 */
class PersonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)
        setTranslucent()
        tv1.setOnClickListener {
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }
        tv2.setOnClickListener {
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        }
    }

    private fun setTranslucent() {
        QMUIStatusBarHelper.translucent(this)
    }
}