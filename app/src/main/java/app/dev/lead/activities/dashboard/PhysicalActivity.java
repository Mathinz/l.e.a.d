package app.dev.lead.activities.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.dev.lead.R;
import app.dev.lead.activities.physicalActivity.StepsActivity;
import app.dev.lead.activities.physicalActivity.WaterActivity;
import app.dev.lead.models.pojos.StepsData;
import app.dev.lead.models.pojos.StepsPojo;
import app.dev.lead.models.pojos.WaterData;
import app.dev.lead.models.pojos.WaterPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.Methods.goNextActivityWithFinish;
import static app.dev.lead.utilities.Methods.goNextActivityWithOutFinish;
import static app.dev.lead.utilities.PreferenceHandler.getUserData;

public class PhysicalActivity extends AppCompatActivity implements View.OnClickListener {

    View view;
    TextView toolbar;
    CardView viewWater, viewSteps;
    DonutProgress dpSteps,dpDistance,dpCalories,dpTime,dpWater;
    TextView tvSteps,tvDistance,tvCalories,tvTime,tvWater;
    String TAG = "theH";
    public static List<StepsData> stepsDataList;
    public static List<WaterData> waterDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        initializeViews();

        setDataInToolbar();

        getData();

    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        viewWater = findViewById(R.id.card_water);
        viewSteps = findViewById(R.id.card_steps);

        dpSteps = findViewById(R.id.donut_steps);
        dpDistance =findViewById(R.id.donut_distance);
        dpCalories = findViewById(R.id.donut_calories);
        dpTime = findViewById(R.id.donut_minutes);
        dpWater = findViewById(R.id.donut_water);

        tvSteps = findViewById(R.id.tv_steps);
        tvDistance =findViewById(R.id.tv_distance);
        tvCalories = findViewById(R.id.tv_calories);
        tvTime = findViewById(R.id.tv_minutes);
        tvWater = findViewById(R.id.tv_water);

        tvSteps.setText(String.format(getResources().getString(R.string.p_steps), String.valueOf(1)));
        tvDistance.setText(String.format(getResources().getString(R.string.p_distance), String.valueOf(1)));
        tvCalories.setText(String.format(getResources().getString(R.string.p_calories), String.valueOf(1)));
        tvTime.setText(String.format(getResources().getString(R.string.p_minutes), String.valueOf(1)));
        tvWater.setText(String.format(getResources().getString(R.string.p_water), String.valueOf(1)));

