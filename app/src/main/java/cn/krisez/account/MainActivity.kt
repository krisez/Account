package cn.krisez.account

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import cn.krisez.account.presenter.MainPresenter
import cn.krisez.account.view.IMainView
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.IBaseView
import cn.krisez.framework.base.Presenter
import com.bumptech.glide.Glide
import com.qmuiteam.qmui.widget.QMUIRadiusImageView

import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(), IMainView {

    private var mPresenter: MainPresenter? = null

    override fun init(bundle: Bundle?) {
        setRefreshEnable(true)
        setUpTopBar()
        initHead()
        tv.setOnClickListener {
            startActivity(Intent(this, PersonActivity::class.java))
        }
    }

    private fun initHead() {
        val head = QMUIRadiusImageView(this)
        head.isCircle = true
        mTopBar.addLeftView(
            head,
            R.id.main_top_head,
            mTopBar.generateTopBarImageButtonLayoutParams()
        )
        mTopBar.addRightImageButton(R.drawable.add, R.id.main_add_group)

        Glide.with(this)
            .load("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg")
            .into(head)
    }

    override fun newView(): View = View.inflate(this, R.layout.activity_main, null)

    override fun presenter(): Presenter? {
        mPresenter = MainPresenter(this, this)
        return mPresenter
    }

    override fun onRefresh() {

    }
}
