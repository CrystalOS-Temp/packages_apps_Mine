/*
 * Copyright (C) 2020-2022 CrystalOS-Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crystal.mine.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.provider.SearchIndexableResource;

import androidx.preference.PreferenceCategory;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import com.android.internal.logging.nano.MetricsProto;

import com.android.internal.crystal.utils.CrystalUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class Notifications extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String INCALL_VIB_OPTIONS = "incall_vib_options";
    private static final String ALERT_SLIDER_PREF = "alert_slider_notifications";
    private static final String SMS_BREATH = "sms_breath";
    private static final String MISSED_CALL_BREATH = "missed_call_breath";
    private static final String VOICEMAIL_BREATH = "voicemail_breath";

    protected Context mContext;
    private Preference mAlertSlider;
    private SwitchPreference mSmsBreath;
    private SwitchPreference mMissedCallBreath;
    private SwitchPreference mVoicemailBreath;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.category_notifications);
        PreferenceScreen prefSet = getPreferenceScreen();
        final Resources res = getResources();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        mContext = getActivity().getApplicationContext();
        ContentResolver resolver = getActivity().getContentResolver();

        // Breathing Notifications
        mSmsBreath = (SwitchPreference) findPreference(SMS_BREATH);
        mMissedCallBreath = (SwitchPreference) findPreference(MISSED_CALL_BREATH);
        mVoicemailBreath = (SwitchPreference) findPreference(VOICEMAIL_BREATH);

        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            mSmsBreath.setChecked(Settings.Global.getInt(resolver,
                    Settings.Global.KEY_SMS_BREATH, 1) == 1);
            mSmsBreath.setOnPreferenceChangeListener(this);

            mMissedCallBreath.setChecked(Settings.Global.getInt(resolver,
                    Settings.Global.KEY_MISSED_CALL_BREATH, 1) == 1);
            mMissedCallBreath.setOnPreferenceChangeListener(this);

            mVoicemailBreath.setChecked(Settings.Global.getInt(resolver,
                    Settings.Global.KEY_VOICEMAIL_BREATH, 1) == 1);
            mVoicemailBreath.setOnPreferenceChangeListener(this);
        } else {
            prefScreen.removePreference(mSmsBreath);
            prefScreen.removePreference(mMissedCallBreath);
            prefScreen.removePreference(mVoicemailBreath);
        }

        PreferenceCategory incallVibCategory = (PreferenceCategory) findPreference(INCALL_VIB_OPTIONS);
        if (!CrystalUtils.isVoiceCapable(getActivity())) {
                prefScreen.removePreference(incallVibCategory);
        }

        mAlertSlider = (Preference) prefSet.findPreference(ALERT_SLIDER_PREF);
        boolean mAlertSliderAvailable = res.getBoolean(
                com.android.internal.R.bool.config_hasAlertSlider);
        if (!mAlertSliderAvailable)
            prefSet.removePreference(mAlertSlider);
        
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mSmsBreath) {
            boolean value = (Boolean) newValue;
            Settings.Global.putInt(getContentResolver(), SMS_BREATH, value ? 1 : 0);
            return true;
        } else if (preference == mMissedCallBreath) {
            boolean value = (Boolean) newValue;
            Settings.Global.putInt(getContentResolver(), MISSED_CALL_BREATH, value ? 1 : 0);
            return true;
        } else if (preference == mVoicemailBreath) {
            boolean value = (Boolean) newValue;
            Settings.Global.putInt(getContentResolver(), VOICEMAIL_BREATH, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CRYSTAL;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(
                        Context context, boolean enabled) {
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.category_notifications;
                    return Arrays.asList(sir);
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
            };
} 
