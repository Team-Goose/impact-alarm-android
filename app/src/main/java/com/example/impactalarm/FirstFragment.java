package com.example.impactalarm;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class FirstFragment extends Fragment {

    public AlarmManager manager;
    public AlarmManager.AlarmClockInfo alarm;
    public NotificationListenerService notifs;

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
                manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                alarm = manager.getNextAlarmClock();
                Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run(){
                        if(MainActivity.checkingForAlarms) {
                            System.out.println("it's working");
                        }
                    }
                };
                if(alarm != null){
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                    MainActivity.checkingForAlarms = true;
                    handler.postAtTime(run, alarm.getTriggerTime() - (System.currentTimeMillis() - SystemClock.uptimeMillis()) + 30000);
                } else{
                    Snackbar.make(view, "No upcoming alarms to monitor!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL url = null;
                try {
                    url = new URL("https://reqres.in/api/users?page=2");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    connection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }

                InputStream responseStream = null;
                try {
                    responseStream = connection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(responseStream);
            }
        });
    }
}