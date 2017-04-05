package com.Senior.Faff.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.Senior.Faff.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.Senior.Faff.R.id.map;

public class CreateParty extends AppCompatActivity {

    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Party").child("party_list");

    EditText party_name;
    EditText party_member_size;
    EditText party_detail;
    EditText party_appointment;

    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        party_name = (EditText) findViewById(R.id.party_name_edit);
        party_member_size = (EditText) findViewById(R.id.member_size_edit);
        party_detail = (EditText) findViewById(R.id.party_detail_edit);
        party_appointment = (EditText) findViewById(R.id.appointment_party_edit);
        create = (Button) findViewById(R.id.creat_party_button);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = party_name.getText().toString();
                String member_size = party_member_size.getText().toString();
                String detail = party_detail.getText().toString();
                String appointment = party_appointment.getText().toString();

                Map<String, Object> map = new HashMap<String, Object>();

                map.put(name, "");
                root.updateChildren(map);

                DatabaseReference child = root.child(name);
                map.clear();
                map.put("name", name);
                child.updateChildren(map);

                map.clear();
                map.put("member_size", member_size);
                child.updateChildren(map);

                map.clear();
                map.put("detail", detail);
                child.updateChildren(map);

                map.clear();
                map.put("appointment", appointment);
                child.updateChildren(map);

                finish();
            }
        });






    }
}
