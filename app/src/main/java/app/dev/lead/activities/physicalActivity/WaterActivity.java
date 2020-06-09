package app.dev.lead.activities.physicalActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.dev.lead.R;
import app.dev.lead.activities.dashboard.PhysicalActivity;
import app.dev.lead.models.pojos.WaterData;

import static app.dev.lead.utilities.Methods.getStartEndDateOfWeekFromCalendar;
import static app.dev.lead.utilities.Methods.goNextActivityWithFinish;

public class WaterActivity extends AppCompatActivity {

    View view;
    TextView toolbar;
    TextView tvWater, tvWeekly;
    BarChart barChart;
    String TAG = "theWater";
    float mondayCount = 0, tuesdayCount = 0, wednesdayCount = 0, thursdayCount = 0, fridayCount = 0, saturdayCount = 0, sundayCount = 0;

    Calendar currentDate = Calendar.getInstance();
    int currentWeek = currentDate.get(Calendar.WEEK_OF_YEAR);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        initializeViews();

        setDataInToolbar();

        if (PhysicalActivity.waterDataList != null) {

            getTodayData();

            getWeeklyData(currentWeek);
        } else {
            Log.d(TAG, "onCreate: water data is null");
        }
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        barChart = findViewById(R.id.water_chart);
        tvWater = findViewById(R.id.tv_water);
        tvWeekly = findViewById(R.id.tv_weekly);
        tvWater.setText(String.format(getResources().getString(R.string.p_water), String.valueOf(1)));
        tvWeekly.setText(String.format(getResources().getString(R.string.weekly), getStartEndDateOfWeekFromCalendar()));

    }

    public void setDataInToolbar() {
        toolbar.setText("Water");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void onAddClick(View view) {
        goNextActivityWithFinish(WaterActivity.this, AddWaterActivity.class);
    }

    public void drawChart() {

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        Description description = new Description();
        description.setText("");

        barChart.setDescription(description);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        barChart.getAxisRight().setEnabled(false);

        float barWidth = 0.46f;

        List<BarEntry> yVals1 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(0, sundayCount));
        yVals1.add(new BarEntry(1, mondayCount));
        yVals1.add(new BarEntry(2, tuesdayCount));
        yVals1.add(new BarEntry(3, wednesdayCount));
        yVals1.add(new BarEntry(4, thursdayCount));
        yVals1.add(new BarEntry(5, fridayCount));
        yVals1.add(new BarEntry(6, saturdayCount));

        BarDataSet set1;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Water in Liter");
            set1.setColor(Color.rgb(104, 241, 175));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            barChart.setData(data);
        }


        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Sun");
        xAxisLabel.add("Mon");
        xAxisLabel.add("Tue");
        xAxisLabel.add("Wed");
        xAxisLabel.add("Thu");
        xAxisLabel.add("Fri");
        xAxisLabel.add("Sat");


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int i = (int) Math.abs(value);
                if (value == -1.0) {
                    return "";
                } else if (i == 7) {
                    return "";
                }
                Log.d(TAG, "getFormattedValue: " + value + " i: " + i);
                return xAxisLabel.get(i);
            }
        };

        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        barChart.getBarData().setBarWidth(barWidth);
        barChart.invalidate();
    }

    public void getTodayData() {
        Date cDate = null, date = null;
        Log.d(TAG, "StepsActivity: getTodayData: " + PhysicalActivity.waterDataList.size());
        double totalWater = 0.0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            cDate = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (WaterData data : PhysicalActivity.waterDataList) {

            try {
                date = sdf.parse(data.getWaterDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (cDate != null && cDate.equals(date)) {
                totalWater = totalWater + Double.parseDouble(data.getTotalWater());
                if (totalWater > 1.9) {
                    tvWater.setText(String.format(getResources().getString(R.string.p_waters), String.format("%.2f", totalWater)));
                } else {
                    tvWater.setText(String.format(getResources().getString(R.string.p_water), String.format("%.2f", totalWater)));
                }
            }
        }
    }

    public void getWeeklyData(int currentWeek) {
        Log.d(TAG, "WaterActivity: getWeeklyData: waterDataList: " + PhysicalActivity.waterDataList.size());

        tvWeekly.setText(String.format(getResources().getString(R.string.weekly), getStartEndDateOfWeekFromCalendar(currentWeek)));

        for (WaterData data : PhysicalActivity.waterDataList) {
            String date = data.getWaterDate();
            String[] dateArray = date.split("-");
            Calendar targetCal = Calendar.getInstance();
            targetCal.clear();
            targetCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]));
            targetCal.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
            targetCal.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));
            int targetWeek = targetCal.get(Calendar.WEEK_OF_YEAR);
            Log.d(TAG, "getWeeklyData: currentWeek: " + currentWeek + " targetWeek: " + targetWeek);
            if (currentWeek == targetWeek) {
                int day = targetCal.get(Calendar.DAY_OF_WEEK);
                switch (day) {
                    case Calendar.MONDAY:
                        mondayCount += Float.parseFloat(data.getTotalWater());
                        break;
                    case Calendar.TUESDAY:
                        tuesdayCount += Float.parseFloat(data.getTotalWater());
                        break;
                    case Calendar.WEDNESDAY:
                        wednesdayCount += Float.parseFloat(data.getTotalWater());
                        break;
                    case Calendar.THURSDAY:
                        thursdayCount += Float.parseFloat(data.getTotalWater());
                        break;
                    case Calendar.FRIDAY:
                        fridayCount += Float.parseFloat(data.getTotalWater());
                        break;
                    case Calendar.SATURDAY:
                        saturdayCount += Float.parseFloat(data.getTotalWater());
                        break;
                    case Calendar.SUNDAY:
                        sundayCount += Float.parseFloat(data.getTotalWater());
                        break;
                }
            }
        }

        drawChart();

        Log.d(TAG, "getWeeklyData: mondayCount: " + mondayCount);
        Log.d(TAG, "getWeeklyData: tuesdayCount: " + tuesdayCount);
        Log.d(TAG, "getWeeklyData: wednesdayCount: " + wednesdayCount);
        Log.d(TAG, "getWeeklyData: thursdayCount: " + thursdayCount);
        Log.d(TAG, "getWeeklyData: fridayCount: " + fridayCount);
        Log.d(TAG, "getWeeklyData: saturdayCount: " + saturdayCount);
        Log.d(TAG, "getWeeklyData: sundayCount: " + sundayCount);
    }


    public void onRightClick(View view) {
        int weeksOfYear = Calendar.getInstance().getActualMaximum(Calendar.WEEK_OF_YEAR);
        resetBarChart();
        Log.d(TAG, "onRightClick: currentWeek: " + currentWeek + " weeksOfYear: " + weeksOfYear);
        if (currentWeek <= weeksOfYear) {
            currentWeek = currentWeek + 1;
            getWeeklyData(currentWeek);
        }
    }

    public void onLeftClick(View view) {
        resetBarChart();
        Log.d(TAG, "onLeftClick: currentWeek: " + currentWeek);
        if (currentWeek > 0) {
            currentWeek = currentWeek - 1;
            getWeeklyData(currentWeek);
        }
    }

    public void resetBarChart() {
        mondayCount = 0;
        tuesdayCount = 0;
        wednesdayCount = 0;
        thursdayCount = 0;
        fridayCount = 0;
        saturdayCount = 0;
        sundayCount = 0;
    }
}
