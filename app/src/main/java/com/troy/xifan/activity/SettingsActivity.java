package com.troy.xifan.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.troy.xifan.App;
import com.troy.xifan.BuildConfig;
import com.troy.xifan.R;
import com.troy.xifan.config.Constants;
import com.troy.xifan.manage.UserHolder;
import com.troy.xifan.model.response.AppVersionInfoRes;
import com.troy.xifan.service.DownLoadApkService;
import com.troy.xifan.util.UIUtils;
import com.troy.xifan.util.Utils;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * Created by chenlongfei on 2017/1/25.
 */
@Route(Constants.Router.SETTINGS)
public class SettingsActivity extends BaseActivity {
    private static final String FIR_TOKEN = "2def92fc28bb812a895538fa54ed38d3";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.layout_logout) View mViewLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(getString(R.string.title_settings));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initFragment();
        initListeners();
    }

    private void initListeners() {
        mViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showDialog(SettingsActivity.this, getString(R.string.title_tips),
                        getString(R.string.text_logout_tips), new UIUtils.OnDialogListener() {
                            @Override
                            public void onConfirm() {
                                UserHolder.getInstance().cleanUser();
                                Router.build(Constants.Router.LOGIN).go(SettingsActivity.this);
                                App.getInstance().cleanActivityList();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });
    }

    private void initFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment
            implements Preference.OnPreferenceClickListener {
        private Resources mResources;
        private Preference mCleanCachePre;
        private Preference mFeedbackPre;
        private Preference mCheckUpdatePre;
        private Preference mAboutPre;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            initPreference();
        }

        private void initPreference() {
            mResources = getResources();
            addPreferencesFromResource(R.xml.preference);
            //mCleanCachePre = findPreference(mResources.getString(R.string.text_clean_cache));
            //mFeedbackPre = findPreference(mResources.getString(R.string.text_feedback));
            mCheckUpdatePre = findPreference(mResources.getString(R.string.text_check_update));
            mCheckUpdatePre.setOnPreferenceClickListener(this);
            mAboutPre = findPreference(getString(R.string.text_about));
            mAboutPre.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (getString(R.string.text_check_update).equals(key)) {
                checkUpdate();
            } else if (getString(R.string.text_about).equals(key)) {
                UIUtils.showToast(getActivity(), Utils.getVersionName());
            }
            return false;
        }

        private void checkUpdate() {
            FIR.checkForUpdateInFIR(FIR_TOKEN, new VersionCheckCallback() {
                @Override
                public void onSuccess(String versionJson) {
                    AppVersionInfoRes appVersionInfo =
                            new Gson().fromJson(versionJson, AppVersionInfoRes.class);
                    String firVersion = appVersionInfo.getVersionShort().replaceAll("[.]", "");
                    String currentVersion = BuildConfig.VERSION_NAME.replaceAll("[.]", "");
                    if (Integer.parseInt(firVersion) > Integer.parseInt(currentVersion)) {
                        showAppUpdateDialog(appVersionInfo);
                    } else {
                        UIUtils.showOkDialog(getActivity(), getString(R.string.title_check_upate),
                                getString(R.string.text_not_need_update));
                    }
                }

                @Override
                public void onFail(Exception exception) {
                    UIUtils.showToast(getActivity(), getString(R.string.text_check_update_fail));
                }

                @Override
                public void onStart() {
                    UIUtils.showToast(getActivity(), getString(R.string.text_checking_update));
                }

                @Override
                public void onFinish() {
                }
            });
        }

        private void showAppUpdateDialog(final AppVersionInfoRes appVersionInfo) {
            UIUtils.showDialog(getActivity(), getString(R.string.title_check_upate),
                    appVersionInfo.getChangelog(), new UIUtils.OnDialogListener() {
                        @Override
                        public void onConfirm() {
                            Intent serviceIntent =
                                    new Intent(getActivity(), DownLoadApkService.class);
                            serviceIntent.putExtra(DownLoadApkService.EXTRA_URL,
                                    appVersionInfo.getInstallUrl());
                            getActivity().startService(serviceIntent);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
    }
}
