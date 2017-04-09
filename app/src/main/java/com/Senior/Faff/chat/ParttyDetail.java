package com.Senior.Faff.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.Senior.Faff.R;

public class ParttyDetail extends AppCompatActivity {

    Button ok;

    TextView party_name;
    TextView party_member_size;
    TextView party_detail;
    TextView party_appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partty_detail);

        party_name = (TextView) findViewById(R.id.party_name_edit);
        party_member_size = (TextView) findViewById(R.id.member_size_edit);
        party_detail = (TextView) findViewById(R.id.party_detail_edit);
        party_appointment = (TextView) findViewById(R.id.appointment_party_edit);

        ok = (Button) findViewById(R.id.party_detail_ok);

        Intent i = getIntent();

        party_name.setText(i.getExtras().get("name").toString());
        party_member_size.setText(i.getExtras().get("member_size").toString());
        party_detail.setText(i.getExtras().get("detail").toString());
        party_appointment.setText(i.getExtras().get("appointment").toString());

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
