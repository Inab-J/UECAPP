package net.inab_j.uecapp.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import net.inab_j.uecapp.controller.provider.GetPDFTask;
import net.inab_j.uecapp.R;
import net.inab_j.uecapp.model.System.IdCorrespond;

import java.io.File;
import java.util.Map;

public class TimeTableActivity extends AppCompatActivity {

    private final String SAVE_DIR = Environment.getExternalStorageDirectory().getPath() + "/UECApp/";
    private CoordinatorLayout mCoordinatorLayout;
    private int mBelong;
    private int mMajor;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        mBelong = Integer.parseInt(pref.getString("pref_user_belong", "0"));
        mMajor = Integer.parseInt(pref.getString("pref_user_major", "-1"));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_timetable);

    }

    public void onBtnClick(View v) {
        Log.d("dbg", IdCorrespond.ID_TO_NAME.get(v.getId()));
        File file = new File(SAVE_DIR + IdCorrespond.ID_TO_NAME.get(v.getId()));
        /*
        TODO: IdCorrespondを大学院の前後期に対応させる
        TODO: 対応させたら表示しているページに対応したものでファイル名に変換する
        */
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "application/pdf");
            startActivity(intent);
        } else {
            new GetPDFTask(this, IdCorrespond.ID_TO_NAME.get(v.getId())).execute();
            CreateSnackbar(getString(R.string.timet_getting));
        }
    }

    public void onGraduateBtnClick(View v) {
        Log.d("dbg", IdCorrespond.ID_TO_NAME_GRADUATE.get(v.getId()));
        String filename = IdCorrespond.ID_TO_NAME_GRADUATE.get(v.getId());
        if (!filename.endsWith("pdf")) {
            switch (mViewPager.getCurrentItem()) {
                case 0:
                    filename += "zen.pdf";
                    break;
                case 1:
                    filename += "kou.pdf";
                    break;
            }
        }

        Log.d("dbg", filename);
        File file = new File(SAVE_DIR + filename);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "application/pdf");
            startActivity(intent);
        } else {
            new GetPDFTask(this, filename).execute();
            CreateSnackbar(getString(R.string.timet_getting));
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_BELONG = "user_belong";
        private static final String ARG_MAJOR = "user_major";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, int belong, int major) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_BELONG, belong);
            args.putInt(ARG_MAJOR, major);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            if (getArguments().getInt(ARG_BELONG) < 3) {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                    rootView = inflater.inflate(R.layout.fragment_day_first, container, false);
                } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                    rootView = inflater.inflate(R.layout.fragment_day_second, container, false);
                } else {
                    rootView = inflater.inflate(R.layout.fragment_night, container, false);
                }
            } else {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                    rootView = inflater.inflate(R.layout.fragment_ie, container, false);
                } else {
                    rootView = inflater.inflate(R.layout.fragment_ie, container, false);
                    ((TextView) rootView.findViewById(R.id.is)).setText(
                            getResources().getText(R.string.timet_is_2nd));
                    rootView.findViewById(R.id.master_1st_eng_after).setVisibility(View.GONE);
                    rootView.findViewById(R.id.master_1st_eng_before).setVisibility(View.GONE);
                }

                // IEの専攻が登録されていれば専攻別ボタンを表示
                int major = getArguments().getInt(ARG_MAJOR);
                for (int id: IdCorrespond.MAJOR_TO_BUTTON_ID.get(major)) {
                    rootView.findViewById(id).setVisibility(View.VISIBLE);
                }
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, mBelong, mMajor);
        }

        @Override
        public int getCount() {
            if (mBelong < 3) {
                return 3;
            } else {
                return 2;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (mBelong < 3) {
                switch (position) {
                    case 0:
                        return "昼間前期";
                    case 1:
                        return "昼間後期";
                    case 2:
                        return "夜間";
                }
            } else {
                switch (position) {
                    case 0:
                        return "前期";
                    case 1:
                        return "後期";
                }
            }
            return null;
        }
    }

    public void CreateSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
