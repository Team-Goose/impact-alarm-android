package com.example.impactalarm;

import android.app.AlarmManager;
import android.content.Context;
import android.os.AsyncTask;
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

import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
                manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                alarm = manager.getNextAlarmClock();
                Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run(){
                        if(MainActivity.checkingForAlarms) {
                            new MainGetRequest().execute();
                        }
                    }
                };
                if(alarm != null){
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                    MainActivity.checkingForAlarms = true;
                    handler.postAtTime(run, alarm.getTriggerTime() - (System.currentTimeMillis() - SystemClock.uptimeMillis()) + 10000);
                } else{
                    Snackbar.make(view, "No upcoming alarms to monitor!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MainGetRequest().execute();
            }
        });
    }
}

class MainGetRequest extends AsyncTask<Void, Void, Void> {
    protected void onPreExecute(){
    }

    protected Void doInBackground(Void... params){
        URL url = null;
        try {
            url = new URL("http://192.168.93.146:5000/servo");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        }

        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Connection", "keep-alive");

        int responseCode = -1;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Response Code: " + responseCode);
        return null;
    }

    protected void onPostExecute(Void result){
    }
}