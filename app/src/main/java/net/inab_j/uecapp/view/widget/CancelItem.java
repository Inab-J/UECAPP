package net.inab_j.uecapp.view.widget;

public class CancelItem {
    private long mId;
    private String mDate;
    private String mClassname;
    private String mPeriod;
    private String mSubject;
    private String mNote = "";

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getClassname() {
        return mClassname;
    }

    public void setClassname(String classname) {
        this.mClassname = classname;
    }

    public String getPeriod() {
        return mPeriod;
    }

    public void setPeriod(String period) {
        this.mPeriod = period;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        this.mSubject = subject;
    }

    public String getNote() { return mNote; }

    public void setNote(String note) { mNote = note; }
}
