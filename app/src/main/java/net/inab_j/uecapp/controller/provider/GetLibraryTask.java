package net.inab_j.uecapp.controller.provider;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import net.inab_j.uecapp.controller.util.CacheManager;
import net.inab_j.uecapp.view.widget.CalendarView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 公式ページからダウンロードするための基底クラス。
 * 非同期通信を行い、ダウンロードしたHTMLをパースして、アダプターにセットする。
  */
public class GetLibraryTask extends AsyncTask<Void, Void, List<Integer>>{

    private final String URL = "http://limefront.cc.uec.ac.jp/modules/news/index.php?caldate=";
    public static final int sOPEN = 0;
    public static final int sSATURDAY = 1;
    public static final int sCLOSED = 2;
    public static final int sSHORTEND = 3;
    public static final int sEXTRA = 4;
    public static final int sPREV_OR_NEXT = -1;

    private Context mContext;
    private CalendarView mCalendarView;

    /**
     * コンストラクタ
     */
    public GetLibraryTask(Context context, CalendarView calendarView) {
        mContext = context;
        mCalendarView = calendarView;
    }

    @Override
    protected List<Integer> doInBackground(Void... params) {
        if (CacheManager.hasValidCache(mContext, CacheManager.sLIBRARY)) {
            List<String> tmp = CacheManager.getCache(mContext, CacheManager.sLIBRARY);
            List<Integer> cache = new ArrayList<>();
            for (String cacheItem: tmp) {
                cache.add(Integer.valueOf(cacheItem));
            }

            return cache;
        } else {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Log.d("library", "http");
            return doGet(URL + sdf.format(cal.getTime()));

        }
    }

    @Override
    protected void onPostExecute(List<Integer> result) {
        super.onPostExecute(result);
        CacheManager.setCache(result, mContext, CacheManager.sLIBRARY);
        Calendar cal = Calendar.getInstance();

        mCalendarView.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), result);
    }

    private List<Integer> doGet(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("tbody td");

            int i = 0;
            List<Integer> openInfo = new ArrayList<>();
            for (Element elem: elements) {
                String open = elem.attr("class");
                if (parseOpenInfo(open) != sPREV_OR_NEXT) {
                    openInfo.add(parseOpenInfo(open));
                }
            }

            return openInfo;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int parseOpenInfo(String classAttr) {
        String[] open = classAttr.split(" ");

        if (open.length == 1) {
            return sOPEN;
        } else {
            switch (open[1]) {
                case "onsaturday":
                    return sSATURDAY;

                case "closed":
                    return sCLOSED;

                case "shortened":
                    return sSHORTEND;

                case "extra":
                    return sEXTRA;

                default:
                    return sPREV_OR_NEXT;
            }
        }
    }
}
