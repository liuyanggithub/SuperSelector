package com.ly.selector.presenter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

import com.ly.selector.basemvp.BasePresenter;
import com.ly.selector.bean.Bucket;
import com.ly.selector.bean.SelectorParamContext;
import com.ly.selector.ui.adapter.BucketListAdapter;
import com.ly.selector.ui.adapter.GridImageAdapter;
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
    private GridImageAdapter imageAdapter;
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

    public void bindAllImageGridView(SelectorParamContext paramContext){
        bindImageGridView(mCursor, paramContext);
    }

    public void bindImageGridView(Cursor cursor, final SelectorParamContext paramContext) {
        if (imageAdapter != null) {
            imageAdapter.changeCursor(cursor);
        } else {
            imageAdapter = new GridImageAdapter(getContext(), cursor, paramContext.getSelectedFile()) {

                @Override
                public void onItemClick(ItemViewHolder holder, String path) {
                    if (!paramContext.isMult()) {
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
    public void bindBucketListView(ArrayList<Bucket> bucketList, final SelectorParamContext paramContext) {
        if (bucketList == null) return;
//        BaseAdapter adapter = new BaseAdapter<Bucket>(getContext(), bucketList) {
//            @Override
//            public View getView(final int position, View convertView, ViewGroup parent) {
//                if (convertView == null) {
//                    convertView = new BucketView(context);
//                }
//                BucketView item = (BucketView) convertView;
//                final Bucket bucket = (Bucket) getItem(position);
//                item.setView(bucket);
//
//                item.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        getMvpView().setBucketTitleView(bucket.bucketName);
//                        bindImageGridView(MediaHelper.getImagesCursor(context, bucket.bucketId + ""));
//                        getMvpView().hideAlbum();
//                    }
//                });
//                return item;
//            }
//        };
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

    private String getPathString(ArrayList<String> list) {
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
