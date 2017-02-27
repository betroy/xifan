package com.troy.xifan.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.chenenyu.router.Router;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.troy.xifan.R;
import com.troy.xifan.activity.ProfileActivity;
import com.troy.xifan.config.Constants;
import com.troy.xifan.model.response.DirectMessagesListRes;
import com.troy.xifan.model.response.DirectMessagesRes;
import com.troy.xifan.model.response.UserRes;
import com.troy.xifan.util.Utils;

/**
 * Created by chenlongfei on 2017/1/13.
 */

public class ConversationListAdapter extends RecyclerArrayAdapter<DirectMessagesListRes> {
    private Context mContext;

    public ConversationListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectMessageViewHolder(parent);
    }

    public class DirectMessageViewHolder extends BaseViewHolder<DirectMessagesListRes> {
        @BindView(R.id.image_avatar) ImageView mImageAvatar;
        @BindView(R.id.text_name) TextView mTextName;
        @BindView(R.id.text_content) TextView mTextContent;
        @BindView(R.id.text_date) TextView mTextDate;

        public DirectMessageViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_conversation_list);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(DirectMessagesListRes data) {
            DirectMessagesRes dm = data.getDm();
            final UserRes senderUser = dm.getSender();
            final UserRes recipientUser = dm.getRecipient();
            String senderId = dm.getSender_id();
            String recipientId = dm.getRecipient_id();
            String otherId = data.getOtherid();
            final boolean income = otherId.equals(senderId);
            Glide.with(mContext)
                    .load(income ? senderUser.getProfile_image_url_large()
                            : recipientUser.getProfile_image_url_large())
                    .into(mImageAvatar);
            mTextName.setText(income ? senderUser.getName() : recipientUser.getName());
            mTextContent.setText(dm.getText());
            mTextDate.setText(Utils.getIntervalTime(dm.getCreated_at()));
            mImageAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle userBundle = new Bundle();
                    userBundle.putParcelable(ProfileActivity.BUNDLE_USER,
                            income ? senderUser : recipientUser);
                    Router.build(Constants.Router.PROFILE).extras(userBundle).go(mContext);
                }
            });
        }
    }
}
