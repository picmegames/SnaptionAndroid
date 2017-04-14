package com.snaptiongame.app.data.utils;

import com.snaptiongame.app.SnaptionApplication;

import java.io.File;

import io.reactivex.Completable;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class CacheUtils {

    private static final long MB = 1000;
    private static final String MEGABYTES = " MB";

    public static Completable clearCache() {
        return Completable.defer(CacheUtils::deleteCache);
    }

    public static String getCacheSize() {
        long size = 0;
        File[] files = SnaptionApplication.getContext().getCacheDir().listFiles();

        for (File file : files) {
            size += file.length();
        }

        return String.valueOf((float) size / (float) MB) + MEGABYTES;
    }

    private static Completable deleteCache() {
        try {
            File dir = SnaptionApplication.getContext().getCacheDir();
            deleteDir(dir);
        }
        catch (Exception e) {
            Timber.e(e);
        }
        return Completable.complete();
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else if (dir != null && dir.isFile()) {
            return dir.delete();
        }
        else {
            return false;
        }
    }
}
