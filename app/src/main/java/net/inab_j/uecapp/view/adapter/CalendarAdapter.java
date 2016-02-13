package net.inab_j.uecapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.inab_j.uecapp.R;
import net.inab_j.uecapp.view.widget.CalendarItem;

import java.util.List;

public class CalendarAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater = null;
    private List<CalendarItem> mCalendarList;

    public CalendarAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCalendarList(List<CalendarItem> calendarList) {
        this.mCalendarList = calendarList;
    }

    @Override
    public int getCount() {
        return mCalendarList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCalendarList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCalendarList.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.calendar_item, parent, false);

            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.event = (TextView) convertView.findViewById(R.id.event);
            holder.remark = (TextView) convertView.findViewById(R.id.remark);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.date.setText(mCalendarList.get(position).get_date());
        if (mCalendarList.get(position).is_remark())
            holder.remark.setText("※");
        else
            holder.remark.setText("　");
        holder.event.setText(mCalendarList.get(position).get_event());

        return convertView;
    }

    private class ViewHolder {
        TextView date;
        TextView event;
        TextView remark;
    }
}
