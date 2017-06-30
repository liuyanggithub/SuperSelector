package com.ly.selector.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.ly.selector.R;
import com.ly.selector.base.BaseAdapter;
import com.ly.selector.base.ViewHolder;
import com.ly.selector.bean.Bucket;
import com.ly.selector.util.GlideUtil;

import java.util.List;

/**
 * <Pre>
 * 相册列表适配器
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/22 16:51
 */

public class BucketListAdapter extends BaseAdapter<Bucket> {
    public BucketListAdapter(Context context, int layoutId, List<Bucket> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Bucket bucket) {
        TextView tv_name = holder.getView(R.id.tv_name_la);
        ImageView iv_album = holder.getView(R.id.iv_album_la);
        TextView tv_content = holder.getView(R.id.tv_count_la);
        tv_name.setText(bucket.bucketName);
        tv_content.setText(""+bucket.count);
        GlideUtil.loadImage(getContext(), bucket.bucketUrl, iv_album);
    }
}
