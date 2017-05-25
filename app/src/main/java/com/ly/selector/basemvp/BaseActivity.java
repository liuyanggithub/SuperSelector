package com.ly.selector.basemvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity<V extends BaseView,T extends BasePresenter<V>> extends AppCompatActivity {

    public T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewID());
        ButterKnife.bind(this);
        presenter = initPresenter();
        presenter.attach(this,(V)this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    public abstract T initPresenter();

    /**
     * 设置activity的布局文件，用于butterknife的绑定
     * @return
     */
    public abstract int setContentViewID();
}
