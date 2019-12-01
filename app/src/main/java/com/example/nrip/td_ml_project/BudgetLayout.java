package com.example.nrip.td_ml_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nrip.td_ml_project.animation.ProgressBarAnimation;
import com.example.nrip.td_ml_project.models.BudgetCalculator;
import com.example.nrip.td_ml_project.models.BudgetEntry;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;
import pl.droidsonroids.gif.GifImageView;

public class BudgetLayout extends AppCompatActivity {



    String TAG ="LogCatDemo";
    ArrayList<BudgetEntry> budgetEntries;
    BigDecimal weeklyBudget;
    BudgetCalculator bc;
    Button contBtn;

    BigDecimal budgTotalWeekly;
    BigDecimal budgAlreadySpent;
    BigDecimal budgToBeSpent;

    ProgressBar firstBar = null;
    TextView tvBudgetToBeSpent = null;
    TextView tvCurrentBudget = null;


    Chip priceChip,taxChip;
    Animation animation;
    AnimationSet animationSet,animationSetT,animationSetcd ;
    GifImageView gifImageButton;
    BigDecimal price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

      //  setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_layout);
        contBtn = findViewById(R.id.contBtn);
        priceChip= findViewById(R.id.priceChip);
        taxChip= findViewById(R.id.TaxChip);
        gifImageButton = findViewById(R.id.gifImageView);
        this.onLoadData();
        Intent intt = getIntent();
        Bundle extras = intt.getExtras();


       validatePrice(extras.getString("costImage"));
       priceChip.setText("Price :"+ extras.getString("costImage"));
       // priceChip.setText("Price : 300.65");




        for(int i  = 0 ; i < 2 ; ++i){
            if(i  == 0) {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(6000);
                animationSet = new AnimationSet(true); //change to false
                animationSet.addAnimation(fadeIn);
                priceChip.setAnimation(animationSet);
            }
            else{
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(10000);
                animationSetT = new AnimationSet(true); //change to false
                animationSetT.addAnimation(fadeIn);
                taxChip.setAnimation(animationSetT);
            }
        }

        budgTotalWeekly = weeklyBudget;
        budgAlreadySpent = new BigDecimal(350);
        budgToBeSpent = new BigDecimal(100);

        tvBudgetToBeSpent = (TextView) findViewById(R.id.tvBudgetToBeSpent);
        tvCurrentBudget = (TextView) findViewById(R.id.tvCurrentBudget);

        tvCurrentBudget.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(budgTotalWeekly)));

        firstBar = (ProgressBar)findViewById(R.id.firstBar);
        firstBar.setMax(budgTotalWeekly.add(new BigDecimal(0.99)).intValue());
        firstBar.setProgress(budgAlreadySpent.intValue());



        ProgressBarAnimation anim1 = new ProgressBarAnimation(firstBar, false, 0, budgAlreadySpent.intValue(),tvBudgetToBeSpent);
        anim1.setDuration(400);
        firstBar.startAnimation(anim1);

        firstBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                //tv.setX((int)(firstBar.getProgress()/firstBar.getWidth()));
                //tv.setX(40);
                ProgressBarAnimation anim2 = new ProgressBarAnimation(firstBar, true, budgAlreadySpent.intValue(), budgAlreadySpent.add(budgToBeSpent).intValue(),tvBudgetToBeSpent);
                anim2.setDuration(4000);
                firstBar.startAnimation(anim2);
            }
        }, 400);

    }


    private void validatePrice(String x){
        Log.d("String is  === =========   ",x);
        String tvPrice  = "";
        boolean foundDollar = false;
        for (int i = 0; i < x.length(); i++){
            char c = x.charAt(i);
            if(foundDollar){
                tvPrice +=  c ;
            }
            else if(c =='$'){
                foundDollar = true;
            }else{
                Log.d("","No dollar found");
            }
        }
        price = new BigDecimal( tvPrice);
        if(foundDollar) {
            comparePrices();
        }
    }

    public void comparePrices(){
        if(price.doubleValue() <= weeklyBudget.doubleValue())
        {
            gifImageButton.setImageResource(R.drawable.checkmarksingleplay);
            Drawable drawable = gifImageButton.getDrawable();
            if (drawable instanceof Animatable)
            {
                ((Animatable) drawable).start();
            }
        }
        else{
            gifImageButton.setImageResource(R.drawable.xmarksingleplay);
            Drawable drawable = gifImageButton.getDrawable();
            if (drawable instanceof Animatable)
            {
                ((Animatable) drawable).start();
            }
        }
        // disable the continue button
        contBtn.setEnabled(false);
    }
    public void onLoadData() {
        bc = new BudgetCalculator();
        try {
            // read in the file
            InputStream ins = getResources().openRawResource(
                    getResources().getIdentifier("csv35639",
                            "raw", getPackageName()));

            // parse the data from the file
            budgetEntries = bc.parseData(ins);

            // calculate budget
            weeklyBudget = bc.calcBudget(budgetEntries);

        } catch (IOException e) {
            Log.d(TAG, e.toString());
        } catch (ParseException e) {
            Log.d(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public void onClickContinueBtn(View view) {
        Intent intent = new Intent(BudgetLayout.this, InvestmentLayout.class);
        startActivity( intent );
    }
}
