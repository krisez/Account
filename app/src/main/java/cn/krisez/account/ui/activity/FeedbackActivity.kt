package cn.krisez.account.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.krisez.account.R
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.BaseFragment
import cn.krisez.framework.base.Presenter
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_person.*

/**
 *Created by zhouchaoxing on 2019/10/16
 */
class FeedbackActivity : BaseActivity() {
    override fun init(bundle: Bundle?) {
        setRefreshEnable(true)
        setUpTopBar(true)
        showBackIconAndClick()
    }

    override fun onRefresh() {
    }

    override fun newView(): View? = null

    override fun presenter(): Presenter? = null
}