package com.troy.xifan.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.chenenyu.router.Router;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.troy.xifan.R;
import com.troy.xifan.activity.ProfileActivity;
import com.troy.xifan.activity.WirteStatusActivity;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.request.StatusesRequest;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.model.response.UserRes;
import com.troy.xifan.util.PatternUtils;
import com.troy.xifan.util.UIUtils;
import com.troy.xifan.util.Utils;

/**
 * Created by chenlongfei on 2016/12/1.
 */

public class StatusAdapter extends RecyclerArrayAdapter<StatusRes> {
    private static final String TYPE_FAVORITE_STATUS = "favorite_status";
    private static final String TYPE_DELETE_STATUS = "delete_status";

    private Context mContext;

    public StatusAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new StatusViewHolder(parent);
    }

    public class StatusViewHolder extends BaseViewHolder<StatusRes> {
        @BindView(R.id.image_avatar) ImageView mImageAvatar;
        @BindView(R.id.text_name) TextView mTextName;
        @BindView(R.id.text_date) TextView mTextDate;
        @BindView(R.id.text_content) TextView mTextContent;
        @BindView(R.id.text_source) TextView mTextSource;
        @BindView(R.id.image_menu) ImageView mImageMenu;
        @BindView(R.id.image_photo) ImageView mImagePhoto;
        @BindView(R.id.button_favorite) Button mButtonFavorite;
        @BindView(R.id.view_favorite) View mViewFavorite;
        @BindView(R.id.view_repeat) View mViewRepeat;
        @BindView(R.id.view_comment) View mViewComment;

        public StatusViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_status);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(final StatusRes data) {
            final UserRes user = data.getUser();
            String imageUrl = user.getProfile_image_url_large();
            Glide.with(mContext).load(imageUrl).into(mImageAvatar);
            mTextName.setText(user.getName());
            mTextDate.setText(Utils.getIntervalTime(data.getCreated_at()));
            mTextContent.setText(
                    PatternUtils.formatWeiBoContent(mContext, data.getText(), mTextContent));
            mTextSource.setText(Html.fromHtml(data.getSource()).toString());

            if (data.getPhoto() != null) {
                Glide.with(mContext).load(data.getPhoto().getLargeurl()).into(mImagePhoto);
                mImagePhoto.setVisibility(View.VISIBLE);
            } else {
                mImagePhoto.setVisibility(View.GONE);
            }

            if (data.isFavorited()) {
                mButtonFavorite.setBackgroundResource(R.mipmap.ic_favorited);
                mButtonFavorite.setBackgroundTintList(ColorStateList.valueOf(
                        mContext.getResources().getColor(R.color.colorYellow)));
            } else {
                mButtonFavorite.setBackgroundResource(R.mipmap.ic_unfavorite);
                mButtonFavorite.setBackgroundTintList(ColorStateList.valueOf(
                        mContext.getResources().getColor(R.color.colorTextGray)));
            }

            OnClickLister onClickLister = new OnClickLister(data);

            mImageAvatar.setOnClickListener(onClickLister);
            mImageMenu.setOnClickListener(onClickLister);
            mViewFavorite.setOnClickListener(onClickLister);
            mViewRepeat.setOnClickListener(onClickLister);
            mViewComment.setOnClickListener(onClickLister);
        }
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
                remove(statusRes);
                UIUtils.showToast(mContext, mContext.getString(R.string.text_delete_success));
            }
        }

        @Override
        public void onFail(ApiException apiException) {
            if (TYPE_DELETE_STATUS.equals(type)) {
                UIUtils.showToast(mContext, mContext.getString(R.string.text_delete_fail));
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
                case R.id.image_menu:
                    showPopupMenu(view, statusRes);
                    break;
                case R.id.image_avatar:
                    Bundle userBundle = new Bundle();
                    userBundle.putParcelable(ProfileActivity.BUNDLE_USER, statusRes.getUser());
                    Router.build(Constants.Router.PROFILE).extras(userBundle).go(mContext);
                    break;
                case R.id.view_favorite:
                    createOrDestroyFavorite(statusRes);
                    statusRes.setFavorited(!statusRes.isFavorited());
                    notifyDataSetChanged();
                    break;
                case R.id.view_comment:
                    Bundle commentBundle = new Bundle();
                    commentBundle.putString(WirteStatusActivity.BUNDLE_STATUS_TYPE,
                            WirteStatusActivity.BUNDLE_VALUE_STATUS_COMMENT);
                    commentBundle.putParcelable(WirteStatusActivity.BUNDLE_STATUS, statusRes);
                    Router.build(Constants.Router.WRITE_STATUS).extras(commentBundle).go(mContext);
                    break;
                case R.id.view_repeat:
                    Bundle repeatBundle = new Bundle();
                    repeatBundle.putString(WirteStatusActivity.BUNDLE_STATUS_TYPE,
                            WirteStatusActivity.BUNDLE_VALUE_STATUS_REPEAT);
                    repeatBundle.putParcelable(WirteStatusActivity.BUNDLE_STATUS, statusRes);
                    Router.build(Constants.Router.WRITE_STATUS).extras(repeatBundle).go(mContext);
                    break;
            }
        }
    }

    private void showPopupMenu(View view, final StatusRes statusRes) {
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        if (statusRes.isIs_self()) {
            popupMenu.inflate(R.menu.menu_item_status_self);
        } else {
            popupMenu.inflate(R.menu.menu_item_status);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_delete:
                        showDeleteStatusDailog(statusRes);
                        break;
                    case R.id.action_copy:
                        copyStatus(statusRes);
                        break;
                    case R.id.action_share:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void copyStatus(StatusRes statusRes) {
        ClipboardManager clipboardManager =
                (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, statusRes.getText()));
        UIUtils.showToast(mContext, mContext.getString(R.string.text_copy_success));
    }

    private void showDeleteStatusDailog(final StatusRes statusRes) {
        new AlertDialog.Builder(mContext).setMessage(
                mContext.getString(R.string.text_dialog_delete_msg))
                .setPositiveButton(mContext.getString(R.string.text_dialog_positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                destoryStatuses(statusRes);
                            }
                        })
                .setNegativeButton(mContext.getString(R.string.text_dialog_negative),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                .create()
                .show();
    }
}
