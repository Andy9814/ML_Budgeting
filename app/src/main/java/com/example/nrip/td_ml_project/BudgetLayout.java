package com.example.nrip.td_ml_project;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;

import com.example.nrip.td_ml_project.animation.Animator;
import com.example.nrip.td_ml_project.animation.AnimatorListener;
import com.example.nrip.td_ml_project.animation.FadeInAnimation;
import com.example.nrip.td_ml_project.animation.ProgressBarAnimation;
import com.example.nrip.td_ml_project.models.BudgetCalculator;
import com.example.nrip.td_ml_project.models.BudgetEntry;
import com.example.nrip.td_ml_project.models.UserAcount;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import pl.droidsonroids.gif.GifImageView;

public class BudgetLayout extends AppCompatActivity {

    String TAG = "LogCatDemo";
    ArrayList<BudgetEntry> budgetEntries;
    BigDecimal weeklyBudget;
    BudgetCalculator bc;
    Button contBtn, plusBtn;

    BigDecimal budgTotalWeekly;
    BigDecimal budgAlreadySpent;
    BigDecimal budgToBeSpent;
    BigDecimal imagePrice;
    BigDecimal autoInvestRate;
    BigDecimal investAmt;
    BigDecimal taxRate = new BigDecimal(0.13);
    BigDecimal taxAmt;
    BigDecimal totalAmount;

    ProgressBar firstBar = null;
    TextView tvBudgetToBeSpent = null;
    TextView tvCurrentBudget = null;
    Boolean tvFoundDollarSign = false;

    // Chip tvPriceChip, tvTaxChip, tvAutoInvestChip, tvTotalAmtChip;
    Animation animation;
    AnimationSet animationSet, animationSetT, animationSetcd;
    GifImageView gifImageButton;
    UserAcount userProfile = null;

    TextView tvPriceValue = null;
    TextView tvTaxHeader = null;
    TextView tvTaxValue = null;
    TextView tvInvestHeader = null;
    TextView tvInvestValue = null;
    TextView tvTotalValue = null;
    TextView tvTotalBudgetValue = null;
    TextView tvSpentBudgetValue = null;

    MaterialCardView mcvPrice;
    MaterialCardView mcvTax;
    MaterialCardView mcvAutoInvest;
    MaterialCardView mcvTotal;
    MaterialCardView mcvTotalBudget;
    MaterialCardView mcvSpentBudget;
    MaterialCardView mcvProgressBar;

