package net.inab_j.uecapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.inab_j.uecapp.R;
import net.inab_j.uecapp.view.widget.CancelItem;

import java.util.List;

public class CancelAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater = null;
    private List<CancelItem> mCancelList;

    public CancelAdapter(Context context) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCancelList(List<CancelItem> cancelList) {
        this.mCancelList = cancelList;
    }

    @Override
    public int getCount() {
        return mCancelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCancelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCancelList.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.cancel_item, parent, false);

            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.class_name = (TextView) convertView.findViewById(R.id.class_name);
            holder.period = (TextView) convertView.findViewById(R.id.period);
            holder.subject = (TextView) convertView.findViewById(R.id.subject);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.date.setText(mCancelList.get(position).get_date());
        holder.class_name.setText(mCancelList.get(position).get_classname());
        holder.period.setText(mCancelList.get(position).get_period() + "時限");
        holder.subject.setText(mCancelList.get(position).get_subject());

        return convertView;
    }

    private class ViewHolder {
        TextView date;
        TextView class_name;
        TextView period;
        TextView subject;
    }
}
