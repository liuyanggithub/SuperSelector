package com.ly.selector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.ly.selector.basemvp.BaseActivity;
import com.ly.selector.presenter.MainPresenter;
import com.ly.selector.ui.image.ImageSelectorActivity;
import com.ly.selector.ui.video.VideoSelectorActivity;
import com.ly.selector.ui.view.MainView;
import com.ly.selector.util.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <Pre>
 * TODO 描述该文件做什么
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/17 11:13
 */

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView{
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private final int PERMISSION_REQUEST_CODE = 1;

    @OnClick(R.id.btn_image)
    void clickBtnImage(){
        Intent intent = new Intent(this, ImageSelectorActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_video)
    void clickBtnVideo(){
        Intent intent = new Intent(this, VideoSelectorActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (!permissionCheck()) {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
            } else {
                showNoPermissionTip(getString(noPermissionTip[mNoPermissionIndex]));
                finish();
            }
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
//                            Toast.makeText(getApplicationContext(), toastTip, Toast.LENGTH_SHORT);
                            showNoPermissionTip(String.valueOf(toastTip));
                            finish();
                        }
                    }
                }
                break;
        }
    }
    /**
     * 没有权限的提醒
     *
     * @param tip
     */
    private void showNoPermissionTip(String tip) {
        ToastUtils.showShort(tip);
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

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @Override
    public int setContentViewID() {
        return R.layout.activity_main;
    }
}
