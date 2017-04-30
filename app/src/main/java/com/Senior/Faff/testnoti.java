package com.Senior.Faff;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.model.UserProfile;

public class testnoti extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testnoti);

        final NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                       .setSmallIcon(R.drawable.faff_logo)
                       .setContentTitle("My notification")
                       .setContentText("Hello World!")
                       .setAutoCancel(true);

        Intent resultIntent = new Intent(this, ShowUserprofile.class);
        resultIntent.putExtra(UserProfile.Column.Ownerid,"a1412");
        resultIntent.putExtra(UserProfile.Column.UserID,"a1412");
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
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
                    @Override
                    public void run() {
                        int incr;
                       mBuilder.setProgress(0, 0, true);
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr += 5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                          //  mBuilder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                          //  mNotificationManager.notify(0, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time

                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {

                                }
                            }
                            // When the loop is finished, updates the notification
                            mBuilder.setContentText("Download complete")
                                    // Removes the progress bar
                                    .setProgress(0, 0, false);
                            mNotificationManager.notify(0, mBuilder.build());
                        }

                }
// Starts the thread by calling the run() method in its Runnable
        ).start();


    }
}
