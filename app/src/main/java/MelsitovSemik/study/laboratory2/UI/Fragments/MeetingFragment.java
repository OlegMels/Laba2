package MelsitovSemik.study.laboratory2.UI.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import MelsitovSemik.study.laboratory2.Entity.Meeting;
import MelsitovSemik.study.laboratory2.UI.Activities.SingleFragmentActivity;
import MelsitovSemik.study.laboratory2.UI.Fragments.Dialogs.DatePickerFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import MelsitovSemik.study.laboratory2.Interfaces.IChangeMeeting;
import MelsitovSemik.study.laboratory2.R;

public class MeetingFragment extends Fragment {

    private final static String EXTRA_MEETING = "EXTRA MEETING", EXTRA_USER_NAME = "EXTRA USER NAME", DIALOG_DATE = "Dialog date";
    private static final int REQUEST_TIME = 10;
    private Meeting mMeeting;
    private Unbinder mUnbinder;
    private String mUserName;
    private IChangeMeeting mHandler;
    private boolean isChange, canCanel;
    @BindView(R.id.meeting_fragment_title)
    EditText mTitleEditText;
    @BindView(R.id.meeting_fragment_description)
    EditText mDescriptionEditText;
    @BindView(R.id.meeting_fragment_create_btn)
    Button mCreateBtn;
    @BindView(R.id.meeting_fragment_cancel_btn)
    Button mCancelBtn;
    @BindView(R.id.meeting_fragment_go)
    Button mGoBtn;
    @BindView(R.id.meeting_fragment_spinner)
    Spinner mSpinner;
    @BindView(R.id.meeting_fragment_start_date_tv)
    TextView mStartDateTextView;
    @BindView(R.id.meeting_fragment_end_date_tv)
    TextView mEndDateTextView;
    @BindView(R.id.meeting_fragment_end_date_btn)
    ImageView mEndDateBtn;
    @BindView(R.id.meeting_fragment_start_date_btn)
    ImageView mStartDateBtn;
    @BindView(R.id.meeting_fragment_share)
    Button mShareBtn;


    public static MeetingFragment getInstance(Meeting meeting, String userName){
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MEETING, meeting);
        bundle.putString(EXTRA_USER_NAME, userName);

        MeetingFragment fragment = new MeetingFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMeeting = getArguments().getParcelable(EXTRA_MEETING);
        mUserName = getArguments().getString(EXTRA_USER_NAME);
        mHandler = (IChangeMeeting) getActivity();
        canCanel = false;

        if(mMeeting == null) {
            mMeeting = new Meeting();
            mMeeting.setCreator(mUserName);
            mMeeting.addToMemberList(mUserName);
            isChange = true;
        } else {
            isChange = mMeeting.getCreator().equals(mUserName);
            if(isChange)
                canCanel = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meeting_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMeeting.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMeeting.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(getPosition());
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] prioity = getResources().getStringArray(R.array.priority);
                mMeeting.setPriority(prioity[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Random color = new Random();
        view.setBackgroundColor(Color.argb(255, color.nextInt(255), color.nextInt(255), color.nextInt(255)));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleEditText.setText(mMeeting.getTitle());
        mDescriptionEditText.setText(mMeeting.getDescription());
        if(mMeeting.getStartDate() == null)
            mStartDateTextView.setText(R.string.select_date);
        else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mMeeting.getStartDate());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            mStartDateTextView.setText(String.format(getString(R.string.date_pattern), day, month, year));
        }

