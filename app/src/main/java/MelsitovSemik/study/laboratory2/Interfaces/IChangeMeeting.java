package MelsitovSemik.study.laboratory2.Interfaces;

import MelsitovSemik.study.laboratory2.Entity.Meeting;

public interface IChangeMeeting {
    void ICreatedMeeting(Meeting meeting);
    void ICancelMeeting(String document);
    void IUpdateMeeting(Meeting meeting);
    void IWillGoToMeeting(Meeting meeting);
}
