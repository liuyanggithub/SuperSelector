package com.ly.selector.ui.image;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ly.selector.R;
import com.ly.selector.basemvp.BaseActivity;
import com.ly.selector.customview.ViewPagerIndicator;
import com.ly.selector.presenter.ImageBrowserPresenter;
import com.ly.selector.ui.view.ImageBrowserView;
import com.ly.selector.util.GlideUtil;

import java.util.List;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoView;

/**
 * 图片预览
 */
public class ImageBrowserActivity extends BaseActivity<ImageBrowserView, ImageBrowserPresenter>implements ImageBrowserView {
    /**
     * 从图片选择页面传过来的图片列表
     */
    private List<String> dataList;

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.ll_dot)
    LinearLayout mDotLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = presenter.initData();
//        initData();
        initViews();
        initListeners();
    }


    @Override
    public ImageBrowserPresenter initPresenter() {
        return new ImageBrowserPresenter();
    }

    @Override
    public int setContentViewID() {
        return R.layout.activity_image_browser;
    }

   /* private void initData() {
        Intent intent = getIntent();
        currIndex = intent.getIntExtra("currIndex", 0);
        dataList = (List<String>) intent.getSerializableExtra("dataList");
        if (dataList == null) {
            Toast.makeText(getApplicationContext(), "数据初始化失败", Toast.LENGTH_SHORT);
            finish();
            return;
        }
        count = dataList.size();
    }*/

    private void initViews() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager.setAdapter(new ImagePagerAdapter());
//        viewPager.setCurrentItem(currIndex);
        viewPager.addOnPageChangeListener(new ViewPagerIndicator(this, viewPager, mDotLayout, dataList.size()));
    }

    private void initListeners() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /**
     * 图片适配器
     */
    class ImagePagerAdapter extends PagerAdapter {
        private PhotoView currView;

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
//            if (currIndex == position && currView != null) return;
            if (currView != null) {
                currView.reset();
            }
//            currIndex = position;
            currView = (PhotoView) object;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            PhotoView convertView = new PhotoView(ImageBrowserActivity.this);
            String path = dataList.get(position);
            collection.addView(convertView, 0);
            GlideUtil.loadImage(ImageBrowserActivity.this, path, convertView);
            return convertView;
        }
    }
}
