package com.ly.selector.presenter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

import com.ly.selector.basemvp.BasePresenter;
import com.ly.selector.bean.Bucket;
import com.ly.selector.bean.SelectorParamContext;
import com.ly.selector.ui.adapter.BucketListAdapter;
import com.ly.selector.ui.adapter.ImageSelectorAdapter;
import com.ly.selector.ui.view.ImageSelectorView;
import com.ly.selector.util.MediaHelper;

import java.util.ArrayList;

/**
 * <Pre>
 * 选择图片界面代理
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/18 15:13
 */

public class ImageSelectorPresenter extends BasePresenter<ImageSelectorView> {
    //图片瀑布流适配器
    private ImageSelectorAdapter imageAdapter;
    //游标
    private Cursor mCursor;
    /**
     * 初始化数据
     */
    public SelectorParamContext initData() {
        if(!isActivityAlive()){
            return null;
        }
        Intent intent = ((Activity)getContext()).getIntent();
        SelectorParamContext paramContext;
        if (intent.hasExtra("paramContext")) {
            paramContext = (SelectorParamContext) intent.getSerializableExtra("params");
        } else {
            paramContext = new SelectorParamContext();
        }
        loadImages();
        return paramContext;
    }

    /**
     * 取本地图片
     */
    public void loadImages() {
        new Thread() {
            @Override
            public void run() {
                mCursor = MediaHelper.getImagesCursor(getContext());
                final ArrayList<Bucket> bucketList = MediaHelper.getBucketList(getContext());
                getMvpView().onLoadImages(bucketList);
            }
        }.start();
    }

    /**
     * 所有图片的相册关联里面的图片
     * @param paramContext 配置
     */

    public void bindAllImageGridView(SelectorParamContext paramContext){
        bindImageGridView(mCursor, paramContext);
    }

    /**
     * 关联相册和相册下的图片
     * @param cursor 游标
     * @param paramContext 配置
     */
    public void bindImageGridView(Cursor cursor, final SelectorParamContext paramContext) {
        if (imageAdapter != null) {
            imageAdapter.changeCursor(cursor);
        } else {
            imageAdapter = new ImageSelectorAdapter(getContext(), cursor, paramContext.getSelectedFile()) {

                @Override
                public void onItemClick(ItemViewHolder holder, String path) {
                    if (!paramContext.isMulti()) {
                        getMvpView().singleClick(path);
                    } else if (paramContext.isChecked(path)) { //取消选中
                        holder.setChecked(false);
                        paramContext.removeItem(path);
                        getMvpView().refreshUi();
                    } else if (paramContext.isAvaliable()) { //继续可选
                        holder.setChecked(true);
                        paramContext.addItem(path);
                        getMvpView().refreshUi();
                    }
                }
            };
            getMvpView().setImageGridAdapter(imageAdapter);
        }
    }

    /**
     * 初始化相册列表
     * @param bucketList 相册列表数组
     * @param paramContext 配置
     */
    public void initBucketListView(ArrayList<Bucket> bucketList, final SelectorParamContext paramContext) {
        if (bucketList == null) return;
        BucketListAdapter adapter = new BucketListAdapter(getContext(), bucketList){
            @Override
            public void onItemClick(Bucket bucket) {
                getMvpView().setBucketTitleView(bucket.bucketName);
                bindImageGridView(MediaHelper.getImagesCursor(getContext(), bucket.bucketId + ""), paramContext);
                getMvpView().hideAlbum();
            }
        };
        getMvpView().setBucketListViewAdapter(adapter);
    }

    public String getPathString(ArrayList<String> list) {
        if (list.size() <= 0)
            return "";
        String text = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            text += "," + list.get(i);
        }
        return text;
    }

    public void onDestroy(){
        if (mCursor != null) {
            mCursor.close();
        }
    }
}
