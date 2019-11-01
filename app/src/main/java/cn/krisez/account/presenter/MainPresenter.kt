package cn.krisez.account.presenter

import android.content.Context
import android.util.Log
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SQLQueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.krisez.account.App
import cn.krisez.account.bean.ConsumerBean
import cn.krisez.account.bean.Group
import cn.krisez.account.bean.User
import cn.krisez.account.utils.Logs
import cn.krisez.account.utils.SharePreferencesUtils
import cn.krisez.account.view.IMainView
import cn.krisez.framework.base.Presenter

/**
 *Created by zhouchaoxing on 2019/10/15
 */
class MainPresenter(view: IMainView, context: Context) : Presenter(view, context) {
    private val mView: IMainView = view
    private val mContext: Context = context

    override fun onCreate() {

    }

    fun getNewData() {
        //查询是否有人加组，查询最新账单数据，查询是否加组成功
        if (!BmobUser.isLogin()) {
            mView.error("请先登录")
            return
        }
        if (!SharePreferencesUtils.getGid().isNullOrEmpty()) {
            val query = BmobQuery<Group>()
            query.addWhereEqualTo("g_id", SharePreferencesUtils.getGid())
            query.include("g_a,g_b")
            query.findObjects(object : FindListener<Group>() {
                override fun done(list: MutableList<Group>?, e: BmobException?) {
                    if (e == null) {
                        if (!list.isNullOrEmpty()) {
                            val group = list[0]
                            Logs.d("asdads", "${group.g_a}")
                            SharePreferencesUtils.saveGroupGid(group.g_id)
                            if (group.agree == 1 && group.g_a.objectId == App.user.objectId) {
                                mView.showApply(group.g_b)
                            } else if (group.agree == 2) {
                                if (group.g_a == App.user) {
                                    mView.notifyGroup(group.g_b)
                                } else {
                                    mView.notifyGroup(group.g_a)
                                }
                            }
                        }
                    } else {
                        e.message?.let { mView.error(it) }
                    }
                }
            })
        }
        queryBill()
    }

    fun createGroup() {
        val group = Group()
        group.save(object : SaveListener<String>() {
            override fun done(s: String, e: BmobException?) {
                Logs.d(javaClass.simpleName, s)
                Logs.e(javaClass.simpleName, e.toString())
                if (e == null) {
                    mView.showDialog(group.g_id)
                } else {
                    e.message?.let { mView.error(it) }
                }
            }
        })
    }

    private fun queryBill() {
        val query = BmobQuery<ConsumerBean>()
        query.addWhereEqualTo("consumer", App.user)
    }

    fun addGroup() {
        val query = BmobQuery<Group>()
        query.addWhereEqualTo("g_id", SharePreferencesUtils.getGid())
        query.addWhereEqualTo("agree", 0)
        query.setLimit(1)
        query.include("a,b")
        query.findObjects(object : FindListener<Group>() {
            override fun done(list: MutableList<Group>?, e: BmobException?) {
                if (e == null) {
                    if (!list.isNullOrEmpty()) {
                        val group = list[0]
                        if (group.g_a.objectId == App.user.objectId) {
                            mView.error("不能自己跟自己组队~")
                            return
                        }
                        group.g_b = App.user
                        group.agree = 1
                        group.update(object : UpdateListener() {
                            override fun done(e: BmobException?) {
                                if (e == null) {
                                    mView.error("提交成功~")
                                } else {
                                    mView.error(e.message)
                                }
                            }
                        })
                    } else {
                        mView.error("没有相关内容~")
                    }
                } else {
                    mView.error(e.message)
                }
            }
        })
    }
}