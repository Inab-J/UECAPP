package net.inab_j.uecapp.controller.provider;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import net.inab_j.uecapp.controller.util.CacheManager;
import net.inab_j.uecapp.view.adapter.CalendarAdapter;
import net.inab_j.uecapp.view.widget.CalendarItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 学年暦をダウンロードし、パースをしてアダプターにセットする。
 * 学年暦ページのHTMLファイルは和暦ベースで作成されているため、西暦→和暦変換を行う。
 * 年号が変わる毎に、その年号の開始年、日本語と英語それぞれの年号の名前が必要となる。
 * @TODO 年号関係のパラメータをどこかから取得する？
 */
public class GetCalendarTask extends GetArrayHttpTask<CalendarAdapter> {

    private final int ERA_BEGIN = 1989;
    private final String CALENDAR_URL = "http://www.uec.ac.jp/campus/academic/calendar/";
    private final String ERA_NAME_JP = "平成";
    private final String ERA_NAME_EN = "heisei";
    private final String ENCODING = "UTF8";

    private Context mContext;

    public GetCalendarTask(CalendarAdapter adapter, ListView listView, Context context) {
        super(adapter, listView);
        mContext = context;
        setEncoding(ENCODING);
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        if (CacheManager.hasValidCache(mContext, CacheManager.sCALENDAR)) {
            return CacheManager.getCache(mContext, CacheManager.sCALENDAR);
        } else {
            return doGet(createCalendarURL());
        }
    }

    @Override
    protected void onPostExecute(List<String> result) {
        final int CALENDAR_DATE = 1;
        final int CALENDAR_EVENT = 2;
        final int CALENDAR_NOTE = 0;

        CacheManager.setCache(result, mContext, CacheManager.sCALENDAR);
        List<CalendarItem> calendarItemList = new ArrayList<>();

        int newYear = 0;  // 途中で"平成n年"だけの行が現れるためその後の補正用
        CalendarItem item = new CalendarItem();
        for (int i = 0; i < result.size(); i++) {
            Log.d("calendar", i + ": " + result.get(i));

            if (result.get(i).startsWith(ERA_NAME_JP)) {
                if (i != 0)
                    newYear = 1;
                item.isHeader(true);
                item.setYear(result.get(i));
                calendarItemList.add(item);
                item = new CalendarItem();
                continue;
            }

            switch ((i - newYear) % 3) {
                case CALENDAR_DATE:
                    // "平成n年"の行
                    String dateStr = replaceZenkakuHankaku(result.get(i));
                    item.setDate(replaceTilde(dateStr));
                    break;

                case CALENDAR_EVENT:
                    item.setEvent(replaceZenkakuHankaku(result.get(i)));
                    break;

                case CALENDAR_NOTE:
                    item.isRemark(result.get(i).length() >= 3);
                    calendarItemList.add(item);
                    item = new CalendarItem();
                    break;
            }
        }

        mAdapter.setCalendarList(calendarItemList);
        registerAdapter();
      }

    /**
     * 公式ページのHTMLファイル名 "(年号)(和暦)-1.html" を生成する。
     * @return HTMLファイル名を返す。
     */
    private String createCalendarURL() {
        Calendar cal = Calendar.getInstance();

        int jpEra;
        if (cal.get(Calendar.MONTH) < 4) {
            jpEra = cal.get(Calendar.YEAR) - ERA_BEGIN;
        } else {
            jpEra = cal.get(Calendar.YEAR) - ERA_BEGIN + 1;
        }

        return CALENDAR_URL + ERA_NAME_EN.substring(0, 1) + jpEra + "-1.html";
    }

    private String replaceTilde(String str) {
        return str.replace("から", "〜");
    }
}
