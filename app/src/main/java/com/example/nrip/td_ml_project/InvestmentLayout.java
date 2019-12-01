package com.example.nrip.td_ml_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.math.BigDecimal;

public class InvestmentLayout extends AppCompatActivity {

    CardView cdView;
    Button  btnMatch, btnDoubleInvt, btnOther;
    EditText et;
    BigDecimal autoInvestMent = new BigDecimal(2); // or the value of auto investment
    BigDecimal priceSpent = new BigDecimal(500);
    BigDecimal buddget = new BigDecimal(1000);

    TextView spentEt, investEt,budgetEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_layout);
        getSupportActionBar().setTitle("InvestMent");
       // getSupportActionBar().hide();
        cdView = findViewById(R.id.cardAgree);
        btnDoubleInvt = findViewById(R.id.btnDoubleInvst);
        btnMatch= findViewById(R.id.btnMatch);
        btnOther= findViewById(R.id.btnOther);
        cdView.setVisibility(View.GONE);
        spentEt = findViewById(R.id.spentEt);
        investEt = findViewById(R.id.investedEt);
        budgetEt = findViewById(R.id.remainingBudgetEt);

    }

    public void onClickInvstBtn(View view) {
        switch (view.getId()){
            case R.id.btnDoubleInvst:

                cdView.setVisibility(View.VISIBLE);
                setTextValues((autoInvestMent).multiply(new BigDecimal(2)));

                break;
            case R.id.btnMatch:
                cdView.setVisibility(View.VISIBLE);
                setTextValues(priceSpent);
                break;
            case R.id.btnOther:
             MaterialAlertDialogBuilder ad =    new MaterialAlertDialogBuilder(InvestmentLayout.this, R.style.AlertDialogTheme);
                        ad.setTitle("amount");
                et = new EditText(this);
                ad.setView(et);


               // ad.setCancelable(false);
                ad.setPositiveButton("okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BigDecimal otherInvest  = new BigDecimal(et.getText().toString());
                                setTextValues(otherInvest);
                               Toast.makeText(getApplicationContext(), et.getText(), Toast.LENGTH_LONG).show();
                                cdView.setVisibility(View.VISIBLE);
                            }
                        })
//                        .setNeutralButton("LATER", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                            }
//                        })
                        .show();

        }
    }


    public void setTextValues(BigDecimal investment){
        spentEt.setText(priceSpent.toString());
        investEt.setText(investment.toString() );
        budgetEt.setText(buddget.toString());
    }

    public void onClickOkay(View view) {
        finish();
    }
}