    ConstraintLayout progressContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_layout);
        contBtn = findViewById(R.id.contBtn);

        InitializeControls();

        // tvPriceChip = findViewById(R.id.priceChip);
        // tvTaxChip = findViewById(R.id.TaxChip);
        // tvAutoInvestChip = findViewById(R.id.autoInvestChip);
        // tvTotalAmtChip = findViewById(R.id.totalChip);

        this.onLoadData();
        Intent intt = getIntent();
        Bundle extras = intt.getExtras();







        imagePrice = validatePrice(extras.getString("costImage"));
        autoInvestRate = new BigDecimal(extras.getString("autoInvestAmt"));
        totalAmount = calculateTotal();

        tvPriceValue.setText(extras.getString("costImage"));
        tvTaxHeader.setText(getText(R.string.txtTax) + " @" + String.valueOf(taxRate.setScale(2, BigDecimal.ROUND_HALF_UP)) + "%:");
        tvTaxValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(taxAmt)));
        tvInvestHeader.setText(getText(R.string.txtAutoInvestment) + " @" + String.valueOf(autoInvestRate.setScale(2, BigDecimal.ROUND_HALF_UP)) + "%:");
        tvInvestValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(investAmt)));
        tvTotalValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(totalAmount)));

        // tvPriceChip.setText("Price :" + extras.getString("costImage"));
        // tvAutoInvestChip.setText("Auto Invest:" + extras.getString("autoInvestAmt") +
        // "%");
        // tvTaxChip.setText("Tax : " +
        // String.valueOf(NumberFormat.getCurrencyInstance().format(tax)));

        budgTotalWeekly = weeklyBudget;
        budgAlreadySpent = new BigDecimal(350); // TODO NEEDS TO BE PULLED FROM JSON OR WHEREVER
        budgToBeSpent = totalAmount;

        tvTotalBudgetValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(weeklyBudget)));
        tvSpentBudgetValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(budgAlreadySpent)));

        //tvPriceChip.setText("Price :" + extras.getString("costImage"));
        //tvAutoInvestChip.setText("Auto Invest:" + extras.getString("autoInvestAmt") + "%");
        //tvTaxChip.setText("Tax : " + String.valueOf(NumberFormat.getCurrencyInstance().format(tax)));


        //tvTotalAmtChip.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(totalAmount)));

        if (tvFoundDollarSign) {
            comparePricesAndShowGif();
        }

        // Create UserProfile for this Trasaction
        userProfile = new UserAcount(this.imagePrice, this.autoInvestRate);
        userProfile.setUserTotalAmount(totalAmount);

        // if(userProfile.isUserWantToBuyCheck())
        userProfile.setUserBudget(weeklyBudget.subtract(totalAmount));

        // FOR TEST PURPOSES ----------------- --------------------------------------
        // DELETE AFTER
        // tvPriceChip.setText("Price : " + 300);
        // tvTaxChip.setText("Tax : " + 4.2);
        // tvAutoInvestChip.setText("autoInvest : "+ 4+"%");
        // tvTotalAmtChip.setText("Total : "+ 4+"%");

        // // Each Chips Animation Speed.
        // for (int i = 0; i <= 3; ++i) {
        // if (i == 0) {
        // Animation fadeIn = new AlphaAnimation(0, 1);
        // fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        // fadeIn.setDuration(1000);
        // animationSet = new AnimationSet(true); //change to false
        // animationSet.addAnimation(fadeIn);
        // tvPriceChip.setAnimation(animationSet);
        // } else if (i == 1) {
        // Animation fadeIn = new AlphaAnimation(0, 1);
        // fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        // fadeIn.setDuration(2000);
        // animationSetT = new AnimationSet(true); //change to false
        // animationSetT.addAnimation(fadeIn);
        // tvTaxChip.setAnimation(animationSetT);
        // } else if (i == 2) {
        // Animation fadeIn = new AlphaAnimation(0, 1);
        // fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        // fadeIn.setDuration(3000);
        // animationSetT = new AnimationSet(true); //change to false
        // animationSetT.addAnimation(fadeIn);
        // tvAutoInvestChip.setAnimation(animationSetT);
        // } else {
        // Animation fadeIn = new AlphaAnimation(0, 1);
        // fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        // fadeIn.setDuration(4000);
        // animationSetT = new AnimationSet(true); //change to false
        // animationSetT.addAnimation(fadeIn);
        // tvTotalAmtChip.setAnimation(animationSetT);
        // }
        // }

        Animation fadeIn1 = new FadeInAnimation(0, 1, 700);
        fadeIn1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mcvPrice.setVisibility(View.VISIBLE);
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
                mcvTax.setVisibility(View.VISIBLE);
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
                mcvAutoInvest.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        Animation fadeIn4 = new FadeInAnimation(0, 1, 700);
        fadeIn4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mcvTotal.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        Animation fadeIn5 = new FadeInAnimation(0, 1, 700);
        fadeIn5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                mcvTotalBudget.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        Animation fadeIn6 = new FadeInAnimation(0, 1, 700);
        fadeIn6.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                mcvSpentBudget.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        Animation fadeIn7 = new FadeInAnimation(0, 1, 700);
        fadeIn7.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                mcvProgressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        Queue<Pair<View,Animation>> animationQueue = new LinkedList<>();
        animationQueue.add(new Pair<View, Animation>(mcvPrice,fadeIn1));
        animationQueue.add(new Pair<View, Animation>(mcvTax,fadeIn2));
        animationQueue.add(new Pair<View, Animation>(mcvAutoInvest,fadeIn3));
        animationQueue.add(new Pair<View, Animation>(mcvTotal,fadeIn4));
        animationQueue.add(new Pair<View, Animation>(mcvTotalBudget,fadeIn5));
        animationQueue.add(new Pair<View, Animation>(mcvSpentBudget,fadeIn6));
        animationQueue.add(new Pair<View, Animation>(mcvProgressBar,fadeIn7));




        tvBudgetToBeSpent = (TextView) findViewById(R.id.tvBudgetToBeSpent);
        tvCurrentBudget = (TextView) findViewById(R.id.tvCurrentBudget);

        tvCurrentBudget.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(budgTotalWeekly)));
        firstBar = (ProgressBar) findViewById(R.id.firstBar);

        firstBar.setMax(budgTotalWeekly.add(new BigDecimal(0.99)).intValue());
        firstBar.setProgress(budgAlreadySpent.intValue());
        // ProgressBarAnimation anim1 //= new ProgressBarAnimation(firstBar, false, 0,
        // budgAlreadySpent.intValue(), tvBudgetToBeSpent);
        FadeInAnimation anim1 = new FadeInAnimation(0, 1, 900);
        anim1.setDuration(100);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                progressContainer.setVisibility(View.VISIBLE);
                ProgressBarAnimation anim2 = new ProgressBarAnimation(firstBar, true, budgAlreadySpent.floatValue(), budgAlreadySpent.add(budgToBeSpent).floatValue(), tvBudgetToBeSpent);
                anim2.setDuration(1000);
                anim2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tvBudgetToBeSpent.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(budgAlreadySpent.add(budgToBeSpent))));
                        //tvBudgetToBeSpent.setText("test");
