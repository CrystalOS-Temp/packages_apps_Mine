/*
 * Copyright (C) 2020 Project-Awaken
 * Copyright (C) 2021 CrystalOS-Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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

import com.android.internal.logging.nano.MetricsProto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class DesignCorner extends SettingsPreferenceFragment {

    final static String TAG = "MonetSettings";

    private static final String PREF_CUSTOM_COLOR = "monet_engine_custom_color";
    private static final String PREF_COLOR_OVERRIDE = "monet_engine_color_override";
    private static final String PREF_CHROMA_FACTOR = "monet_engine_chroma_factor";
    private static final String PREF_ACCURATE_SHADES = "monet_engine_accurate_shades";
    private static final String PREF_LINEAR_LIGHTNESS = "monet_engine_linear_lightness";
    private static final String PREF_WHITE_LUMINANCE = "monet_engine_white_luminance_user";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.category_designcorner);
        PreferenceScreen prefSet = getPreferenceScreen();
        final Resources res = getResources();
        final PreferenceScreen prefScreen = getPreferenceScreen();
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        Settings.Secure.putIntForUser(resolver,
                PREF_CUSTOM_COLOR, 0, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                PREF_COLOR_OVERRIDE, 0xffffffff, UserHandle.USER_CURRENT);
        Settings.Secure.putFloatForUser(resolver,
                PREF_CHROMA_FACTOR, 100.0f, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                PREF_ACCURATE_SHADES, 1, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                PREF_LINEAR_LIGHTNESS, 0, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                PREF_WHITE_LUMINANCE, 425, UserHandle.USER_CURRENT);
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
}