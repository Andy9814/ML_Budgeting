package com.example.nrip.td_ml_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
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

import com.example.nrip.td_ml_project.models.Transaction;
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
    Transaction userTransaction = null;
    BigDecimal totalAmt, autoInvest, budgetLeft;


    TextView spentEt, investEt, budgetEt,totalValueEt;

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

        Intent intt = getIntent();
        Bundle extras = intt.getExtras();
        userTransaction = new Transaction();
        userTransaction = (Transaction) extras.getSerializable("userTransaction");

        totalAmt = userTransaction.getUserTotalAmount();
        autoInvest = userTransaction.getUserAutoInvestment();
        budgetLeft = userTransaction.getUserBudget();
    }

    public void onClickInvstBtn(View view) {
        switch (view.getId()) {
            case R.id.btnDoubleInvst:
                final BigDecimal tvPrice = (((autoInvest.multiply(new BigDecimal(2))
                        .divide(new BigDecimal(100)))
                        .multiply(totalAmt)).add(totalAmt));
                userTransaction.setUserTotalAmount(tvPrice);
                userTransaction.setUserAutoInvestment(autoInvest.multiply(new BigDecimal(2)));
                userTransaction.setUserBudget(budgetLeft.subtract(userTransaction.getUserTotalAmount()));
                cdView.setVisibility(View.VISIBLE);
                setTextValues(userTransaction.getUserAutoInvestment());
                break;
            case R.id.btnMatch:
                BigDecimal tvPrice1 = totalAmt;
                tvPrice1 = tvPrice1.add(totalAmt);
                userTransaction.setUserTotalAmount(tvPrice1);
                userTransaction.setUserAutoInvestment(autoInvest);
                userTransaction.setUserBudget(budgetLeft.subtract(userTransaction.getUserTotalAmount()));
                cdView.setVisibility(View.VISIBLE);
                setTextValues(totalAmt);
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
                        userTransaction.setUserTotalAmount(tvPrice1);
                        userTransaction.setUserAutoInvestment(otherInvest);
                        userTransaction.setUserBudget(budgetLeft.subtract(userTransaction.getUserTotalAmount()));
                        setTextValues(userTransaction.getUserAutoInvestment());
                        Toast.makeText(getApplicationContext(), et.getText(), Toast.LENGTH_LONG).show();
                        cdView.setVisibility(View.VISIBLE);
                    }
                }).show();
                break;
            case R.id.btnNotToday:
                userTransaction.setUserTotalAmount(totalAmt);
                userTransaction.setUserAutoInvestment(autoInvest);
                userTransaction.setUserBudget(budgetLeft.subtract(userTransaction.getUserTotalAmount()));
                cdView.setVisibility(View.VISIBLE);
                setTextValues(userTransaction.getUserAutoInvestment());
        }

    }


    public void setTextValues(BigDecimal investment) {
        totalValueEt.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(userTransaction.getUserTotalAmount())));
        investEt.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(investment)));
        budgetEt.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(userTransaction.getUserBudget())));
        spentEt.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(totalAmt)));
    }

    // After Click the Transaction will be finished.
    public void onClickOkay(View view) {

        userTransaction.testx.add(totalAmt);
        // save the user in the sharedPreferences.
        String jsonArray;
        Gson gson = new Gson();
        jsonArray = gson.toJson(userTransaction);

        SharedPreferences settings = getSharedPreferences("userSharedPrefEditor",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userProfile",jsonArray);
        // commit the shared pref
        editor.apply();
        editor.commit();



//        SharedPreferences userSharedPreference = getSharedPreferences("userSharedPreference", Context.MODE_PRIVATE);
//        SharedPreferences.Editor userSharedPrefEditor = userSharedPreference.edit();
//        Gson gson = new Gson();
//        String userJson = gson.toJson(userTransaction);
//        userSharedPrefEditor.putString("userProfile", userJson);
//        userSharedPrefEditor.commit();

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
