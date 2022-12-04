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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.development.SystemPropPoker;
import com.android.settingslib.search.SearchIndexable;

import com.android.internal.logging.nano.MetricsProto;

import com.crystal.mine.fragments.extras.SmartPixels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class Miscellaneous extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String SMART_PIXELS = "smart_pixels";
    private static final String KEY_GAMES_SPOOF = "use_games_spoof";
    private static final String KEY_PHOTOS_SPOOF = "use_photos_spoof";
    private static final String KEY_SYSTEM_BOOST = "system_boost";
    private static final String KEY_STREAM_SPOOF = "use_stream_spoof";

    private static final String SYS_GAMES_SPOOF = "persist.sys.pixelprops.games";
    private static final String SYS_PHOTOS_SPOOF = "persist.sys.pixelprops.gphotos";
    private static final String SYS_SYSTEM_BOOST = "persist.sys.system.boost";
    private static final String SYS_STREAM_SPOOF = "persist.sys.stream";
    private static final String SYS_RENDER_BOOST_THREAD = "persist.sys.perf.topAppRenderThreadBoost.enable";
    private static final String SYS_COMPACTION = "persist.sys.appcompact.enable_app_compact";
    private static final String SYS_BSERVICE_LIMIT = "persist.sys.fw.bservice_limit";
    private static final String SYS_BSERVICE_AGE = "persist.sys.fw.bservice_age";
    private static final String SYS_BSERVICE = "persist.sys.fw.bservice_enable";

    private Preference mSmartPixels;
    private SwitchPreference mGamesSpoof;
    private SwitchPreference mSystemBoost;
    private SwitchPreference mPhotosSpoof;
    private SwitchPreference mStreamSpoof;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.category_miscellaneous);
        PreferenceScreen prefScreen = getPreferenceScreen();

        mSmartPixels = (Preference) prefScreen.findPreference(SMART_PIXELS);
        boolean mSmartPixelsSupported = getResources().getBoolean(
                com.android.internal.R.bool.config_supportSmartPixels);
        if (!mSmartPixelsSupported)
            prefScreen.removePreference(mSmartPixels);

        mGamesSpoof = (SwitchPreference) prefScreen.findPreference(KEY_GAMES_SPOOF);
        mGamesSpoof.setChecked(SystemProperties.getBoolean(SYS_GAMES_SPOOF, false));
        mGamesSpoof.setOnPreferenceChangeListener(this);

        mPhotosSpoof = (SwitchPreference) prefScreen.findPreference(KEY_PHOTOS_SPOOF);
        mPhotosSpoof.setChecked(SystemProperties.getBoolean(SYS_PHOTOS_SPOOF, true));
        mPhotosSpoof.setOnPreferenceChangeListener(this);

        mSystemBoost = (SwitchPreference) findPreference(KEY_SYSTEM_BOOST);
        mSystemBoost.setChecked(SystemProperties.getBoolean(SYS_SYSTEM_BOOST, false));
        mSystemBoost.setOnPreferenceChangeListener(this);

        final String useStreamSpoof = SystemProperties.get(SYS_STREAM_SPOOF, "1");
        mStreamSpoof = (SwitchPreference) findPreference(KEY_STREAM_SPOOF);
        mStreamSpoof.setChecked("1".equals(useStreamSpoof));
        mStreamSpoof.setOnPreferenceChangeListener(this);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CRYSTAL;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mGamesSpoof) {
            boolean value = (Boolean) newValue;
            SystemProperties.set(SYS_GAMES_SPOOF, value ? "true" : "false");
            return true;
        } else if (preference == mPhotosSpoof) {
            boolean value = (Boolean) newValue;
            SystemProperties.set(SYS_PHOTOS_SPOOF, value ? "true" : "false");
            return true;
        } else if (preference == mSystemBoost) {
            boolean value = (Boolean) newValue;
            SystemProperties.set(SYS_SYSTEM_BOOST, value ? "true" : "false");
            SystemProperties.set(SYS_RENDER_BOOST_THREAD, value ? "true" : "false");
            SystemProperties.set(SYS_COMPACTION, value ? "false" : "true");
            SystemProperties.set(SYS_BSERVICE_LIMIT, value ? "8" : "15");
            SystemProperties.set(SYS_BSERVICE_AGE, value ? "8000" : "300000");
            SystemProperties.set(SYS_BSERVICE, value ? "true" : "false");
            SystemPropPoker.getInstance().poke();
            return true;
        } else if (preference == mStreamSpoof) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.USE_STREAM_SPOOF, value ? 1 : 0);
            SystemProperties.set(SYS_STREAM_SPOOF, value ? "1" : "0");
            Toast.makeText(getActivity(),
                    (R.string.stream_spoof_toast),
                    Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(
                        Context context, boolean enabled) {
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.category_miscellaneous;
                    return Arrays.asList(sir);
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);

                    boolean mSmartPixelsSupported = context.getResources().getBoolean(
                            com.android.internal.R.bool.config_supportSmartPixels);
                    if (!mSmartPixelsSupported)
                        keys.add(SMART_PIXELS);

                    return keys;
                }
            };
} 
