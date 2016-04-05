package net.inab_j.uecapp.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import net.inab_j.uecapp.R;

public class LinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onLinkClick(View v) {
        final String UEC = "http://www.uec.ac.jp";
        final String CAMPUS_WEB = "https://campusweb.office.uec.ac.jp/campusweb/";
        final String AT_UNIV = "http://uec.atuniv.jp/";
        final String WEBCLASS = "https://webclass.cdel.uec.ac.jp/";
        final String COOP = "http://www.univcoop.jp/uec/";
        final String INFORMATION = "https://www.cc.uec.ac.jp";

        Intent i = null;
        switch (v.getId()) {
            case R.id.uec:
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(UEC));
                break;

            case R.id.campus_web:
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(CAMPUS_WEB));
                break;

            case R.id.at_univ:
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(AT_UNIV));
                break;

            case R.id.webclass:
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(WEBCLASS));
                break;

            case R.id.coop:
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(COOP));
                break;

            case R.id.information:
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(INFORMATION));
                break;
        }

        startActivity(i);
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
