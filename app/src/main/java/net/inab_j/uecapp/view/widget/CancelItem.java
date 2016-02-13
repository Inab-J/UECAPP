package net.inab_j.uecapp.view.widget;

public class CancelItem {
    private long mId;
    private String mDate;
    private String mClassname;
    private int mPeriod;
    private String mSubject;

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

    public String get_classname() {
        return mClassname;
    }

    public void set_classname(String classname) {
        this.mClassname = classname;
    }

    public int get_period() {
        return mPeriod;
    }

    public void set_period(int period) {
        this.mPeriod = period;
    }

    public String get_subject() {
        return mSubject;
    }

    public void set_subject(String subject) {
        this.mSubject = subject;
    }

}
