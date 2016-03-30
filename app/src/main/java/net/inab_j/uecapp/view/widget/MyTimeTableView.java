package net.inab_j.uecapp.view.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.inab_j.uecapp.R;
import net.inab_j.uecapp.view.activity.MyTimeTableActivity;

import java.util.Arrays;
import java.util.List;

/**
  * アプリ設定で入力された所属情報を元に、My時間割のViewを生成する。
 */

public class MyTimeTableView extends LinearLayout {

    // Belong check number
    private static final int FACULTY_DAY = 0;
    private static final int FACULTY_NIGHT = 1;
    private static final int FACULTY_AREA = 2;
    // layout
    private final int BORDER_WEIGHT = 1;

    // Table Header String
    private List<String> DAY_NAME =
            Arrays.asList(getResources().getStringArray(R.array.day_name));
    private List<String> PERIOD_START =
            Arrays.asList(getResources().getStringArray(R.array.period_start));

    // Row parameters
    private int mRowCount;
    private int mColumnCount;
    private LinearLayout.LayoutParams mRowParam;
    private LinearLayout.LayoutParams mRowItemParam;
    private LayerDrawable mCellStroke;
    private int mRippleResourceId;

    private SharedPreferences mSharedPref;
    private Context mContext;
    private MyTimeTableActivity mActivity;

    /**
     * コンストラクタ
     * @param context 使用するContext
     * @param attrs
     */
    public MyTimeTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSharedPref = mContext.getSharedPreferences(MyTimeTableActivity.SHARED_PREF_TAG, Context.MODE_PRIVATE);
        initialize();
    }

    /**
     * Viewの生成を行う。
     * @param activity セルを長押しした時のリスナ
     */
    public void createView(MyTimeTableActivity activity) {
        mActivity = activity;
        setTableSize();
        createRow();
    }

    /**
     * Layoutの各種設定を行う。
     */
    private void initialize() {

        this.setOrientation(VERTICAL);

        // LayoutParams
        mRowParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mRowParam.weight = 1;
        mRowItemParam = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        mRowItemParam.weight = 1;

        // Cell stroke
        GradientDrawable border = new GradientDrawable();
        border.setStroke(BORDER_WEIGHT, Color.LTGRAY);


        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(android.R.attr.colorBackground, typedValue, true);
        border.setColor(getResources().getColor(typedValue.resourceId));
        mCellStroke = new LayerDrawable(new Drawable[]{border});
        mCellStroke.setLayerInset(0, 0, 0, -BORDER_WEIGHT, -BORDER_WEIGHT);

        // Button Animation
        TypedValue value = new TypedValue();
        mContext.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
        mRippleResourceId = value.resourceId;
    }

    /**
     * SharedPreferencesから所属を読み込み、適した曜日数、時限数を設定する。
     */
    private void setTableSize() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);

        int belong = Integer.parseInt(pref.getString("pref_user_belong", "0"));
        if (belong == FACULTY_NIGHT) {
            mRowCount = 8;
            mColumnCount = 7;
        } else {
            mRowCount = 6;
            mColumnCount = 6;
        }
    }

    /**
     * 左端の列に表示する時限と各時限の開始時刻の文字列を生成する。
     * @param period 時限[1-7]
     * @return 時限と開始時刻の文字列
     */
    private String createPeriodText(int period) {
        return period + "\n" + PERIOD_START.get(period - 1);
    }

    /**
     * 曜日を表示するヘッダ列を生成する。
     * @return ヘッダレイアウト
     */
    private LinearLayout createHeaderRow() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setLayoutParams(mRowParam);

        // 左上の空白セル
        TextView tv;
        tv = new TextView(mContext);
        tv.setLayoutParams(mRowItemParam);
        if (Build.VERSION.SDK_INT > 15) {
            tv.setBackground(mCellStroke);
        } else {
            tv.setBackgroundDrawable(mCellStroke);
        }
        layout.addView(tv);

        // 曜日セル
        for (int i = 1; i < mColumnCount; i++) {
            tv = new TextView(mContext);
            tv.setText(DAY_NAME.get(i - 1));
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.BLACK);
            tv.setLayoutParams(mRowItemParam);
            if (Build.VERSION.SDK_INT > 15) {
                tv.setBackground(mCellStroke);
            } else {
                tv.setBackgroundDrawable(mCellStroke);
            }

            layout.addView(tv);
        }

        return layout;
    }

    /**
     * 時間割テーブルを生成する。
     */
    private void createRow() {

        LinearLayout row, container;
        TextView tv;

        this.addView(createHeaderRow());
        for (int j = 1; j < mRowCount; j++) {
            row = new LinearLayout(mContext);
            row.setLayoutParams(mRowParam);
            row.setOrientation(HORIZONTAL);

            for (int i = 0; i < mColumnCount; i++) {
                tv = createItemView(i, j);
                container = createItemContainer();
                container.addView(tv);
                row.addView(container);
            }

            this.addView(row);
        }
    }

    /**
     * 時限開始時刻や登録済み情報などの、セルに表示するTextViewを生成する。
     * @param x セルのx座標
     * @param y セルのy座標
     * @return セルに表示するTextView
     */
    private TextView createItemView(int x, int y) {
        String table_pos = String.valueOf(x) + String.valueOf(y);
        TextView tv = new TextView(mContext);

        if (x == 0) {
            tv.setText(createPeriodText(y));
        } else {
            tv.setClickable(true);
            tv.setOnLongClickListener((OnLongClickListener) mActivity);
            tv.setText(mSharedPref.getString(table_pos, ""));
        }

        tv.setId(Integer.parseInt(table_pos));
        tv.setTag(table_pos);
        tv.setMaxLines(2);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setTextColor(Color.BLACK);
        tv.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(mRippleResourceId);

        return tv;
    }

    /**
     * セルのTextViewを表示するためのコンテナを生成する。
     * @return コンテナ
     */
    private LinearLayout createItemContainer() {
        LinearLayout itemContainer = new LinearLayout(mContext);
        itemContainer.setOrientation(VERTICAL);
        itemContainer.setLayoutParams(mRowItemParam);

        if (Build.VERSION.SDK_INT > 15) {
            itemContainer.setBackground(mCellStroke);
        } else {
            itemContainer.setBackgroundDrawable(mCellStroke);
        }

        return itemContainer;
    }
}
