package com.example.savings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView creditText;
    FloatingActionButton addButton;
    static boolean creating = false;
    RecyclerView recyclerView;

    //AddCard
    CardView addCard;
    CurrencyEditText amount;
    EditText subj;
    EditText description;
    Switch isEarnedSwitch;
    Button cancelBtn;
    Button okBtn;
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        creditText = findViewById(R.id.creditText);
        addButton = findViewById(R.id.addBtn);
        addCard = findViewById(R.id.cardView);
        amount = findViewById(R.id.amountText);
        subj = findViewById(R.id.subjectText);
        description = findViewById(R.id.descriptionText);
        isEarnedSwitch = findViewById(R.id.spentSwitch);
        cancelBtn = findViewById(R.id.cancelBtn);
        okBtn = findViewById(R.id.okBtn);
        date = findViewById(R.id.dateText);

        showAllRecords();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard.setVisibility(View.VISIBLE);
                creating = true;
                setInitialDate(date);
                manageDate(MainActivity.this, date);
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record toAdd = addRecord(MainActivity.this, amount, subj, description, isEarnedSwitch, date);
                if(toAdd!=null){
                    showAllRecords();
                    isEarnedSwitch.setChecked(false);
                    amount.setText("0.00");
                    subj.getText().clear();
                    description.getText().clear();
                    setInitialDate(date);
                    addCard.setVisibility(View.GONE);
                    creating = false;
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard.setVisibility(View.GONE);
                creating = false;
            }
        });
    }

    public static String formatCurrencyNumber(double number){
        DecimalFormat formatter = new DecimalFormat("¤###,###,##0.00");
        return formatter.format(number);
    }

    public void setCredit(){
        int credit = 0;
        DataBaseHelper dbH = new DataBaseHelper(MainActivity.this);
        List<Record> all = dbH.getAll();
        for(Record r: all){
            if(r.getEarned()){
                credit += r.getAmount();
            } else {
                credit -= r.getAmount();
            }
        }
        creditText.setText(formatCurrencyNumber((double)credit/100.0));
    }

    public void showAllRecords(){
        List<Record> allRecords = new DataBaseHelper(MainActivity.this).getAll();
        Collections.sort(allRecords);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);
        adapter.setRecordList(allRecords);
        recyclerView = findViewById(R.id.recordList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        setCredit();
    }

    public void setInitialDate(TextView dateTextView){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dateTextView.setText(sdf.format(cal.getTime()));
    }

    public static void manageDate(final Context context, final TextView dateTxt){
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateTxt.setText(sdf.format(cal.getTime()));
            }
        };


        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, dateListener,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public static Record addRecord(Context context, CurrencyEditText am, EditText sub, EditText desc, Switch isEar, TextView dat){
        Record record = null;
        long amountVal = am.getRawValue();
        if(amountVal!=0 && sub.length()!=0 && desc.length()!=0){

            record = new Record(-1, (int)amountVal, sub.getText().toString(), desc.getText().toString(),
                    isEar.isChecked(), dat.getText().toString());

            //add to database
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            if(dbHelper.addOne(record)){
                Toast.makeText(context, "Recorded correctly", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something wrong happened..."+dbHelper.addOne(record), Toast.LENGTH_SHORT).show();
            }
        } else if(amountVal!=0){
            Toast.makeText(context, "Please complete all fields", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Please insert the correct amount", Toast.LENGTH_SHORT).show();
        }
        return record;
    }

    @Override
    public void onBackPressed() {

    }
}