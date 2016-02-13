package net.inab_j.uecapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.inab_j.uecapp.controller.provider.GetCancelTask;
import net.inab_j.uecapp.R;
import net.inab_j.uecapp.view.adapter.CancelAdapter;

public class CancelActivity extends AppCompatActivity {

    private CancelAdapter mCancelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_cancel));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCancelAdapter = new CancelAdapter(getApplicationContext());
        ListView lv = (ListView) findViewById(R.id.content_list_view);
        GetCancelTask task = new GetCancelTask(mCancelAdapter, lv, this);
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

    /**
     * 休講情報がなかった場合、GetCancelTaskから呼び出して、メッセージを表示する。
     */
    public void setVisible() {
        ((ViewGroup) findViewById(R.id.test)).getChildAt(1).setVisibility(View.VISIBLE);
    }
}
