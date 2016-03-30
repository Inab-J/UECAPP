package net.inab_j.uecapp.view.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.inab_j.uecapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SportsFragment extends Fragment {

    private WebView mWebView;

    public SportsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SportsFragment.
     */
    public static SportsFragment newInstance() {
        SportsFragment fragment = new SportsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (!isConnectable(getActivity().getApplicationContext())) {
            getActivity().findViewById(R.id.progressbar).setVisibility(View.GONE);
            getActivity().findViewById(R.id.cancel_error_msg).setVisibility(View.VISIBLE);
            return null;
        }

        mWebView = new WebView(getActivity());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("dbg", "started");
                try {
                    getActivity().findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("dbg", "finished");
                try {
                    getActivity().findViewById(R.id.progressbar).setVisibility(View.GONE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        // check cache date

        mWebView.loadUrl("http://sport.edu.uec.ac.jp/info/");

        return mWebView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mWebView.stopLoading();
        mWebView.destroy();
    }

    private boolean isConnectable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null) {
            return info.isConnected();
        } else {
            return false;
        }
    }
}
