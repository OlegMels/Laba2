package MelsitovSemik.study.laboratory2.UI.Fragments.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import MelsitovSemik.study.laboratory2.R;

public class DatePickerFragment extends DialogFragment {

    private final static String EXTRA_VALUE = "EXTRA VALUE", EXTRA_DATE = "EXTRA_DATE";
    public final static String EXTRA_SEND_RESULT = "Extra result", EXTRA_SEND_VALUE = "Extra send value";
    private Date mDate;
    private int mValue;
    private DatePicker mDatePicker;

    public static DatePickerFragment getInstance(int value, Date date){
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_VALUE, value);
        bundle.putString(EXTRA_DATE, date != null ? date.toString() : "");

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mValue = getArguments().getInt(EXTRA_VALUE);
        mDate = new Date();
        String date = getArguments().getString(EXTRA_DATE);
        if(date != null && !date.equals(""))
            mDate = new Date(date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.date_picker, null);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mDatePicker = view.findViewById(R.id.date_picker);
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.select_date)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day).getTime();

                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .setNeutralButton(android.R.string.cancel, null)
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SEND_RESULT, date);
        intent.putExtra(EXTRA_SEND_VALUE, mValue);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
