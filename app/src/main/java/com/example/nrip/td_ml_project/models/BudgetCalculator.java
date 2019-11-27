package com.example.nrip.td_ml_project.models;

import android.util.Log;
import android.view.View;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class BudgetCalculator {

    String TAG ="LogCatDemo";


    public ArrayList<BudgetEntry> parseData(InputStream ins) throws IOException, ParseException {
        ArrayList<BudgetEntry> budgetEntries;
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
        return budgetEntries;
    }




    public BigDecimal calcBudget(ArrayList<BudgetEntry> budgetEntries) {

        Calendar calendar = new GregorianCalendar();
        Map<String, BudgetPeriod> budgetMap = new HashMap<>();

        for (int i = 0; i < budgetEntries.size(); i++) {
            calendar.setTime(budgetEntries.get(i).Date);
            calendar.get(Calendar.YEAR);

            String key = String.format("%1$s-%2$s",calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));
            BudgetPeriod bp;

            if (budgetMap.containsKey(key)){
                bp = budgetMap.get(key);
            }
            else {
                bp = new BudgetPeriod(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR));
            }

            bp.Entries.add(budgetEntries.get(i));
            budgetMap.put(key, bp);
        }

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
        return avgWeeklyDeposit.subtract(avgWeeklyWithdrawal);
    }
}
