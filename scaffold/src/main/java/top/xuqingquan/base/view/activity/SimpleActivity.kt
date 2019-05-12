package top.xuqingquan.base.view.activity

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import top.xuqingquan.app.AppComponentUtils
import top.xuqingquan.cache.Cache
import top.xuqingquan.cache.CacheType
import top.xuqingquan.delegate.IActivity
import top.xuqingquan.stack.DebugStackDelegate

/**
 * Created by 许清泉 on 2019-04-24 23:32
 * 不使用MVVM模式的时候可以使用这个类
 */
abstract class SimpleActivity : AppCompatActivity(), IActivity {

    private var mCache: Cache<String, Any>? = null
    private var debugStackDelegate: DebugStackDelegate? = null

    /**
     * @return 布局id
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    final override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            @Suppress("UNCHECKED_CAST")
            mCache =
                AppComponentUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE) as Cache<String, Any>
        }
        return mCache!!
    }

    override fun useEventBus(): Boolean {
        return true
    }

    final override fun setDebugStackDelegate(delegate: DebugStackDelegate?) {
        this.debugStackDelegate = delegate
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        debugStackDelegate?.onPostCreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(savedInstanceState)
    }

    /**
     * 方便给子类继承选择是否使用这种方式填充布局
     * @param savedInstanceState
     */
    protected open fun initView(savedInstanceState: Bundle?) {
        setContentView(getLayoutId())
        initData(savedInstanceState)
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftKeyboard() {
        val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
        if (imm != null && imm.isActive) {
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }
    }

    /**
     * 显示软键盘
     * @param view 需要输入的组件
     * @param time 延迟时间，默认200毫秒
     */
    fun showSoftKeyboard(view: View, time: Long = 200L) {
        val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
        if (imm != null) {
            view.postDelayed({
                view.requestFocus()
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
            }, time)
        }
    }
}
