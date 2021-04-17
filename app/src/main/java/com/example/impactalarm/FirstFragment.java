package com.example.impactalarm;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    public AlarmManager manager;
    public AlarmManager.AlarmClockInfo alarm;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

                manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                alarm = manager.getNextAlarmClock();
                System.out.println("Time of next alarm: " + alarm.getTriggerTime());
                Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run(){
                        System.out.println("it's fuckin working hahaha");
                    }
                };
                handler.postAtTime(run, alarm.getTriggerTime() - (System.currentTimeMillis() - SystemClock.uptimeMillis()) + 30000);
            }
        });
    }
}