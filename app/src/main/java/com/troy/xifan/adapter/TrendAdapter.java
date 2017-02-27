package com.troy.xifan.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.troy.xifan.R;
import com.troy.xifan.model.response.TrendsRes;

/**
 * Created by chenlongfei on 2017/1/10.
 */

public class TrendAdapter extends RecyclerArrayAdapter<TrendsRes.Trends> {
    private Context mContext;

    public TrendAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrendViewHolder(parent);
    }

    public class TrendViewHolder extends BaseViewHolder<TrendsRes.Trends> {
        @BindView(R.id.text_trend_name) TextView mTextTrendName;

        public TrendViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_trend);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(TrendsRes.Trends data) {
            mTextTrendName.setText(data.getName());
        }
    }
}
