package com.example.nrip.td_ml_project;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nrip.td_ml_project.animation.ProgressBarAnimation;
import com.example.nrip.td_ml_project.models.BudgetCalculator;
import com.example.nrip.td_ml_project.models.BudgetEntry;
import com.example.nrip.td_ml_project.models.UserAcount;
import com.google.android.material.chip.Chip;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class BudgetLayout extends AppCompatActivity {


    String TAG = "LogCatDemo";
    ArrayList<BudgetEntry> budgetEntries;
    BigDecimal weeklyBudget;
    BudgetCalculator bc;
    Button contBtn,plusBtn;

    BigDecimal budgTotalWeekly;
    BigDecimal budgAlreadySpent;
    BigDecimal budgToBeSpent;
    BigDecimal imagePrice;
    BigDecimal autoInvestAmt;
    BigDecimal tax = new BigDecimal(0.13);
    BigDecimal totalAmount;

    ProgressBar firstBar = null;
    TextView tvBudgetToBeSpent = null;
    TextView tvCurrentBudget = null;
    Boolean tvFoundDollarSign = false;


    Chip tvPriceChip, tvTaxChip, tvAutoInvestChip, tvTotalAmtChip;
    Animation animation;
    AnimationSet animationSet, animationSetT, animationSetcd;
    GifImageView gifImageButton;
    UserAcount userProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //  setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_layout);
        contBtn = findViewById(R.id.contBtn);
        tvPriceChip = findViewById(R.id.priceChip);
        tvTaxChip = findViewById(R.id.TaxChip);
        tvAutoInvestChip = findViewById(R.id.autoInvestChip);
        tvTotalAmtChip = findViewById(R.id.totalChip);
        plusBtn = findViewById(R.id.plusBtn);

        gifImageButton = findViewById(R.id.gifImageView);
        this.onLoadData();
        Intent intt = getIntent();
        Bundle extras = intt.getExtras();
        tvPriceChip.setText("Price :" + extras.getString("costImage"));
        tvAutoInvestChip.setText("Auto Invest:" + extras.getString("autoInvestAmt") + "%");
        tvTaxChip.setText("Tax : " + String.valueOf(NumberFormat.getCurrencyInstance().format(tax)));

        imagePrice = validatePrice(extras.getString("costImage"));
        autoInvestAmt = new BigDecimal(extras.getString("autoInvestAmt"));
        totalAmount = calculateTotal();
        tvTotalAmtChip.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(totalAmount)));

        if (tvFoundDollarSign) {
            comparePricesAndShowGif();
        }

        // Create UserProfile for this Trasaction
        userProfile = new UserAcount(this.imagePrice, this.autoInvestAmt);
        userProfile.setUserTotalAmount(totalAmount);

        //if(userProfile.isUserWantToBuyCheck())
            userProfile.setUserBudget(weeklyBudget.subtract(totalAmount));

        // FOR TEST PURPOSES ----------------- -------------------------------------- DELETE AFTER
//        tvPriceChip.setText("Price : " + 300);
//        tvTaxChip.setText("Tax : " + 4.2);
//        tvAutoInvestChip.setText("autoInvest : "+ 4+"%");
//        tvTotalAmtChip.setText("Total : "+ 4+"%");

        // Each Chips Animation Speed.
        for (int i = 0; i <= 3; ++i) {
            if (i == 0) {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(1000);
                animationSet = new AnimationSet(true); //change to false
                animationSet.addAnimation(fadeIn);
                tvPriceChip.setAnimation(animationSet);
            } else if (i == 1) {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(2000);
                animationSetT = new AnimationSet(true); //change to false
                animationSetT.addAnimation(fadeIn);
                tvTaxChip.setAnimation(animationSetT);
            } else if (i == 2) {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(3000);
                animationSetT = new AnimationSet(true); //change to false
                animationSetT.addAnimation(fadeIn);
                tvAutoInvestChip.setAnimation(animationSetT);
            } else {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(4000);
                animationSetT = new AnimationSet(true); //change to false
                animationSetT.addAnimation(fadeIn);
                tvTotalAmtChip.setAnimation(animationSetT);
            }
        }
        budgTotalWeekly = weeklyBudget;
        budgAlreadySpent = new BigDecimal(350);
        budgToBeSpent = new BigDecimal(100);

        tvBudgetToBeSpent = (TextView) findViewById(R.id.tvBudgetToBeSpent);
        tvCurrentBudget = (TextView) findViewById(R.id.tvCurrentBudget);

        tvCurrentBudget.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(budgTotalWeekly)));
        firstBar = (ProgressBar) findViewById(R.id.firstBar);
        firstBar.setMax(budgTotalWeekly.add(new BigDecimal(0.99)).intValue());
        firstBar.setProgress(budgAlreadySpent.intValue());
        ProgressBarAnimation anim1 = new ProgressBarAnimation(firstBar, false, 0, budgAlreadySpent.intValue(), tvBudgetToBeSpent);
        anim1.setDuration(400);
        firstBar.startAnimation(anim1);
        firstBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                //tv.setX((int)(firstBar.getProgress()/firstBar.getWidth()));
                //tv.setX(40);
                ProgressBarAnimation anim2 = new ProgressBarAnimation(firstBar, true, budgAlreadySpent.intValue(), budgAlreadySpent.add(budgToBeSpent).intValue(), tvBudgetToBeSpent);
                anim2.setDuration(4000);
                firstBar.startAnimation(anim2);
            }
        }, 400);
    }


    /*
    Validate Price
    it checks for $ sign in the scanned picture.
     */
    private BigDecimal validatePrice(String x) {
        Log.d("String is  === =========   ", x);
        String tvPrice = "";
        for (int i = 0; i < x.length(); i++) {
            char c = x.charAt(i);
            if (tvFoundDollarSign) {
                tvPrice += c;
            } else if (c == '$') {
                tvFoundDollarSign = true;
            } else {
                Log.d("", "No dollar found");


            }
        }
        return new BigDecimal(tvPrice);
    }

   /*
   Check if the Price  is lower than Budget.
    */
    public void comparePricesAndShowGif() {
        if (totalAmount.doubleValue() <= weeklyBudget.doubleValue()) {
            gifImageButton.setImageResource(R.drawable.checkmarksingleplay);
            Drawable drawable = gifImageButton.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
            //userProfile.setUserWantToBuyCheck(true);
        } else {
            gifImageButton.setImageResource(R.drawable.xmarksingleplay);
            Drawable drawable = gifImageButton.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
            // disable the continue button
            contBtn.setEnabled(false);
            //userProfile.setUserWantToBuyCheck(false);
        }

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
        Bundle b = new Bundle();
        b.putSerializable("userProfile", userProfile);  // send userProfile to new Activity
        intent.putExtras(b);
        startActivity(intent);
    }

    private BigDecimal calculateTotal() {
        BigDecimal tvPrice;
        tvPrice = ((tax.multiply(imagePrice)).add(imagePrice));  // Calculate Tax
        return (((autoInvestAmt.divide(new BigDecimal(100))).multiply(tvPrice)).add(tvPrice)); // Calculate TotalAmount
    }

    // Disable Back Button
    @Override
    public void onBackPressed() {
        // when Back Pressed Do Nothing.
        Toast toast = Toast.makeText(this, "This is the Main Screen. If you want to add new Trasaction Please Click on " +
                "Floating Plus on the right", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onClickPlusBtn(View view) {




    }
}
