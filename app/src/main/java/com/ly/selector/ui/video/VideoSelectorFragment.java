package com.ly.selector.ui.video;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ly.selector.R;
import com.ly.selector.base.BaseAdapter;
import com.ly.selector.base.BaseFragment;
import com.ly.selector.presenter.VideoSelectorPresenter;
import com.ly.selector.ui.view.VideoSelectorView;

import butterknife.Bind;

public class VideoSelectorFragment extends BaseFragment<VideoSelectorView, VideoSelectorPresenter> implements VideoSelectorView {
    @Bind(R.id.gridView)
    RecyclerView mGridView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    /**
     * Empty constructor as per the Fragment documentation
     */
    public VideoSelectorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.getVideoFile();
    }

    @Override
    protected void initView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VideoSelectorActivity) getActivity()).back();
            }
        });
        mGridView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        presenter.setAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.notifyListRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public VideoSelectorPresenter initPresenter() {
        return new VideoSelectorPresenter();
    }

    @Override
    public int setContentViewID() {
        return R.layout.fragment_video_selector;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                Uri uri = data.getParcelableExtra("uri");
                String[] projects = new String[]{MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DURATION};
                Cursor cursor = getActivity().getContentResolver().query(
                        uri, projects, null,
                        null, null);
                int duration = 0;
                String filePath = null;

                if (cursor.moveToFirst()) {
                    // 路径：MediaStore.Audio.Media.DATA
                    filePath = cursor.getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    // 总播放时长：MediaStore.Audio.Media.DURATION
                    duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    System.out.println("duration:" + duration);
                }
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }

                getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent().putExtra("path", filePath).putExtra("dur", duration));
                getActivity().finish();

            }
        }

    }
    @Override
    public void setAdapter(BaseAdapter adapter) {
        mGridView.setAdapter(adapter);
    }
}