        if(mMeeting.getEndDate() == null)
            mEndDateTextView.setText(R.string.select_date);
        else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mMeeting.getEndDate());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            mEndDateTextView.setText(String.format(getString(R.string.date_pattern), day, month, year));
        }

        if(!isChange){
          mTitleEditText.setEnabled(false);
          mDescriptionEditText.setEnabled(false);
          mSpinner.setEnabled(false);
          mCreateBtn.setVisibility(View.GONE);
          mStartDateTextView.setEnabled(false);
          mEndDateTextView.setEnabled(false);
          mStartDateBtn.setEnabled(false);
          mEndDateBtn.setEnabled(false);
        }
        mGoBtn.setVisibility(isChange ? View.GONE : View.VISIBLE);

        if(!isChange){
            boolean value = false;
            for(String meeting: mMeeting.getMembers()){
                if(meeting.equals(mUserName)) {
                    value = true;
                    break;
                }
            }
            mGoBtn.setText(value ? R.string.i_will_not_go : R.string.i_will_go);
        }

        if(canCanel)
            mCreateBtn.setText(R.string.update);

        Random color = new Random();
        view.setBackgroundColor(Color.argb(255, color.nextInt(255), color.nextInt(255), color.nextInt(255)));

        mCancelBtn.setVisibility(canCanel ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.meeting_fragment_create_btn, R.id.meeting_fragment_cancel_btn, R.id.meeting_fragment_go})
    void click(View view){

        Activity activity = getActivity();

        if(activity != null && activity instanceof SingleFragmentActivity){
            if(!((SingleFragmentActivity)activity).isNetworkAvailable()) {
                Toast.makeText(getActivity(), R.string.internet_disconnect, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        mGoBtn.setEnabled(false);
        mCreateBtn.setEnabled(false);
        mCancelBtn.setEnabled(false);

        Random color = new Random();
        view.setBackgroundColor(Color.argb(255, color.nextInt(255), color.nextInt(255), color.nextInt(255)));

        switch (view.getId()){
            case R.id.meeting_fragment_create_btn:
                if(mCreateBtn.getText().toString().equals(getString(R.string.create)))
                    mHandler.ICreatedMeeting(mMeeting);
                else
                    mHandler.IUpdateMeeting(mMeeting);
                break;
            case R.id.meeting_fragment_cancel_btn:
                mHandler.ICancelMeeting(mMeeting.getDocument());
                break;
            case R.id.meeting_fragment_go:
                if(mGoBtn.getText().toString().equals(getString(R.string.i_will_go))) {
                    mMeeting.addToMemberList(mUserName);
                    mHandler.IWillGoToMeeting(mMeeting);
                } else {
                    mMeeting.removeMemberFromList(mUserName);
                    mHandler.IWillGoToMeeting(mMeeting);
                }
                break;
        }
    }
    @OnClick({R.id.meeting_fragment_start_date_btn, R.id.meeting_fragment_end_date_btn})
    void showDatePicker(View view){
        DatePickerFragment dialog = null;
        switch (view.getId()){
            case R.id.meeting_fragment_start_date_btn:
                dialog = DatePickerFragment.getInstance(0, mMeeting.getStartDate());
                break;
            case R.id.meeting_fragment_end_date_btn:
                dialog = DatePickerFragment.getInstance(1, mMeeting.getStartDate());
                break;
        }
        if(dialog != null) {
            dialog.setTargetFragment(MeetingFragment.this, REQUEST_TIME);
            dialog.show(getFragmentManager(), DIALOG_DATE);
        }

        Random color = new Random();
        view.setBackgroundColor(Color.argb(255, color.nextInt(255), color.nextInt(255), color.nextInt(255)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_SEND_RESULT);
            int value = data.getIntExtra(DatePickerFragment.EXTRA_SEND_VALUE, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if (value == 0){
                if(mMeeting.getEndDate() == null) {
                    mStartDateTextView.setText(String.format(getString(R.string.date_pattern), day, month, year));
                    mMeeting.setStartDate(date);
                } else {

                    Calendar end = Calendar.getInstance();
                    end.setTime(mMeeting.getEndDate());

                    if(calendar.get(Calendar.YEAR) <= end.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) <= end.get(Calendar.DAY_OF_YEAR)){
                        mStartDateTextView.setText(String.format(getString(R.string.date_pattern), day, month, year));
                        mMeeting.setStartDate(date);
                    } else
                        Toast.makeText(getActivity(), R.string.error_date, Toast.LENGTH_SHORT).show();
                }
            } else {
                if(mMeeting.getStartDate() == null) {
                    mEndDateTextView.setText(String.format(getString(R.string.date_pattern), day, month, year));
                    mMeeting.setEndDate(date);
                } else {

                    Calendar start = Calendar.getInstance();
                    start.setTime(mMeeting.getStartDate());

                    if(calendar.get(Calendar.YEAR) >= start.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.DAY_OF_YEAR)){
                        mEndDateTextView.setText(String.format(getString(R.string.date_pattern), day, month, year));
                        mMeeting.setEndDate(date);
                    } else
                        Toast.makeText(getActivity(), R.string.error_date, Toast.LENGTH_SHORT).show();

                }
            }

        }
    }

    @OnClick(R.id.meeting_fragment_share)
    void shareMessage(){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/plain");
        String[] addressee = mMeeting.getMembers().toArray(new String[mMeeting.getMembers().size()]);
        email.putExtra(Intent.EXTRA_EMAIL, addressee);

        startActivity(email);
    }

    private int getPosition(){
        if(mMeeting.getPriority()!= null && !mMeeting.getPriority().equals("")){
            String[] prioity = getResources().getStringArray(R.array.priority);
            for(int i = 0; i < prioity.length; i++){
                if(prioity[i].equals(mMeeting.getPriority()))
                    return i;
            }
        }
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
