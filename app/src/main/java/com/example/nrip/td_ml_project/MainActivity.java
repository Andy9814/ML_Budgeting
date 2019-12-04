package com.example.nrip.td_ml_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nrip.td_ml_project.animation.Animator;
import com.example.nrip.td_ml_project.animation.FadeInAnimation;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;

import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    Button tvCamBtn, tvManuallAddingBtn;
    TextView tvOR;
    EditText manualPrice, autoInvest;
    Intent goToBudgetLayout;
    String tvAutoInvest = "";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeControls();

        goToBudgetLayout = new Intent(this, BudgetLayout.class);

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        tvAutoInvest = prefs.getString("sharedPrefAutoInvest", "");
        if (firstStart) {
            showStartDialog();
        }

        AnimateControls();
    }

    void InitializeControls(){
        tvCamBtn = findViewById(R.id.btnCamera);
        tvManuallAddingBtn = findViewById(R.id.btnManualPriceAdding);
        tvOR = findViewById(R.id.tvOR);

        tvCamBtn.setVisibility(View.INVISIBLE);
        tvManuallAddingBtn.setVisibility(View.INVISIBLE);
        tvOR.setVisibility(View.INVISIBLE);
    }

    void AnimateControls() {
        Animation fadeIn1 = new FadeInAnimation(0, 1, 700);
        fadeIn1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                tvCamBtn.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        Animation fadeIn2 = new FadeInAnimation(0, 1, 700);
        fadeIn2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                tvOR.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        Animation fadeIn3 = new FadeInAnimation(0, 1, 700);
        fadeIn3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                tvManuallAddingBtn.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        Queue<Pair<View,Animation>> animationQueue = new LinkedList<>();
        animationQueue.add(new Pair<View, Animation>(tvCamBtn,fadeIn1));
        animationQueue.add(new Pair<View, Animation>(tvOR,fadeIn2));
        animationQueue.add(new Pair<View, Animation>(tvManuallAddingBtn,fadeIn3));

        Animator animator = new Animator(animationQueue);
        animator.PlayAllUsingDelay(500);
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
