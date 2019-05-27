package MelsitovSemik.study.laboratory2.Entity;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MeetingList {

    private List<Meeting> mMeetings;

    public MeetingList(){
        mMeetings = new ArrayList<>();
    }

    public void setList(List<Meeting> meetings){
        if(meetings != null)
            mMeetings = meetings;
    }

    public List<Meeting> getCurrentDateList(){
        isListNull();
        List<Meeting> curr = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());

        Calendar meetingDate = Calendar.getInstance();

        for(Meeting meeting: mMeetings){
            meetingDate.setTime(meeting.getStartDate());
            if(today.get(Calendar.YEAR) == meetingDate.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) == meetingDate.get(Calendar.DAY_OF_YEAR))
                curr.add(meeting);
        }

        return curr;
    }
    public List<Meeting> getSearchList(String query){
        isListNull();
        if (query.equals(""))
            return getCurrentDateList();
        if(query.equals("all"))
            return mMeetings;
        List<Meeting> curr = new ArrayList<>();

        for(Meeting meeting: mMeetings){
            if(meeting.getDescription().contains(query))
                curr.add(meeting);
        }

        return curr;
    }

    private void isListNull(){
        if(mMeetings == null)
            mMeetings = new ArrayList<>();
    }

}
