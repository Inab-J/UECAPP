package net.inab_j.uecapp.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.inab_j.uecapp.R;
import net.inab_j.uecapp.view.widget.CalendarItem;

import org.w3c.dom.Text;

import java.util.List;

public class CalendarAdapter extends BaseAdapter {

    // view type
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ROW = 1;

    private LayoutInflater mLayoutInflater = null;
    private List<CalendarItem> mCalendarList;
    private Context mContext;

    public CalendarAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCalendarList(List<CalendarItem> calendarList) {
        this.mCalendarList = calendarList;
    }

    @Override
    public int getCount() {
        return mCalendarList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (mCalendarList.get(position).isHeader()) {
            return ITEM_VIEW_TYPE_HEADER;
        } else {
            return ITEM_VIEW_TYPE_ROW;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        if (mCalendarList.get(position).isHeader()) {
            return false;
        } else {
            return super.isEnabled(position);
        }
    }

    @Override
    public Object getItem(int position) {
        return mCalendarList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCalendarList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mCalendarList.get(position).isHeader()) {
            return getHeaderView(position, convertView, parent);
        } else {
            return getRowView(position, convertView, parent);
        }
    }

    public View getRowView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.calendar_item, parent, false);

            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.event = (TextView) convertView.findViewById(R.id.event);
            holder.remark = (ImageView) convertView.findViewById(R.id.has_remarks);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.date.setText(mCalendarList.get(position).getDate());
        holder.event.setText(mCalendarList.get(position).getEvent());
        // 備考がない場合はアイコンを非表示にする
        if (mCalendarList.get(position).isRemark()) {
            holder.remark.setVisibility(View.VISIBLE);
        } else {
            holder.remark.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        final SubHeaderHolder holder;

        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.calendar_subheader, null);
            holder = new SubHeaderHolder();
            holder.subheader = (TextView) convertView.findViewById(R.id.subheader);
            convertView.setTag(holder);
        } else {
            holder = (SubHeaderHolder) convertView.getTag();
        }

        holder.subheader.setText(mCalendarList.get(position).getYear());
        return convertView;
    }

    private class ViewHolder {
        TextView date;
        TextView event;
        ImageView remark;
    }

    private class SubHeaderHolder {
        TextView subheader;
    }

    /**
     * dpをpxに変換
     * @param context 使用するコンテキスト
     * @param dp 変換するdp
     * @return dpをpxに変換した値
     */
    private static int convertDpToPx(Context context, int dp) {
        float d = context.getResources().getDisplayMetrics().density;
        return (int)((dp * d) + 0.5);
    }
}