//                        GifImageView gifImageView = new GifImageView(this.context);
//                        gifImageView.playgif
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                firstBar.startAnimation(anim2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        // // firstBar.startAnimation(anim1);
        // firstBar.postDelayed(new Runnable() {
        // @Override
        // public void run() {
        // //tv.setX((int)(firstBar.getProgress()/firstBar.getWidth()));
        // //tv.setX(40);
        // ProgressBarAnimation anim2 = new ProgressBarAnimation(firstBar, true,
        // budgAlreadySpent.intValue(), budgAlreadySpent.add(budgToBeSpent).intValue(),
        // tvBudgetToBeSpent);
        // anim2.setDuration(4000);
        // firstBar.startAnimation(anim2);
        // }
        // }, 400);

        animationQueue.add(new Pair<View, Animation>(progressContainer, anim1));

        Animator animator = new Animator(animationQueue);
        animator.PlayAllUsingDelay(600);
    }

    /*
    Initializes Controls
     */
    private void InitializeControls() {
        tvPriceValue = findViewById(R.id.tvPriceValue);
        tvTaxHeader = findViewById(R.id.tvTaxHeader);
        tvTaxValue = findViewById(R.id.tvTaxValue);
        tvInvestHeader = findViewById(R.id.tvInvestHeader);
        tvInvestValue = findViewById(R.id.tvInvestValue);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvTotalBudgetValue = findViewById(R.id.tvTotalBudgetValue);
        tvSpentBudgetValue = findViewById(R.id.tvSpentBudgetValue);

        mcvPrice = findViewById(R.id.mcvPrice);
        mcvTax = findViewById(R.id.mcvTax);
        mcvAutoInvest = findViewById(R.id.mcvAutoInvest);
        mcvTotal = findViewById(R.id.mcvTotal);
        mcvTotalBudget = findViewById(R.id.mcvTotalBudget);
        mcvSpentBudget = findViewById(R.id.mcvSpentBudget);
        mcvProgressBar = findViewById(R.id.mcvProgressBar);

        progressContainer = findViewById(R.id.progressContainer);

        mcvPrice.setVisibility(View.INVISIBLE);
        mcvTax.setVisibility(View.INVISIBLE);
        mcvAutoInvest.setVisibility(View.INVISIBLE);
        mcvTotal.setVisibility(View.INVISIBLE);
        mcvTotalBudget.setVisibility(View.INVISIBLE);
        mcvSpentBudget.setVisibility(View.INVISIBLE);
        mcvProgressBar.setVisibility(View.INVISIBLE);

        progressContainer.setVisibility(View.INVISIBLE);

        gifImageButton = findViewById(R.id.gifImageView);
    }

    /*
     * Validate Price it checks for $ sign in the scanned picture.
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
     * Check if the Price is lower than Budget.
     */
    public void comparePricesAndShowGif() {
        if (totalAmount.doubleValue() <= weeklyBudget.doubleValue()) {
            gifImageButton.setImageResource(R.drawable.checkmarksingleplay);
            Drawable drawable = gifImageButton.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
            // userProfile.setUserWantToBuyCheck(true);
        } else {
            gifImageButton.setImageResource(R.drawable.xmarksingleplay);
            Drawable drawable = gifImageButton.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
            // disable the continue button
            contBtn.setEnabled(false);
            // userProfile.setUserWantToBuyCheck(false);
        }

    }

    public void onLoadData() {
        bc = new BudgetCalculator();
        try {
            // read in the file
            InputStream ins = getResources()
                    .openRawResource(getResources().getIdentifier("csv35639", "raw", getPackageName()));

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
        b.putSerializable("userProfile", userProfile); // send userProfile to new Activity
        intent.putExtras(b);
        startActivity(intent);
    }

    private BigDecimal calculateTotal() {
        BigDecimal amt;
        taxAmt = imagePrice.multiply(taxRate);  // Calculate Tax Amount
        amt = taxAmt.add(imagePrice);
        investAmt = imagePrice.multiply(autoInvestRate).divide(new BigDecimal(100));  // Calculate Automatic Investment Amount
        return (((autoInvestRate.divide(new BigDecimal(100))).multiply(amt)).add(amt)); // Calculate TotalAmount
    }

    // Disable Back Button
    @Override
    public void onBackPressed() {
        // when Back Pressed Do Nothing.
        Toast toast = Toast.makeText(this, "This is the Main Screen. If you want to add new Trasaction Please Click on "
                + "Floating Plus on the right", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onClickPlusBtn(View view) {

    }
}
