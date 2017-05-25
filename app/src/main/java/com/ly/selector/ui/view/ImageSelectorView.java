package com.ly.selector.ui.view;

import com.ly.selector.basemvp.BaseView;
import com.ly.selector.bean.Bucket;
import com.ly.selector.ui.adapter.BucketListAdapter;
import com.ly.selector.ui.adapter.GridImageAdapter;

import java.util.ArrayList;

/**
 * <Pre>
 *     图片选择界面操作接口
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/17 16:20
 */

public interface ImageSelectorView extends BaseView {
    void showMessage(String msg);

    /**
     * 弹出相册列表
     */
    void popAlbum();

    /**
     * 隐藏相册列表
     */
    void hideAlbum();

    /**
     * 改变剪切按钮的状态
     * @param isCrop
     */
    void changeCropViewStatus(boolean isCrop);

    /**
     * 刷新图片列表
     */
    void refreshUi();

    /**
     * 加载图片
     * @param bucketList
     */
    void onLoadImages(ArrayList<Bucket> bucketList);

    /**
     * 只选择一张图片
     * @param path
     */
    void singleClick(String path);

    /**
     * 设置图片瀑布流适配器
     * @param adapter
     */
    void setImageGridAdapter(GridImageAdapter adapter);

    /**
     * 设置相册列表适配器
     * @param adapter
     */
    void setBucketListViewAdapter(BucketListAdapter adapter);

    /**
     * 设置相册列表标题
     * @param str
     */
    void setBucketTitleView(String str);
}
