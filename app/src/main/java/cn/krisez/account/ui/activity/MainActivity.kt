package cn.krisez.account.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import cn.krisez.account.R
import cn.krisez.account.presenter.MainPresenter
import cn.krisez.account.view.IMainView
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.Presenter
import com.bumptech.glide.Glide
import com.qmuiteam.qmui.widget.QMUIRadiusImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.navigation_header.view.*


class MainActivity : BaseActivity(), IMainView {

    private var mPresenter: MainPresenter? = null

    override fun init(bundle: Bundle?) {
        setRefreshEnable(true)
        setUpTopBar(true)
        initHead()
        initNavigation()
        checkPermissions(needPermissions)
    }

    private fun initNavigation() {
        main_drawer.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View) {
                setRefreshEnable(true)
            }

            override fun onDrawerOpened(drawerView: View) {
                setRefreshEnable(false)
            }

        })
        main_navigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_nav_personal -> startActivity(Intent(this, PersonActivity::class.java))
                R.id.menu_nav_setting -> startActivity(Intent(this, SettingActivity::class.java))
                R.id.menu_nav_feedback -> startActivity(Intent(this, PersonActivity::class.java))
                R.id.menu_nav_about -> startActivity(Intent(this, PersonActivity::class.java))
            }
            main_drawer.closeDrawer(main_navigation)
            false
        }

        Glide.with(this)
            .load("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg")
            .into(main_navigation.getHeaderView(0).person_head)
        main_navigation.getHeaderView(0).person_nick.text = "test"
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

        head.setOnClickListener {
            main_drawer.openDrawer(main_navigation)
        }

        Glide.with(this)
            .load("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg")
            .into(head)
        Glide.with(this)
            .load("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg")
            .into(group_user)
        Glide.with(this)
            .load("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg")
            .into(group_user_other)
    }

    override fun newView(): View = View.inflate(this, R.layout.activity_main, null)

    override fun presenter(): Presenter? {
        mPresenter = MainPresenter(this, this)
        return mPresenter
    }

    override fun onRefresh() {
        disableRefresh()
    }
}
