package com.ly.selector.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * <Pre>
 *     通用Adapter
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/6/26 14:50
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder>{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected OnItemClickListener mOnItemClickListener;

    public BaseAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, viewGroup, mLayoutId);
        setListener(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        convert(viewHolder, mDatas.get(position));
    }


    protected void setListener(final ViewHolder viewHolder) {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                mOnItemClickListener.onItemClick(view, viewHolder, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public interface OnItemClickListener{
        void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int position);
    }
    public Context getContext() {
        return mContext;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public abstract void convert(ViewHolder holder, T t);
}
