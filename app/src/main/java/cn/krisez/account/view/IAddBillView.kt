package cn.krisez.account.view

import cn.krisez.framework.base.IBaseView

/**
 *Created by zhouchaoxing on 2019/12/2
 */
interface IAddBillView :IBaseView {
    fun success()
    fun failed(error:String?)
}