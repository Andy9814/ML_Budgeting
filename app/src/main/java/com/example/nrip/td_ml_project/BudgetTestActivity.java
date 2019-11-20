package com.example.nrip.td_ml_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_calc_test);



    }

    public void onLoadData(View view) {
        Log.d(TAG, "hi");
        try {

            InputStream ins = getResources().openRawResource(
                    getResources().getIdentifier("csv35639",
                            "raw", getPackageName()));


            CSVReader reader = new CSVReader(new InputStreamReader(ins));

            budgetEntries = new ArrayList<>();
            String[] nextLine;
            Log.d(TAG, "hi");
            int i = 0;
            while ((nextLine = reader.readNext()) != null) {
                if (i > 0) {
//                    Log.d(TAG, nextLine[0]);
//                    Log.d(TAG, String.valueOf(new BigDecimal(nextLine[6]).doubleValue()));
//                    Log.d(TAG, "=========================");
                    if (new BigDecimal(nextLine[6]).doubleValue() > 0 && nextLine[0].equals("Chequing")){
                        budgetEntries.add((new BudgetEntry(nextLine[0], new SimpleDateFormat("MM/dd/yyyy").parse(nextLine[2]), new BigDecimal(nextLine[6]))));
                    }
                    else if(new BigDecimal(nextLine[6]).doubleValue() < 0 && nextLine[0].equals("MasterCard")){
                        budgetEntries.add((new BudgetEntry(nextLine[0], new SimpleDateFormat("MM/dd/yyyy").parse(nextLine[2]), new BigDecimal(nextLine[6]))));
                    }
                }
                i++;
                // nextLine[] is an array of values from the line
                //Log.d(TAG, nextLine[6]);
                // System.out.println(nextLine[0] + nextLine[1] + "etc...");

            }
            calcBudget();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        } catch (ParseException e) {
            Log.d(TAG, e.toString());
            e.printStackTrace();
        }



    }

    public void calcBudget() {

        Calendar calendar = new GregorianCalendar();
        Map<String, BudgetPeriod> budgetMap = new HashMap<>();


        for (int i = 0; i < budgetEntries.size(); i++) {
            //Date trialTime = new Date();
            calendar.setTime(budgetEntries.get(i).Date);
//            Log.d(TAG, "Week number:" +
//                    calendar.get(Calendar.WEEK_OF_YEAR));
            calendar.get(Calendar.YEAR);

            String key = String.format("%1$s-%2$s",calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));
            BudgetPeriod bp;// = budgetMap.get(key);

            if (budgetMap.containsKey(key)){
                bp = budgetMap.get(key);
                //Log.d(TAG, key + "exists");
            }
            else {
                bp = new BudgetPeriod(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR));
                //Log.d(TAG, key + "is new");
            }

            bp.Entries.add(budgetEntries.get(i));
            budgetMap.put(key, bp);
        }

        Log.d(TAG, "map size:" +
                budgetMap.size());

        BigDecimal totalDeposit = BigDecimal.ZERO;
        BigDecimal totalWithdrawal = BigDecimal.ZERO;

        for(String key : budgetMap.keySet()){
            totalDeposit = totalDeposit.add(budgetMap.get(key).CalcDeposits());
            totalWithdrawal = totalWithdrawal.add(budgetMap.get(key).CalcWithdrawals());
        }

        BigDecimal avgWeeklyDeposit = totalDeposit.divide(new BigDecimal(budgetMap.size()),2, RoundingMode.HALF_EVEN);
        BigDecimal avgWeeklyWithdrawal = totalWithdrawal.divide(new BigDecimal(budgetMap.size()),2, RoundingMode.HALF_EVEN);

        Log.d(TAG, "totalDeposit: $" + totalDeposit);
        Log.d(TAG, "totalWithdrawal: $" + totalWithdrawal);
        Log.d(TAG, "avgWeeklyDeposit: $" + avgWeeklyDeposit);
        Log.d(TAG, "avgWeeklyWithdrawal: $" + avgWeeklyWithdrawal);
        Log.d(TAG, "avgWeeklyBudget: $" + avgWeeklyDeposit.subtract(avgWeeklyWithdrawal));
    }

    public static BigDecimal round(BigDecimal value, BigDecimal increment,
                                   RoundingMode roundingMode) {
        if (increment.signum() == 0) {
            // 0 increment does not make much sense, but prevent division by 0
            return value;
        } else {
            BigDecimal divided = value.divide(increment, 0, roundingMode);
            BigDecimal result = divided.multiply(increment);
            return result;
        }
    }
}
