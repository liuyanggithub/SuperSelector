package com.ly.selector.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <Pre>
 *     通用ViewHolder
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/6/26 15:25
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public ViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public static ViewHolder get(Context context, ViewGroup parent, int layoutId){
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, itemView);
        return viewHolder;
    }

    public <T extends View> T getView(int ViewId){
        View view = mViews.get(ViewId);
        if(view == null){
            view = mConvertView.findViewById(ViewId);
            mViews.put(ViewId, view);
        }
        return (T)view;
    }
    public View getConvertView(){
        return mConvertView;
    }
}
