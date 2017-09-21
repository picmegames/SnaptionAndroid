package com.snaptiongame.app.data.utils

import com.bumptech.glide.Glide
import com.snaptiongame.app.SnaptionApplication

import java.io.File
import java.util.Locale

import io.reactivex.Completable
import timber.log.Timber

/**
 * @author Tyler Wong
 */

object CacheUtils {

    private const val MB: Long = 1024
    private const val SIZE_FORMAT = "%.1f %sB"
    private const val PREFIX = "KMGTPE"
    private const val BYTE = " B"

    @JvmStatic
    fun clearCache(): Completable {
        return Completable.defer({ deleteCache() })
    }

    @JvmStatic
    val cacheSize: String
        get() {
            var size: Long = 0
            size += getDirSize(SnaptionApplication.context?.cacheDir)
            size += getDirSize(SnaptionApplication.context?.externalCacheDir)
            return humanReadableByteCount(size)
        }

    private fun getDirSize(dir: File?): Long {
        var size: Long = 0

        dir?.listFiles()?.forEach { file ->
            if (file != null && file.isDirectory) {
                size += getDirSize(file)
            }
            else if (file != null && file.isFile) {
                size += file.length()
            }
        }

        return size
    }

    private fun humanReadableByteCount(bytes: Long): String {
        if (bytes < MB) {
            return bytes.toString() + BYTE
        }
        val exp = (Math.log(bytes.toDouble()) / Math.log(MB.toDouble())).toInt()
        val pre = PREFIX[exp - 1]
        return String.format(Locale.US, SIZE_FORMAT, bytes / Math.pow(MB.toDouble(), exp.toDouble()), pre)
    }

    private fun deleteCache(): Completable {
        try {
            val context = SnaptionApplication.context
            Glide.get(context).clearMemory()
            deleteDir(context?.cacheDir)
        }
        catch (e: Exception) {
            Timber.e(e)
        }

        return Completable.complete()
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            children.forEach { child ->
                val success = deleteDir(File(dir, child))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        }
        else return if (dir != null && dir.isFile) {
            dir.delete()
        }
        else {
            false
        }
    }
}
