package com.ly.selector.ui.view;

import com.ly.selector.basemvp.BaseView;
import com.ly.selector.bean.Bucket;
import com.ly.selector.ui.adapter.BucketListAdapter;
import com.ly.selector.ui.adapter.GridImageAdapter;

import java.util.ArrayList;

/**
 * <Pre>
 * TODO 描述该文件做什么
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/17 16:20
 */

public interface ImageSelectorView extends BaseView {
    void showMessage(String msg);
    void popAlbum();
    void hideAlbum();
    void changeCropViewStatus(boolean isCrop);
    void refreshUi();
    void onLoadImages(ArrayList<Bucket> bucketList);
    void singleClick(String path);
    void setImageGridAdapter(GridImageAdapter adapter);
    void setBucketListViewAdapter(BucketListAdapter adapter);
    void setBucketTitleView(String str);
}
