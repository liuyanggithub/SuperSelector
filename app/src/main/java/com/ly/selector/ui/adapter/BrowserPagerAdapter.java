package com.ly.selector.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ly.selector.customview.PageImageView;
import com.ly.selector.util.GlideUtil;

import java.util.List;

/**
 * <Pre>
 * TODO 描述该文件做什么
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/24 14:36
 */

public class BrowserPagerAdapter extends PagerAdapter{
    private PageImageView currView;
    private List<String> dataList;
    private Context context;


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
//        if (currIndex == position && currView != null) return;
        if (currView != null) {
            currView.getImageView().reset();
        }
//        currIndex = position;
        currView = (PageImageView) object;
        if (!currView.isLoadSuccess()) {
            Toast.makeText(context, "图片加载失败,请确认图片或网络可用!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        PageImageView convertView = new PageImageView(context);
        String path = dataList.get(position);
        collection.addView(convertView, 0);
//            ImageLoadUtil.getInstance(3, ImageLoadUtil.Type.LIFO).loadImage(path, convertView.getImageView(), bindListener(convertView));
        GlideUtil.loadImage(context, path, convertView.getImageView());
        return convertView;
    }
}
