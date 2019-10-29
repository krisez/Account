package cn.krisez.account.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.krisez.account.R
import cn.krisez.account.bean.User
import cn.krisez.account.presenter.MainPresenter
import cn.krisez.account.view.IMainView
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.Presenter
import com.bumptech.glide.Glide
import com.qmuiteam.qmui.widget.QMUIRadiusImageView
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity(), IMainView {

    private var mPresenter: MainPresenter? = null

    override fun init(bundle: Bundle?) {
        setRefreshEnable(true)
        setUpTopBar(true)
        initHead()
        tv.setOnClickListener {
            val user = User()
            user.head =
                "http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg"
            user.username = "123456"
            user.setPassword("123456")
            user.login(object : SaveListener<User>() {
                override fun done(p0: User?, p1: BmobException?) {
                    Log.d("asd",p0!!.objectId)
                }
            })
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
        mTopBar.addRightImageButton(
            R.drawable.add,
            R.id.main_add_group
        )

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
