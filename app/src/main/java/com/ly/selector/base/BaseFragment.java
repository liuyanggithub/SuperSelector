package com.ly.selector.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * <Pre>
 *     基础MVP架构的Fragment
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/6/7 12:03
 */

public abstract class BaseFragment<V extends BaseView,T extends BasePresenter<V>> extends Fragment {
    public T presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        presenter.attach(getActivity(),(V)this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(setContentViewID(), container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    /**
     * 初始化视图
     */
    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    public abstract T initPresenter();

    /**
     * 设置fragment的布局文件，用于butterknife的绑定
     * @return
     */
    public abstract int setContentViewID();
}
