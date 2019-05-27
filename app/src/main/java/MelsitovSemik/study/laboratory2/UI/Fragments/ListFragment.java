package MelsitovSemik.study.laboratory2.UI.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import MelsitovSemik.study.laboratory2.Entity.Meeting;
import MelsitovSemik.study.laboratory2.Global.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import MelsitovSemik.study.laboratory2.UI.Adapters.MeetingAdapter;
import MelsitovSemik.study.laboratory2.Interfaces.ICreateMeeting;
import MelsitovSemik.study.laboratory2.Interfaces.IGetMeetings;
import MelsitovSemik.study.laboratory2.Interfaces.IMenuHandler;
import MelsitovSemik.study.laboratory2.R;

public class ListFragment extends Fragment {

    private Unbinder mUnbinder;
    private ICreateMeeting mHandler;
    private List<Meeting> mMeetings;
    private IGetMeetings iGetMeetings;
    private MeetingAdapter mAdapter;
    private IMenuHandler iMenuHandler;
    @BindView(R.id.list_fragment_empty_list)
    TextView mEmptyListTextView;
    @BindView(R.id.list_fragment_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.list_fragment_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.list_fragment_refresh_btn)
    Button mRefreshBtn;

    public static ListFragment getInstance(){
        return new ListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mHandler = (ICreateMeeting) getActivity();
        iGetMeetings = (IGetMeetings) getActivity();
        iMenuHandler = (IMenuHandler) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Random color = new Random();
        view.setBackgroundColor(Color.argb(255, color.nextInt(255), color.nextInt(255), color.nextInt(255)));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        iGetMeetings.getMeetings();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_update:
                iGetMeetings.getMeetings();
                break;
            case R.id.menu_sign_out:
                iMenuHandler.IClickMenuItem(Constants.Menu.SIGN_OUT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSearchList(List<Meeting> meetings, String query){
        mMeetings = meetings;
        mAdapter.setSearchQuery(query);
        updateUI();
    }

    public void setList(List<Meeting> meetings){
        mMeetings = meetings;
        setVisibleProgressBar(false);
        setVisibleRefreshBtn(false);
        updateUI();
    }

    private void updateUI(){
        mEmptyListTextView.setVisibility(mMeetings == null || mMeetings.size() == 0 ? View.VISIBLE : View.GONE);
        if(mAdapter == null)
            mAdapter = new MeetingAdapter(getActivity(), mMeetings);
        else {
            mAdapter.updateList(mMeetings);
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setVisibleProgressBar(boolean value){
        if(mProgressBar != null)
            mProgressBar.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    public void setVisibleRefreshBtn(boolean value){
        if(mRefreshBtn != null)
            mRefreshBtn.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.list_fragment_fb)
    void createMeeting(){
        if(mHandler != null) mHandler.ICreateMeeting();
    }

    @OnClick(R.id.list_fragment_refresh_btn)
    void refresh(){
        if(iGetMeetings != null) {
            iGetMeetings.getMeetings();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
