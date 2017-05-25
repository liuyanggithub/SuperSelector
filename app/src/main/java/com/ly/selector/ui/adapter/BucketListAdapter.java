package com.ly.selector.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ly.selector.R;
import com.ly.selector.bean.Bucket;
import com.ly.selector.util.GlideUtil;

import java.util.ArrayList;

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
 *          Create by 2017/5/22 16:51
 */

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ItemViewHolder> {
    private ArrayList<Bucket> bucketList;
    private Context context;

    public BucketListAdapter( Context context, ArrayList<Bucket> bucketList) {
        this.bucketList = bucketList;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Bucket bucket = bucketList.get(position);
        GlideUtil.loadImage(context, bucket.bucketUrl, holder.imageView);
        holder.nameTextView.setText(bucket.bucketName);
        holder.countTextView.setText(""+bucket.count);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(bucket);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bucketList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_album_la) ImageView imageView;
        @Bind(R.id.tv_name_la) TextView nameTextView;
        @Bind(R.id.tv_count_la) TextView countTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void onItemClick(Bucket bucket){}
}
