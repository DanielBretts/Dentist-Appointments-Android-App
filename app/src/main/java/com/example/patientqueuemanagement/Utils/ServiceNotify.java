package com.example.patientqueuemanagement.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.patientqueuemanagement.R;

/**
 * The ServiceNotify is an android service to handle the "confirm appointment" notification
 */
public class ServiceNotify extends Service {

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("DoctorQueue","DoctorQueue", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        String action = intent.getAction();
        if(action.equals(ACTION_START)) {
            createNotification();
        }
        //START_STICKY Lets the service stay alive after destroying the app
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"DoctorQueue");
        builder.setContentTitle("You made an appointment with Dr. Brettschneider");
        builder.setContentText(getAppointmentFormat());
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.icon);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH).setDefaults(NotificationCompat.DEFAULT_ALL);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,builder.build());

    }

    /**
     * This function formats the notification text
     */
    private String getAppointmentFormat() {
        SharedPreferences sharedPreferences = SharedPreferences.getInstance();
        String day = (sharedPreferences.getString(SharedPreferences.DAY).replace("\"",""));
        String month = (sharedPreferences.getString(SharedPreferences.MONTH).replace("\"",""));
        String year = (sharedPreferences.getString(SharedPreferences.YEAR).replace("\"",""));
        String hour = sharedPreferences.getString(SharedPreferences.HOUR).replace("\"","");
        String dayName = sharedPreferences.getString(SharedPreferences.DAY_NAME).replace("\"","");
        return String.format("%s %s %s.%s.%s",dayName,hour,day,month,year);
    }
}
