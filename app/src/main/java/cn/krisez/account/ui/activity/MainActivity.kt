package cn.krisez.account.ui.activity

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import cn.bmob.v3.BmobUser
import cn.krisez.account.App
import cn.krisez.account.R
import cn.krisez.account.bean.ConsumerBean
import cn.krisez.account.bean.User
import cn.krisez.account.presenter.MainPresenter
import cn.krisez.account.utils.SharePreferencesUtils
import cn.krisez.account.view.IMainView
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.Presenter
import com.bumptech.glide.Glide
import com.qmuiteam.qmui.widget.QMUIRadiusImageView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
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
        initListener()
        checkPermissions(needPermissions)
    }

    private fun initListener(){
        add_new_bill.setOnClickListener{
            startActivity(Intent(this,AddBillActivity::class.java))
        }
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
                R.id.menu_nav_personal -> {
                    if (BmobUser.isLogin()) {
                        startActivity(Intent(this, PersonActivity::class.java))
                    } else {
                        startActivityForResult(Intent(this, LoginActivity::class.java), 100)
                    }
                }
                R.id.menu_nav_setting -> startActivity(Intent(this, SettingActivity::class.java))
                R.id.menu_nav_feedback -> startActivity(Intent(this, FeedbackActivity::class.java))
                R.id.menu_nav_about -> startActivity(Intent(this, AboutActivity::class.java))
            }
            main_drawer.closeDrawer(main_navigation)
            false
        }

        Glide.with(this)
                .load(App.user.head)
                .error(R.drawable.ic_money_eye)
                .into(main_navigation.getHeaderView(0).person_head)
        main_navigation.getHeaderView(0).person_nick.text = App.user.nickname
    }

    private fun initHead() {
        val head = QMUIRadiusImageView(this)
        head.isCircle = true
        mTopBar.addLeftView(head, R.id.main_top_head, mTopBar.generateTopBarImageButtonLayoutParams())

        if (SharePreferencesUtils.getGid().isNullOrEmpty()) {
            mTopBar.addRightImageButton(R.drawable.add, R.id.main_add_group).setOnClickListener {
                if (!BmobUser.isLogin()) {
                    error("请先登录~")
                    return@setOnClickListener
                }
                QMUIDialog.MenuDialogBuilder(this)
                        .addItems(arrayOf("创建队伍", "加入队伍")) { dialog, index ->
                            when (index) {
                                0 -> {
                                    tipLog = QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                            .setTipWord("正在创建")
                                            .create(false)
                                    tipLog?.show()
                                    dialog.dismiss()
                                    mPresenter?.createGroup()
                                }
                                1 -> {
                                    dialog.dismiss()
                                    val b = QMUIDialog.EditTextDialogBuilder(this)
                                    b.setTitle("加入队伍")
                                            .setPlaceholder("队伍ID")
                                            .setInputType(InputType.TYPE_CLASS_TEXT)
                                            .addAction("取消") { d, _ -> d.dismiss() }
                                            .addAction(0, "提交", QMUIDialogAction.ACTION_PROP_NEGATIVE) { d, _ ->
                                                SharePreferencesUtils.saveGroupGid(b.editText.text.toString())
                                                mPresenter?.addGroup()
                                                tipLog = QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                                        .setTipWord("提交中~")
                                                        .create(false)
                                                tipLog?.show()
                                                d.dismiss()
                                            }.show()
                                }
                            }
                        }.show()
            }
        }

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

    override fun notifyList(arrayList: ArrayList<ConsumerBean>) {
        disableRefresh()
    }

    override fun notifyGroup(user: User) {
        disableRefresh()
    }

    private var tipLog: QMUITipDialog? = null
    override fun showDialog(s: String) {
        tipLog?.dismiss()
        SharePreferencesUtils.saveGroupGid(s)
        mTopBar.removeAllRightViews()
        QMUIDialog.MessageDialogBuilder(this)
                .setMessage(s)
                .addAction("复制") { d, _ ->
                    //获取剪贴板管理器
                    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", s)
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData)
                    d.dismiss()
                    toast("复制成功")
                }
                .addAction("确定") { d, _ -> d.dismiss() }
                .show()
    }

    override fun showApply(other: User) {
        tipLog?.dismiss()
        QMUIDialog.MessageDialogBuilder(this)
                .setMessage("${other.nickname}申请加入")
                .addAction("拒绝") { d, _ ->
                    d.dismiss()
                }
                .addAction("确定") { d, _ ->
                    notifyGroup(other)
                    d.dismiss()
                }
                .show()
    }

    override fun error(s: String?) {
        super.error(s)
        tipLog?.dismiss()
    }

    override fun newView(): View = View.inflate(this, R.layout.activity_main, null)

    override fun presenter(): Presenter? {
        mPresenter = MainPresenter(this, this)
        return mPresenter
    }

    override fun onRefresh() {
        mPresenter?.getNewData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Glide.with(this)
                    .load(App.user.head)
                    .error(R.drawable.ic_money_eye)
                    .into(main_navigation.getHeaderView(0).person_head)
            main_navigation.getHeaderView(0).person_nick.text = App.user.nickname
            onRefresh()
        }
    }
}
