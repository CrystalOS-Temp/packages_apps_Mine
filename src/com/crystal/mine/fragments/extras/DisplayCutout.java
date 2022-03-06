/*
 * Copyright (C) 2016-2022 crDroid Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crystal.mine.fragments.extras;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

@SearchIndexable
public class DisplayCutout extends SettingsPreferenceFragment {

    public static final String TAG = "DisplayCutout";
    private static final String KEY_FORCE_FULL_SCREEN = "display_cutout_force_fullscreen_settings";

    private Preference mShowCutoutForce;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.category_miscellaneous);
        PreferenceScreen prefSet = getPreferenceScreen();
        Context mContext = getActivity().getApplicationContext();

        final String displayCutout = mContext.getResources().getString(com.android.internal.R.string.config_mainBuiltInDisplayCutout);
        if (TextUtils.isEmpty(displayCutout)) {
            mShowCutoutForce = (Preference) findPreference(KEY_FORCE_FULL_SCREEN);
            prefSet.removePreference(mShowCutoutForce);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CRYSTAL;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider(R.xml.category_miscellaneous);
}