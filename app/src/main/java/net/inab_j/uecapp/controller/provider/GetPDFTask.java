package net.inab_j.uecapp.controller.provider;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import net.inab_j.uecapp.view.activity.TimeTableActivity;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 時間割のPDFをダウンロードして保存し、Intentを発行する。
 */
public class GetPDFTask extends AsyncTask<Void, Void, Void> {

    private final String BASE_URL = "http://kyoumu.office.uec.ac.jp/timet/";
    public static final String SAVE_DIR = Environment.getExternalStorageDirectory().getPath() + "/UECApp/";

    private TimeTableActivity mActivity;
    private String mFileName;
    private boolean mIsNotFound = false;

    /**
     * コンストラクタ
     * @param activity 呼び出し元Activity
     * @param fileName 取得するPDFのファイル名
     */
    public GetPDFTask(TimeTableActivity activity, String fileName) {
        mActivity = activity;
        mFileName = fileName;
    }

    /**
     * 非同期でPDFを取得する。
     * 既にファイルがあるか、404エラーであればreturn.
     * @param params
     * @return null
     */
    @Override
    protected Void doInBackground(Void... params) {
        final int BUFFER_SIZE = 4096;

        checkDirectoryExists();
        try {
            URL url = new URL(BASE_URL + mFileName);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setInstanceFollowRedirects(true);
            con.connect();

            if (con.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                mIsNotFound = true;
                return null;
            }

            DataInputStream dataInStream = new DataInputStream(con.getInputStream());
            FileOutputStream fileOutStream = new FileOutputStream(new File(SAVE_DIR + mFileName));
            DataOutputStream dataOutStream
                    = new DataOutputStream(new BufferedOutputStream(fileOutStream));

            byte[] buffer = new byte[BUFFER_SIZE];
            int readByte = 0;
            while ((readByte = dataInStream.read(buffer)) != -1) {
                dataOutStream.write(buffer, 0, readByte);
            }

            dataInStream.close();
            dataOutStream.close();
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * PDFファイルが存在すれば表示するIntentを発行し、なければSnackBarで上表を表示する。
     * @param aVoid
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (mIsNotFound) {
            mActivity.CreateSnackbar("Not Found");
        } else {
            File file = new File(SAVE_DIR + mFileName);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "application/pdf");
            mActivity.getApplicationContext().startActivity(intent);
        }
    }

    /**
     * PDF保存用ディレクトリの存在確認をし、存在しなければ作成する。
     */
    private void checkDirectoryExists() {
        File file = new File(SAVE_DIR);

        if (!file.exists()) {
            file.mkdir();
            Log.d("dbg", "mkdir");
        }
    }
}
