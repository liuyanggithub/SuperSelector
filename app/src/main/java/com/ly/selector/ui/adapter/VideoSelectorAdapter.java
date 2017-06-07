package com.ly.selector.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ly.selector.R;
import com.ly.selector.bean.VideoEntity;
import com.ly.selector.customview.RecyclingImageView;
import com.ly.selector.util.GlideUtil;
import com.ly.selector.util.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <Pre>
 * TODO 描述该文件做什么
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/6/7 15:41
 */

public class VideoSelectorAdapter extends RecyclerView.Adapter<VideoSelectorAdapter.ItemViewHolder> {
    private List<VideoEntity> mList;
    private Context context;

    public VideoSelectorAdapter(List<VideoEntity> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_choose_griditem, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.icon.setVisibility(View.VISIBLE);
        VideoEntity entty = mList.get(position);
        holder.tvDur.setVisibility(View.VISIBLE);

        // FIXME: 2015/12/3 时间转换与大小转换
        holder.tvDur.setText(Utils.formatTime(entty.duration));
        holder.tvSize.setText(entty.size / 1024 / 1024 + "MB");

        holder.imageView.setImageResource(R.mipmap.video_empty_photo);
        GlideUtil.loadImage(context, entty.filePath, holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView)
        RecyclingImageView imageView;
        @Bind(R.id.video_icon)
        ImageView icon;
        @Bind(R.id.chatting_length_iv)
        TextView tvDur;
        @Bind(R.id.chatting_size_iv)
        TextView tvSize;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public void onItemClick(VideoEntity vEntty){

    }
}
