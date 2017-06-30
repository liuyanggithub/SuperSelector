package com.ly.selector.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ly.selector.R;
import com.ly.selector.base.BaseAdapter;
import com.ly.selector.base.ViewHolder;
import com.ly.selector.bean.VideoEntity;
import com.ly.selector.customview.RecyclingImageView;
import com.ly.selector.util.GlideUtil;
import com.ly.selector.util.Utils;

import java.util.List;

/**
 * <Pre>
 *     视频选择列表适配器
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/6/7 15:41
 */

public class VideoSelectorAdapter extends BaseAdapter<VideoEntity> {
    public VideoSelectorAdapter(Context context, int layoutId, List<VideoEntity> datas) {
        super(context, layoutId, datas);
    }
    @Override
    public void convert(ViewHolder holder, VideoEntity videoEntity) {
        RecyclingImageView iv_video = holder.getView(R.id.imageView);
        TextView tv_time = holder.getView(R.id.chatting_length_iv);
        TextView tv_size = holder.getView(R.id.chatting_size_iv);
        tv_time.setText(Utils.formatTime(videoEntity.duration));
        tv_size.setText(videoEntity.size / 1024 / 1024 + "MB");
        GlideUtil.loadImage(getContext(), videoEntity.filePath, iv_video);
    }
}
