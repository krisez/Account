package cn.krisez.account.presenter

import android.content.Context
import android.text.TextUtils
import android.view.View
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.krisez.account.bean.ConsumerBean
import cn.krisez.account.view.IAddBillView
import cn.krisez.framework.base.Presenter

/**
 *Created by zhouchaoxing on 2019/12/2
 */
class AddBillPresenter(context: Context, view: IAddBillView) : Presenter(view, context) {
    private var v: IAddBillView? = null

    init {
        v = view
    }

    override fun onCreate() {
    }

    fun submit(bean: ConsumerBean) {
        if (!TextUtils.isEmpty(bean.objectId)) {
            bean.update(object : UpdateListener() {
                override fun done(e: BmobException?) {
                    v?.let {
                        if (e == null) {
                            it.success()
                        } else {
                            it.failed(e.message)
                        }
                    }
                }
            })
        } else {
            bean.save(object : SaveListener<String>() {
                override fun done(s: String?, e: BmobException?) {
                    v?.let {
                        if (e == null) {
                            it.success()
                        } else {
                            it.failed(e.message)
                        }
                    }
                }
            })
        }
    }
}