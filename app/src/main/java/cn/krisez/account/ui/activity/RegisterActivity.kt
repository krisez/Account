package cn.krisez.account.ui.activity

import android.os.Bundle
import android.view.View
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.krisez.account.App
import cn.krisez.account.R
import cn.krisez.account.bean.User
import cn.krisez.account.utils.Logs
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.Presenter
import kotlinx.android.synthetic.main.activity_login.*

/**
 *Created by zhouchaoxing on 2019/11/1
 */
class RegisterActivity : BaseActivity() {
    override fun init(bundle: Bundle?) {
        setUpTopBar(true)
        showBackIconAndClick()
        setTitle("注册")
        login_btn.setText(R.string.register)
        login_btn.setOnClickListener {
            if (check()) {
                val user = User()
                user.username = login_user.text.toString()
                user.setPassword(login_pw.text.toString())
                user.signUp(object : SaveListener<User>() {
                    override fun done(u: User?, e: BmobException?) {
                        if (e == null) {
                            Logs.d(localClassName, "${BmobUser.isLogin()}")
                            u?.let { App.user = it }
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            error(e.message)
                        }
                    }
                })
            }

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

    override fun onRefresh() {}

    override fun newView(): View = View.inflate(this, R.layout.activity_login, null)

    override fun presenter(): Presenter? = null
}