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

import com.android.internal.crystal.utils.CrystalUtils;
import com.android.internal.logging.nano.MetricsProto;

import com.crystal.mine.preferences.SystemSettingListPreference;
import com.crystal.mine.preferences.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class DesignCorner extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    // SETTINGS_LAYOUT_VARS
    private static final String ALT_SETTINGS_LAYOUT = "alt_settings_layout";
    private static final String SETTINGS_DASHBOARD_STYLE = "settings_dashboard_style";
    private static final String USE_STOCK_LAYOUT = "use_stock_layout";
    private static final String DISABLE_USERCARD = "disable_usercard";

    // settingsLayoutVars
    private SystemSettingListPreference mSettingsDashBoardStyle;
    private SystemSettingSwitchPreference mAltSettingsLayout;
    private SystemSettingSwitchPreference mUseStockLayout;
    private SystemSettingSwitchPreference mDisableUserCard; 

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.category_designcorner);
        PreferenceScreen prefSet = getPreferenceScreen();
        final Resources res = getResources();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        mSettingsDashBoardStyle = (SystemSettingListPreference) findPreference(SETTINGS_DASHBOARD_STYLE);
        mSettingsDashBoardStyle.setOnPreferenceChangeListener(this);
        mAltSettingsLayout = (SystemSettingSwitchPreference) findPreference(ALT_SETTINGS_LAYOUT);
        mAltSettingsLayout.setOnPreferenceChangeListener(this);
        mUseStockLayout = (SystemSettingSwitchPreference) findPreference(USE_STOCK_LAYOUT);
        mUseStockLayout.setOnPreferenceChangeListener(this);
        mDisableUserCard = (SystemSettingSwitchPreference) findPreference(DISABLE_USERCARD);
        mDisableUserCard.setOnPreferenceChangeListener(this);
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
                    sir.xmlResId = R.xml.category_designcorner;
                    return Arrays.asList(sir);
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
            };

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mSettingsDashBoardStyle) {
            CrystalUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mAltSettingsLayout) {
            CrystalUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mUseStockLayout) {
            CrystalUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mDisableUserCard) {
            CrystalUtils.showSettingsRestartDialog(getContext());
            return true;
        }
        return false;
    }
}
