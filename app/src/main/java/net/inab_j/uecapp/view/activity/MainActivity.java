package net.inab_j.uecapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import net.inab_j.uecapp.R;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewLayout = (LinearLayout) findViewById(R.id.root);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onMainClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.calendar_btn:
                Snackbar.make(mViewLayout, "calendar", Snackbar.LENGTH_SHORT).show();
                i = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(i);
                break;

            case R.id.timetable_btn:
                Snackbar.make(mViewLayout, "timetable", Snackbar.LENGTH_SHORT).show();
                i = new Intent(MainActivity.this, TimeTableActivity.class);
                startActivity(i);
                break;

            case R.id.cancel_btn:
                Snackbar.make(mViewLayout, "cancel", Snackbar.LENGTH_SHORT).show();
                i = new Intent(MainActivity.this, CancelActivity.class);
                startActivity(i);
                break;

            case R.id.library_btn:
                Snackbar.make(mViewLayout, "library", Snackbar.LENGTH_SHORT).show();
                i = new Intent(MainActivity.this, LibraryActivity.class);
                startActivity(i);
                break;

            case R.id.link_btn:
                i = new Intent(MainActivity.this, LinkActivity.class);
                startActivity(i);
                break;

            case R.id.mytimetable_btn:
                i = new Intent(MainActivity.this, MyTimeTableActivity.class);
                startActivity(i);
                break;
        }
    }
}
