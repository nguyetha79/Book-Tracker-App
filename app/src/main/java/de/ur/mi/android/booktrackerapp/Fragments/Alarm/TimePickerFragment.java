package de.ur.mi.android.booktrackerapp.Fragments.Alarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;


import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    private TextView tvAlarm;
    private Calendar calendar;

    public TimePickerFragment(TextView textView) {
        this.tvAlarm = textView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        String min = "";
        if (minute < 10)
            min = "0" + minute ;
        else
            min = String.valueOf(minute);

        String time = new StringBuilder().append("Alarm set for: ")
                                        .append(hour)
                                        .append(":")
                                        .append(min)
                                        .toString();
        tvAlarm.setText(time);
    }
}