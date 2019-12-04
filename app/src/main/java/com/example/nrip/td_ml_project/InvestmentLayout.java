package com.example.nrip.td_ml_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nrip.td_ml_project.models.UserAcount;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class InvestmentLayout extends AppCompatActivity {

    CardView cdView;
    Button btnMatch, btnDoubleInvt, btnOther;
    EditText et;
    UserAcount userProfile = null;
    BigDecimal totalAmt, autoInvest, budgetLeft;


    TextView spentEt, investEt, budgetEt,totalValueEt, tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_layout);
        getSupportActionBar().setTitle("InvestMent");
        // getSupportActionBar().hide();
        cdView = findViewById(R.id.cardAgree);
        btnDoubleInvt = findViewById(R.id.btnDoubleInvst);
        btnMatch = findViewById(R.id.btnMatch);
        btnOther = findViewById(R.id.btnOther);
        cdView.setVisibility(View.GONE);
        spentEt = findViewById(R.id.spentEt);
        investEt = findViewById(R.id.investedEt);
        budgetEt = findViewById(R.id.remainingBudgetEt);
        totalValueEt = findViewById(R.id.totalValueEt);
        tvSummary = findViewById(R.id.tvSummary);

        Intent intt = getIntent();
        Bundle extras = intt.getExtras();
        userProfile = (UserAcount) extras.getSerializable("userProfile");

        totalAmt = userProfile.getUserTotalAmount();
        autoInvest = userProfile.getUserAutoInvestment();
        budgetLeft = userProfile.getUserBudget();
    }

    public void onClickInvstBtn(View view) {
        switch (view.getId()) {
            case R.id.btnDoubleInvst:
                final BigDecimal tvPrice = (((autoInvest.multiply(new BigDecimal(2))
                        .divide(new BigDecimal(100)))
                        .multiply(totalAmt)).add(totalAmt));
                userProfile.setUserTotalAmount(tvPrice);
                userProfile.setUserAutoInvestment(autoInvest.multiply(new BigDecimal(2)));
                userProfile.setUserBudget(budgetLeft.subtract(userProfile.getUserTotalAmount()));
                cdView.setVisibility(View.VISIBLE);
                setTextValues(userProfile.getUserAutoInvestment());
                tvSummary.setText(getText(R.string.summaryHeader) + " - " + getText(R.string.addTypeDouble));
                break;
            case R.id.btnMatch:
                BigDecimal tvPrice1 = totalAmt;
                tvPrice1 = tvPrice1.add(totalAmt);
                userProfile.setUserTotalAmount(tvPrice1);
                userProfile.setUserAutoInvestment(autoInvest);
                userProfile.setUserBudget(budgetLeft.subtract(userProfile.getUserTotalAmount()));
                cdView.setVisibility(View.VISIBLE);
                setTextValues(totalAmt);
                tvSummary.setText(getText(R.string.summaryHeader) + " - " + getText(R.string.addTypeMatch));
                break;
            case R.id.btnOther:
                //MaterialAlertDialogBuilder ad = new MaterialAlertDialogBuilder(InvestmentLayout.this, R.style.AlertDialogTheme);
                MaterialAlertDialogBuilder ad = new MaterialAlertDialogBuilder(InvestmentLayout.this);
                ad.setTitle("amount");
                et = new EditText(this);
                ad.setView(et);


                // ad.setCancelable(false);  // you can click on differnt area of screen to cancell the dialog box
                ad.setPositiveButton("okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BigDecimal otherInvest = new BigDecimal(et.getText().toString());
                        BigDecimal tvPrice1 = totalAmt;
                        tvPrice1 = tvPrice1.add(otherInvest);
                        userProfile.setUserTotalAmount(tvPrice1);
                        userProfile.setUserAutoInvestment(otherInvest);
                        userProfile.setUserBudget(budgetLeft.subtract(userProfile.getUserTotalAmount()));
                        setTextValues(userProfile.getUserAutoInvestment());
                        Toast.makeText(getApplicationContext(), et.getText(), Toast.LENGTH_LONG).show();
                        cdView.setVisibility(View.VISIBLE);
                    }
                })

                        // need to decide if we need this
//                        .setNeutralButton("LATER", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                            }
//                        })
                        .show();
                tvSummary.setText(getText(R.string.summaryHeader) + " - " + getText(R.string.addTypeCustom));
                break;
            case R.id.btnNotToday:
                userProfile.setUserTotalAmount(totalAmt);
                userProfile.setUserAutoInvestment(autoInvest);
                userProfile.setUserBudget(budgetLeft.subtract(userProfile.getUserTotalAmount()));
                cdView.setVisibility(View.VISIBLE);
                setTextValues(userProfile.getUserAutoInvestment());
                tvSummary.setText(getText(R.string.summaryHeader) + " - " + getText(R.string.addTypeAnotherTime));
        }

    }


    public void setTextValues(BigDecimal investment) {
        totalValueEt.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(userProfile.getUserTotalAmount())));
        investEt.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(investment)));
        budgetEt.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(userProfile.getUserBudget())));
        spentEt.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(totalAmt)));
    }

    // After Click the Transation will be finished.
    public void onClickOkay(View view) {
        // save the user in the sharedPreferences.
        SharedPreferences userSharedPreference = getSharedPreferences("userSharedPreference", MODE_PRIVATE);
        SharedPreferences.Editor userSharedPrefEditor = userSharedPreference.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(userProfile);
        userSharedPrefEditor.putString("userProfile", userJson);
        userSharedPrefEditor.apply();

        Intent intent = new Intent(InvestmentLayout.this, MainActivity.class);
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
