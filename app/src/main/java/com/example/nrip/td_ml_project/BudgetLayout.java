package com.example.nrip.td_ml_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;

import com.example.nrip.td_ml_project.animation.Animator;
import com.example.nrip.td_ml_project.animation.FadeInAnimation;
import com.example.nrip.td_ml_project.animation.ProgressBarAnimation;
import com.example.nrip.td_ml_project.models.BudgetCalculator;
import com.example.nrip.td_ml_project.models.BudgetEntry;
import com.example.nrip.td_ml_project.models.Transaction;
import com.example.nrip.td_ml_project.models.UserAcount;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

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
    BigDecimal manualPrice;
    BigDecimal goalsAmt;

    ProgressBar firstBar = null;
    TextView tvBudgetToBeSpent = null;
    TextView tvCurrentBudget = null;
    Boolean tvFoundDollarSign = false;

    Transaction userTransaction = null;

    String tvManuallPrice  = "";

    TextView tvPriceValue = null;
    TextView tvTaxHeader = null;
    TextView tvTaxValue = null;
    TextView tvInvestHeader = null;
    TextView tvInvestValue = null;
    TextView tvTotalValue = null;
    TextView tvTotalBudgetValue = null;
    TextView tvSpentBudgetValue = null;
    TextView tvResultMsg    = null;

    MaterialCardView mcvPrice;
    MaterialCardView mcvTax;
    MaterialCardView mcvAutoInvest;
    MaterialCardView mcvTotal;
    MaterialCardView mcvTotalBudget;
    MaterialCardView mcvSpentBudget;
    MaterialCardView mcvProgressBar;

    ConstraintLayout progressContainer;

    ImageView imgBudgetResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_layout);
        contBtn = findViewById(R.id.contBtn);

        InitializeControls();
        this.onLoadData();

        Intent mainIntent = getIntent();
        Bundle mainExtras = mainIntent.getExtras();
        if(mainIntent.hasExtra("ManualPrice")){

            tvManuallPrice = mainExtras.getString("ManualPrice");
            autoInvestRate = new BigDecimal(mainExtras.getString("autoInvestAmt"));
            goalsAmt      =  new BigDecimal(mainExtras.getString("GoalsAmt"));
            manualPrice   = new BigDecimal(tvManuallPrice);
            totalAmount   = calculateManualTotal();


            tvPriceValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(manualPrice)));
            tvInvestValue.setText(mainExtras.getString("autoInvestAmt") + "%");
            tvTaxValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(taxAmt)));
            tvTaxHeader.setText(getText(R.string.txtTax) + " @" + String.valueOf(taxRate.setScale(2, BigDecimal.ROUND_HALF_UP)) + "%:");
            tvInvestHeader.setText(getText(R.string.txtAutoInvestment) + " @" + String.valueOf(autoInvestRate.setScale(2, BigDecimal.ROUND_HALF_UP)) + "%:");
            tvInvestValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(investAmt)));

            comparePricesAndShowGif();
        }
        else{
            Intent intt = getIntent();
            Bundle extras = intt.getExtras();
            imagePrice = validatePrice(extras.getString("costImage"));
            autoInvestRate = new BigDecimal(extras.getString("autoInvestAmt"));
            totalAmount = calculateTotal();

            tvTaxValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(taxAmt)));
            tvPriceValue.setText(extras.getString("costImage"));
            tvTaxHeader.setText(getText(R.string.txtTax) + " @" + String.valueOf(taxRate.setScale(2, BigDecimal.ROUND_HALF_UP)) + "%:");
            tvInvestHeader.setText(getText(R.string.txtAutoInvestment) + " @" + String.valueOf(autoInvestRate.setScale(2, BigDecimal.ROUND_HALF_UP)) + "%:");
            tvInvestValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(investAmt)));
            if (tvFoundDollarSign) {
                comparePricesAndShowGif();
            }
        }
        tvTotalValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(totalAmount)));

        budgTotalWeekly = weeklyBudget;
        budgAlreadySpent = new BigDecimal(0); // TODO NEEDS TO BE PULLED FROM JSON OR WHEREVER
        budgToBeSpent = totalAmount;

