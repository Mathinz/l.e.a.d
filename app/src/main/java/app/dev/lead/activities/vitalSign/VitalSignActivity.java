package app.dev.lead.activities.vitalSign;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.dev.lead.R;
import app.dev.lead.models.pojos.VitalSignData;
import app.dev.lead.models.pojos.VitalSignPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.Methods.getStartEndDateOfWeekFromCalendar;
import static app.dev.lead.utilities.Methods.goNextActivityWithFinish;
import static app.dev.lead.utilities.PreferenceHandler.getUserData;

public class VitalSignActivity extends AppCompatActivity {
    View view;
    TextView toolbar;
    BarChart chartGlucose, chartTemperature;
    LineChart lineChartBlood;
    TextView tvWeekly;
    public List<VitalSignData> vitalSignDataList;
    List<String> monGList = new ArrayList<String>(), tueGList = new ArrayList<String>(), wedGList = new ArrayList<String>(), thuGList = new ArrayList<String>(), friGList = new ArrayList<String>(), satGList = new ArrayList<String>(), sunGList = new ArrayList<String>();
    List<String> monBList = new ArrayList<String>(), tueBList = new ArrayList<String>(), wedBList = new ArrayList<String>(), thuBList = new ArrayList<String>(), friBList = new ArrayList<String>(), satBList = new ArrayList<String>(), sunBList = new ArrayList<String>();
    List<String> monB2List = new ArrayList<String>(), tueB2List = new ArrayList<String>(), wedB2List = new ArrayList<String>(), thuB2List = new ArrayList<String>(), friB2List = new ArrayList<String>(), satB2List = new ArrayList<String>(), sunB2List = new ArrayList<String>();
    List<String> monTList = new ArrayList<String>(), tueTList = new ArrayList<String>(), wedTList = new ArrayList<String>(), thuTList = new ArrayList<String>(), friTList = new ArrayList<String>(), satTList = new ArrayList<String>(), sunTList = new ArrayList<String>();
    double mondayCountG = 0, tuesdayCountG = 0, wednesdayCountG = 0, thursdayCountG = 0, fridayCountG = 0, saturdayCountG = 0, sundayCountG = 0;
    double mondayCountT = 0, tuesdayCountT = 0, wednesdayCountT = 0, thursdayCountT = 0, fridayCountT = 0, saturdayCountT = 0, sundayCountT = 0;
    double mondayCountB = 0, tuesdayCountB = 0, wednesdayCountB = 0, thursdayCountB = 0, fridayCountB = 0, saturdayCountB = 0, sundayCountB = 0;
    double mondayCountB2 = 0, tuesdayCountB2 = 0, wednesdayCountB2 = 0, thursdayCountB2 = 0, fridayCountB2 = 0, saturdayCountB2 = 0, sundayCountB2 = 0;


    String TAG = "theH";

    Calendar currentDate = Calendar.getInstance();
    int currentWeek = currentDate.get(Calendar.WEEK_OF_YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_sign);

        initializeViews();

        setDataInToolbar();

