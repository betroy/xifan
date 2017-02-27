package com.troy.xifan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.troy.xifan.R;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SendStatusRequestCallback;
import com.troy.xifan.http.request.StatusesRequest;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.util.CompatUtils;
import com.troy.xifan.util.FanFouUtils;
import com.troy.xifan.util.FileUtils;
import com.troy.xifan.util.UIUtils;
import java.io.File;

/**
 * Created by chenlongfei on 2016/12/18.
 */
@Route(Constants.Router.WRITE_STATUS)
public class WirteStatusActivity extends BaseActivity {
    public static final String BUNDLE_VALUE_STATUS_COMMENT = "status_comment";
    public static final String BUNDLE_VALUE_STATUS_REPEAT = "status_repeat";
    public static final String BUNDLE_STATUS = "extra_status";
    public static final String BUNDLE_STATUS_TYPE = "extra_status_type";
    public static final int REQUEST_GET_CHOOSE_USER = 1;
    public static final int REQUEST_PICK_PHOTO = 2;
    public static final int REQUEST_OPEN_CAMERA = 3;

    public static final int CONTENT_LENGHT_LIMIT = 140;

    private StatusRes mStatusRes;
    private String mStatusType;
    private File mPhotoFile;
    private String mStrContent;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.edit_content) EditText mEditContent;
    @BindView(R.id.image_preview_photo) ImageView mImagePreviewPhoto;
    @BindView(R.id.view_photo_container) View mViewPhotoContainer;
    @BindView(R.id.view_photo) View mViewPhoto;
    @BindView(R.id.view_at) View mViewAt;
    @BindView(R.id.view_topic) View mViewTopic;
    @BindView(R.id.text_content_length) TextView mTextContentLength;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_status);
        ButterKnife.bind(this);
        initExtra();
        initListener();
        initViews();
    }

    private void initExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mStatusRes = bundle.getParcelable(BUNDLE_STATUS);
            mStatusType = bundle.getString(BUNDLE_STATUS_TYPE);
        }
    }

    @Override
    public void initViews() {
        mToolbar.setTitle(R.string.title_write);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPhotoContainer.setVisibility(View.GONE);

        if (BUNDLE_VALUE_STATUS_COMMENT.equals(mStatusType)) {
            mEditContent.setText(FanFouUtils.formatAt(mStatusRes));
            mEditContent.setSelection(mEditContent.getText().length());
        } else if (BUNDLE_VALUE_STATUS_REPEAT.equals(mStatusType)) {
            mEditContent.setText(FanFouUtils.formatRepeat(mStatusRes));
            mEditContent.setSelection(0);
        }

        mEditContent.requestFocus();
    }

    private void initListener() {
        mViewPhoto.setOnClickListener(mClickListener);
        mViewAt.setOnClickListener(mClickListener);
        mViewTopic.setOnClickListener(mClickListener);
        mImagePreviewPhoto.setOnClickListener(mClickListener);
        mEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStrContent = charSequence.toString().trim();
                int length = mStrContent.length();
                mTextContentLength.setText(String.valueOf(140 - length));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wirte_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_send_status:
                sendStatus();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendStatus() {
        StatusesRequest request = new StatusesRequest();

        if (BUNDLE_VALUE_STATUS_COMMENT.equals(mStatusType)) {
            request.setIn_reply_to_status_id(mStatusRes.getId());
        } else if (BUNDLE_VALUE_STATUS_REPEAT.equals(mStatusType)) {
            request.setRepost_status_id(mStatusRes.getId());
        }

        if (mPhotoFile == null) {
            sendCommonStatus(request);
        } else {
            sendImageStatus(request);
        }
    }

    private void sendCommonStatus(StatusesRequest request) {
        if (TextUtils.isEmpty(mStrContent)) {
            UIUtils.showToast(this, getString(R.string.text_status_not_empty));
            return;
        }

        SendStatusRequestCallback callback = new SendStatusRequestCallback();
        request.setStatus(mStrContent);

        HttpRequestFactory.getInstance().updateStatus(request, callback);
        finish();
    }

    private void sendImageStatus(StatusesRequest request) {
        if (mPhotoFile == null) {
            UIUtils.showToast(this, getString(R.string.text_photo_path_error));
            return;
        }
        if (TextUtils.isEmpty(mStrContent)) {
            mStrContent = getString(R.string.text_upload_new_photo);
        }
        SendStatusRequestCallback callback = new SendStatusRequestCallback();
        request.setStatus(mStrContent);
        request.setPhoto(mPhotoFile.getPath());
        HttpRequestFactory.getInstance().uploadPhoto(request, callback);
        finish();
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.view_photo:
                    showChooseDialog();
                    break;
                case R.id.view_at:
                    Bundle bundle = new Bundle();
                    bundle.putString(UserListActivity.BUNLDE_TYPE,
                            UserListActivity.TYPE_GET_AT_FOLLOWING);
                    Router.build(Constants.Router.USER_LIST)
                            .extras(bundle)
                            .requestCode(REQUEST_GET_CHOOSE_USER)
                            .go(WirteStatusActivity.this);
                    break;
                case R.id.view_topic:
                    insertTopic();
                    break;
                case R.id.image_preview_photo:
                    removePhotoDialog();
                    break;
            }
        }
    };

    private void showChooseDialog() {
        new AlertDialog.Builder(this).setItems(getResources().getStringArray(R.array.photo_choose),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                openCamera();
                                break;
                            case 1:
                                pickPhoto();
                                break;
                        }
                    }
                }).show();
    }

    private void removePhotoDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.text_dialog_title_tips)
                .setMessage(R.string.text_remove_photo)
                .setPositiveButton(R.string.text_dialog_positive,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mImagePreviewPhoto.setBackground(null);
                                mImagePreviewPhoto.setImageDrawable(null);
                                mViewPhotoContainer.setVisibility(View.GONE);
                                mPhotoFile = null;
                            }
                        })
                .setNegativeButton(R.string.text_dialog_negative,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                .show();
    }

    private void insertTopic() {
        int selectionStart = mEditContent.getSelectionStart();
        mEditContent.getText().insert(selectionStart, "##");
        mEditContent.setSelection(selectionStart + 1);
    }

    private void openCamera() {
        mPhotoFile = FileUtils.getPhotoFile();
        Uri uri = Uri.fromFile(mPhotoFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.text_capture_photo)),
                REQUEST_OPEN_CAMERA);
    }

    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.text_choose_photo)),
                REQUEST_PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_GET_CHOOSE_USER) {
            String userName = data.getExtras().getString(UserListActivity.EXTRAS_USER_NAME);
            if (!TextUtils.isEmpty(userName)) {
                String atUserName = FanFouUtils.formatAt(userName);
                mEditContent.getText().insert(mEditContent.getSelectionStart(), atUserName);
            }
        } else if (requestCode == REQUEST_PICK_PHOTO) {
            Uri uri = data.getData();
            String path = CompatUtils.getPath(this, uri);
            mPhotoFile = new File(path);
            Glide.with(this).load(mPhotoFile).into(mImagePreviewPhoto);
            mViewPhotoContainer.setVisibility(View.VISIBLE);
        } else if (requestCode == REQUEST_OPEN_CAMERA) {
            Glide.with(this).load(mPhotoFile).into(mImagePreviewPhoto);
            mViewPhotoContainer.setVisibility(View.VISIBLE);
        }
    }
}
