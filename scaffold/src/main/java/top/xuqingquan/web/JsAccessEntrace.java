package top.xuqingquan.web;

import com.tencent.smtt.sdk.ValueCallback;

public interface JsAccessEntrace extends QuickCallJs {

    void callJs(String js, ValueCallback<String> callback);

    void callJs(String js);

}
