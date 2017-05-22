package com.Senior.Faff;

import android.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;

import com.Senior.Faff.Fragment.Party.Room_Recycler;
import com.Senior.Faff.Fragment.Party.Show_party_profile;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.UserProfile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by InFiNity on 28-Apr-17.
 */

public class server_service extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback  {
    private Looper mServiceLooper;
    private GoogleApiClient googleApiClient;
    private ServiceHandler mServiceHandler;
    private Message getmsg;
    private Location mLastLocation;
    private ArrayList<Party> party_list;
    private ArrayList<Party> re_list = new ArrayList<>();
    private ArrayList<Party> re_list_new = new ArrayList<>();
    private int count_room_old = 0;
    private int count_room_new = 0;
    private boolean check_first;
    private  String user_id;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
            if (locationAvailability != null) {

                LocationRequest locationRequest = new LocationRequest()  // ใช้สำหรับ onlicationchange ทำเรื่อยๆ
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(2000)
                        .setFastestInterval(2000);
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                }

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);  // ใช้สำหรัรับตำแหน่งแรกเลย ครั้งเดียว

            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(server_service.this, "Location was changed", Toast.LENGTH_SHORT).show();
        mLastLocation = location;
        String show_location = mLastLocation.toString();

/*        new Thread(
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

        ).start();*/

    }

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
            check_first = true;
           show();

            googleApiClient.connect();

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
        googleApiClient = new GoogleApiClient.Builder(server_service.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(server_service.this)
                .addOnConnectionFailedListener(server_service.this)
                .build();
        SharedPreferences sp = getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        user_id = sp.getString(UserProfile.Column.UserID,"nothing");
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
        new check_room().execute();
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
    public class check_room extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            mRootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.child("All_Room").getChildrenCount();
                    party_list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.child("All_Room").getChildren()) {
                        Party post = postSnapshot.getValue(Party.class);
                        party_list.add(post);
                    }
                    get_room(party_list,0,23);
                    //Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                   // Toast.makeText(server_serice.this, "Cant connect database", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }
    public ArrayList<Party> calculatedistance(int de_distance, Map<String, String> rule_gender, Map<String, Integer> rule_age, ArrayList<Party> listRes) {
        int distance;

        float latitude, longtitude;
        mLastLocation  = new Location("Target");
        mLastLocation.setLatitude(13.6529);
        mLastLocation.setLongitude(100.486);
        ArrayList<Party> res = new ArrayList<>();
        for (int i = 0; i < listRes.size(); i++) {
            String[] position = listRes.get(i).getLocation().split(",");
            latitude = Float.parseFloat(position[0]);
            longtitude = Float.parseFloat(position[1]);
            Location target = new Location("Target");
            target.setLatitude(latitude);
            target.setLongitude(longtitude);
            if (mLastLocation != null) {
                distance = (int) mLastLocation.distanceTo(target);
                if (distance <= de_distance) {
                    int countcheck = 0;
                    for (Map.Entry<String, String> check_rule : rule_gender.entrySet()) {
                        String check = listRes.get(i).getRule().get(check_rule.getKey());
                        String a = check_rule.getValue();
                        if (check != null) {
                            String[] sp = check.split(",");
                            for (int j = 0; j < sp.length; j++)
                                if (sp[j].equals(a)) {
                                    countcheck++;
                                }
                        } else {
                            countcheck++;
                        }
                    }
                    for (Map.Entry<String, Integer> check_rule : rule_age.entrySet()) {
                        String check = listRes.get(i).getRule().get(check_rule.getKey());
                        int a = check_rule.getValue();
                        if (check != null) {
                            int b = Integer.valueOf(check);
                            if (a < b) {
                                countcheck++;
                            }
                        } else {
                            countcheck++;
                        }
                        if (countcheck == 2) {
                            countcheck = 0;
                            res.add(listRes.get(i));
                        }
                    }
                }
            }
        }
        return res;
    }

    public void get_room(ArrayList<Party> Pary_list, int gender, int age) {

        Map<String, String> rule_gender = new HashMap<>();
        if (gender == 0) {
            rule_gender.put("gender", "Female");
        } else {
            rule_gender.put("gender", "Male");
        }
        Map<String, Integer> rule_age = new HashMap<>();
        rule_age.put("age", age);
        // Restaurant_manager res_manager = new Restaurant_manager(mcontext);
        if (Pary_list != null) {
            if(check_first){
                re_list = calculatedistance(2000, rule_gender, rule_age, Pary_list); // 900 meter
                count_room_old = re_list.size();
                check_first = false;
            }else{
                int count_now = 0;
                 Party new_room = new Party();
                re_list_new = calculatedistance(2000, rule_gender, rule_age, Pary_list);
                count_room_new = re_list_new.size();
                for(int i =0;i<re_list_new.size(); i++){
                    boolean check_same = false;
                    for(int j=0 ; j < re_list.size() ; j++){
                        if(re_list.get(j).getRoomID().equals(re_list_new.get(i).getRoomID())){
                            check_same = true;
                            count_now++;
                        }
                    }
                    if(!check_same){
                        new_room = re_list_new.get(i);
                    }
                }
                if(count_now < count_room_new) {
                    send_notification(new_room);
                }
                count_room_old = count_room_new;
                re_list = re_list_new;
            }
        }
    }
    public void send_notification(Party new_room){
        final NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.newlogo)
                        .setContentTitle("New Party Near You")
                        .setContentText("   Title:" + new_room.getName() )
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(getApplicationContext(), Show_party_profile.class);
        resultIntent.putExtra(UserProfile.Column.UserID,user_id);
        resultIntent.putExtra(Party.Column.RoomID,new_room.getRoomID());
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

        int request_id = getmsg.arg1;
        mNotificationManager.notify(request_id, mBuilder.build());
    }



}
