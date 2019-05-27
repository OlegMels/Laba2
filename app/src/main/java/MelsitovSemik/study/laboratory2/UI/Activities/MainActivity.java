package MelsitovSemik.study.laboratory2.UI.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import MelsitovSemik.study.laboratory2.Entity.Meeting;
import MelsitovSemik.study.laboratory2.Entity.MeetingList;
import MelsitovSemik.study.laboratory2.Global.Constants;
import MelsitovSemik.study.laboratory2.Broadcasts.NotificationBroadcast;
import MelsitovSemik.study.laboratory2.Broadcasts.RefreshMeetingsBroadcast;
import MelsitovSemik.study.laboratory2.R;
import MelsitovSemik.study.laboratory2.UI.Fragments.ListFragment;
import MelsitovSemik.study.laboratory2.UI.Fragments.MeetingFragment;
import MelsitovSemik.study.laboratory2.Interfaces.ICreateMeeting;
import MelsitovSemik.study.laboratory2.Interfaces.IChangeMeeting;
import MelsitovSemik.study.laboratory2.Interfaces.IGetMeetings;
import MelsitovSemik.study.laboratory2.Interfaces.IMenuHandler;
import MelsitovSemik.study.laboratory2.Interfaces.ISearch;
import MelsitovSemik.study.laboratory2.Interfaces.ISelectedMeeting;

public class MainActivity extends SingleFragmentActivity implements ICreateMeeting, IChangeMeeting, IGetMeetings, IMenuHandler, ISelectedMeeting, ISearch {

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private MeetingList mMeetingBank;
    private BroadcastReceiver mRefreshMeetings;
    private AlarmManager mAlarmManager;

    @Override
    protected int getResID() {
        return R.layout.activity_main;
    }

