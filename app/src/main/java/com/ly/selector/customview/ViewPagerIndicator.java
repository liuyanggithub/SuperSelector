package com.ly.selector.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ly.selector.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <Pre>
 *     ViewPager指示器
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/23 15:59
 */

public class ViewPagerIndicator implements ViewPager.OnPageChangeListener{
    private Context context;
    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private int size;
    private int imgSelected = R.mipmap.ic_advert_point_white_pure, imgNoselect = R.mipmap.ic_advert_point_white;
    private int imgSize = 15;
    private List<ImageView> dotViewLists = new ArrayList<>();

    public ViewPagerIndicator(Context context, ViewPager viewPager, LinearLayout dotLayout, int size) {
        this.context = context;
        this.viewPager = viewPager;
        this.dotLayout = dotLayout;
        this.size = size;

        for(int i = 0; i < size; i++){
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.height = imgSize;
            params.width = imgSize;
            if(0 ==i){
                imageView.setBackgroundResource(imgSelected);
            }else {
                imageView.setBackgroundResource(imgNoselect);
            }
            dotLayout.addView(imageView, params);
            dotViewLists.add(imageView);

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i = 0; i < size; i++){
            if((position % size) == i){
                (dotViewLists.get(i)).setBackgroundResource(imgSelected);
            }else {
                (dotViewLists.get(i)).setBackgroundResource(imgNoselect);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
