package com.example.myapplication.etc;

import java.util.ArrayList;

public class RecordDB {
    private ArrayList<Record> records = new ArrayList<>();

    public RecordDB() { }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public RecordDB setRecords(ArrayList<Record> records) {
        this.records = records;
        return this;
    }
}