    @Override
    protected Fragment getBasicFragment() {
        return ListFragment.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mMeetingBank = new MeetingList();
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        mRefreshMeetings = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getMeetings();
            }
        };

        try {
            IntentFilter intentFilter = new IntentFilter(Constants.REFRESH_MEETINGS_BROADCAST_ACTION);
            registerReceiver(mRefreshMeetings, intentFilter);
        } catch (Exception e){
            Log.d("==> ", e.toString());
        }

        initRefreshMeetingsAlarm();


    }

    private void initRefreshMeetingsAlarm(){
        long interval = 3000;

        Intent resultIntent = new Intent(this, RefreshMeetingsBroadcast.class);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTime().getTime(),  10000, mPendingIntent);
    }

    @Override
    public void ICreateMeeting() {
        setFragment(MeetingFragment.getInstance(null, mFirebaseUser.getEmail()));
    }

    @Override
    public void ICreatedMeeting(Meeting meeting) {
        Map<String, Object> obj = new HashMap<>();
        obj.put(Constants.Meeting.Cols.CREATOR, mFirebaseUser.getEmail() != null ? mFirebaseUser.getEmail() : "someone");
        obj.put(Constants.Meeting.Cols.TITLE, meeting.getTitle());
        obj.put(Constants.Meeting.Cols.DESCRIPTION, meeting.getDescription());
        obj.put(Constants.Meeting.Cols.MEMBERS, meeting.getMembers());
        obj.put(Constants.Meeting.Cols.PRIORITY, meeting.getPriority());
        obj.put(Constants.Meeting.Cols.DATE_START, meeting.getStartDate());
        obj.put(Constants.Meeting.Cols.DATE_END, meeting.getEndDate());

        mFirebaseFirestore
                .collection(Constants.Meeting.NAME)
                .add(obj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, R.string.meeting_was_created, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        sendBroadcast(new Intent(MainActivity.this, NotificationBroadcast.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, R.string.error_create_meeting, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
    }

    @Override
    public void ICancelMeeting(String document) {
        mFirebaseFirestore
                .collection(Constants.Meeting.NAME)
                .document(document)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, R.string.meeting_was_delete, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, R.string.error_delete_meeting, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
    }

    @Override
    public void IUpdateMeeting(Meeting meeting) {
        Map<String, Object> obj = new HashMap<>();
        obj.put(Constants.Meeting.Cols.CREATOR, mFirebaseUser.getEmail() != null ? mFirebaseUser.getEmail() : "someone");
        obj.put(Constants.Meeting.Cols.TITLE, meeting.getTitle());
        obj.put(Constants.Meeting.Cols.DESCRIPTION, meeting.getDescription());
        obj.put(Constants.Meeting.Cols.MEMBERS, meeting.getMembers().toString());
        obj.put(Constants.Meeting.Cols.PRIORITY, meeting.getPriority());
        obj.put(Constants.Meeting.Cols.DATE_START, meeting.getStartDate());
        obj.put(Constants.Meeting.Cols.DATE_END, meeting.getEndDate());

        mFirebaseFirestore
                .collection(Constants.Meeting.NAME)
                .document(meeting.getDocument())
                .set(obj)
                .addOnSuccessListener(new OnSuccessListener < Void > () {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, R.string.meeting_was_update, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, R.string.error_update_meeting, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
    }

    @Override
    public void IWillGoToMeeting(Meeting meeting) {
        Map<String, Object> obj = new HashMap<>();
        obj.put(Constants.Meeting.Cols.CREATOR, meeting.getCreator());
        obj.put(Constants.Meeting.Cols.TITLE, meeting.getTitle());
        obj.put(Constants.Meeting.Cols.DESCRIPTION, meeting.getDescription());
        obj.put(Constants.Meeting.Cols.MEMBERS, meeting.getMembers().toString());
        obj.put(Constants.Meeting.Cols.PRIORITY, meeting.getPriority());
        obj.put(Constants.Meeting.Cols.DATE_START, meeting.getStartDate());
        obj.put(Constants.Meeting.Cols.DATE_END, meeting.getEndDate());

        mFirebaseFirestore
                .collection(Constants.Meeting.NAME)
                .document(meeting.getDocument())
                .set(obj)
                .addOnSuccessListener(new OnSuccessListener < Void > () {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, R.string.meeting_i_will_go, Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, R.string.meeting_i_will_go_error, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });

    }

    @Override
    public void getMeetings() {
        final List<Meeting> list = new ArrayList<>();
        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment != null && fragment instanceof ListFragment) {
            ((ListFragment) fragment).setVisibleProgressBar(true);
            ((ListFragment)fragment).setVisibleRefreshBtn(false);
        }

        if(!isNetworkAvailable()){
            Toast.makeText(this, R.string.internet_disconnect, Toast.LENGTH_SHORT).show();
            ((ListFragment) fragment).setVisibleProgressBar(false);
            ((ListFragment)fragment).setVisibleRefreshBtn(true);
            return;
        }

        mFirebaseFirestore
                .collection(Constants.Meeting.NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Meeting meeting = new Meeting();

                                meeting.setTitle(document.getData().get(Constants.Meeting.Cols.TITLE).toString());
                                meeting.setDescription(document.getData().get(Constants.Meeting.Cols.DESCRIPTION).toString());
                                meeting.setCreator(document.getData().get(Constants.Meeting.Cols.CREATOR).toString());
                                meeting.setDocument(document.getId());
                                meeting.setPriority(document.getData().get(Constants.Meeting.Cols.PRIORITY).toString());

                                String line = document.get(Constants.Meeting.Cols.MEMBERS).toString();
                                line = line.substring(1, line.length() - 1);
                                line = line.replaceAll("\\s", "");
                                meeting.setMembers(new ArrayList<>(Arrays.asList(line.split(","))));

                                meeting.setStartDate(document.getDate(Constants.Meeting.Cols.DATE_START));
                                meeting.setEndDate(document.getDate(Constants.Meeting.Cols.DATE_END));

                                list.add(meeting);
                            }
                        } else {

                        }

                        mMeetingBank.setList(list);

                        if(fragment != null && fragment instanceof ListFragment)
                            ((ListFragment)fragment).setList(mMeetingBank.getCurrentDateList());
                    }
                });
    }

    @Override
    public void IClickMenuItem(int value) {
        if(!isNetworkAvailable()){
            Toast.makeText(this, R.string.internet_disconnect, Toast.LENGTH_SHORT).show();
            return;
        }
        switch (value){
            case Constants.Menu.SIGN_OUT:
                mFirebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, SignActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null)
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshMeetings);
    }

    @Override
    public void ISelectedMeeting(Meeting meeting) {
        setFragment(MeetingFragment.getInstance(meeting, mFirebaseUser.getEmail()));
    }

    @Override
    public void ISearch(String query) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment != null && fragment instanceof ListFragment)
            ((ListFragment)fragment).setSearchList(mMeetingBank.getSearchList(query), query);
    }
}
