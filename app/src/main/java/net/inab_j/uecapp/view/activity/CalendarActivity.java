package net.inab_j.uecapp.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_calendar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lv = (ListView) findViewById(R.id.content_list_view);
        lv.setOnItemClickListener(this);
        mCalendarAdapter = new CalendarAdapter(getApplicationContext());
        GetCalendarTask task = new GetCalendarTask(mCalendarAdapter, lv, this);
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
        CalendarItem item = (CalendarItem) parent.getItemAtPosition(position);
        RemarkDialog remarkDialog = RemarkDialog.newInstance(
                item.getDate(), item.getEvent(), item.getNote());
        remarkDialog.show(getFragmentManager(), "remark");
    }

    public void showProgress() {
        ((ContentLoadingProgressBar) findViewById(R.id.progressbar_list)).show();
    }

    public void hideProgress() {
        ((ContentLoadingProgressBar) findViewById(R.id.progressbar_list)).hide();
    }

    public static class RemarkDialog extends DialogFragment {

        public static RemarkDialog newInstance(String date, String event, String note) {
            RemarkDialog fragment = new RemarkDialog();
            Bundle args = new Bundle();
            args.putString("date", date);
            args.putString("event", event);
            args.putString("note", note);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] dates = getArguments().getString("date").split("、");
            String event = getArguments().getString("event");
            String note = getArguments().getString("note");

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View content = inflater.inflate(R.layout.dialog_calendar_detail, null);

            String dateStr = "";
            for (String date: dates) {
                dateStr += date.trim() + "\n";
            }
            ((TextView) content.findViewById(R.id.calendar_date)).setText(dateStr.trim());
            if (note.length() > 0) {
                ((TextView) content.findViewById(R.id.calendar_note)).setText(note);
                (content.findViewById(R.id.calendar_note_container)).setVisibility(View.VISIBLE);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(event);
            builder.setView(content);
            return builder.create();
        }
    }
}
