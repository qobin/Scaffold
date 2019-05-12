package top.xuqingquan.utils

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * Created by 许清泉 on 2019/4/14 21:49
 */
object FileUtils {

    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    @JvmStatic
    fun makeDirs(file: File): File {
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 返回缓存文件夹
     */
    @JvmStatic
    fun getCacheFile(context: Context): File {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            var file: File?
            file = context.externalCacheDir//获取系统管理的sd卡缓存文件
            if (file == null) {//如果获取的文件为空,就使用自己定义的缓存文件夹做缓存路径
                file = File(getCacheFilePath(context))
                makeDirs(file)
            }
            file
        } else {
            context.cacheDir
        }
    }

    /**
     * 获取自定义缓存文件地址
     *
     * @param context
     * @return
     */
    fun getCacheFilePath(context: Context): String {
        val packageName = context.packageName
        return "${Environment.getExternalStorageDirectory().path}/$packageName"
    }
}