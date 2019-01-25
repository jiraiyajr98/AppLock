package com.duologic.ankan.applock;


import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.duologic.ankan.applock.dataobjects.LockObject;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static com.duologic.ankan.applock.utils.App.CHANNEL_ID;

public class ExampleService extends Service {

    private static Timer timer;
    public static final String TAG = "APPLICATION";
    private String prev="PKG";
    private Context context;
    private Dialog dialog;
    ImageView imageView;
    private WindowManager windowManager;
    public static List<LockObject> list = new ArrayList<>();
    public static ArrayList<String> selectedList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        imageView = new ImageView(this);
        imageView.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.START | Gravity.TOP;
            params.x = ((getApplicationContext().getResources().getDisplayMetrics().widthPixels) / 2);
            params.y = ((getApplicationContext().getResources().getDisplayMetrics().heightPixels) / 2);
            windowManager.addView(imageView, params);

        } else {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);


            params.gravity = Gravity.START | Gravity.TOP;
            params.x = ((getApplicationContext().getResources().getDisplayMetrics().widthPixels) / 2);
            params.y = ((getApplicationContext().getResources().getDisplayMetrics().heightPixels) / 2);
            windowManager.addView(imageView, params);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_service_icon)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        timer = new Timer();
        timer.scheduleAtFixedRate(new CheckAppTask(), 0, 500);


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        if (imageView != null) {
            windowManager.removeView(imageView);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class CheckAppTask extends TimerTask
    {
        public void run()
        {

            String topPackageName;
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                // We get usage stats for the last 10 seconds
                List <UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
                // Sort the stats by the last time used
                if (stats != null) {
                    SortedMap< Long, UsageStats > mySortedMap = new TreeMap< Long, UsageStats >();
                    for (UsageStats usageStats: stats) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (!mySortedMap.isEmpty()) {
                        topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();

                        if(!prev.equals(topPackageName)) {
                            Log.e(TAG, topPackageName);
                            prev = topPackageName;

                            if(selectedList.contains(prev))
                            {
                                if(imageView != null)
                                {
                                    imageView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                showDialog(); }
                                        });
                                }
                            }


                        }
                        else {
                            prev = topPackageName;

                            if(!selectedList.contains(prev))
                            {
                                imageView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (dialog != null) {
                                                    if (dialog.isShowing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                            }

                        }
                    }
                }


        }
    }


    void showDialog() {

        if (context == null)
            context = getApplicationContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptsView = layoutInflater.inflate(R.layout.popup_unlock, null, false);

        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        }
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(promptsView);
        dialog.getWindow().setGravity(Gravity.CENTER);

        Button unlock = (Button) promptsView.findViewById(R.id.unlock);

        final EditText pass = (EditText)promptsView.findViewById(R.id.pass);



        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (dialog != null) {

                        if(pass.getText().toString().equals("12345")) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                        else
                            pass.setError("Wrong Password");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }
                return true;
            }
        });

        dialog.show();

    }
}
