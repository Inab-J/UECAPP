package net.inab_j.uecapp.view.widget;

public class CalendarItem {
    private long mId;
    private boolean mIsHeader = false;
    private String mYear;
    private String mDate;
    private String mEvent;
    private boolean mRemark = false;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public boolean isHeader() {
        return mIsHeader;
    }

    public void isHeader(boolean isHeader) {
        this.mIsHeader = isHeader;
    }

    public String getYear() { return mYear; }

    public void setYear(String year) {
        this.mYear = year;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getEvent() {
        return mEvent;
    }

    public void setEvent(String event) {
        this.mEvent = event;
    }

    public void isRemark(boolean remark) {
        this.mRemark = remark;
    }

    public boolean isRemark() {
        return mRemark;
    }
}
