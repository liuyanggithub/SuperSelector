/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ly.selector.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ly.selector.R;
import com.ly.selector.customview.CropImageView;
import com.ly.selector.util.Crop;
import com.ly.selector.util.CropUtil;
import com.ly.selector.util.UriConvertUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * Modified from original in AOSP.
 */
public class CropImageActivity extends MonitoredActivity {

    private static final int SIZE_DEFAULT = 2048;
    private static final int SIZE_LIMIT = 4096;

    private final Handler handler = new Handler();


    // Output image
//    private int maxX;
//    private int maxY;
//    private int exifRotation;

    private Uri sourceUri;
    private Uri saveUri;

    private boolean isSaving;

//    private int sampleSize;
    private Bitmap mBitmap;
    private CropImageView imageView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setupWindowFlags();
        setupViews();

        loadInput();
        if (mBitmap == null) {
            finish();
            return;
        }
        startCrop();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setupWindowFlags() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void setupViews() {
        setContentView(R.layout.activity_crop);

        imageView = (CropImageView) findViewById(R.id.crop_image);

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveClicked();
            }
        });
    }

    private void loadInput() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
//            maxX = extras.getInt(Crop.Extra.MAX_X);
//            maxY = extras.getInt(Crop.Extra.MAX_Y);
            saveUri = extras.getParcelable(MediaStore.EXTRA_OUTPUT);
        }

        sourceUri = intent.getData();
        if (sourceUri != null) {
            mBitmap = BitmapFactory.decodeFile(UriConvertUtil.getImageAbsolutePath(this, sourceUri));

            if(mBitmap == null){
                Toast.makeText(this, "选择的图片格式有误", Toast.LENGTH_SHORT).show();
                return;
            }
            int maxP = Math.max(mBitmap.getWidth(), mBitmap.getHeight());
            float scale1280 = (float)maxP / 1280;

            mBitmap = Bitmap.createScaledBitmap(mBitmap, (int)(mBitmap.getWidth()/scale1280),
                    (int)(mBitmap.getHeight()/scale1280), true);
        }
    }

    private int calculateBitmapSampleSize(Uri bitmapUri) throws IOException {
        InputStream is = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            is = getContentResolver().openInputStream(bitmapUri);
            BitmapFactory.decodeStream(is, null, options); // Just get image size
        } finally {
            CropUtil.closeSilently(is);
        }

        int maxSize = getMaxImageSize();
        int sampleSize = 1;
        while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    private int getMaxImageSize() {
        int textureLimit = getMaxTextureSize();
        if (textureLimit == 0) {
            return SIZE_DEFAULT;
        } else {
            return Math.min(textureLimit, SIZE_LIMIT);
        }
    }

    private int getMaxTextureSize() {
        // The OpenGL texture size is the maximum size that can be drawn in an ImageView
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    private void startCrop() {
        if (isFinishing()) {
            return;
        }
        imageView.setImageBitmap(mBitmap);
//        CropUtil.startBackgroundJob(this, null, getResources().getString(R.string.crop__wait),
//                new Runnable() {
//                    public void run() {
//                        final CountDownLatch latch = new CountDownLatch(1);
//                        handler.post(new Runnable() {
//                            public void run() {
////                                if (imageView.getScale() == 1F) {
////                                    imageView.center();
////                                }
//                                latch.countDown();
//                            }
//                        });
//                        try {
//                            latch.await();
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                        new Cropper().crop();
//                    }
//                }, handler
//        );
    }

//    private class Cropper {
//
//        private void makeDefault() {
//            if (mBitmap == null) {
//                return;
//            }
//
////            HighlightView hv = new HighlightView(imageView);
//            final int width = mBitmap.getWidth();
//            final int height = mBitmap.getHeight();
//
//            Rect imageRect = new Rect(0, 0, width, height);
//
//            // Make the default size about 4/5 of the width or height
//            int cropWidth = Math.min(width, height) * 4 / 5;
//            @SuppressWarnings("SuspiciousNameCombination")
//            int cropHeight = cropWidth;
//
//            if (aspectX != 0 && aspectY != 0) {
//                if (aspectX > aspectY) {
//                    cropHeight = cropWidth * aspectY / aspectX;
//                } else {
//                    cropWidth = cropHeight * aspectX / aspectY;
//                }
//            }
//
//            int x = (width - cropWidth) / 2;
//            int y = (height - cropHeight) / 2;
//
////            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
////            hv.setup(imageView.getUnrotatedMatrix(), imageRect, cropRect, aspectX != 0 && aspectY != 0);
////            imageView.add(hv);
//        }
//
//        public void crop() {
//            handler.post(new Runnable() {
//                public void run() {
////                    makeDefault();
////                    imageView.invalidate();
////                    if (imageView.highlightViews.size() == 1) {
////                        cropView = imageView.highlightViews.get(0);
////                        cropView.setFocus(true);
////                    }
//                }
//            });
//        }
//    }

    private void onSaveClicked() {
        if (isSaving) {
            return;
        }
        isSaving = true;

//        Bitmap croppedImage;
//        Rect r = cropView.getScaledCropRect(sampleSize);
//        Rect r = imageView.getCroppedImage().get;
        final Bitmap cropped = imageView.getCroppedBitmap();
//        int width = cropped.getWidth();
//        int height = cropped.getHeight();
//        Rect r = new Rect();
//        imageView.getBitmapRect().roundOut(r);
//
//        int outWidth = width;
//        int outHeight = height;
//        if (maxX > 0 && maxY > 0 && (width > maxX || height > maxY)) {
//            float ratio = (float) width / (float) height;
//            if ((float) maxX / (float) maxY > ratio) {
//                outHeight = maxY;
//                outWidth = (int) ((float) maxY * ratio + .5f);
//            } else {
//                outWidth = maxX;
//                outHeight = (int) ((float) maxX / ratio + .5f);
//            }
//        }
//
//        try {
//            croppedImage = decodeRegionCrop(r, outWidth, outHeight);
//        } catch (IllegalArgumentException e) {
//            setResultException(e);
//            finish();
//            return;
//        }

//        if (croppedImage != null) {
//            imageView.setImageRotateBitmapResetBase(new RotateBitmap(croppedImage, exifRotation), true);
//            imageView.center();
//            imageView.highlightViews.clear();
//        }
        saveImage(cropped);
    }

    private void saveImage(Bitmap croppedImage) {
        if (croppedImage != null) {
            final Bitmap b = croppedImage;
            CropUtil.startBackgroundJob(this, null, getResources().getString(R.string.crop__saving),
                    new Runnable() {
                        public void run() {
                            saveOutput(b);
                        }
                    }, handler
            );
        } else {
            finish();
        }
    }


    private void clearImageView() {
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        System.gc();
    }

    private void saveOutput(Bitmap croppedImage) {
        if (saveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException e) {
                setResultException(e);
                Log.e("Cannot open file: " + saveUri, String.valueOf(e));
            } finally {
                CropUtil.closeSilently(outputStream);
            }

            CropUtil.copyExifRotation(
                    CropUtil.getFromMediaUri(this, getContentResolver(), sourceUri),
                    CropUtil.getFromMediaUri(this, getContentResolver(), saveUri)
            );

            setResultUri(saveUri);
        }

        final Bitmap b = croppedImage;
        handler.post(new Runnable() {
            public void run() {
                b.recycle();
            }
        });

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }

    @Override
    public boolean onSearchRequested() {
        return false;
    }

    public boolean isSaving() {
        return isSaving;
    }

    private void setResultUri(Uri uri) {
        setResult(RESULT_OK, new Intent().putExtra(MediaStore.EXTRA_OUTPUT, uri));
    }

    private void setResultException(Throwable throwable) {
        setResult(Crop.RESULT_ERROR, new Intent().putExtra(Crop.Extra.ERROR, throwable));
    }

}
