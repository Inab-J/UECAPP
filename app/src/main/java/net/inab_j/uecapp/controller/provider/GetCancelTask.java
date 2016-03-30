package net.inab_j.uecapp.controller.provider;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import net.inab_j.uecapp.controller.util.CacheManager;
import net.inab_j.uecapp.view.activity.CancelActivity;
import net.inab_j.uecapp.view.adapter.CancelAdapter;
import net.inab_j.uecapp.view.fragment.CancelFragment;
import net.inab_j.uecapp.view.widget.CancelItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 休講情報ページのHTMLを取得し、パースしてその情報をUIに適用する。
 */
public class GetCancelTask extends GetArrayHttpTask<CancelAdapter> {

    private final String CANCEL_URL = "http://kyoumu.office.uec.ac.jp/kyuukou/kyuukou.html";
    private final String ENCODING = "SJIS";

    private CancelFragment mFragment;

    /**
     * コンストラクタ
     * @param adapter リストのアダプタ
     * @param listView 情報を表示するListView
     * @param fragment ListViewを管理するfragment
     */
    public GetCancelTask(CancelAdapter adapter, ListView listView, CancelFragment fragment) {
        super(adapter, listView, fragment.getActivity().getApplicationContext());
        mFragment = fragment;
        setEncoding(ENCODING);
        ((CancelActivity) mFragment.getActivity()).setProgressVisibility(View.VISIBLE);
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

        if (result == null) {
            ((CancelActivity) mFragment.getActivity()).setErrorMsgVisibility(View.VISIBLE);
            ((CancelActivity) mFragment.getActivity()).setProgressVisibility(View.GONE);
            printError();
            return;
        }

        CacheManager.setCache(result, mContext, CacheManager.sCANCEL);
        List<CancelItem> cancelItemList = new ArrayList<>();

        CancelItem item = new CancelItem();
        for (int i = 6; i < result.size(); i++) {
            switch (i % 6) {
                case CANCEL_CLASS:
                    item.setClassname(result.get(i));
                    break;

                case CANCEL_DATE:
                    item.setDate(result.get(i));
                    break;

                case CANCEL_PERIOD:
                    item.setPeriod(result.get(i));
                    break;

                case CANCEL_SUBJECT:
                    item.setSubject(replaceZenkakuHankaku(result.get(i)));
                    break;

                case CANCEL_NOTE:
                    if (result.get(i).length() > 3) {
                        item.setNote(result.get(i));
                    }
                    cancelItemList.add(item);
                    item = new CancelItem();
                    break;
            }
        }

        mFragment.hideProgress();

        // 休講情報がなければ、対応するメッセージを表示する
        if (cancelItemList.size() == 0) {
            mFragment.setVisible();
        } else {
            mAdapter.setCancelList(cancelItemList);
            registerAdapter();
        }
    }
}

