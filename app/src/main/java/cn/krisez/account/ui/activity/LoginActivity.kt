package cn.krisez.account.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import cn.krisez.account.R
import cn.krisez.account.bean.User
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.Presenter
import kotlinx.android.synthetic.main.activity_login.*

/**
 *Created by zhouchaoxing on 2019/11/1
 */
class LoginActivity : BaseActivity() {
    override fun init(bundle: Bundle?) {
        setUpTopBar(true)
        showBackIconAndClick()
        setTitle("登录")
        login_btn.setOnClickListener {
            if (check()) {
                BmobUser.loginByAccount(login_user.text.toString(), login_pw.text.toString(), object : LogInListener<User>() {
                    override fun done(u: User?, e: BmobException?) {
                        if (e == null) {
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            error(e.message)
                        }
                    }
                })
            }
        }
        mTopBar.addRightTextButton(R.string.register, R.id.login_register).setOnClickListener {
            startActivityForResult(Intent(this, RegisterActivity::class.java), 100)
        }
    }

    private fun check(): Boolean {
        if (login_user.text.toString().isEmpty()) {
            toast("账号不能为空~")
            return false
        }
        if (login_pw.text.toString().length < 6) {
            toast("密码长度过短~")
            return false
        }
        return true
    }

    override fun newView(): View = View.inflate(this, R.layout.activity_login, null)

    override fun presenter(): Presenter? = null

    override fun onRefresh() {
    }
}