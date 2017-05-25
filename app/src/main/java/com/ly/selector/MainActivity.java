package com.ly.selector;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.ly.selector.basemvp.BaseActivity;
import com.ly.selector.presenter.MainPresenter;
import com.ly.selector.ui.ImageSelectorActivity;
import com.ly.selector.ui.view.MainView;

import butterknife.Bind;
import butterknife.OnClick;

import static com.ly.selector.R.layout.activity_main;

/**
 * <Pre>
 * TODO 描述该文件做什么
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/17 11:13
 */

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView{
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.btn_image)
    void clickBtnImage(){
        Intent intent = new Intent(this, ImageSelectorActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

    }

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @Override
    public int setContentViewID() {
        return activity_main;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
