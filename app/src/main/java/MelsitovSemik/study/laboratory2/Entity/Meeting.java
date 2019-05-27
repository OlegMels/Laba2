package MelsitovSemik.study.laboratory2.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meeting implements Parcelable {

    private String mTitle, mDescription, mCreator, mDocument, mPriority;
    private List<String> mMembers;
    private Date mStartDate, mEndDate;

    public Meeting(){
        mMembers = new ArrayList<>();
    }


    protected Meeting(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
        mCreator = in.readString();
        mDocument = in.readString();
        mPriority = in.readString();
        mMembers = in.createStringArrayList();
        mStartDate = new Date(in.readString());
        mEndDate = new Date(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mCreator);
        dest.writeString(mDocument);
        dest.writeString(mPriority);
        dest.writeStringList(mMembers);
        dest.writeString(mStartDate.toString());
        dest.writeString(mEndDate.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setPriority(String priority) {
        mPriority = priority;
    }

    public String getPriority() {
        return mPriority;
    }

    public void addToMemberList(String member){
        mMembers.add(member);
    }

    public void removeMemberFromList(String member){
        mMembers.remove(member);
    }

    public void setMembers(List<String> members) {
        mMembers = members;
    }

    public List<String> getMembers() {
        return mMembers;
    }

    public void setDocument(String document) {
        mDocument = document;
    }

    public String getDocument() {
        return mDocument;
    }

    public void setCreator(String creator) {
        mCreator = creator;
    }

    public String getCreator() {
        return mCreator;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

}
