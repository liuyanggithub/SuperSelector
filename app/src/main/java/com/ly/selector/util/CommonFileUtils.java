package com.ly.selector.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CommonFileUtils {

    /**
     * copy file
     *
     * @param source source file
     * @param dest   dest file
     * @return true if success copied
     */
    public static boolean copy(File source, File dest) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        boolean result = true;
        try {
            bis = new BufferedInputStream(new FileInputStream(source));
            bos = new BufferedOutputStream(new FileOutputStream(dest, false));

            byte[] buf = new byte[1024];
            bis.read(buf);

            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            result = false;
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                result = false;
            }
        }

        return result;
    }

    /**
     * 在缓存文件夹生成一个临时文件
     *
     * @param context context
     * @param ext     文件后缀名 e.g ".jpg"
     * @return 生成的临时文件
     */
    public static File generateExternalImageCacheFileName(Context context, String prefix, String ext) {
//        String fileName = "img_" + System.currentTimeMillis();
        String fileName = prefix + System.currentTimeMillis();
        return generateExternalImageCacheFile(context, fileName, ext);
    }

    private static File generateExternalImageCacheFile(Context context, String fileName, String ext) {
        File cacheDir = getExternalImageCacheDir(context, fileName.substring(0, fileName.indexOf("_")));
        String path = cacheDir.getPath() + File.separator + fileName + ext;
        return new File(path);
    }

    public static File getExternalImageCacheDir(Context context, String type) {
        File externalCacheDir = getExternalCacheDir(context);
        if (externalCacheDir != null) {
            String path = externalCacheDir.getPath() + "/" + type +"/sxt";
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache" + "/" + type;
        return new File(cacheDir);
    }

    public static File getExternalCacheDir(Context context) {
        File file = context.getExternalCacheDir();
        if (file == null) {
            final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache";
            file = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        }
        return file;
    }
}