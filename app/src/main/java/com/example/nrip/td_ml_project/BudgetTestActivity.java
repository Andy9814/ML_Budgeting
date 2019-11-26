package com.example.nrip.td_ml_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.nrip.td_ml_project.models.BudgetCalculator;
import com.example.nrip.td_ml_project.models.BudgetEntry;
import com.example.nrip.td_ml_project.models.BudgetPeriod;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BudgetTestActivity extends AppCompatActivity {
    final String TestUserName="Pedro Sanchez";
    Date StartDate = new Date();
    Calendar Calendario;


    String TAG ="LogCatDemo";

    ArrayList<BudgetEntry> budgetEntries;
    BudgetCalculator bc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_calc_test);
        bc = new BudgetCalculator();
    }

    public void onLoadData(View view) {
        try {
            InputStream ins = getResources().openRawResource(
                    getResources().getIdentifier("csv35639",
                            "raw", getPackageName()));

            CSVReader reader = new CSVReader(new InputStreamReader(ins));
            budgetEntries = new ArrayList<>();
            String[] nextLine;
            int i = 0;
            while ((nextLine = reader.readNext()) != null) {
                if (i > 0) {
                    if (new BigDecimal(nextLine[6]).doubleValue() > 0 && nextLine[0].equals("Chequing")){
                        budgetEntries.add((new BudgetEntry(nextLine[0], new SimpleDateFormat("MM/dd/yyyy").parse(nextLine[2]), new BigDecimal(nextLine[6]))));
                    }
                    else if(new BigDecimal(nextLine[6]).doubleValue() < 0 && nextLine[0].equals("MasterCard")){
                        budgetEntries.add((new BudgetEntry(nextLine[0], new SimpleDateFormat("MM/dd/yyyy").parse(nextLine[2]), new BigDecimal(nextLine[6]))));
                    }
                }
                i++;
            }
            bc.calcBudget(budgetEntries);
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        } catch (ParseException e) {
            Log.d(TAG, e.toString());
            e.printStackTrace();
        }
    }
}
