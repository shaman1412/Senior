package com.Senior.Faff.UserProfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpdateUserProfile extends AppCompatActivity {
    private int genderid;
    private EditText name, email, address;
    private Spinner gender, type;
    private Button submit;
    private String userid;
    private Context mcontext;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> type_list;
    private String type_check;
    private List_type list_adapter;
    private boolean first = true;
    private String user_profile;
    private Button upload_picture;
    private EditText Tel;
    private EditText Age;
    private String old_picture = "";

    private static final int request_code = 1;                          //request code for OnClick result

    public static ArrayList<Bitmap> bmap = new ArrayList<>();           //keep bitmap data
    public static ArrayList<String> imgPath = new ArrayList<>();        //keep uri

    public static int image_count = 0;                                    //number of images

    public static UpdateUserProfileRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user_profile);
        image_count = 0;
        String[] values = {"Femail", "Male"};
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mcontext = this;

        type_list = new ArrayList<>();
        name = (EditText) findViewById(R.id.name);
        upload_picture = (Button) findViewById(R.id.picture_button);
        email = (EditText) findViewById(R.id.email);
        Tel = (EditText) findViewById(R.id.telephone);
        Age = (EditText) findViewById(R.id.age);
        address = (EditText) findViewById(R.id.address);
        gender = (Spinner) findViewById(R.id.gender);
        type = (Spinner) findViewById(R.id.favourite_type);
        submit = (Button) findViewById(R.id.submit);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        gender.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this, R.array.type_food_dropdown, android.R.layout.simple_spinner_item);
        adapter_type.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        type.setAdapter(adapter_type);

        upload_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_count < 1) {
                    Intent sdintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //sdintent.setType("image/*");
                    startActivityForResult(sdintent, request_code);
                }

            }
        });

        Bundle arg = getIntent().getExtras();

        if (arg != null) {
            userid = arg.getString("userid");
            user_profile = arg.getString("user_profile");
        }

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = type.getSelectedItem().toString();
                boolean same = false;

                for (int i = 0; i < type_list.size(); i++) {
                    if (text.equals(type_list.get(i))) {
                        same = true;
                    }
                }
                if (same == false && first == false) {
                    type_list.add(text);
                    list_adapter = new List_type(type_list, mcontext);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView = (RecyclerView) findViewById(R.id.list_show);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(list_adapter);

                }
                if (first == true) {
                    first = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {
                switch (pos) {
                    case 0:
                        genderid = 0;
                        break;
                    case 1:
                        genderid = 1;
                        break;
                }

               /* Toast.makeText(parent.getContext(),
                        "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();*/
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        //for ????
//        type_list = new ArrayList<>();
//        list_adapter = new List_type();


        new getData().execute(userid);

        //Userid  = arg.getInt(UserAuthen.Column.ID);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (list_adapter != null && list_adapter.getlist() != null) {
                        ArrayList<String> list_get = list_adapter.getlist();
                        for (int i = 0; i < list_get.size(); i++) {
                            if (i == 0) {
                                type_check = list_get.get(i);
                            } else {
                                type_check = type_check + list_get.get(i);
                            }
                            if (i != list_get.size() - 1) {
                                type_check = type_check + ",";
                            }
                        }
                    }
                    UserProfile user = new UserProfile(userid, name.getText().toString(), address.getText().toString(), email.getText().toString(), Tel.getText().toString(), type_check, genderid, Integer.parseInt(Age.getText().toString()), imgPath.get(0).toString());
                    UpdateUserProfile.AddUserProfile add_pro = new UpdateUserProfile.AddUserProfile();
                    add_pro.execute(user);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == request_code && data != null) {
                Uri selectedImg = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cur = getContentResolver().query(selectedImg, filePathColumn, null, null, null);
                if (cur == null) imgPath.add(null);
                else {
                    int column_index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cur.moveToFirst();
                    imgPath.add(cur.getString(column_index));
                    cur.close();
                }
                bmap.add(BitmapFactory.decodeFile(imgPath.get(image_count)));
                //convert to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmap.get(image_count).compress(Bitmap.CompressFormat.PNG, 100, stream);
//                imgByte.add(stream.toByteArray());
                image_count++;

                adapter = new UpdateUserProfileRecyclerView(this, bmap);
                RecyclerView lv = (RecyclerView) findViewById(R.id.rlist1);
                lv.setNestedScrollingEnabled(false);
                lv.setAdapter(adapter);
            }
        }
    }

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

    private class AddUserProfile extends AsyncTask<UserProfile, String, String> {

        private ArrayList<String> imgPath = new ArrayList<>();
        String result = "";

        @Override
        protected String doInBackground(UserProfile... params) {
            try {
                Map<String, String> paras = new HashMap<>();
                paras.put(UserProfile.Column.UserID, params[0].getUserid());
                paras.put(UserProfile.Column.Name, params[0].getName());
                paras.put(UserProfile.Column.Address, params[0].getAddress());
                paras.put(UserProfile.Column.Email, params[0].getEmail());
                paras.put(UserProfile.Column.Gender, String.valueOf(params[0].getGender()));
                paras.put(UserProfile.Column.Favourite_type, params[0].getFavourite_type());
                paras.put(UserProfile.Column.Age, String.valueOf(params[0].getAge()));
                paras.put(UserProfile.Column.Telephone, params[0].getTelephone());
                paras.put("old_filename", old_picture);

                Log.i("TEST:", " old_picture : "+old_picture);

                String img_path_tmp = params[0].getPicture();
                Log.i("TEST:", "img path tmp : "+img_path_tmp);
                UpdateUserProfile.AddUserProfile.this.imgPath = new ArrayList<String>(Arrays.asList(img_path_tmp));

                URL url = new URL("https://faff-1489402013619.appspot.com/user/" + params[0].getUserid());

                Helper hp = new Helper();
                hp.setRequest_method("PUT");
                result = hp.multipartRequest(url.toString(),paras, UpdateUserProfile.AddUserProfile.this.imgPath, "image", "image/jpeg");

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                ((Activity) mcontext).finish();
                //Toast.makeText(mcontext, result, Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(mcontext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private class addprofile extends AsyncTask<UserProfile, String, UserProfile> {
//
//        int responseCode;
//        HttpURLConnection connection;
//
//        @Override
//        protected UserProfile doInBackground(UserProfile... params) {
//
//            try {
//                JSONObject para = new JSONObject();
//                para.put(UserProfile.Column.Name, params[0].getName());
//                para.put(UserProfile.Column.Address, params[0].getAddress());
//                para.put(UserProfile.Column.Email,params[0].getEmail());
//                para.put(UserProfile.Column.Telephone, params[0].getTelephone());
//                para.put(UserProfile.Column.Favourite_type, params[0].getFavourite_type());
//
//                String urls = "https://faff-1489402013619.appspot.com/user/" + params[0].getUserid();
//                URL url = new URL(urls);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("PUT");
//                connection.setDoOutput(true);
//                connection.setDoInput(true);
//
//                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
//                BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//                buffer.write(getPostDataString(para));
//                buffer.flush();
//                buffer.close();
//                out.close();
//
//                responseCode = connection.getResponseCode();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (responseCode == 200) {
//                Log.i("Request Status", "This is success response status from server: " + responseCode);
//
//                return params[0];
//            } else {
//                Log.i("Request Status", "This is failure response status from server: " + responseCode);
//                return null;
//
//            }
//        }
//
//        @Override
//        protected void onPostExecute(UserProfile userProfile) {
//            if (userProfile != null) {
//                Toast.makeText(mcontext, type_check, Toast.LENGTH_LONG).show();
//                ((Activity) mcontext).finish();
//            } else {
//                Toast.makeText(mcontext, "Fail", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public String getPostDataString(JSONObject params) throws Exception {
//
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//
//        Iterator<String> itr = params.keys();
//
//        while (itr.hasNext()) {
//
//            String key = itr.next();
//            Object value = params.get(key);
//
//            if (first)
//                first = false;
//            else
//                result.append("&");
//
//            result.append(URLEncoder.encode(key, "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
//
//        }
//        return result.toString();
//    }

    private class getData extends AsyncTask<String, String, UserProfile> {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;

        @Override
        protected UserProfile doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/user/" + args[0];
            try {
                URL url = new URL(url_api);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream in = new BufferedInputStream(connection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                responseCode = connection.getResponseCode();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (responseCode == 200) {
                Log.i("Request Status", "This is success response status from server: " + responseCode);
                Gson gson = new Gson();
                UserProfile userPro = gson.fromJson(result.toString(), UserProfile.class);
                return userPro;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null;

            }

        }

        @Override
        protected void onPostExecute(UserProfile userpro) {
            super.onPostExecute(userpro);
            if (userpro != null) {
                try {
                    Log.i("TEST:", "userpro is : "+userpro.toString());

                    name.setText(userpro.getName());
                    if (userpro.getAddress()!=null) {
                        address.setText(userpro.getAddress());
                    }
                    if (userpro.getTelephone()!=null) {
                        Tel.setText(userpro.getTelephone());
                    }
                    if (userpro.getAge() != 0) {
                        Age.setText(String.valueOf(userpro.getAge()));
                    }
                    if(userpro.getEmail()!=null)
                    {
                        email.setText(userpro.getEmail());
                    }

                    if(userpro.getPicture()!=null)
                    {
                        old_picture = userpro.getPicture();
                    }

                    switch (userpro.getGender()) {
                        case 0:
                            gender.setSelection(0);
                            break;
                        case 1:
                            gender.setSelection(1);
                            break;
                    }
                    if (userpro.getFavourite_type() != null) {
                        String[] list = userpro.getFavourite_type().split(",");
                        for (int i = 0; i < list.length; i++) {
                            type_list.add(list[i]);
                        }
                        list_adapter = new List_type(type_list, mcontext);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
                        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mRecyclerView = (RecyclerView) findViewById(R.id.list_show);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(list_adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                String message = getString(R.string.login_error_message);
                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
            }

        }

    }
}
