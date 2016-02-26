package net.inab_j.uecapp.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.inab_j.uecapp.R;
import net.inab_j.uecapp.view.widget.MyTimeTableView;

/**
 * My時間割の処理を行う。
 */
public class MyTimeTableActivity extends AppCompatActivity
        implements View.OnLongClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String[] DAY_NAME = {"月曜", "火曜", "水曜", "木曜", "金曜", "土曜"};
    public static final String SHARED_PREF_TAG = "mytimetable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_time_table);
        ((MyTimeTableView) findViewById(R.id.content_mytime)).createView(this);
        getSharedPreferences(SHARED_PREF_TAG, MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * 長押しした時に、ダイアログを表示する。
     */
    @Override
    public boolean onLongClick(View v) {
        ClassDialog dialog = ClassDialog.newInstance(v.getTag().toString());
        dialog.show(getFragmentManager(), "edit");
        Log.d("dbg", v.getTag().toString());
        return true;
    }

    /**
     * SharedPreferencesに変更があったら、時間割の表示を変更する。
     * @param sharedPreferences 変更されたSharedPreferences
     * @param key 変更されたキー
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("dbg", key);
        ((TextView) findViewById(Integer.parseInt(key))).setText(sharedPreferences.getString(key, ""));
    }

    /**
     * 削除確認ダイアログを生成するクラス
     */
    public static class DeleteConfirmDialog extends DialogFragment {
        /**
         * インスタンスを生成する。
         * @return ダイアログフラグメントを返す。
         */
        public static DeleteConfirmDialog newInstance() {
            return new DeleteConfirmDialog();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final SharedPreferences sharedPref =
                    getActivity().getSharedPreferences(SHARED_PREF_TAG, MODE_PRIVATE);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("削除してよろしいですか")
                    .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String posName = getArguments().getString("table_pos");
                            sharedPref.edit().remove(posName).apply();
                            dismiss();
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getFragmentManager().popBackStack();
                        }
                    });
            return builder.create();
        }
    }

    /**
     * 授業の詳細を表示するクラス。
     */
    public static class ClassDialog extends DialogFragment {

        /**
         * ダイアログのインスタンスを生成する。
         * @param tablePosition 時間割のセルの名前
         * @return fragment dialog instance
         */
        public static ClassDialog newInstance(String tablePosition) {
            ClassDialog fragment = new ClassDialog();
            Bundle args = new Bundle();
            args.putString("table_pos", tablePosition);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final SharedPreferences sharedPref =
                    getActivity().getSharedPreferences(SHARED_PREF_TAG, MODE_PRIVATE);
            final String posName = getArguments().getString("table_pos");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // set view
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View content = inflater.inflate(R.layout.dialog_edit, null);
            ((EditText) content.findViewById(R.id.subject_edit)).setText(sharedPref.getString(posName, ""));
            builder.setView(content);

            // set buttons
            int day = Integer.parseInt(posName.substring(0,1));
            String message = DAY_NAME[day - 1] + posName.charAt(1) + "限";
            builder.setMessage(message)
                    .setPositiveButton("登録", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPref.edit()
                                    .putString(posName,
                                            ((EditText)content.findViewById(R.id.subject_edit)).getText().toString())
                                    .apply();
                            dismiss();
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });
            // 登録済みの時間割データがあれば削除ボタンを追加
            if (!sharedPref.getString(posName, "").equals("")) {
                builder.setNeutralButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteConfirmDialog confirmDialog = new DeleteConfirmDialog();
                        confirmDialog.show(getFragmentManager(), "confirm");

                        Bundle pos = new Bundle();
                        pos.putString("table_pos", posName);
                        confirmDialog.setArguments(pos);

                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction()
                                .remove(fm.findFragmentByTag("edit"))
                                .show(confirmDialog)
                                .addToBackStack("edit")
                                .commit();
                    }
                });
            }
            return builder.create();
        }
    }
}
