package top.xuqingquan.web;

import top.xuqingquan.web.agent.BaseIndicatorSpec;
import top.xuqingquan.web.agent.IndicatorController;

public class IndicatorHandler implements IndicatorController {
	private BaseIndicatorSpec mBaseIndicatorSpec;

	@Override
	public void progress(android.webkit.WebView v, int newProgress) {
		if (newProgress == 0) {
			reset();
		} else if (newProgress > 0 && newProgress <= 10) {
			showIndicator();
		} else if (newProgress > 10 && newProgress < 95) {
			setProgress(newProgress);
		} else {
			setProgress(newProgress);
			finish();
		}
	}

	@Override
	public void progress(com.tencent.smtt.sdk.WebView v, int newProgress) {
		if (newProgress == 0) {
			reset();
		} else if (newProgress > 0 && newProgress <= 10) {
			showIndicator();
		} else if (newProgress > 10 && newProgress < 95) {
			setProgress(newProgress);
		} else {
			setProgress(newProgress);
			finish();
		}
	}

	@Override
	public BaseIndicatorSpec offerIndicator() {
		return this.mBaseIndicatorSpec;
	}

	public void reset() {
		if (mBaseIndicatorSpec != null) {
			mBaseIndicatorSpec.reset();
		}
	}

	@Override
	public void finish() {
		if (mBaseIndicatorSpec != null) {
			mBaseIndicatorSpec.hide();
		}
	}

	@Override
	public void setProgress(int n) {
		if (mBaseIndicatorSpec != null) {
			mBaseIndicatorSpec.setProgress(n);
		}
	}

	@Override
	public void showIndicator() {

		if (mBaseIndicatorSpec != null) {
			mBaseIndicatorSpec.show();
		}
	}

	static IndicatorHandler getInstance() {
		return new IndicatorHandler();
	}


	IndicatorHandler inJectIndicator(BaseIndicatorSpec baseIndicatorSpec) {
		this.mBaseIndicatorSpec = baseIndicatorSpec;
		return this;
	}
}
