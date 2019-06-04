package top.xuqingquan.web.agent;

import android.os.Build;
import android.webkit.JavascriptInterface;
import top.xuqingquan.web.x5.AgentWebConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class JsBaseInterfaceHolder implements JsInterfaceHolder {

    private SecurityType mSecurityType;

    protected JsBaseInterfaceHolder(SecurityType securityType) {
        this.mSecurityType = securityType;
    }

    @Override
    public boolean checkObject(Object v) {
        if (AgentWebConfig.WEBVIEW_TYPE == AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE) {
            return true;
        }
        boolean tag = false;
        Class clazz = v.getClass();
        Method[] mMethods = clazz.getMethods();
        for (Method mMethod : mMethods) {
            Annotation[] mAnnotations = mMethod.getAnnotations();
            for (Annotation mAnnotation : mAnnotations) {
                if (mAnnotation instanceof JavascriptInterface) {
                    tag = true;
                    break;
                }
            }
            if (tag) {
                break;
            }
        }
        return tag;
    }

    protected boolean checkSecurity() {
        return mSecurityType != SecurityType.STRICT_CHECK || (AgentWebConfig.WEBVIEW_TYPE == AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE || Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1);
    }

}