        getData();

    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        chartGlucose = findViewById(R.id.glucose_chart);
        lineChartBlood = findViewById(R.id.blood_chart);
        chartTemperature = findViewById(R.id.temperature_chart);
        tvWeekly = findViewById(R.id.tv_weekly);
        tvWeekly.setText(String.format(getResources().getString(R.string.weekly), getStartEndDateOfWeekFromCalendar()));
    }

    public void setDataInToolbar() {
        toolbar.setText("Vital Sign");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void onAddClick(View view) {
        goNextActivityWithFinish(VitalSignActivity.this, AddVitalSignActivity.class);
    }

    public void getData() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<VitalSignPojo> vitalSignPojoCall = apiService.getVitalSignData(getUserData(getApplicationContext()).getUserId());
        vitalSignPojoCall.enqueue(new Callback<VitalSignPojo>() {
            @Override
            public void onResponse(Call<VitalSignPojo> call, Response<VitalSignPojo> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().getData().isEmpty()) {
                            vitalSignDataList = response.body().getData();

                            getWeeklyData(currentWeek);
                            Log.d(TAG, "onResponse: stepsDataList: " + vitalSignDataList.size());
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some problems occurred, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VitalSignPojo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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

    public void getWeeklyData(int currentWeek) {
        if (vitalSignDataList != null) {
//            Log.d(TAG, "getWeeklyData: currentWeek: " + currentWeek);
            tvWeekly.setText(String.format(getResources().getString(R.string.weekly), getStartEndDateOfWeekFromCalendar(currentWeek)));
            for (VitalSignData data : vitalSignDataList) {

                String date = data.getDate();
                String[] dateArray = date.split("-");
                Calendar targetCal = Calendar.getInstance();
                targetCal.clear();
                targetCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]));
                targetCal.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
                targetCal.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));
                int targetWeek = targetCal.get(Calendar.WEEK_OF_YEAR);
//                Log.d(TAG, "getWeeklyData: currentWeek: " + currentWeek + " targetWeek: " + targetWeek);
                if (currentWeek == targetWeek) {
                    int day = targetCal.get(Calendar.DAY_OF_WEEK);
                    switch (day) {
                        case Calendar.MONDAY:
                            if (!data.getGlucoseLevel().equals("0"))
                                monGList.add(data.getGlucoseLevel());
                            if (!data.getBodyTemperature().equals("0"))
                                monTList.add(data.getBodyTemperature());
                            if (!data.getBloodPressure().equals("0/0")) {
                                String[] monB = data.getBloodPressure().split("/");
                                monBList.add(monB[0]);
                                monB2List.add(monB[1]);
                            }
//                            mondayCountG += Double.parseDouble(data.getGlucoseLevel());
//                            mondayCountB += Double.parseDouble(data.getBloodPressure());
//                            mondayCountT += Double.parseDouble(data.getBodyTemperature());
                            break;
                        case Calendar.TUESDAY:
                            if (!data.getGlucoseLevel().equals("0"))
                                tueGList.add(data.getGlucoseLevel());
                            if (!data.getBodyTemperature().equals("0"))
                                tueTList.add(data.getBodyTemperature());
                            if (!data.getBloodPressure().equals("0/0")) {
                                String[] tueB = data.getBloodPressure().split("/");
                                tueBList.add(tueB[0]);
                                tueB2List.add(tueB[1]);
                            }
//                            tuesdayCountG += Double.parseDouble(data.getGlucoseLevel());
//                            tuesdayCountB += Double.parseDouble(data.getBloodPressure());
//                            tuesdayCountT += Double.parseDouble(data.getBodyTemperature());
                            break;
                        case Calendar.WEDNESDAY:
                            if (!data.getGlucoseLevel().equals("0"))
                                wedGList.add(data.getGlucoseLevel());
                            if (!data.getBodyTemperature().equals("0"))
                                wedTList.add(data.getBodyTemperature());
                            if (!data.getBloodPressure().equals("0/0")) {
                                String[] wedB = data.getBloodPressure().split("/");
                                wedBList.add(wedB[0]);
                                wedB2List.add(wedB[1]);
                            }
//                            wednesdayCountG += Double.parseDouble(data.getGlucoseLevel());
//                            wednesdayCountB += Double.parseDouble(data.getBloodPressure());
//                            wednesdayCountT += Double.parseDouble(data.getBodyTemperature());
                            break;
                        case Calendar.THURSDAY:
                            if (!data.getGlucoseLevel().equals("0"))
                                thuGList.add(data.getGlucoseLevel());
                            if (!data.getBodyTemperature().equals("0"))
                                thuTList.add(data.getBodyTemperature());
                            if (!data.getBloodPressure().equals("0/0")) {
                                String[] thuB = data.getBloodPressure().split("/");
                                thuBList.add(thuB[0]);
                                thuB2List.add(thuB[1]);
                            }
//                            thursdayCountG += Double.parseDouble(data.getGlucoseLevel());
//                            thursdayCountB += Double.parseDouble(data.getBloodPressure());
//                            thursdayCountT += Double.parseDouble(data.getBodyTemperature());
                            break;
                        case Calendar.FRIDAY:
                            if (!data.getGlucoseLevel().equals("0"))
                                friGList.add(data.getGlucoseLevel());
                            if (!data.getBodyTemperature().equals("0"))
                                friTList.add(data.getBodyTemperature());
                            if (!data.getBloodPressure().equals("0/0")) {
                                String[] friB = data.getBloodPressure().split("/");
                                friBList.add(friB[0]);
                                friB2List.add(friB[1]);
                            }
//                            fridayCountG += Double.parseDouble(data.getGlucoseLevel());
//                            fridayCountB += Double.parseDouble(data.getBloodPressure());
//                            fridayCountT += Double.parseDouble(data.getBodyTemperature());
                            break;
                        case Calendar.SATURDAY:
                            if (!data.getGlucoseLevel().equals("0"))
                                satGList.add(data.getGlucoseLevel());
                            if (!data.getBodyTemperature().equals("0"))
                                satTList.add(data.getBodyTemperature());
                            if (!data.getBloodPressure().equals("0/0")) {
                                String[] satB = data.getBloodPressure().split("/");
                                satBList.add(satB[0]);
                                satB2List.add(satB[1]);
                            }
//                            saturdayCountG += Double.parseDouble(data.getGlucoseLevel());
//                            saturdayCountB += Double.parseDouble(data.getBloodPressure());
//                            saturdayCountT += Double.parseDouble(data.getBodyTemperature());
                            break;
                        case Calendar.SUNDAY:
                            if (!data.getGlucoseLevel().equals("0"))
                                sunGList.add(data.getGlucoseLevel());
                            if (!data.getBodyTemperature().equals("0"))
                                sunTList.add(data.getBodyTemperature());
                            if (!data.getBloodPressure().equals("0/0")) {
                                String[] sunB = data.getBloodPressure().split("/");
                                sunBList.add(sunB[0]);
                                sunB2List.add(sunB[1]);
                            }
//                            sundayCountG += Double.parseDouble(data.getGlucoseLevel());
//                            sundayCountB += Double.parseDouble(data.getBloodPressure());
//                            sundayCountT += Double.parseDouble(data.getBodyTemperature());
                            break;

                    }
                }
            }
        }

        getAverageData();

        drawChart(chartGlucose, "Glucose Level", ContextCompat.getColor(VitalSignActivity.this, R.color.colorStepsLight));
        drawChart(chartTemperature, "Boody Temperature", ContextCompat.getColor(VitalSignActivity.this, R.color.colorWaterLight));
        drawLineChart();
    }


    public void drawLineChart() {
//
//        lineChartBlood.setDrawBarShadow(false);
//        lineChartBlood.setDrawValueAboveBar(true);

        Description description = new Description();
        description.setText("");

        lineChartBlood.setDescription(description);
        lineChartBlood.setMaxVisibleValueCount(50);
        lineChartBlood.setPinchZoom(false);
        lineChartBlood.setDrawGridBackground(false);

        YAxis leftAxis = lineChartBlood.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        lineChartBlood.getAxisRight().setEnabled(false);

        float barWidth = 0.46f;

        List<Entry> yVals1 = new ArrayList<Entry>();
        List<Entry> yVals2 = new ArrayList<Entry>();

        yVals1.add(new Entry(0, (float) sundayCountB));
        yVals1.add(new Entry(1, (float) mondayCountB));
        yVals1.add(new Entry(2, (float) tuesdayCountB));
        yVals1.add(new Entry(3, (float) wednesdayCountB));
        yVals1.add(new Entry(4, (float) thursdayCountB));
        yVals1.add(new Entry(5, (float) fridayCountB));
        yVals1.add(new Entry(6, (float) saturdayCountB));

        yVals2.add(new Entry(0, (float) sundayCountB2));
        yVals2.add(new Entry(1, (float) mondayCountB2));
        yVals2.add(new Entry(2, (float) tuesdayCountB2));
        yVals2.add(new Entry(3, (float) wednesdayCountB2));
        yVals2.add(new Entry(4, (float) thursdayCountB2));
        yVals2.add(new Entry(5, (float) fridayCountB2));
        yVals2.add(new Entry(6, (float) saturdayCountB2));


        LineDataSet setComp1 = new LineDataSet(yVals1, "SYSTOLIC");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setCircleColor(ContextCompat.getColor(VitalSignActivity.this, R.color.colorFood));
        setComp1.setColor(ContextCompat.getColor(VitalSignActivity.this, R.color.colorFood));
        LineDataSet setComp2 = new LineDataSet(yVals2, "DIASTOLIC");
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp2.setCircleColor(ContextCompat.getColor(VitalSignActivity.this, R.color.colorNavyBlue));
        setComp2.setColor(ContextCompat.getColor(VitalSignActivity.this, R.color.colorNavyBlue));


        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Sun");
        xAxisLabel.add("Mon");
        xAxisLabel.add("Tue");
        xAxisLabel.add("Wed");
        xAxisLabel.add("Thu");
        xAxisLabel.add("Fri");
        xAxisLabel.add("Sat");


        XAxis xAxis = lineChartBlood.getXAxis();
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
//                Log.d(TAG, "getFormattedValue: " + value + " i: " + i);
                return xAxisLabel.get(i);
            }
        };

        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        LineData data = new LineData(dataSets);
        lineChartBlood.setData(data);
        lineChartBlood.invalidate(); // refresh
    }

    public void drawChart(BarChart barChart, String name, int color) {

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
        if (name.contains("Glucose")) {
            yVals1.add(new BarEntry(0, (float) sundayCountG));
            yVals1.add(new BarEntry(1, (float) mondayCountG));
            yVals1.add(new BarEntry(2, (float) tuesdayCountG));
            yVals1.add(new BarEntry(3, (float) wednesdayCountG));
            yVals1.add(new BarEntry(4, (float) thursdayCountG));
            yVals1.add(new BarEntry(5, (float) fridayCountG));
            yVals1.add(new BarEntry(6, (float) saturdayCountG));
        } else if (name.contains("Temperature")) {
            yVals1.add(new BarEntry(0, (float) sundayCountT));
            yVals1.add(new BarEntry(1, (float) mondayCountT));
            yVals1.add(new BarEntry(2, (float) tuesdayCountT));
            yVals1.add(new BarEntry(3, (float) wednesdayCountT));
            yVals1.add(new BarEntry(4, (float) thursdayCountT));
            yVals1.add(new BarEntry(5, (float) fridayCountT));
            yVals1.add(new BarEntry(6, (float) saturdayCountT));
        }


        BarDataSet set1;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, name);
            set1.setColor(color);

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
//                Log.d(TAG, "getFormattedValue: " + value + " i: " + i);
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

    public void resetBarChart() {
        mondayCountG = 0;
        tuesdayCountG = 0;
        wednesdayCountG = 0;
        thursdayCountG = 0;
        fridayCountG = 0;
        saturdayCountG = 0;
        sundayCountG = 0;

        mondayCountT = 0;
        tuesdayCountT = 0;
        wednesdayCountT = 0;
        thursdayCountT = 0;
        fridayCountT = 0;
        saturdayCountT = 0;
        sundayCountT = 0;

        mondayCountB = 0;
        tuesdayCountB = 0;
        wednesdayCountB = 0;
        thursdayCountB = 0;
        fridayCountB = 0;
        saturdayCountB = 0;
        sundayCountB = 0;

        mondayCountB2 = 0;
        tuesdayCountB2 = 0;
        wednesdayCountB2 = 0;
        thursdayCountB2 = 0;
        fridayCountB2 = 0;
        saturdayCountB2 = 0;
        sundayCountB2 = 0;
    }

    public void getAverageData() {
        mondayCountG = calculateAverage(monGList);
        tuesdayCountG = calculateAverage(tueGList);
        wednesdayCountG = calculateAverage(wedGList);
        thursdayCountG = calculateAverage(thuGList);
        fridayCountG = calculateAverage(friGList);
        saturdayCountG = calculateAverage(satGList);
        sundayCountG = calculateAverage(sunGList);

        mondayCountT = calculateAverage(monTList);
        tuesdayCountT = calculateAverage(tueTList);
        wednesdayCountT = calculateAverage(wedTList);
        thursdayCountT = calculateAverage(thuTList);
        fridayCountT = calculateAverage(friTList);
        saturdayCountT = calculateAverage(satTList);
        sundayCountT = calculateAverage(sunTList);

        mondayCountB = calculateAverage(monBList);
        tuesdayCountB = calculateAverage(tueBList);
        wednesdayCountB = calculateAverage(wedBList);
        thursdayCountB = calculateAverage(thuBList);
        fridayCountB = calculateAverage(friBList);
        saturdayCountB = calculateAverage(satBList);
        sundayCountB = calculateAverage(sunBList);

        mondayCountB2 = calculateAverage(monB2List);
        tuesdayCountB2 = calculateAverage(tueB2List);
        wednesdayCountB2 = calculateAverage(wedB2List);
        thursdayCountB2 = calculateAverage(thuB2List);
        fridayCountB2 = calculateAverage(friB2List);
        saturdayCountB2 = calculateAverage(satB2List);
        sundayCountB2 = calculateAverage(sunB2List);

    }

    private double calculateAverage(List<String> marks) {
        Log.d(TAG, "calculateAverage: list size: " + marks.size());
        double sum = 0;
        if (!marks.isEmpty()) {
            for (String mark : marks) {
                Log.d(TAG, "calculateAverage: value: " + mark);
                sum += Double.parseDouble(mark);
            }
            return sum / marks.size();
        }
//        Log.d(TAG, "calculateAverage: " + sum);
        return sum;
    }

}
