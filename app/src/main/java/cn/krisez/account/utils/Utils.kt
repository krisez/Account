package cn.krisez.account.utils

import android.util.SparseArray

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
}