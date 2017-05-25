package com.ly.selector.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.selector.R;
import com.ly.selector.basemvp.BaseActivity;
import com.ly.selector.bean.Bucket;
import com.ly.selector.bean.SelectorParamContext;
import com.ly.selector.presenter.ImageSelectorPresenter;
import com.ly.selector.ui.adapter.BucketListAdapter;
import com.ly.selector.ui.adapter.GridImageAdapter;
import com.ly.selector.ui.view.ImageSelectorView;
import com.ly.selector.util.AnimUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;


public class ImageSelectorActivity extends BaseActivity<ImageSelectorView, ImageSelectorPresenter> implements ImageSelectorView {
    private static final int REQUEST_CROP_PHOTO = 0x00;

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.head_bar_title_view)
    TextView titleView;
    @Bind(R.id.album_view)
    TextView bucketView;
    @Bind(R.id.btn_browser)
    Button browserView;
    @Bind(R.id.btn_ok)
    TextView okView;
    @Bind(R.id.tv_crop)
    TextView cropView;
    /**
     * 展示图片
     */
    @Bind(R.id.rv_image)
    RecyclerView imageGridView;
    /**
     * 弹出式album列表
     */
    @Bind(R.id.layout_arrow)
    View albumRLayout;
    @Bind(R.id.rv_album)
    RecyclerView albumListView;

    @OnClick(R.id.album_view)
    void clickAlbumView() {
        changeAlbum();
    }

    @OnClick(R.id.tv_crop)
    void clickCropView() {
        if (paramContext.getSelectedFile().size() < 2) {
            beginCrop(Uri.fromFile(new File(paramContext.getSelectedFile().get(0))));
        } else {
            Toast.makeText(getApplicationContext(), "选择的图片出错！", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_browser)
    void clickBrowser() {
        Intent intent = new Intent(ImageSelectorActivity.this, ImageBrowserActivity.class);
        intent.putExtra("dataList", paramContext.getSelectedFile());
        startActivity(intent);
    }

    @OnClick(R.id.btn_ok)
    void clickOkView() {
        Intent intent = new Intent();
//		intent.putExtra("filePath", getPathString(paramContext.getSelectedFile()));
        intent.putExtra(SelectorParamContext.TAG_SELECTOR, paramContext);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    //    private Cursor mCursor;
//    protected ImageCursorAdapter imageAdapter;

    /**
     * 剪切后的图片输出目录
     */
    private Uri mCameraCropUri;

    private boolean isExplore = false;


    private SelectorParamContext paramContext;//配置选项

    private final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public ImageSelectorPresenter initPresenter() {
        return new ImageSelectorPresenter();
    }

    @Override
    public int setContentViewID() {
        return R.layout.activity_image_selector;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!permissionCheck()) {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
            } else {
                showNoPermissionTip(getString(noPermissionTip[mNoPermissionIndex]));
                finish();
            }
        }
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        paramContext = presenter.initData();
        initViews();
        initListeners();

//        loadImages(0, 3);
    }

    /**
     * 初始化数据
     */
//    public void initData() {
//        Intent intent = getIntent();
//        if (intent.hasExtra("paramContext")) {
//            paramContext = (SelectorParamContext) intent.getSerializableExtra("params");
//        } else {
//            paramContext = new SelectorParamContext();
//        }
//        presenter.loadImages();
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
//            case Crop.REQUEST_CROP:
////                Toast.makeText(getApplicationContext(), mCameraCropUri.getPath(),Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.putExtra("crop",  mCameraCropUri.getPath());
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        int toastTip = noPermissionTip[i];
                        mNoPermissionIndex = i;
                        if (toastTip != 0) {
                            Toast.makeText(getApplicationContext(), toastTip, Toast.LENGTH_SHORT);
                            finish();
                        }
                    }
                }
                init();
                break;
        }
    }

    /**
     * 初始化views
     */
    private void initViews() {
//		picQuality = (TextView) findViewById(R.id.pic_quality);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageGridView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
//        imageGridView.setEmptyView(findViewById(R.id.result_llyt));
//        Util.initListViewStyle(albumListView);
        albumListView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 绑定点击事件
     */
    private void initListeners() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onLoadImages(final ArrayList<Bucket> bucketList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.bindAllImageGridView(paramContext);
                presenter.bindBucketListView(bucketList, paramContext);
                if (paramContext.isMult()) {
                    findViewById(R.id.pic_browser_bottom).setVisibility(View.VISIBLE);
                    refreshUi();
                } else {
                    findViewById(R.id.pic_browser_bottom).setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    /**
     * 改变是否可以剪切状态
     *
     * @param isCrop 是否可以剪切 true表示剪切可以点击
     */
    public void changeCropViewStatus(boolean isCrop) {
        if (isCrop == true) {
            cropView.setTextColor(getResources().getColor(R.color.green));
            cropView.setClickable(true);
        } else {
            cropView.setTextColor(Color.GRAY);
            cropView.setClickable(false);
        }
    }

    /**
     * 选取单张图片
     *
     * @param path
     */
    public void singleClick(String path) {
//		intent.putExtra("filePath", path);
        paramContext.addItem(path);
        Intent intent = new Intent();
        intent.putExtra(SelectorParamContext.TAG_SELECTOR, paramContext);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void setImageGridAdapter(GridImageAdapter adapter) {
        imageGridView.setAdapter(adapter);
    }

    @Override
    public void setBucketListViewAdapter(BucketListAdapter adapter) {
        albumListView.setAdapter(adapter);
    }

    @Override
    public void setBucketTitleView(String str) {
        titleView.setText(str);
    }


    /**
     * 多张图片返回
     * path0,path1,path2..
     */
//    private void onOKClick() {
//        Intent intent = new Intent();
////		intent.putExtra("filePath", getPathString(paramContext.getSelectedFile()));
//        intent.putExtra(SelectorParamContext.TAG_SELECTOR, paramContext);
//        setResult(RESULT_OK, intent);
//        this.finish();
//    }

    /**
     * 弹出/隐藏相册列表
     */
    protected void changeAlbum() {
        if (albumRLayout.getVisibility() == View.INVISIBLE) {
            popAlbum();
        } else {
            hideAlbum();
        }
    }

    /**
     * 选择图片质量
     */
//    private void choosePicQuality() {
//        final ActionSheet actionSheet = new ActionSheet(ImageSelectorActivty.this);
//        actionSheet.show(paramContext.menuItems, new ActionSheet.Action1<Integer>() {
//            @Override
//            public void invoke(Integer index) {
//                actionSheet.hide();
//                paramContext.setHighQulity(index == 1);
////				picQuality.setText(paramContext.getQuality());
//            }
//        });
//    }
    public void refreshUi() {
        okView.setText("完成(" + paramContext.getPercent() + ")");
        if (paramContext.getSelectedFile().size() > 0) {
            browserView.setTextColor(paramContext.mcolor);
            browserView.setEnabled(true);
            if (paramContext.getSelectedFile().size() == 1) {
                changeCropViewStatus(true);
            } else {
                changeCropViewStatus(false);
            }
//			picQuality.setText(paramContext.getQuality());
        } else {
            browserView.setTextColor(paramContext.gcolor);
            browserView.setEnabled(false);
            changeCropViewStatus(false);
        }
//		picQuality.setVisibility(paramContext.hasQulityMenu() ?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    /**
     * 弹出相册列表
     **/
    public void popAlbum() {
        albumRLayout.setVisibility(View.VISIBLE);
        new AnimUtil(ImageSelectorActivity.this, R.anim.img_selector_translate_up_current).setLinearInterpolator().startAnimation(
                albumRLayout);
    }

    /**
     * 隐藏相册列表
     **/
    public void hideAlbum() {
        new AnimUtil(ImageSelectorActivity.this, R.anim.img_selector_translate_down).setLinearInterpolator().startAnimation(
                albumRLayout);
        albumRLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

//    /**
//     * 第一张可见图片的下标
//     */
//    private int mFirstVisibleItem;
//
//    /**
//     * 一屏有多少张图片可见
//     */
//    private int mVisibleItemCount;
//
//    /**
//     * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
//     */
//    private boolean isFirstEnter = true;
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        // 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
//        if (scrollState == SCROLL_STATE_IDLE) {
//            loadImages(mFirstVisibleItem, mVisibleItemCount);
//        } else {
//            ImageLoadUtil.getInstance().cancelAllTasks();
//        }
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
//                         int totalItemCount) {
//        mFirstVisibleItem = firstVisibleItem;
//        mVisibleItemCount = visibleItemCount;
//        // 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
//        // 因此在这里为首次进入程序开启下载任务。
//        if (isFirstEnter && visibleItemCount > 0) {
//            loadImages(firstVisibleItem, visibleItemCount);
//            isFirstEnter = false;
//        }
//    }
//
//    public void loadImages(int start, int count) {
//        for (int i = start + count - 1; i >= start; i--) {
//            String path = presenter.imageAdapter.getViewHolderMap().get(i).getPath();
//            if (path != null && !path.trim().equals("")) {
//                View view = imageGridView.findViewWithTag(CheckedImageView.TAG + path);
//                if (view != null) {
//                    ((CheckedImageView) view).loadImage();
//                }
//            }
//        }
//    }

    /**
     * 开始剪切
     *
     * @param source
     */
    private void beginCrop(Uri source) {
//        mCameraCropUri = Uri.fromFile(CommonFileUtils.generateExternalImageCacheFileName(this, "img_crop_", ".jpg"));
//        Crop.of(source, mCameraCropUri).asSquare().start(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private int mNoPermissionIndex = 0;
    private final String[] permissionManifest = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,

    };

    private final int[] noPermissionTip = {
            R.string.no_write_external_storage_permission,
            R.string.no_read_external_storage_permission
    };

    /**
     * 权限检查（适配6.0以上手机）
     */
    private boolean permissionCheck() {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        String permission;
        for (int i = 0; i < permissionManifest.length; i++) {
            permission = permissionManifest[i];
            mNoPermissionIndex = i;
            if (PermissionChecker.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionCheck = PackageManager.PERMISSION_DENIED;
            }
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 没有权限的提醒
     *
     * @param tip
     */
    private void showNoPermissionTip(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
    }
}
