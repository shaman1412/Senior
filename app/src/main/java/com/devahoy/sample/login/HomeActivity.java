package com.devahoy.sample.login;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devahoy.sample.login.model.Promotion;
import com.devahoy.sample.login.utils.PromotionManager;

public class HomeActivity extends ActionBarActivity {

    private Button addPromotion;
    private EditText Title;
    private EditText TitlePicture;
    private EditText StartDate;
    private EditText EndDate;
    private EditText PromotionDetail;
    private EditText Location;
    private Context mContext;

    private PromotionManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mManager = new PromotionManager(this);

        mContext = this;

        addPromotion = (Button) findViewById(R.id.addPromotion);
        Title = (EditText) findViewById(R.id.title);
        TitlePicture = (EditText) findViewById(R.id.titlePicture);
        StartDate = (EditText) findViewById(R.id.startDate);
        EndDate = (EditText) findViewById(R.id.endDate);
        PromotionDetail = (EditText) findViewById(R.id.promotionDetail);
        Location = (EditText) findViewById(R.id.location);

        addPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPromotion();
            }
        });
    }

    private void AddPromotion() {
        String title = Title.getText().toString();
        String titlePicture = TitlePicture.getText().toString();
        String startDate = StartDate.getText().toString();
        String endDate = EndDate.getText().toString();
        String promotionDetail = PromotionDetail.getText().toString();
        String location = Location.getText().toString();

        Promotion promotion = new Promotion(title, titlePicture, startDate, endDate, promotionDetail,location);

        long rowId = mManager.addPromotion(promotion);

        if (rowId == -1) {
            String message = getString(R.string.register_error_message);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } else {
            String message = getString(R.string.register_success);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
