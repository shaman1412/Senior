package com.Senior.Faff.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.BitmapImageManager;
import com.Senior.Faff.utils.Helper;
import com.Senior.Faff.utils.LoadingFragment;
import com.Senior.Faff.utils.ProfileManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class ShowUserprofile extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView text;
    private ProfileManager profileManager;
    private UserProfile userProfile;
    private String userid;
    private static Context mcontext;
    private TextView name, age, address, email, telephone, gender;
    private ArrayList<String> favourite_type;
    private RecyclerView mRecyclerView;
    private ListTypeNodel list_adapter;
    private FloatingActionButton fab;
    private int width;
    private int height;
    private String ownerid;
    private static ImageView image;

    private static FrameLayout loading;
    private static LoadingFragment loadingFragment;
    private CoordinatorLayout inc;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_userprofile);
        setTitle("Profile");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        mcontext = this;
        Bundle args = getIntent().getExtras();
        userid = args.getString(UserProfile.Column.UserID, null); // id เข้าของ profile ที่ดู
        ownerid = args.getString(UserProfile.Column.Ownerid,null); // id ของเข้าของบัญชีขณะนั้น
        if(userid.equals(ownerid)) {
            View a = findViewById(R.id.fab);
            a.setVisibility(View.VISIBLE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        address = (TextView) findViewById(R.id.address);
        email = (TextView) findViewById(R.id.gemail);
        telephone = (TextView) findViewById(R.id.gtelephone);
        gender = (TextView) findViewById(R.id.ggender);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        inc = (CoordinatorLayout) findViewById(R.id.inc);
        loading = (FrameLayout) inc.findViewById(R.id.loading);
        inc.bringChildToFront(loading);

/*        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle("Title");*/


        text = (TextView) findViewById(R.id.text);
        profileManager = new ProfileManager(this);
        userProfile = new UserProfile();
        //  String json = profileManager.getUserProfile(id);
        // text.setText(json);
        BitmapImageManager bitmap = new BitmapImageManager();
        image = (ImageView) findViewById(R.id.profile_image);
//        image.setImageBitmap(bitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.woman, 100, 100));

        final NestedScrollView childScroll = (NestedScrollView) findViewById(R.id.scroll);

        ShowUserPro sh = new ShowUserPro(new ShowUserPro.AsyncResponse() {
            @Override
            public void processFinish(final String output) throws JSONException {
                JSONObject item = new JSONObject(output);

                ArrayList<String[]> bitmap_url_list = new ArrayList<>();

                String[] arr_url = item.getString("picture").split(",");
                bitmap_url_list.add(arr_url);
                final UserProfile userpro = new Gson().fromJson(item.toString(), UserProfile.class);
                userid = userpro.getUserid();

                if (userid != null) {
                    name.setText(userpro.getName());
                    age.setText(String.valueOf(userpro.getAge()));
                    address.setText(userpro.getAddress());
                    email.setText(userpro.getEmail());
                    telephone.setText(userpro.getTelephone());

                    String img_path_tmp = userpro.getPicture();
                    String[] img_path = img_path_tmp.split(",");

                    try {
//                        Picasso.with(mcontext).load(img_path[0]).resize(width+100, height+100).centerInside().into(image);
                        Picasso.with(mcontext).load(img_path[0]).fit().into(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    switch (userpro.getGender()) {
                        case 0:
                            gender.setText("Female");
                            break;
                        case 1:
                            gender.setText("Male");
                            break;
                    }
                    if (userpro.getFavourite_type() != null) {
                        String[] list = userpro.getFavourite_type().split(",");
                        favourite_type = new ArrayList<String>();
                        for (int i = 0; i < list.length; i++) {
                            favourite_type.add(list[i]);
                        }
                        list_adapter = new ListTypeNodel(favourite_type, mcontext);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
                        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(list_adapter);
                    }

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(mcontext, UpdateUserProfile.class);
                            intent.putExtra("user_profile", output);
                            intent.putExtra(UserProfile.Column.UserID, userid);
                            startActivity(intent);
                        }
                    });

                }

                if(loadingFragment!=null)
                {
                    loadingFragment.onStop();
                    hideLoading();
                }
            }
        });
        sh.execute(userid);

    }


    private static class ShowUserPro extends AsyncTask<String, String, String> {

        String result = "";

        public interface AsyncResponse {
            void processFinish(String output) throws JSONException;
        }

        public AsyncResponse delegate = null;

        public ShowUserPro(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                showLoading();
                loadingFragment = new LoadingFragment();
                ((AppCompatActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.loading, loadingFragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {

                URL url = new URL("https://faff-1489402013619.appspot.com/user/" + params[0].toString());
                //URL url = new URL("http://localhost:8080/promotion_list");

                Helper hp = new Helper();
                hp.setRequest_method("GET");
                result = hp.getRequest(url.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                //Toast.makeText(mcontext, result, Toast.LENGTH_LONG).show();
                try {
                    delegate.processFinish(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Toast.makeText(mcontext, "Fail to retrieve from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        hideLoading();
        ShowUserPro sh = new ShowUserPro(new ShowUserPro.AsyncResponse() {
            @Override
            public void processFinish(final String output) throws JSONException {
                JSONObject item = new JSONObject(output);

                ArrayList<String[]> bitmap_url_list = new ArrayList<>();

                String[] arr_url = item.getString("picture").split(",");
                bitmap_url_list.add(arr_url);
                final UserProfile userpro = new Gson().fromJson(item.toString(), UserProfile.class);
                userid = userpro.getUserid();

                if (userid != null) {
                    name.setText(userpro.getName());
                    age.setText(String.valueOf(userpro.getAge()));
                    address.setText(userpro.getAddress());
                    email.setText(userpro.getEmail());
                    telephone.setText(userpro.getTelephone());

                    String img_path_tmp = userpro.getPicture();
                    String[] img_path = img_path_tmp.split(",");

                    try {
//                        Picasso.with(mcontext).load(img_path[0]).resize(width+100, height+100).centerInside().into(image);
                        Picasso.with(mcontext).load(img_path[0]).fit().into(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    switch (userpro.getGender()) {
                        case 0:
                            gender.setText("Female");
                            break;
                        case 1:
                            gender.setText("Male");
                            break;
                    }
                    if (userpro.getFavourite_type() != null) {
                        String[] list = userpro.getFavourite_type().split(",");
                        favourite_type = new ArrayList<String>();
                        for (int i = 0; i < list.length; i++) {
                            favourite_type.add(list[i]);
                        }
                        list_adapter = new ListTypeNodel(favourite_type, mcontext);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
                        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(list_adapter);
                    }

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(mcontext, UpdateUserProfile.class);
                            intent.putExtra("user_profile", output);
                            intent.putExtra(UserProfile.Column.UserID, userid);
                            startActivity(intent);
                        }
                    });

                }
                if(loadingFragment!=null)
                {
                    loadingFragment.onStop();
                    hideLoading();
                }
            }
        });
        sh.execute(userid);
    }


    //    private class getData extends AsyncTask<String, String, UserProfile> {
//
//        String pass;
//        int responseCode;
//        HttpURLConnection connection;
//        String resultjson;
//        @Override
//        protected UserProfile doInBackground(String... args) {
//            StringBuilder result = new StringBuilder();
//            String url_api = "https://faff-1489402013619.appspot.com/user/" + args[0];
//            try {
//                URL url = new URL(url_api);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                InputStream in = new BufferedInputStream(connection.getInputStream());
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//                responseCode = connection.getResponseCode();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    result.append(line);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (responseCode == 200) {
//                Log.i("Request Status", "This is success response status from server: " + responseCode);
//                Gson gson = new Gson();
//                UserProfile userPro  =  gson.fromJson(result.toString(),  UserProfile.class);
//                return userPro;
//            } else {
//                Log.i("Request Status", "This is failure response status from server: " + responseCode);
//                return null ;
//
//            }
//
//        }
//        @Override
//        protected void onPostExecute(UserProfile userpro) {
//            super.onPostExecute(userpro);
//            if (userpro != null) {
//                name.setText(userpro.getName());
//                age.setText(String.valueOf(userpro.getAge()));
//                address.setText(userpro.getAddress());
//                email.setText(userpro.getEmail());
//                telephone.setText(userpro.getTelephone());
//
//                String img_path_tmp = userpro.getPicture();
//                String[] img_path = img_path_tmp.split(",");
//                for(String s:img_path)
//                {
//                    Log.i("TEST: ", "  String s is : "+s);
//                }
//
//                switch (userpro.getGender()){
//                    case 0:
//                        gender.setText("Female");
//                        break;
//                    case 1:
//                        gender.setText("Male");
//                        break;
//                }
//                if(userpro.getFavourite_type() != null){
//                    String[] list = userpro.getFavourite_type().split(",");
//                    favourite_type = new ArrayList<String>();
//                    for(int i = 0; i < list.length; i++){
//                        favourite_type.add(list[i]);
//                    }
//                    list_adapter = new ListTypeNodel(favourite_type, mcontext);
//                    LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mcontext);
//                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                    mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
//                    mRecyclerView.setLayoutManager(mLayoutManager);
//                    mRecyclerView.setAdapter(list_adapter);
//                }
//
//
//
//            }
//            else{
//                String message = getString(R.string.login_error_message);
//                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void showLoading(){
        if(loading!=null)
        {
            if(loading.getVisibility()==View.GONE)
            {
                loading.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void hideLoading(){
        if(loading!=null)
        {
            if(loading.getVisibility()==View.VISIBLE)
            {
                loading.setVisibility(View.GONE);
            }
        }
    }

}
