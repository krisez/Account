package cn.krisez.framework.base;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import cn.krisez.framework.R;


public abstract class BaseActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, IBaseView {

    public SwipeRefreshLayout mSwipeRefreshLayout;
    private Presenter mPresenter;
    private QMUITopBarLayout mTopBarLayout;
    public QMUITopBar mTopBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mSwipeRefreshLayout = findViewById(R.id.base_layout);
        View view = newView();
        if (view != null) {
            mSwipeRefreshLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.black, R.color.blue);

        mPresenter = presenter();
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
        init(savedInstanceState);
    }

    protected void showBackIconAndClick() {
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }

    protected void setTitle(String title) {
        mTopBar.setTitle(title);
    }

    protected void setRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    protected void setUpTopBar(boolean light) {
        mTopBarLayout = findViewById(R.id.topbar_layout);
        mTopBarLayout.setVisibility(View.VISIBLE);
        mTopBar = findViewById(R.id.topbar);
        mTopBar.setTitle("");
        setTranslucent(light);
    }

    protected void hideToolbar() {
        if (mTopBarLayout == null) {
            return;
        }
        mTopBarLayout.setVisibility(View.VISIBLE);
    }

    protected void setTranslucent(boolean light) {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        if (light) {
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        } else {
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        }
    }

    protected void setTranslucent(View view, boolean light) {
        setTranslucent(light);
        TypedArray array = obtainStyledAttributes(null, R.styleable.QMUITopBar, R.attr.QMUITopBarStyle, 0);
        int color = array.getColor(R.styleable.QMUITopBar_qmui_topbar_bg_color, getResources().getColor(R.color.qmui_config_color_transparent));
        array.recycle();
        view.setBackgroundColor(color);
    }

    protected abstract View newView();

    protected abstract Presenter presenter();

    protected abstract void init(Bundle bundle);

    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(String s) {
        toast(s);
        disableRefresh();
    }

    @Override
    public void handle(HandleType type, Bundle bundle) {
        switch (type) {
            case INTENT:
                startActivity(new Intent(this, (Class<?>) bundle.getSerializable("class")));
                finish();
                break;
            case REFRESH:
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case NOTIFICATION:
                break;
            case OTHER:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroy();
    }

    public void disableRefresh() {
        handle(HandleType.REFRESH, null);
    }
}
