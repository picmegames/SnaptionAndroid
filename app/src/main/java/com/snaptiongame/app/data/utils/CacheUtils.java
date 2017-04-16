package com.snaptiongame.app.data.utils;

import com.snaptiongame.app.SnaptionApplication;

import java.io.File;
import java.util.Locale;

import io.reactivex.Completable;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class CacheUtils {

    private static final long MB = 1024;
    private static final String SIZE_FORMAT = "%.1f %sB";
    private static final String PREFIX = "KMGTPE";
    private static final String BYTE = " B";

    public static Completable clearCache() {
        return Completable.defer(CacheUtils::deleteCache);
    }

    public static String getCacheSize() {
        long size = 0;
        size += getDirSize(SnaptionApplication.getContext().getCacheDir());
        size += getDirSize(SnaptionApplication.getContext().getExternalCacheDir());
        return humanReadableByteCount(size);
    }

    private static long getDirSize(File dir) {
        long size = 0;

        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            }
            else if (file != null && file.isFile()) {
                size += file.length();
            }
        }

        return size;
    }

    private static String humanReadableByteCount(long bytes) {
        if (bytes < MB) {
            return bytes + BYTE;
        }
        int exp = (int) (Math.log(bytes) / Math.log(MB));
        char pre = PREFIX.charAt(exp - 1);
        return String.format(Locale.US, SIZE_FORMAT, bytes / Math.pow(MB, exp), pre);
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
