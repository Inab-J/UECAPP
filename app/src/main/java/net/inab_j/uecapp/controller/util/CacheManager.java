package net.inab_j.uecapp.controller.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * キャッシュの読み書きを行う。
 */
public final class CacheManager {

    public static final int sCALENDAR = 0;
    public static final int sCANCEL = 1;
    public static final int sLIBRARY = 2;

    private CacheManager() {  }

    /**
     * キャッシュを読み込み、Listで返す。
     * @param context 使用するContext
     * @param cacheType キャッシュのタイプ
     * @return キャッシュファイルが存在すれば{@code ArrayList<String>}、しなければnullを返す。
     */
    public static List<String> getCache(Context context, int cacheType) {


        FileInputStream input = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            input = new FileInputStream(getCacheFile(context, cacheType));
            reader = new InputStreamReader(input);
            br = new BufferedReader(reader);

            List<String> cacheData = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                cacheData.add(line);
            }

            return cacheData;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("dbg", "file not found:" + e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (br != null) try { br.close(); } catch (IOException e) {
                e.printStackTrace();
            }
            if (input != null) try { input.close(); } catch (IOException e) {
                e.printStackTrace();
            }
            if (reader != null) try { reader.close(); } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Sets the cache data to cache file in cache directory.
     * @param addData New cache data.
     * @param context The context to use.
     * @param cacheType The type of cache to update.
     */
    public static void setCache(List<String> addData, Context context, int cacheType) {
        File file = getCacheFile(context, cacheType);

        FileOutputStream output = null;
        PrintWriter pw = null;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            output = new FileOutputStream(file);
            pw = new PrintWriter(output);

            for (String line: addData) {
                pw.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (pw != null) pw.close();

            if (output != null) try { output.close(); } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * キャッシュファイルの存在確認をして、有効なキャッシュであるか検証する。
     * @param context 使用するコンテキスト
     * @param cacheType キャッシュのタイプ
     * @return 有効なキャッシュが存在すれば{@code true}、しでなければ{@code false}を返す。
     */
    public static boolean hasValidCache(Context context, int cacheType) {
        /** cacheTypeのキャッシュが存在するかチェックする。*/
        File file = getCacheFile(context, cacheType);

        if (file.exists()) {
            Calendar currentDate = Calendar.getInstance();
            Calendar cacheDate = Calendar.getInstance();
            cacheDate.setTime(new Date(file.lastModified()));

            switch (cacheType) {
                // 最終キャッシュが5分以内なら true
                // 5分以内のアクセスはさせない
                case sCANCEL:
                    cacheDate.add(Calendar.MINUTE, 5);
                    if (currentDate.before(cacheDate)) {
                        return true;
                    } else {
                        return false;
                    }

                // 最終キャッシュから月が変わっていれば true
                // 同じ月でアクセスさせない
                case sLIBRARY:
                    int currentMonth = currentDate.get(Calendar.MONTH);
                    int cacheMonth = cacheDate.get(Calendar.MONTH);

                    if (currentMonth == cacheMonth) {
                        return true;
                    } else {
                        return false;
                    }

                // 1日以内のアクセスならtrue(キャッシュ有り)
                // 同一日にアクセスは1度のみ
                case sCALENDAR:
                    int currentDay = currentDate.get(Calendar.DAY_OF_YEAR);
                    int cacheDay = cacheDate.get(Calendar.DAY_OF_YEAR);

                    if (currentDay == cacheDay) {
                        return true;
                    } else {
                        return true;
                    }

                default:
                    return false;
            }

        } else {
            return false;
        }
    }

    /**
     * 指定されたキャッシュのFileオブジェクトを返す。
     * @param context 使用するContext
     * @param cacheType キャッシュのタイプ
     * @return キャッシュのファイルオブジェクトを返す。
     */
    private static File getCacheFile(Context context, int cacheType) {
        File file;

        switch (cacheType) {
            case sCALENDAR:
                file = new File(context.getCacheDir(), "cache_calendar.dat");
                break;

            case sCANCEL:
                file = new File(context.getCacheDir(), "cache_cancel.dat");
                break;

            case sLIBRARY:
                file = new File(context.getCacheDir(), "cache_library.dat");
                break;

            default:
                file = null;
        }

        return file;
    }
}
