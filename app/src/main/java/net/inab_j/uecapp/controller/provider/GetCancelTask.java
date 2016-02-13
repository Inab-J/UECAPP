package net.inab_j.uecapp.controller.provider;

import android.content.Context;
import android.widget.ListView;

import net.inab_j.uecapp.controller.util.CacheManager;
import net.inab_j.uecapp.view.activity.CancelActivity;
import net.inab_j.uecapp.view.adapter.CancelAdapter;
import net.inab_j.uecapp.view.widget.CancelItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 休講情報ページのHTMLを取得し、パースしてその情報をUIに適用する。
 */
public class GetCancelTask extends GetArrayHttpTask<CancelAdapter> {

    private final String CANCEL_URL = "http://kyoumu.office.uec.ac.jp/kyuukou/kyuukou.html";
    private final String ENCODING = "SJIS";

    private Context mContext;
    private CancelActivity mActivity;

    /**
     * コンストラクタ
     * @param adapter リストのアダプタ
     * @param listView 情報を表示するListView
     * @param activity ListViewを管理するアクティビティ
     */
    public GetCancelTask(CancelAdapter adapter, ListView listView, CancelActivity activity) {
        super(adapter, listView);
        mContext = activity.getApplicationContext();
        mActivity = activity;
        setEncoding(ENCODING);
    }

    /**
     * 非同期で休講情報の取得を行う。
     * @param params
     * @return 有効なキャッシュが存在すればそのデータを、しなければダウンロードし、{@code List<String>}で返す。
     */
    @Override
    protected List<String> doInBackground(Void... params) {
        if (CacheManager.hasValidCache(mContext, CacheManager.sCANCEL)) {
            return CacheManager.getCache(mContext, CacheManager.sCANCEL);
        }
        return doGet(CANCEL_URL);
    }

    /**
     * 非同期で取得した休講情報をUIのListViewに適用する。
     * @param result 取得したデータ
     */
    @Override
    protected void onPostExecute(List<String> result) {
        final int CANCEL_CLASS = 0;
        final int CANCEL_DATE = 1;
        final int CANCEL_PERIOD = 2;
        final int CANCEL_SUBJECT = 3;
        final int CANCEL_NOTE = 5;

        CacheManager.setCache(result, mContext, CacheManager.sCANCEL);
        List<CancelItem> cancelItemList = new ArrayList<>();

        CancelItem item = new CancelItem();
        for (int i = 6; i < result.size(); i++) {
            switch (i % 6) {
                case CANCEL_CLASS:
                    item.set_classname(result.get(i));
                    break;

                case CANCEL_DATE:
                    item.set_date(result.get(i));
                    break;

                case CANCEL_PERIOD:
                    item.set_period(Integer.parseInt(result.get(i)));
                    break;

                case CANCEL_SUBJECT:
                    item.set_subject(replaceZenkakuHankaku(result.get(i)));
                    break;

                case CANCEL_NOTE:
                    cancelItemList.add(item);
                    item = new CancelItem();
                    break;
            }
        }

        // 休講情報がなければ、対応するメッセージを表示する
        if (cancelItemList.size() == 0) {
            mActivity.setVisible();
        } else {
            mAdapter.setCancelList(cancelItemList);
            registerAdapter();
        }
    }
}

