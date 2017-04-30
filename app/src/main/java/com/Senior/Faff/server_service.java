package com.Senior.Faff;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;
import android.os.Process;

import com.Senior.Faff.UserProfile.ShowUserprofile;

/**
 * Created by InFiNity on 28-Apr-17.
 */

public class server_service extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private Message getmsg;
    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            getmsg = msg;
           show();

            final NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.faff_logo)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!")
                            .setAutoCancel(true);

            Intent resultIntent = new Intent(getApplicationContext(), ShowUserprofile.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(Main2Activity.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            final NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            new Thread(
                    new Runnable() {
                        int request_id = getmsg.arg1;
                        @Override
                        public void run() {
                            int incr;

                           // mBuilder.setProgress(0, 0, true);
                            // Do the "lengthy" operation 20 times
                            for (incr = 0; incr <= 50; incr += 1) {
                                // Sets the progress indicator to a max value, the
                                // current completion percentage, and "determinate"
                                // state
                                mBuilder.setProgress(100, incr, false);
                                // Displays the progress bar for the first time.
                                 mNotificationManager.notify(request_id, mBuilder.build());
                                // Sleeps the thread, simulating an operation
                                // that takes time

                                try {
                                    // Sleep for 5 seconds
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {

                                }
                            }
                            // When the loop is finished, updates the notification
                            mBuilder.setContentText("Download complete")
                                    // Removes the progress bar
                                    .setProgress(0, 0, false);
                            mNotificationManager.notify(request_id, mBuilder.build());

                           stopSelf(request_id);
                        }


                    }
// Starts the thread by calling the run() method in its Runnable
            ).start();
           // stopSelf(getmsg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
    public class MyThread implements Runnable {

        public MyThread(Object parameter) {
            // store parameter for later user
        }

        public void run() {
        }
    }
    public  void  show(){
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Bobo")
                .setContentText("Hello World!")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(99, notification);
    }
}
