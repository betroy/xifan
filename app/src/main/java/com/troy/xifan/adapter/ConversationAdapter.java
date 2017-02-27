package com.troy.xifan.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.troy.xifan.R;
import com.troy.xifan.model.response.DirectMessagesRes;
import com.troy.xifan.util.Utils;
import java.security.InvalidParameterException;

/**
 * Created by chenlongfei on 2017/1/16.
 */

public class ConversationAdapter extends RecyclerArrayAdapter<DirectMessagesRes> {
    private static final int TYPE_INVALID = 0;
    private static final int TYPE_IN = 1;
    private static final int TYPE_OUT = 2;

    private Context mContext;
    private String mOtherUserId;

    public ConversationAdapter(Context context, String otherUserId) {
        super(context);
        mContext = context;
        mOtherUserId = otherUserId;
    }

    @Override
    public int getViewType(int position) {
        DirectMessagesRes dm = getItem(position);
        if (mOtherUserId.equals(dm.getSender_id())) {
            return TYPE_IN;
        } else if (mOtherUserId.equals(dm.getRecipient_id())) {
            return TYPE_OUT;
        } else {
            return TYPE_INVALID;
        }
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_IN:
                return new DmInViewHolder(parent);
            case TYPE_OUT:
                return new DmOutViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    public class DmInViewHolder extends BaseViewHolder<DirectMessagesRes> {
        @BindView(R.id.text_date) TextView mTextDate;
        @BindView(R.id.text_msg) TextView mTextMsg;

        public DmInViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_conversation_in);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(DirectMessagesRes data) {
            mTextDate.setText(Utils.getDMDateFormatString(data.getCreated_at()));
            mTextMsg.setText(data.getText());
        }
    }

    public class DmOutViewHolder extends BaseViewHolder<DirectMessagesRes> {
        @BindView(R.id.text_date) TextView mTextDate;
        @BindView(R.id.text_msg) TextView mTextMsg;

        public DmOutViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_conversation_out);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(DirectMessagesRes data) {
            mTextDate.setText(Utils.getDMDateFormatString(data.getCreated_at()));
            mTextMsg.setText(data.getText());
        }
    }
}
