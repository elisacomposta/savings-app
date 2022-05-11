package com.example.savings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Record implements Comparable<Record> {
    private int id;
    private int amount;
    private String subject;
    private String description;
    private Boolean isEarned;
    private String date;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEarned() {
        return isEarned;
    }

    public void setEarned(Boolean earned) {
        isEarned = earned;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Record(int id, int amount, String subject, String description, Boolean isEarned, String date) {
        this.id = id;
        this.amount = amount;
        this.subject = subject;
        this.description = description;
        this.isEarned = isEarned;
        this.date = date;
    }


    @Override
    public String toString() {
        return amount/100.0 +
                "    " + subject +
                "    " + description +
                "    " + date;
    }

    @Override
    public int compareTo(Record o) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = format.parse(this.getDate());
            Date date2 = format.parse(o.getDate());
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;
        Record record = (Record) o;
        return getDate().equals(record.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate());
    }
}
