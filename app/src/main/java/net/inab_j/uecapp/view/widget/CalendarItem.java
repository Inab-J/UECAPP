package net.inab_j.uecapp.view.widget;

public class CalendarItem {
    private long mId;
    private boolean mIsHeader = false;
    private String mYear;
    private String mDate;
    private String mEvent;
    private String mNote = "";
    private boolean mIsRemark = false;

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
        this.mIsRemark = remark;
    }

    public boolean isRemark() {
        return mIsRemark;
    }

    public void setNote(String note) { this.mNote = note; }

    public String getNote() { return this.mNote; }
}
