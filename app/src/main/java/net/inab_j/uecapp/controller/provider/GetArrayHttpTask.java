package net.inab_j.uecapp.controller.provider;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 公式ページからダウンロードするための基底クラス。
 * 非同期通信を行い、ダウンロードしたHTMLをパースして、アダプターにセットする。
 * @param <T> BaseAdapterを継承したリスト用アダプター
 */
public class GetArrayHttpTask<T extends BaseAdapter> extends AsyncTask<Void, Void, List<String>>{

    private String mEncoding;
    private ListView mListView;
    protected T mAdapter;

    /**
     * コンストラクタ
     * @param adapter ListViewと紐付けるアダプターのインスタンス
     * @param listView 表示するListView
     */
    public GetArrayHttpTask(T adapter, ListView listView) {
        mAdapter = adapter;
        mListView = listView;
    }

    /**
     * HTML取得時のエンコード設定を行う。
     * ページによりエンコード指定がないため、ページごとに指定を行う。
     * エンコード形式は継承先のprivate定数としてもたせる。
     * @param encoding 使用するエンコード形式
     */
    protected void setEncoding(String encoding) {
        mEncoding = encoding;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(List<String> result) {
        super.onPostExecute(result);
    }

    /**
     * HTMLを取得し、{@code <td>}タグでパースする。
     * @param url 取得するURL
     * @return パースしたデータをArrayListで返す。
     */
    protected List<String> doGet(String url) {
        Log.d("dbg", "http");
        String html = null;
        try {
            html = getHtml(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("td");

        List<String> data = new ArrayList<>();
        for (Element elem: elements) {
            data.add(elem.text());
        }

        return data;
    }

    /**
     * Download HTML
     * @return ダウンロードしたHTMLの文字列を返す。
     * @throws IOException
     */
    private String getHtml(String getUrl) throws IOException{

        URL url = new URL(getUrl);
        Log.d("dbg", url.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), mEncoding));

        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        return sb.toString();
    }

    /**
     * 全角のスペースとカッコを半角に直した文字列を返す。
     * @param str 変換する文字列。全角スペースやカッコを含まないものでも良い。
     * @return 半角に変換した文字列
     */
    protected String replaceZenkakuHankaku(String str) {
        String wspace = String.valueOf('\u3000');  // 全角スペース
        String left = String.valueOf('\uff08');    // 全角左カッコ
        String right = String.valueOf('\uff09');   // 全角右カッコ

        return str.replaceAll(left, "(").replaceAll(right, ")").replaceAll(wspace, " ");
    }

    /**
     * ListViewにアダプターをセットする。
     */
    protected void registerAdapter() {
        mListView.setAdapter(mAdapter);
    }
}
