package com.example.savings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;

import org.w3c.dom.Text;

public class RecordActivity extends AppCompatActivity {

    final static String CANCEL = "CANCEL";
    final static String EDIT = "EDIT";
    Record shownRecord;

    Button cancelRec;
    Button editBtn;
    Button deleteBtn;
    TextView amountRec;
    TextView subjectRec;
    TextView descriptionRec;
    TextView dateRec;
    Switch isEarnedRec;
    CurrencyEditText amountEdit;
    EditText subjectEdit;
    EditText descriptionEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        cancelRec = findViewById(R.id.cancelDtlBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        amountRec = findViewById(R.id.amountVal);
        subjectRec = findViewById(R.id.subjectVal);
        descriptionRec = findViewById(R.id.descriptionVal);
        dateRec = findViewById(R.id.dateVal);
        isEarnedRec = findViewById(R.id.switchDtl);
        isEarnedRec.setClickable(false);
        amountEdit = findViewById(R.id.amountDtl);
        subjectEdit = findViewById(R.id.subjectDtl);
        descriptionEdit = findViewById(R.id.descriptionDtl);

        cancelRec.setText(CANCEL);
        editBtn.setText(EDIT);
        deleteBtn.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        if(intent!=null){
            int id = intent.getIntExtra(RecyclerViewAdapter.RECORD_ID_KEY, -1);
            DataBaseHelper dbh = new DataBaseHelper(RecordActivity.this);
            shownRecord = dbh.searchRecord(id);
            if(shownRecord!=null){
                amountRec.setText(MainActivity.formatCurrencyNumber(shownRecord.getAmount()/100.0));
                subjectRec.setText(shownRecord.getSubject());
                descriptionRec.setText(shownRecord.getDescription());
                dateRec.setText(shownRecord.getDate());
                isEarnedRec.setChecked(shownRecord.getEarned());
            } else {
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editBtn.getText()!=CANCEL) {
                    amountEdit.setText(amountRec.getText());
                    subjectEdit.setText(subjectRec.getText());
                    descriptionEdit.setText(descriptionRec.getText());
                    amountRec.setVisibility(View.INVISIBLE);
                    subjectRec.setVisibility(View.INVISIBLE);
                    descriptionRec.setVisibility(View.INVISIBLE);
                    amountEdit.setVisibility(View.VISIBLE);
                    subjectEdit.setVisibility(View.VISIBLE);
                    descriptionEdit.setVisibility(View.VISIBLE);
                    isEarnedRec.setClickable(true);
                    cancelRec.setText("SAVE");
                    deleteBtn.setVisibility(View.INVISIBLE);
                    dateRec.setTextColor(getResources().getColor(R.color.colorBlack));
                    MainActivity.manageDate(RecordActivity.this, dateRec);
                    editBtn.setText(CANCEL);
                } else{
                    //backToMain();
                    amountRec.setVisibility(View.VISIBLE);
                    subjectRec.setVisibility(View.VISIBLE);
                    descriptionRec.setVisibility(View.VISIBLE);
                    amountEdit.setVisibility(View.INVISIBLE);
                    subjectEdit.setVisibility(View.INVISIBLE);
                    descriptionEdit.setVisibility(View.INVISIBLE);
                    isEarnedRec.setClickable(false);
                    cancelRec.setText(CANCEL);
                    deleteBtn.setVisibility(View.VISIBLE);
                    dateRec.setTextColor(getResources().getColor(R.color.colorPrimary));
                    editBtn.setText(EDIT);
                }
            }
        });

        cancelRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cancelRec.getText()!=CANCEL){
                    Record toAdd = MainActivity.addRecord(RecordActivity.this, amountEdit, subjectEdit, descriptionEdit,
                            isEarnedRec, dateRec);
                    if(toAdd!=null){
                        DataBaseHelper dbh = new DataBaseHelper(RecordActivity.this);
                        dbh.deleteOne(shownRecord);
                        onBackPressed();
                    }
                } else {
                    onBackPressed();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                builder.setMessage("Are you sure you want to delete this record?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper dbh = new DataBaseHelper(RecordActivity.this);
                        dbh.deleteOne(shownRecord);
                        onBackPressed();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(RecordActivity.this, MainActivity.class);
        RecordActivity.this.startActivity(intent);
        finish();
    }



}