package com.troy.xifan.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.troy.xifan.R;
import com.troy.xifan.model.response.UserRes;

/**
 * Created by chenlongfei on 2017/1/4.
 */

public class FollowingAdapter extends RecyclerArrayAdapter<UserRes> {
    private Context mContext;

    public FollowingAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FollowingViewHolder(parent);
    }

    public class FollowingViewHolder extends BaseViewHolder<UserRes> {
        @BindView(R.id.image_avatar) ImageView mImageAvatar;
        @BindView(R.id.text_name) TextView mTextName;

        public FollowingViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_user);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(UserRes data) {
            String imageUrl = data.getProfile_image_url_large();
            Glide.with(mContext).load(imageUrl).into(mImageAvatar);
            mTextName.setText(data.getName());
        }
    }
}
