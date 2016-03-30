package net.inab_j.uecapp.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import net.inab_j.uecapp.R;
import net.inab_j.uecapp.controller.util.CacheManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener{

        private final String BELONG_PREF_KEY = "pref_user_belong";
        private String[] mBelongString;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mBelongString = getResources().getStringArray(R.array.pref_belong_list);
            addPreferencesFromResource(R.xml.preferences);

            // 所属が登録されていればセット
            SharedPreferences pref = getPreferenceManager().getSharedPreferences();
            findPreference(BELONG_PREF_KEY).setSummary(
                    mBelongString[Integer.parseInt(pref.getString(BELONG_PREF_KEY, "所属"))]
            );
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(BELONG_PREF_KEY)) {
                CacheManager.clearAllCache(getActivity().getApplicationContext());
                findPreference(key).setSummary(
                        mBelongString[Integer.parseInt(sharedPreferences.getString(key, "所属"))]);
            }
        }
    };

}
