package cn.krisez.account.ui.activity

import android.os.Bundle
import android.view.View
import cn.krisez.account.App
import cn.krisez.account.R
import cn.krisez.account.bean.ConsumerBean
import cn.krisez.account.presenter.AddBillPresenter
import cn.krisez.account.view.IAddBillView
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.Presenter
import kotlinx.android.synthetic.main.activity_add_bill.*
import java.text.SimpleDateFormat
import java.util.*

/**
 *Created by zhouchaoxing on 2019/11/29
 */
class AddBillActivity :BaseActivity(),IAddBillView {
    private var presenter:AddBillPresenter? = null
    private var mObjectId:String? = null
    override fun init(bundle: Bundle?) {
        setUpTopBar(true)
        showBackIconAndClick()
        mObjectId = intent.getStringExtra("id")
        mTopBar.addRightTextButton("完成",R.id.add_bill_submit).setOnClickListener {
            val bean = ConsumerBean()
            bean.money = add_bill_cash.text.toString()
            bean.describe = add_bill_remark.text.toString()
            bean.time = if(add_bill_time.text.toString().isEmpty()){
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            }else{
                add_bill_time.text.toString()
            }
            bean.money = add_bill_cash.text.toString()
            bean.consumer = App.user
            presenter?.submit(bean)
        }
    }


    override fun newView(): View = View.inflate(this, R.layout.activity_add_bill,null)

    override fun presenter(): Presenter? {
        presenter = AddBillPresenter(this,this)
        return presenter
    }

    override fun success() {
        finish()
    }

    override fun failed(error: String?) {

    }

    override fun onRefresh() {
    }

}