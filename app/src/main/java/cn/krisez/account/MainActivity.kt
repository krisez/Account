package cn.krisez.account

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.IBaseView
import cn.krisez.framework.base.Presenter
import com.bumptech.glide.Glide
import com.qmuiteam.qmui.widget.QMUIRadiusImageView

import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(){
    override fun init(bundle: Bundle?) {
        setRefreshEnable(true)
        setUpTopBar()
        initHead()
        tv.setOnClickListener {
            Toast.makeText(this,":asdasd",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initHead() {
        val head = QMUIRadiusImageView(this)
        head.isCircle = true
        mTopBar.addLeftView(head,R.id.main_top_head,mTopBar.generateTopBarImageButtonLayoutParams())
        mTopBar.addRightImageButton(R.drawable.add,R.id.main_add_group)

        Glide.with(this).load("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg").into(head)
    }

    override fun newView(): View = View.inflate(this,R.layout.activity_main,null)

    override fun presenter(): Presenter? = null


    override fun onRefresh() {
    }
}
