package MelsitovSemik.study.laboratory2.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import MelsitovSemik.study.laboratory2.Entity.Meeting;
import MelsitovSemik.study.laboratory2.Global.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import MelsitovSemik.study.laboratory2.Interfaces.ISearch;
import MelsitovSemik.study.laboratory2.Interfaces.ISelectedMeeting;
import MelsitovSemik.study.laboratory2.R;

public class MeetingAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Meeting> mMeetings;
    private ISelectedMeeting mHandler;
    private ISearch iSearch;
    private String mSearchQuery;

    public MeetingAdapter(Context context, List<Meeting> list){
        mContext = context;
        mHandler = (ISelectedMeeting) context;
        iSearch = (ISearch) context;
        updateList(list);
    }

    public void updateList(List<Meeting> list){
        List<Meeting> up = new ArrayList<>();
        up.add(null);
        up.addAll(list);
        mMeetings = up;
    }

    public void setSearchQuery(String query){
        mSearchQuery = query;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == Constants.VIEW_TYPES.NORMAL)
            return new MeetingViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_meeting, viewGroup , false));
        else
            return new MeetingSearchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_meeting_search, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder != null && viewHolder instanceof MeetingViewHolder)
            ((MeetingViewHolder)viewHolder).bind(mMeetings.get(i));
        if(viewHolder != null && viewHolder instanceof MeetingSearchViewHolder)
            ((MeetingSearchViewHolder)viewHolder).bind();
    }

    @Override
    public int getItemCount() {
        return mMeetings != null ? mMeetings.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return Constants.VIEW_TYPES.HEADER;
        else
            return Constants.VIEW_TYPES.NORMAL;
    }

    class MeetingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Meeting mMeeting;
        @BindView(R.id.item_meeting_title)
        TextView mTitleTextView;
        @BindView(R.id.item_meeting_creator)
        TextView mCreatorTextView;

        MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Meeting meeting){
            mMeeting = meeting;
            mTitleTextView.setText(mMeeting.getTitle());
            mCreatorTextView.setText(String.format(mContext.getString(R.string.item_meeting_creator), mMeeting.getCreator()));
        }

        @Override
        public void onClick(View v) {
            if(mHandler != null)
                mHandler.ISelectedMeeting(mMeeting);
        }
    }
    class MeetingSearchViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_meeting_search_edit_text)
        EditText mSearchEditText;

        public MeetingSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(){
            if(mSearchQuery != null && !mSearchQuery.equals(""))
                mSearchEditText.setText(mSearchQuery);
        }

        @OnClick(R.id.item_meeting_search_btn)
        void search(){
            String query = mSearchEditText.getText().toString();
            if(query == null)
                return;
            if(iSearch != null)
                iSearch.ISearch(query);
        }
    }

}
