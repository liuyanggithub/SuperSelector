package com.ly.selector.presenter;

import android.app.Activity;
import android.content.Intent;

import com.ly.selector.basemvp.BasePresenter;
import com.ly.selector.ui.view.ImageBrowserView;

import java.util.ArrayList;
import java.util.List;

/**
 * <Pre>
 * 图片预览界面代理
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/23 15:41
 */

public class ImageBrowserPresenter extends BasePresenter<ImageBrowserView> {
    /**
     * 初始化数据
     * @return 从图片选择界面传来的图片数组
     */
    public List<String> initData() {
        if(!isActivityAlive()){
            return null;
        }
        Intent intent = ((Activity)getContext()).getIntent();
        List<String> dataList = new ArrayList<>();
        dataList = (List<String>) intent.getSerializableExtra("dataList");
        if (dataList == null) {
            dataList = new ArrayList<String>();
        }
        return dataList;
    }
}
