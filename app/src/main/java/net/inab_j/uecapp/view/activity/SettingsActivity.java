package net.inab_j.uecapp.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import net.inab_j.uecapp.R;
import net.inab_j.uecapp.controller.util.CacheManager;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG_TIMETABLE = "timetable";
    private static final String TAG_MY_TIMETABLE = "mytimetable";

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
            implements SharedPreferences.OnSharedPreferenceChangeListener,
            Preference.OnPreferenceClickListener {

        private final String BELONG_PREF_KEY = "pref_user_belong";
        private final String MAJOR_PREF_KEY = "pref_user_major";
        private String[] mBelongString;
        private String[] mMajorString;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mBelongString = getResources().getStringArray(R.array.pref_belong_list);
            mMajorString = getResources().getStringArray(R.array.pref_major_list);
            addPreferencesFromResource(R.xml.preferences);

            // 所属が登録されていればセット
            SharedPreferences pref = getPreferenceManager().getSharedPreferences();
            int belong = Integer.parseInt(pref.getString(BELONG_PREF_KEY, "0"));
            findPreference(BELONG_PREF_KEY).setSummary(mBelongString[belong]);

            if (belong == 3) {
                Preference preference = findPreference(MAJOR_PREF_KEY);
                preference.setEnabled(true);
                int major = Integer.parseInt(pref.getString(MAJOR_PREF_KEY, "0"));
                preference.setSummary(mMajorString[major]);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
            findPreference(getString(R.string.pref_clear_timetable))
                    .setOnPreferenceClickListener(this);
            findPreference(getString(R.string.pref_clear_mytimetable))
                    .setOnPreferenceClickListener(this);
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
                int belong = Integer.parseInt(sharedPreferences.getString(key, "0"));
                findPreference(key).setSummary(mBelongString[belong]);
                findPreference(MAJOR_PREF_KEY).getEditor().remove(MAJOR_PREF_KEY).commit();

                if (belong == 3) {
                    findPreference(MAJOR_PREF_KEY).setEnabled(true);
                } else {
                    findPreference(MAJOR_PREF_KEY).setEnabled(false);
                }
            } else if (key.equals(MAJOR_PREF_KEY)) {
                int major = Integer.parseInt(sharedPreferences.getString(key, "0"));
                findPreference(key).setSummary(mMajorString[major]);
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            Log.d("dbg", preference.getKey());
            if (preference.getTitleRes() == R.string.pref_clear_timetable) {
                final DialogFragment fragment = ConfirmDialog.newInstance(this);
                fragment.show(getFragmentManager(), TAG_TIMETABLE);

            } else if (preference.getTitleRes() == R.string.pref_clear_mytimetable) {
                final DialogFragment fragment = ConfirmDialog.newInstance(this);
                fragment.show(getFragmentManager(), TAG_MY_TIMETABLE);

            }

            return false;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    };

    public static class ConfirmDialog extends DialogFragment {

        public static ConfirmDialog newInstance(Fragment fragment) {
            ConfirmDialog dialog = new ConfirmDialog();
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final String tag = getTag();
            return new AlertDialog.Builder(getActivity())
                    .setTitle("削除しますか？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (tag.equals(TAG_TIMETABLE)) {
                                CacheManager.clearTimetPDF(getActivity().getApplicationContext());
                            } else {
                                SharedPreferences pref = getActivity().getSharedPreferences(
                                        MyTimeTableActivity.SHARED_PREF_TAG, MODE_PRIVATE
                                );

                                pref.edit().clear().commit();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
        }
    }
}
