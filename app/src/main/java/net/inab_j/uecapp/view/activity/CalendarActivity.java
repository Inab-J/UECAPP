package net.inab_j.uecapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.inab_j.uecapp.controller.provider.GetCalendarTask;
import net.inab_j.uecapp.R;
import net.inab_j.uecapp.view.adapter.CalendarAdapter;

public class CalendarActivity extends AppCompatActivity {

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
}
