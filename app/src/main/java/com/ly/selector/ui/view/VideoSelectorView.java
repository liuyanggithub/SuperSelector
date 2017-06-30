package com.ly.selector.ui.view;

import com.ly.selector.base.BaseAdapter;
import com.ly.selector.base.BaseView;

/**
 * <Pre>
 *     视频选择界面视图操作
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/6/7 15:14
 */

public interface VideoSelectorView extends BaseView {
    void setAdapter(BaseAdapter adapter);
}
