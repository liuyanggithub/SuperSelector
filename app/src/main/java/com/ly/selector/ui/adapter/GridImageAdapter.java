package com.ly.selector.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ly.selector.R;
import com.ly.selector.util.GlideUtil;
import com.ly.selector.util.MediaHelper;
import com.ly.selector.util.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * <Pre>
 * 图片瀑布流适配器
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/22 10:47
 */

public class GridImageAdapter extends RecyclerViewCursorAdapter<GridImageAdapter.ItemViewHolder> {

    private Context context;
    private List<String> selectedList;

    public GridImageAdapter(Context context, Cursor c, List<String> selectedList) {
        super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
        this.context = context;
        this.selectedList = selectedList;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checked_image_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    protected void onContentChanged() {

    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, Cursor cursor) {
        final String path = cursor.getString(MediaHelper.IMAGE_PATH);
        holder.setView(path, selectedList.contains(path));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(holder,path);
            }
        });
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        final int checkedColor = Color.parseColor("#cc0081e4");
        @Bind(R.id.image_view) ImageView imageView; //图片
        @Bind(R.id.choosed_view) View checkedView; //选中框
        private boolean isChecked;
        private String path;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setView(String path, boolean isChecked){
            if (!Util.isNullOrWhiteSpace(path)) {
                this.path = path;
                GlideUtil.loadImage(context, path, imageView);
            }
            setChecked(isChecked);
        }

        public void setChecked(boolean isChecked){
            this.isChecked = isChecked;
            if(isChecked){
                imageView.setColorFilter(checkedColor);
                checkedView.setVisibility(VISIBLE);
            }else{
                imageView.setColorFilter(Color.TRANSPARENT);
                checkedView.setVisibility(INVISIBLE);
            }
        }
        public String getPath(){
            return path;
        }
    }
    public void onItemClick(ItemViewHolder holder, String path){

    }

}
