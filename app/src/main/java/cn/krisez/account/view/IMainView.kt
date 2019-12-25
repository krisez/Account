package cn.krisez.account.view

import cn.krisez.account.bean.ConsumerBean
import cn.krisez.account.bean.User
import cn.krisez.framework.base.IBaseView

/**
 *Created by zhouchaoxing on 2019/10/15
 */
interface IMainView : IBaseView {
    fun notifyList(list: MutableList<ConsumerBean>)
    fun notifyGroup(user: User)
    fun showDialog(s: String)
    fun showApply(other:User)
}