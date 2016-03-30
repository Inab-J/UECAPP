package net.inab_j.uecapp.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.inab_j.uecapp.R;
import net.inab_j.uecapp.view.fragment.CancelFragment;
import net.inab_j.uecapp.view.fragment.SportsFragment;

import java.util.List;


public class CancelActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_cancel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_cancel));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_study_sports);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().getFragments().get(0);

                if (fragment.getTag().equals("sports")) {
                    fab.setImageResource(R.drawable.ic_directions_run_white_24dp);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.cancel_fragment_container, CancelFragment.newInstance(), "school")
                            .commit();
                    setProgressVisibility(View.VISIBLE);
                    setErrorMsgVisibility(View.GONE);
                } else {
                    fab.setImageResource(R.drawable.ic_school_white_24dp);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.cancel_fragment_container, SportsFragment.newInstance(), "sports")
                            .commit();
                    setProgressVisibility(View.VISIBLE);
                    setErrorMsgVisibility(View.GONE);
                }
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mErrorMsg = (TextView) findViewById(R.id.cancel_error_msg);

        if (savedInstanceState == null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.cancel_fragment_container, CancelFragment.newInstance(), "school");
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setProgressVisibility(int visibility) {
        mProgressBar.setVisibility(visibility);
    }

    public void setErrorMsgVisibility(int visibility) {
        mErrorMsg.setVisibility(visibility);
    }

}
