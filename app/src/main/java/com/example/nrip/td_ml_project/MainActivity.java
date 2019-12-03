package com.example.nrip.td_ml_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;

public class MainActivity extends AppCompatActivity {
    Button tvCamBtn, tvManuallAddingBtn;
    EditText manualPrice, autoInvest;
    Intent goToBudgetLayout;
    String tvAutoInvest = "";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCamBtn = findViewById(R.id.btnCamera);
        tvManuallAddingBtn = findViewById(R.id.btnManualPriceAdding);
        goToBudgetLayout = new Intent(this, BudgetLayout.class);

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        tvAutoInvest = prefs.getString("sharedPrefAutoInvest", "");
        if (firstStart) {
            showStartDialog();
        }


    }

    private void showStartDialog() {
        // also you can put the custom xml instead of using EditText.
        MaterialAlertDialogBuilder ad =      new MaterialAlertDialogBuilder(MainActivity.this)
                //.setTitle("AutoInvestment")
                .setMessage("Please Provide the AutoInvestment : ");
        autoInvest = new EditText(this);
        ad.setView(autoInvest);
        ad.setCancelable(false);
        ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("firstStart", false);
                editor.putString("sharedPrefAutoInvest", autoInvest.getText().toString());
                editor.apply();
            }
        })
                .create().show();


    }


    public void onClickCameraBtn(View view) {
        switch (view.getId()) {
            case R.id.btnCamera:
                Intent it = new Intent(this,AlertDialogActivity.class);
                tvAutoInvest = prefs.getString("sharedPrefAutoInvest", "");
                it.putExtra("AutoInvest",tvAutoInvest);
                startActivity(it);
                break;


            case R.id.btnManualPriceAdding:
                MaterialAlertDialogBuilder ad =      new MaterialAlertDialogBuilder(MainActivity.this)
                        //.setTitle("AutoInvestment")
                        .setMessage("Please Provide the Amount you want to spend : ");
                manualPrice = new EditText(this);
                ad.setView(manualPrice);
                ad.setCancelable(false);
                ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        tvAutoInvest = prefs.getString("sharedPrefAutoInvest", "");
                        goToBudgetLayout.putExtra("ManualPrice",manualPrice.getText().toString());
                        goToBudgetLayout.putExtra("autoInvestAmt",tvAutoInvest);
                        startActivity(goToBudgetLayout);
                    }
                }).create().show();



        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }



}