        viewSteps.setOnClickListener(this);
        viewWater.setOnClickListener(this);
    }

    public void setDataInToolbar() {
        toolbar.setText("Physical Activity");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_steps:
                goNextActivityWithFinish(PhysicalActivity.this, StepsActivity.class);
                break;
            case R.id.card_water:
                goNextActivityWithFinish(PhysicalActivity.this, WaterActivity.class);
                break;
        }
    }

    public void getData() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<StepsPojo> stepsPojoCall = apiService.getStepsData(getUserData(getApplicationContext()).getUserId());
        stepsPojoCall.enqueue(new Callback<StepsPojo>() {
            @Override
            public void onResponse(Call<StepsPojo> call, Response<StepsPojo> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().getData().isEmpty()) {
                            stepsDataList = response.body().getData();
                            getWeeklyDataOfSteps();
                            Log.d(TAG, "onResponse: stepsDataList: " + stepsDataList.size());
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some problems occurred, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StepsPojo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Call<WaterPojo> waterPojoCall = apiService.getWaterData(getUserData(getApplicationContext()).getUserId());
        waterPojoCall.enqueue(new Callback<WaterPojo>() {
            @Override
            public void onResponse(Call<WaterPojo> call, Response<WaterPojo> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().getData().isEmpty()) {
                            waterDataList = response.body().getData();
                            getWeeklyDataOfWater();
                            Log.d(TAG, "onResponse: waterDataList: " + waterDataList.size());
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some problems occurred, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WaterPojo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWeeklyDataOfSteps() {
        int totalSteps = 0 ,stepsTime = 0;
        double stepsDistance = 0.0, stepsCalories = 0.0;
        Calendar currentDate = Calendar.getInstance();
        int currentWeek = currentDate.get(Calendar.WEEK_OF_YEAR);
        for (StepsData data : stepsDataList) {

            String date = data.getStepsDate();
            String[] dateArray = date.split("-");
            Calendar targetCal = Calendar.getInstance();
            targetCal.clear();
            targetCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]));
            targetCal.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
            targetCal.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));
            int targetWeek = targetCal.get(Calendar.WEEK_OF_YEAR);
            Log.d(TAG, "getWeeklyDataOfSteps: currentWeek: " + currentWeek + " targetWeek: " + targetWeek);
            if (currentWeek == targetWeek) {
                totalSteps = totalSteps + data.getTotalSteps();
                stepsDistance = stepsDistance + Double.parseDouble(data.getStepsDistance());
                stepsCalories = stepsCalories + Double.parseDouble(data.getStepsCalories());
                Log.d(TAG, "getWeeklyDataOfSteps: totalSteps: " + totalSteps);
                Log.d(TAG, "getWeeklyDataOfSteps: stepsDistance: " + String.format("%.2f", stepsDistance));
                Log.d(TAG, "getWeeklyDataOfSteps: stepsCalories: " + String.format("%.2f", stepsCalories));
                String[] timeArray = data.getStepsTime().split(":");
                int hMin = Integer.parseInt(timeArray[0]) * 60; // multiply the time value by 60
                int mMin = Integer.parseInt(timeArray[1]);
                int sMin = Integer.parseInt(timeArray[2]) / 60; //divide the time value by 60
                int totalMin = hMin + mMin + sMin;
                stepsTime = stepsTime + totalMin;
                Log.d(TAG, "sendStepsDataOnServer: hMin: " + hMin + " mMin: " + mMin + " sMin: " + sMin + " totalMin: " + totalMin + " stepsTime: " + stepsTime);

//                val day = targetCal.get(Calendar.DAY_OF_WEEK)
//
//                when (day) {
//                    Calendar.MONDAY -> mondayCount += walkActivity.walkcalories!!
//                            Calendar.TUESDAY -> tuesdayCount += walkActivity.walkcalories!!
//                            Calendar.WEDNESDAY -> wednesdayCount += walkActivity.walkcalories!!
//                            Calendar.THURSDAY -> thursdayCount += walkActivity.walkcalories!!
//                            Calendar.FRIDAY -> fridayCount += walkActivity.walkcalories!!
//                            Calendar.SATURDAY -> saturdayCount += walkActivity.walkcalories!!
//                            Calendar.SUNDAY -> sundayCount += walkActivity.walkcalories!!
//                }
            }
        }

        tvSteps.setText(String.format(getResources().getString(R.string.p_steps), String.valueOf(totalSteps)));
        tvTime.setText(String.format(getResources().getString(R.string.p_minutes), String.valueOf(stepsTime)));
        tvDistance.setText(String.format(getResources().getString(R.string.p_distance), String.format("%.2f", stepsDistance)));
        tvCalories.setText(String.format(getResources().getString(R.string.p_calories), String.format("%.2f", stepsCalories)));

        dpSteps.setProgress(getPercentageValue(String.valueOf(totalSteps)));
        dpTime.setProgress(getPercentageValue(String.valueOf(stepsTime)));
        dpDistance.setProgress(getPercentageValue(String.valueOf(stepsDistance)));
        dpCalories.setProgress(getPercentageValue(String.valueOf(stepsCalories)));

        Log.d(TAG, "getPercentageValue: totalSteps: "+getPercentageValue(String.valueOf(totalSteps)));
        Log.d(TAG, "getPercentageValue: stepsTime: "+getPercentageValue(String.valueOf(stepsTime)));
        Log.d(TAG, "getPercentageValue: stepsDistance: "+getPercentageValue(String.valueOf(stepsDistance)));
        Log.d(TAG, "getPercentageValue: stepsCalories: "+getPercentageValue(String.valueOf(stepsCalories)));
    }

    public void getWeeklyDataOfWater() {
        double totalWater = 0.0;
        Calendar currentDate = Calendar.getInstance();
        int currentWeek = currentDate.get(Calendar.WEEK_OF_YEAR);
        for (WaterData data : waterDataList) {

            String date = data.getWaterDate();
            String[] dateArray = date.split("-");
            Calendar targetCal = Calendar.getInstance();
            targetCal.clear();
            targetCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]));
            targetCal.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
            targetCal.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));
            int targetWeek = targetCal.get(Calendar.WEEK_OF_YEAR);
            Log.d(TAG, "getWeeklyDataOfWater: currentWeek: " + currentWeek + " targetWeek: " + targetWeek);
            if (currentWeek == targetWeek) {
                totalWater = totalWater + Double.parseDouble(data.getTotalWater());
                Log.d(TAG, "getWeeklyDataOfWater: totalWater: " + String.format("%.2f", totalWater));
            }
        }

        dpWater.setProgress(getPercentageValue(String.valueOf(totalWater)));
        if (totalWater > 1.9){
            tvWater.setText(String.format(getResources().getString(R.string.p_waters),  String.format("%.2f", totalWater)));
        }else {
            tvWater.setText(String.format(getResources().getString(R.string.p_water),  String.format("%.2f", totalWater)));
        }
    }

    public int getPercentageValue(String value) {
        double newValue = Math.round(Double.parseDouble(value));
        int total = 100;
        if (newValue > 100) {
            total = 1000;
        }else  if (newValue > 1000) {
            total = 10000;
        }else  if (newValue > 10000) {
            total = 100000;
        }
        double result =  (newValue / total) * 100;
        return  (int) Math.round(result);
    }
}
