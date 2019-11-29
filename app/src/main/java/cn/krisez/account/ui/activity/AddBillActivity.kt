package cn.krisez.account.ui.activity

import android.os.Bundle
import android.view.View
import cn.krisez.account.R
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.Presenter

/**
 *Created by zhouchaoxing on 2019/11/29
 */
class AddBillActivity :BaseActivity() {
    override fun init(bundle: Bundle?) {
    }

    override fun onRefresh() {
    }

    override fun newView(): View = View.inflate(this, R.layout.activity_person,null)

    override fun presenter(): Presenter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}