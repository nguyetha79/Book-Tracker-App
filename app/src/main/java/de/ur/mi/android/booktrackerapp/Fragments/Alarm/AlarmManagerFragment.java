package de.ur.mi.android.booktrackerapp.Fragments.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import de.ur.mi.android.booktrackerapp.AlarmNotification.AlertReceiver;
import de.ur.mi.android.booktrackerapp.R;

public class AlarmManagerFragment extends Fragment {

    private TextView tvAlarm;
    private Button btnTimePicker, btnCancelAlarm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alarm_manager, container, false);
        tvAlarm = view.findViewById(R.id.tv_alarm);

        tvAlarm = view.findViewById(R.id.tv_alarm);

        btnTimePicker = view.findViewById(R.id.btn_timepicker);
        btnTimePicker.setOnClickListener(v -> {
            DialogFragment timePicker = new TimePickerFragment(tvAlarm);
            timePicker.show(getActivity().getSupportFragmentManager(), "time picker");
        });

        startAlarm();

        btnCancelAlarm = view.findViewById(R.id.btn_cancel);
        btnCancelAlarm.setOnClickListener(v -> cancelAlarm());
        return view;
    }

//    @Override
//    public void onTimeSet(TimePicker view, int hour, int minute) {
//        Calendar calendar = Calendar.getInstance();
//        hour = calendar.get(Calendar.HOUR_OF_DAY);
//        minute = calendar.get(Calendar.MINUTE);
//
//        String min = "";
//        if (minute < 10)
//            min = "0" + minute ;
//        else
//            min = String.valueOf(minute);
//
//        String time = new StringBuilder().append("Alarm set for: ")
//                .append(hour)
//                .append(":")
//                .append(min)
//                .toString();
//        tvAlarm.setText(time);
//
//        startAlarm(calendar);
//    }

    private void startAlarm() {
        Calendar c = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        tvAlarm.setText("Alarm canceled");
    }


}