package com.troy.xifan.view;

import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import com.chenenyu.router.Router;
import com.orhanobut.logger.Logger;
import com.troy.xifan.App;
import com.troy.xifan.R;
import com.troy.xifan.activity.BrowserActivity;

/**
 * Created by chenlongfei on 2016/12/15.
 */

public class XiFanClickableSpan extends ClickableSpan {
    private URLSpan urlSpan;

    public XiFanClickableSpan(URLSpan urlSpan) {
        this.urlSpan = urlSpan;
    }

    @Override
    public void onClick(View view) {
        Logger.d("URL:" + urlSpan.getURL());
        Bundle bundle = new Bundle();
        bundle.putString(BrowserActivity.BUNDLE_URL, "http://www.baidu.com");
        //Router.build(Constants.Router.BROWSER).extras(bundle).go(view.getContext());
        Router.build(urlSpan.getURL()).go(view.getContext());
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(App.getInstance().getResources().getColor(R.color.colorPrimary));
        ds.setUnderlineText(false);
    }
}
