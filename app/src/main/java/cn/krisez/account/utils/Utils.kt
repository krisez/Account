package cn.krisez.account.utils

import android.content.Context
import android.util.SparseArray
import android.widget.Toast
import cn.krisez.account.R

/**
 *Created by zhouchaoxing on 2019/11/1
 */
object Utils {
    private var array = SparseArray<String>()

    init {
        array.put(0, "消费")
        array.put(1, "餐饮")
        array.put(2, "购物")
        array.put(3, "住房")
        array.put(4, "交通")
        array.put(5, "通讯")
        array.put(6, "娱乐")
        array.put(7, "医疗")
        array.put(8, "教育")
        array.put(9, "红包")
        array.put(10, "旅行")
        array.put(11, "投资")
        array.put(12, "其他")
        //----------------//
        array.put(13, "薪资")
        array.put(14, "红包")
        array.put(15, "理财")
        array.put(16, "其他")
    }

    fun getTypeName(type: Int): String = array.get(type)

    fun getTypeIcon(type:Int):Int{
        when(type){
            0 -> return R.drawable.ic_bill
            1 -> return R.drawable.ic_canyin
            2 -> return R.drawable.ic_gouwuche
            3 -> return R.drawable.ic_house
            4 -> return R.drawable.ic_bus
            5 -> return R.drawable.ic_tongxun
            6 -> return R.drawable.ic_yule
            7 -> return R.drawable.ic_hongbao
            8 -> return R.drawable.ic_jiaoyu
            9 -> return R.drawable.ic_lvxing
            10 -> return R.drawable.ic_jiaoyu
            11 -> return R.drawable.ic_touzi
            12 -> return R.drawable.ic_gengduo
            13 -> return R.drawable.ic_gongzi
            14 -> return R.drawable.ic_hongbao
            15 -> return R.drawable.ic_licai
            16 -> return R.drawable.ic_gengduo
        }
        return 0
    }

    fun showToast(context: Context,msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}