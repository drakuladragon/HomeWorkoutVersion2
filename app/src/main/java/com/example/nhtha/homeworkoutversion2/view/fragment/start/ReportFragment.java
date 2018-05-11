package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.DataDto;
import com.example.nhtha.homeworkoutversion2.dto.UserDto;
import com.example.nhtha.homeworkoutversion2.model.Data;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by nhtha on 05-Mar-18.
 */

public class ReportFragment extends Fragment {

    private LineChart lineChart;

    private ArrayList<Data> datas;

    private FirebaseUser user;
    private DatabaseReference dataDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private EditText edtHeight;
    private EditText edtWeight;
    private TextView tvKcal;
    private TextView tvBMI;
    private TextView txtBMINotify;
    private Button btnSave;
    private ImageView imgMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        dataDatabaseReference = FirebaseDatabase.getInstance().getReference().child("data");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControls();
        init();
    }

    private void init() {
        edtHeight = getView().findViewById(R.id.edt_height);
        edtWeight = getView().findViewById(R.id.edt_weight);
        tvBMI = getView().findViewById(R.id.tv_bmi);
        txtBMINotify = getView().findViewById(R.id.txt_bmi_notify);
        btnSave = getView().findViewById(R.id.btn_save);
        tvKcal = getView().findViewById(R.id.tv_kcal);

        loadWeightHeight();
        loadTodayKcal();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                showBMI();
            }
        });
    }

    private void showBMI() {
        String weight = edtWeight.getText().toString().trim();
        String height = edtHeight.getText().toString().trim();
        if(!TextUtils.isEmpty(weight) && !TextUtils.isEmpty(height)) {
            float bmi = calculateBMI(Float.parseFloat(height), Float.parseFloat(weight));
            String value = String.valueOf(bmi);
            int i = value.indexOf(".");
            tvBMI.setText(value.substring(0, i + 2));
            txtBMINotify.setText(checkBMI(bmi));
        } 
    }


    private void addControls() {

        lineChart = getView().findViewById(R.id.line_chart);

        // no description text
        lineChart.getDescription().setEnabled(false);

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setDrawBorders(true);

        // set an alternative background color
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setNoDataText("thông số sẽ hiển thị tại đây");
        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        // add data
        setData();
        lineChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
//        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.DAYS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        yAxis.setTypeface(mTfLight);
        yAxis.setDrawGridLines(true);
        yAxis.setGranularityEnabled(true);
        yAxis.setAxisMinimum(0f);
        yAxis.setYOffset(-9f);
        yAxis.setTextColor(Color.BLACK);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        imgMenu = getView().findViewById(R.id.img_menu);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartActivity) getActivity()).openDrawer();
            }
        });
    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<>();

        dataLoad();

        for (final Data data : datas) {
            values.add(new Entry(data.getConvertDay(), data.getKCal())); // add one entry per day
        }

        // create a dataset and give it a type
        LineDataSet dataSet = new LineDataSet(values, "DataSet 1");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.GREEN);
        dataSet.setLineWidth(1.5f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setFillAlpha(65);
        dataSet.setHighLightColor(Color.BLACK);
        dataSet.setDrawCircleHole(false);

        // create a data object with the datasets
        LineData data = new LineData(dataSet);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        if (!datas.isEmpty()) {
            lineChart.setData(data);
            lineChart.invalidate();
        }

    }

    private void pullData() {
        ArrayList<Entry> values = new ArrayList<>();

        for (final Data data : datas) {
            values.add(new Entry(data.getConvertDay(), data.getKCal())); // add one entry per day
        }

        // create a dataset and give it a type
        LineDataSet dataSet = new LineDataSet(values, "DataSet 1");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.GREEN);
        dataSet.setLineWidth(1.5f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setFillAlpha(65);
        dataSet.setHighLightColor(Color.BLACK);
        dataSet.setDrawCircleHole(false);

        // create a data object with the datasets
        LineData data = new LineData(dataSet);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        if (!datas.isEmpty()) {
            lineChart.setData(data);
            lineChart.invalidate();
        }
    }

    private void dataLoad() {
        datas = new ArrayList<>();

        dataDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datas.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataDto data = snapshot.getValue(DataDto.class);
                    if (data.getUserId().equals(user.getUid())) {
                        datas.add(new Data(new Date(data.getDate()), data.getKcal()));
                    }
                }
                Log.d("asdfasdf", "onDataChange: " + datas.size());
                pullData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("asdfasdf", "dataLoad: " + datas.size());
    }

    private void saveData() {
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDto userDto = dataSnapshot.getValue(UserDto.class);

                String weight = edtWeight.getText().toString().trim();
                String height = edtHeight.getText().toString().trim();

                if (!TextUtils.isEmpty(weight) && !TextUtils.isEmpty(height)) {
                    userDto.setHeight(Float.parseFloat(height));
                    userDto.setWeight(Float.parseFloat(weight));
                }

                userDatabaseReference.setValue(userDto);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadWeightHeight() {
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDto userDto = dataSnapshot.getValue(UserDto.class);

                String weight = String.valueOf(userDto.getWeight());
                String height = String.valueOf(userDto.getHeight());

                if (!TextUtils.isEmpty(weight) && !TextUtils.isEmpty(height)) {
                    edtHeight.setText(height);
                    edtWeight.setText(weight);
                    float bmi = calculateBMI(Float.parseFloat(height), Float.parseFloat(weight));
                    String value = String.valueOf(bmi);
                    int i = value.indexOf(".");
                    tvBMI.setText(value.substring(0,i+2));
                    txtBMINotify.setText(checkBMI(bmi));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadTodayKcal() {
        dataDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int kcal = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataDto dataDto = snapshot.getValue(DataDto.class);
                    if (dataDto.getDate().equals(new SimpleDateFormat("MM/dd/yyyy").format(new Date()))) {
                        kcal += dataDto.getKcal();
                    }
                }
                tvKcal.setText(String.valueOf(kcal));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private float calculateBMI(float high, float weight) {
        if (weight <= 0 || high <= 0) return 0;
        float bmi;
        float a = high / 100;
        bmi = weight / (a * a);
        return bmi;
    }

    private String checkBMI(float bmi) {
        if (bmi <= 0)
            return "";
        else if (0.0f < bmi && bmi < 18.5f)
            return "Underweight";
        else if (18.5f <= bmi && bmi < 25.0f)
            return "Normal";
        else if (25.0f <= bmi && bmi < 30.0f)
            return "Overweight";
        else
            return "Too Fat";
    }
}