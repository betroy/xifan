package com.troy.xifan.util;

import android.os.Environment;
import com.orhanobut.logger.Logger;
import com.troy.xifan.App;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by chenlongfei on 2016/11/26.
 */

public final class FileUtils {
    private static final String PHOTO_DIR_NAME = "XiFan";

    private FileUtils() {

    }

    public synchronized static boolean saveObjectToFile(Object object, File file) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(object);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.e("sava object fail\n" + e);
            }
        }
    }

    public synchronized static Object readObjectFromFile(File file) {
        ObjectInputStream input = null;
        Object object = null;
        try {
            input = new ObjectInputStream(new FileInputStream(file));
            object = input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("read object from file fail\n" + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Logger.e("read object from file fail\n" + e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static boolean deleteFile(File file) {
        return file.delete();
    }

    public static File getCacheFile(String fileName) {
        File dir = App.getInstance().getCacheDir();
        File file = new File(dir, fileName);
        return file;
    }

    public static File getPhotoDirFile() {
        File photoFile = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dcimDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            photoFile = new File(dcimDir, PHOTO_DIR_NAME);
        } else {
            photoFile = App.getInstance().getCacheDir();
        }
        if (!photoFile.exists()) {
            photoFile.mkdir();
        }
        return photoFile;
    }

    public static File getPhotoFile() {
        long currentTime = System.currentTimeMillis();
        String photoName = String.format("%d.jpg", currentTime);
        return new File(getPhotoDirFile(), photoName);
    }
}
