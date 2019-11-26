package com.papb.challange2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class MainFragment extends Fragment {

    int mInterval, mJackpot;
    int rngesus1, rngesus2, rngesus3;
    int stopCount = 1;
    private MainViewModel mViewModel;
    private TextView number1, number2, number3;
    private Button start, stop, settings;
    private boolean isRunning = false;
    private SharedPreferences mPreferences;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        final View root = inflater.inflate(R.layout.main_fragment, container, false);

        initializeUI(root);
        initChannels(getActivity());
        mPreferences = getActivity().getSharedPreferences(getActivity().getApplication().toString(), Context.MODE_PRIVATE);

        final Handler handler = new Handler();
        //default interval 5 x 100 = 500ms
        mInterval = mPreferences.getInt("INTERVAL", 5) * 100;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                rngesus1 = (int) (Math.random() * 10);
                number1.setText(Integer.toString(rngesus1));
                handler.postDelayed(this, mInterval);
            }
        };
        final Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                rngesus2 = (int) (Math.random() * 10);
                number2.setText(Integer.toString(rngesus2));
                handler.postDelayed(this, mInterval);
            }
        };
        final Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                rngesus3 = (int) (Math.random() * 10);
                number3.setText(Integer.toString(rngesus3));
                handler.postDelayed(this, mInterval);
            }
        };

        start.setOnClickListener(v -> {
            stopCount = 1;
            handler.removeCallbacks(runnable);
            handler.removeCallbacks(runnable2);
            handler.removeCallbacks(runnable3);
            mInterval = mPreferences.getInt("INTERVAL", 5) * 100;
            if (!isRunning) {
                isRunning = true;
                handler.post(runnable);
                handler.post(runnable2);
                handler.post(runnable3);
            } else {
                handler.postDelayed(runnable, mInterval);
                handler.postDelayed(runnable2, mInterval);
                handler.postDelayed(runnable3, mInterval);
            }
        });

        settings.setOnClickListener((view) -> {
            Intent intent = new Intent(getActivity(), SharedPreferencesActivity.class);
            startActivity(intent);
        });

        stop.setOnClickListener(v -> {
            if (stopCount == 0) {
                stopCount += 1;
            } else if (stopCount == 1) {
                stopCount += 1;
                handler.removeCallbacks(runnable);
            } else if (stopCount == 2) {
                stopCount += 1;
                handler.removeCallbacks(runnable2);
            } else if (stopCount == 3) {
                stopCount = 0;
                isRunning = false;
                handler.removeCallbacks(runnable3);
                //default jackpot number 7
                mJackpot = mPreferences.getInt("JACKPOT_NUM", 7);
                if (rngesus1 == rngesus2 && rngesus2 == rngesus3) {
                    if (rngesus1 == mJackpot) {
                        sendNotification();
                    }
                }
            } else {
                stopCount = 0;
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initializeUI(View root) {
        number1 = root.findViewById(R.id.text_number1);
        number2 = root.findViewById(R.id.text_number2);
        number3 = root.findViewById(R.id.text_number3);
        start = root.findViewById(R.id.start_button);
        stop = root.findViewById(R.id.stop_button);
        settings = root.findViewById(R.id.setting_button);
    }

    public void sendNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity(), "default")
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("JACKPOT")
                        .setContentText("jackpot cuy!");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(01, mBuilder.build());

    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }

}
