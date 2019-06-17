package top.xuqingquan.web;

import android.os.Handler;
import android.os.Looper;
import android.webkit.ValueCallback;
import android.webkit.WebView;

public class JsAccessEntraceImpl extends BaseJsAccessEntrace {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static JsAccessEntraceImpl getInstance(WebView webView) {
        return new JsAccessEntraceImpl(webView);
    }

    private JsAccessEntraceImpl(WebView webView) {
        super(webView);
    }

    private void safeCallJs(final String s, final ValueCallback valueCallback) {
        mHandler.post(() -> callJs(s, valueCallback));
    }

    @Override
    public void callJs(String params, final ValueCallback<String> callback) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            safeCallJs(params, callback);
            return;
        }
        super.callJs(params,callback);
    }


}
