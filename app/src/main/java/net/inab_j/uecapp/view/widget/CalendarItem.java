package net.inab_j.uecapp.view.widget;

public class CalendarItem {
    private long mId;
    private String mDate;
    private String mEvent;
    private boolean mRemark = false;

    public long get_id() {
        return mId;
    }

    public void set_id(long id) {
        this.mId = id;
    }

    public String get_date() {
        return mDate;
    }

    public void set_date(String date) {
        this.mDate = date;
    }

    public String get_event() {
        return mEvent;
    }

    public void set_event(String event) {
        this.mEvent = event;
    }

    public void set_remark(boolean remark) {
        this.mRemark = remark;
    }

    public boolean is_remark() {
        return mRemark;
    }
}
