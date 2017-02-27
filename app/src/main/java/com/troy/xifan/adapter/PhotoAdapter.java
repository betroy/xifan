package com.troy.xifan.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.troy.xifan.R;
import com.troy.xifan.model.response.PhotoRes;
import com.troy.xifan.model.response.StatusRes;

/**
 * Created by chenlongfei on 2017/2/9.
 */

public class PhotoAdapter extends RecyclerArrayAdapter<StatusRes> {
    private Context mContext;

    public PhotoAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(parent);
    }

    public class PhotoViewHolder extends BaseViewHolder<StatusRes> {
        @BindView(R.id.image_photo) ImageView mImagePhoto;

        public PhotoViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_photo);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(StatusRes data) {
            PhotoRes photo = data.getPhoto();
            Glide.with(mContext).load(photo.getLargeurl()).into(mImagePhoto);
        }
    }
}
