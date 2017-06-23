package com.ly.selector.presenter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

import com.ly.selector.basemvp.BasePresenter;
import com.ly.selector.bean.VideoEntity;
import com.ly.selector.ui.adapter.VideoSelectorAdapter;
import com.ly.selector.ui.view.VideoSelectorView;
import com.ly.selector.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <Pre>
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/6/7 15:13
 */

public class VideoSelectorPresenter extends BasePresenter<VideoSelectorView> {
    private List<VideoEntity> mList = new ArrayList<>();
    private VideoSelectorAdapter mAdapter;

    public void getVideoFile() {
        ContentResolver mContentResolver = getContext().getContentResolver();
        Cursor cursor = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.DEFAULT_SORT_ORDER);

        if (cursor.moveToFirst()) {
            do {

                // ID:MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media._ID));

                // 名称：MediaStore.Audio.Media.TITLE
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                // 路径：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                // 总播放时长：MediaStore.Audio.Media.DURATION
                int duration = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

                // 大小：MediaStore.Audio.Media.SIZE
                int size = (int) cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                VideoEntity entty = new VideoEntity();
                entty.ID = id;
                entty.title = title;
                entty.filePath = url;
                entty.duration = duration;
                entty.size = size;
                mList.add(entty);
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    public void setAdapter() {
        mAdapter = new VideoSelectorAdapter(mList, getContext()) {
            @Override
            public void onItemClick(VideoEntity vEntty) {
                if (isActivityAlive()) {
                    return;
                }
                Activity activity = (Activity) getContext();
                // 限制大小不能超过100M
//        if (vEntty.size > 1024 * 1024 * 100) {
//            Toast.makeText(getActivity(), "暂不支持大于100M的视频！", Toast.LENGTH_SHORT).show();
//            return;
//        }
                Intent intent = activity.getIntent().putExtra("path", vEntty.filePath).putExtra("dur", vEntty.duration);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
                ToastUtils.showShort(vEntty.filePath);
            }
        };
        getMvpView().setAdapter(mAdapter);
    }

    public void notifyListRefresh() {
        mAdapter.notifyDataSetChanged();
    }
}
