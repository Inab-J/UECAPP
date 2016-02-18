package net.inab_j.uecapp.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.inab_j.uecapp.controller.provider.GetCalendarTask;
import net.inab_j.uecapp.R;
import net.inab_j.uecapp.view.adapter.CalendarAdapter;
import net.inab_j.uecapp.view.widget.CalendarItem;
import net.inab_j.uecapp.view.widget.CancelItem;

public class CalendarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private CalendarAdapter mCalendarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_calendar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lv = (ListView) findViewById(R.id.content_list_view);
        lv.setOnItemClickListener(this);
        mCalendarAdapter = new CalendarAdapter(getApplicationContext());
        GetCalendarTask task = new GetCalendarTask(mCalendarAdapter, lv, getApplicationContext());
        task.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("calendar", ((CalendarItem) parent.getItemAtPosition(position)).getEvent());
        RemarkDialog remarkDialog = RemarkDialog.newInstance(
                ((CalendarItem) parent.getItemAtPosition(position)).getEvent());
        remarkDialog.show(getFragmentManager(), "remark");
    }

    public static class RemarkDialog extends DialogFragment {

        public static RemarkDialog newInstance(String remarkStr) {
            RemarkDialog fragment = new RemarkDialog();
            Bundle args = new Bundle();
            args.putString("remarks", remarkStr);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final String remarks = getArguments().getString("remarks");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(remarks);

            return builder.create();

        }
    }
}
