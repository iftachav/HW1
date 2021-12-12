package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListFragment extends Fragment {

    private AppCompatActivity activity;

    private CallBackList callBackList;
    private RecordDB recordDB;
    private LinearLayout rows[];
    private TextView times[];
    private TextView scores[];

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBackList(CallBackList callBackList) {
        this.callBackList = callBackList;
    }

    public void setRecordDB(RecordDB recordDB) {
        this.recordDB = recordDB;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();


        return view;
    }
    private void initViews() {
        for (int i = 0; i < recordDB.getRecords().size(); i++) {
            int index=i;
            rows[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBackList.getSpecificRecord(index);
                }
            });
        }
        int j=0;
        for (Record record: recordDB.getRecords()) {
            times[j].setText(record.getTime());
            scores[j].setText(record.getScore()+"");
            rows[j].setVisibility(View.VISIBLE);
            j++;
        }
    }

    private void findViews(View view) {
        rows= new LinearLayout[]{
                view.findViewById(R.id.list_ROW_row0),
                view.findViewById(R.id.list_ROW_row1),
                view.findViewById(R.id.list_ROW_row2),
                view.findViewById(R.id.list_ROW_row3),
                view.findViewById(R.id.list_ROW_row4),
                view.findViewById(R.id.list_ROW_row5),
                view.findViewById(R.id.list_ROW_row6),
                view.findViewById(R.id.list_ROW_row7),
                view.findViewById(R.id.list_ROW_row8),
                view.findViewById(R.id.list_ROW_row9)
        };
        times = new TextView[]{
                view.findViewById(R.id.list_TXT_time0),
                view.findViewById(R.id.list_TXT_time1),
                view.findViewById(R.id.list_TXT_time2),
                view.findViewById(R.id.list_TXT_time3),
                view.findViewById(R.id.list_TXT_time4),
                view.findViewById(R.id.list_TXT_time5),
                view.findViewById(R.id.list_TXT_time6),
                view.findViewById(R.id.list_TXT_time7),
                view.findViewById(R.id.list_TXT_time8),
                view.findViewById(R.id.list_TXT_time9)
        };
        scores = new TextView[]{
                view.findViewById(R.id.list_TXT_score0),
                view.findViewById(R.id.list_TXT_score1),
                view.findViewById(R.id.list_TXT_score2),
                view.findViewById(R.id.list_TXT_score3),
                view.findViewById(R.id.list_TXT_score4),
                view.findViewById(R.id.list_TXT_score5),
                view.findViewById(R.id.list_TXT_score6),
                view.findViewById(R.id.list_TXT_score7),
                view.findViewById(R.id.list_TXT_score8),
                view.findViewById(R.id.list_TXT_score9)
        };
    }
}