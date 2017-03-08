package com.troy.xifan.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.troy.xifan.R;
import com.troy.xifan.config.Constants;
import com.troy.xifan.eventbus.StatusRefreshEvent;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.request.StatusesRequest;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.model.response.UserRes;
import com.troy.xifan.util.PatternUtils;
import com.troy.xifan.util.UIUtils;
import com.troy.xifan.util.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by chenlongfei on 2016/12/19.
 */
@Route(Constants.Router.STATUS_DETAIL)
public class StatusDetailActivity extends BaseActivity {
    public static final String EXTRA_STATUS = "extra_status";
    private static final String TYPE_FAVORITE_STATUS = "favorite_status";
    private static final String TYPE_DELETE_STATUS = "delete_status";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.image_avatar) CircleImageView mImageAvatar;
    @BindView(R.id.text_name) TextView mTextName;
    @BindView(R.id.text_date) TextView mTextDate;
    @BindView(R.id.text_source) TextView mTextSource;
    @BindView(R.id.text_content) TextView mTextContent;
    @BindView(R.id.image_photo) ImageView mImagePhoto;
    @BindView(R.id.view_repeat) View mViewRepeat;
    @BindView(R.id.view_comment) View mViewComment;

    private StatusRes mStatusRes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);
        ButterKnife.bind(this);

        mStatusRes = getIntent().getParcelableExtra(EXTRA_STATUS);
        initViews();
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(getString(R.string.title_status_detail));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final UserRes userRes = mStatusRes.getUser();
        String imageUrl = userRes.getProfile_image_url_large();
        Glide.with(this).load(imageUrl).into(mImageAvatar);
        mTextName.setText(userRes.getName());
        mTextDate.setText(Utils.getIntervalTime(mStatusRes.getCreated_at()));
        mTextContent.setText(
                PatternUtils.formatWeiBoContent(this, mStatusRes.getText(), mTextContent));
        mTextSource.setText(Html.fromHtml(mStatusRes.getSource()).toString());

        if (mStatusRes.getPhoto() != null) {
            Glide.with(this).load(mStatusRes.getPhoto().getLargeurl()).into(mImagePhoto);
            mImagePhoto.setVisibility(View.VISIBLE);
        } else {
            mImagePhoto.setVisibility(View.GONE);
        }

        OnClickLister onClickLister = new OnClickLister(mStatusRes);

        mImagePhoto.setOnClickListener(onClickLister);
        mViewRepeat.setOnClickListener(onClickLister);
        mViewComment.setOnClickListener(onClickLister);
        mImageAvatar.setOnClickListener(onClickLister);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mStatusRes.isIs_self()) {
            getMenuInflater().inflate(R.menu.menu_status_detail_self, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_status_deatil, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mStatusRes.isFavorited()) {
            menu.findItem(R.id.action_favorite).setTitle(R.string.action_cancel_favorite);
        } else {
            menu.findItem(R.id.action_favorite).setTitle(R.string.action_favorite);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                showDeleteStatusDailog(mStatusRes);
                break;
            case R.id.action_favorite:
                createOrDestroyFavorite(mStatusRes);
                break;
            case R.id.action_copy:
                copyStatus(mStatusRes);
                break;
            case R.id.action_share:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //删除消息
    private void destoryStatuses(final StatusRes statusRes) {
        SimpleRequestCallback requestCallback =
                new SimpleRequestCallback(statusRes, TYPE_DELETE_STATUS);
        StatusesRequest request = new StatusesRequest();
        request.setId(statusRes.getId());
        HttpRequestFactory.getInstance().destroyStatus(request, requestCallback);
    }

    //收藏或取消收藏消息
    private void createOrDestroyFavorite(final StatusRes statusRes) {
        SimpleRequestCallback requestCallback =
                new SimpleRequestCallback(statusRes, TYPE_FAVORITE_STATUS);

        if (statusRes.isFavorited()) {
            HttpRequestFactory.getInstance().destroyFavorite(statusRes.getId(), requestCallback);
        } else {
            HttpRequestFactory.getInstance().createFavorite(statusRes.getId(), requestCallback);
        }
    }

    private class SimpleRequestCallback extends SimpleHttpRequestCallback<StatusRes> {
        private StatusRes statusRes;
        private String type;

        public SimpleRequestCallback(StatusRes statusRes, String type) {
            this.statusRes = statusRes;
            this.type = type;
        }

        @Override
        public void onSuccess(StatusRes responseData) {
            if (TYPE_DELETE_STATUS.equals(type)) {
                EventBus.getDefault().post(new StatusRefreshEvent(mStatusRes, TYPE_DELETE_STATUS));
                finish();
                UIUtils.showToast(StatusDetailActivity.this,
                        getString(R.string.text_delete_success));
            } else if (TYPE_FAVORITE_STATUS.equals(type)) {
                statusRes.setFavorited(!statusRes.isFavorited());
                EventBus.getDefault()
                        .post(new StatusRefreshEvent(mStatusRes, TYPE_FAVORITE_STATUS));
                UIUtils.showToast(StatusDetailActivity.this,
                        getString(R.string.text_operate_success));
            }
        }

        @Override
        public void onFail(ApiException apiException) {
            if (TYPE_DELETE_STATUS.equals(type)) {
                UIUtils.showToast(StatusDetailActivity.this, getString(R.string.text_delete_fail));
            } else if (TYPE_FAVORITE_STATUS.equals(type)) {
                statusRes.setFavorited(!statusRes.isFavorited());
                UIUtils.showToast(StatusDetailActivity.this, getString(R.string.text_operate_fail));
            }
        }
    }

    private class OnClickLister implements View.OnClickListener {
        private StatusRes statusRes;

        public OnClickLister(StatusRes statusRes) {
            this.statusRes = statusRes;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.image_avatar:
                    Bundle userBundle = new Bundle();
                    userBundle.putParcelable(ProfileActivity.BUNDLE_USER, mStatusRes.getUser());
                    Router.build(Constants.Router.PROFILE)
                            .extras(userBundle)
                            .go(StatusDetailActivity.this);
                    break;
                case R.id.image_photo:
                    if (mStatusRes.getPhoto() != null) {
                        Bundle imageBundle = new Bundle();
                        imageBundle.putString(LargeImageActivity.BUNDLE_IMAGE_URL,
                                mStatusRes.getPhoto().getLargeurl());
                        Router.build(Constants.Router.LARGE_IMAGE)
                                .extras(imageBundle)
                                .go(StatusDetailActivity.this);
                    }
                    break;
                case R.id.view_comment:
                    Bundle commentBundle = new Bundle();
                    commentBundle.putString(WirteStatusActivity.BUNDLE_STATUS_TYPE,
                            WirteStatusActivity.BUNDLE_VALUE_STATUS_COMMENT);
                    commentBundle.putParcelable(WirteStatusActivity.BUNDLE_STATUS, statusRes);
                    Router.build(Constants.Router.WRITE_STATUS)
                            .extras(commentBundle)
                            .go(StatusDetailActivity.this);
                    break;
                case R.id.view_repeat:
                    Bundle repeatBundle = new Bundle();
                    repeatBundle.putString(WirteStatusActivity.BUNDLE_STATUS_TYPE,
                            WirteStatusActivity.BUNDLE_VALUE_STATUS_REPEAT);
                    repeatBundle.putParcelable(WirteStatusActivity.BUNDLE_STATUS, statusRes);
                    Router.build(Constants.Router.WRITE_STATUS)
                            .extras(repeatBundle)
                            .go(StatusDetailActivity.this);
                    break;
            }
        }
    }

    private void copyStatus(StatusRes statusRes) {
        ClipboardManager clipboardManager =
                (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, statusRes.getText()));
        UIUtils.showToast(this, getString(R.string.text_copy_success));
    }

    private void showDeleteStatusDailog(final StatusRes statusRes) {
        new AlertDialog.Builder(this).setMessage(getString(R.string.text_dialog_delete_msg))
                .setPositiveButton(getString(R.string.text_dialog_positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                destoryStatuses(statusRes);
                            }
                        })
                .setNegativeButton(getString(R.string.text_dialog_negative),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                .create()
                .show();
    }
}
