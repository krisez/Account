package cn.krisez.account.adapter

import cn.krisez.account.R
import cn.krisez.account.bean.ConsumerBean
import cn.krisez.account.utils.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 *Created by zhouchaoxing on 2019/12/25
 */
class BillAdapter(data: MutableList<ConsumerBean>?) : BaseQuickAdapter<ConsumerBean, BaseViewHolder>(R.layout.item_bill,data) {

    override fun convert(helper: BaseViewHolder, item: ConsumerBean?) {
        item?.let {
            helper.setText(R.id.item_bill_money,it.money)
            helper.setText(R.id.item_bill_describe,it.describe)
            helper.setText(R.id.item_bill_type,Utils.getTypeName(it.type))
            helper.setImageResource(R.id.item_bill_icon,Utils.getTypeIcon(it.type))
        }
    }

}