//        SharedPreferences sharedPreferences = getSharedPreferences("userSharedPrefEditor",MODE_PRIVATE);
//        //Gson gson = new Gson();
//        String json = sharedPreferences.getString("userProfile", "");
//        Transaction obj = gson.fromJson(json, Transaction.class);


        SharedPreferences settings = getSharedPreferences("userSharedPrefEditor",
                Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String jsonStr = settings.getString("userProfile","");
        Transaction obj = gson.fromJson(jsonStr, Transaction.class);


        if(obj != null){
            for(int i =0 ; i <= obj.testx.size(); ++i){
                BigDecimal test = obj.testx.get(i);
                budgAlreadySpent = budgAlreadySpent.add(test);
            }
        }


        tvTotalBudgetValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(weeklyBudget)));
        tvSpentBudgetValue.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(budgAlreadySpent)));

        // Create UserProfile for this Trasaction
        userTransaction = new Transaction();
        userTransaction.setUserImagePrice(this.imagePrice);
        userTransaction.setUserAutoInvestment(this.autoInvestRate);
        userTransaction.setUserGoalsAmt(this.goalsAmt);
        userTransaction.setUserTotalAmount(totalAmount);
        userTransaction.setUserBudget(this.weeklyBudget);






        // if(userProfile.isUserWantToBuyCheck())
        userTransaction.setUserBudget(weeklyBudget.subtract(totalAmount));

        Animation fadeIn1 = new FadeInAnimation(0, 1, 200);
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

        Animation fadeIn2 = new FadeInAnimation(0, 1, 200);
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

        Animation fadeIn3 = new FadeInAnimation(0, 1, 200);
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

        Animation fadeIn4 = new FadeInAnimation(0, 1, 200);
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

        Animation fadeIn5 = new FadeInAnimation(0, 1, 200);
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

        Animation fadeIn6 = new FadeInAnimation(0, 1, 200);
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

        Animation fadeIn7 = new FadeInAnimation(0, 1, 200);
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
//                        tvBudgetToBeSpent.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(budgAlreadySpent.add(budgToBeSpent))));
//
//                        tvBudgetToBeSpent.setX(200);


                        Drawable checkAnimation = imgBudgetResult.getBackground();
                        if (checkAnimation instanceof Animatable) {
                            imgBudgetResult.setVisibility(View.VISIBLE);
                            ((Animatable)checkAnimation).start();

                            imgBudgetResult.postDelayed(new Runnable() {
                                  @Override
                                  public void run() {


                                      Animation resultMsgAnim = new FadeInAnimation(0, 1, 700);
                                      resultMsgAnim.setAnimationListener(new Animation.AnimationListener() {
                                          @Override
                                          public void onAnimationStart(Animation animation) {
                                          }
                                          @Override
                                          public void onAnimationEnd(Animation animation) {
                                              tvResultMsg.setVisibility(View.VISIBLE);
                                          }
                                          @Override
                                          public void onAnimationRepeat(Animation animation) {
                                          }
                                      });

                                      tvResultMsg.startAnimation(resultMsgAnim);
                                      //resultMsgAnim.start();


                                  }
                            }, 1000);
                        }


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
       animationQueue.add(new Pair<View, Animation>(progressContainer, anim1));

        Animator animator = new Animator(animationQueue);
        animator.PlayAllUsingDelay(500);
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
        tvTotalValue = findViewById(R.id.totalValueEt);
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

        //gifImageButton = findViewById(R.id.gifImageView);
        imgBudgetResult = findViewById(R.id.imgBudgetResult);
        imgBudgetResult.setVisibility(View.INVISIBLE);

        tvResultMsg = findViewById(R.id.tvResultMsg);
        tvResultMsg.setVisibility(View.INVISIBLE);
    }

    /*
     * Validate Price it checks for $ sign in the scanned picture.
     */
    private BigDecimal validatePrice(String x) {

        if(x.equals("")){
            String msg =" `$` is not Found. Please take the picture Again!!";
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(BudgetLayout.this, MainActivity.class);
            startActivity(intent);
            this.finish();
            return BigDecimal.ZERO;
        }

        String tvPrice = "";
        for (int i = 0; i < x.length(); i++) {
            char c = x.charAt(i);
            if (tvFoundDollarSign) {
                tvPrice += c;
            } else if (c == '$') {
                tvFoundDollarSign = true;
            } else {
                Log.d("", "No dollar found");
                String msg =" `$` is not Found. Please take the picture Again!!";
                Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(BudgetLayout.this, MainActivity.class);
                startActivity(intent);
                this.finish();
                return BigDecimal.ZERO;
            }
        }
        return new BigDecimal(tvPrice);
    }

    /*budgAlreadySpent.doubleValue() > totalAmount.doubleValue() &&
     * Check if the Price is lower than Budget.
     */
    public void comparePricesAndShowGif() {
        if (totalAmount.doubleValue() <= weeklyBudget.doubleValue()) {
            imgBudgetResult.setBackgroundResource(R.drawable.checkmark);
            tvResultMsg.setText(getText(R.string.resultFitsBudget));
        } else {
            imgBudgetResult.setBackgroundResource(R.drawable.xmark);
            tvResultMsg.setText(getText(R.string.resultFailsBudget));
            contBtn.setAlpha(0f);
            contBtn.setBackgroundColor(Color.DKGRAY);
            contBtn.setEnabled(false);
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
        b.putSerializable("userTransaction", userTransaction); // send userProfile to new Activity
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

    private  BigDecimal calculateManualTotal() {
        BigDecimal amt;
        taxAmt = manualPrice.multiply(taxRate);  // Calculate Tax Amount
        amt = taxAmt.add(manualPrice);
        investAmt = manualPrice.multiply(autoInvestRate).divide(new BigDecimal(100));  // Calculate Automatic Investment Amount
        return (((autoInvestRate.divide(new BigDecimal(100))).multiply(amt)).add(amt)); // Calculate TotalAmount
    }

    // Disable Back Button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BudgetLayout.this, MainActivity.class);
        startActivity(intent);
    }
    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.infoBtns) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(R.string.aboutStr);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });



            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        return super.onOptionsItemSelected(item);
    }


}
