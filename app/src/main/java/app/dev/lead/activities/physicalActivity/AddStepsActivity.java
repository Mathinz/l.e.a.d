package app.dev.lead.activities.physicalActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.dev.lead.R;
import app.dev.lead.activities.dashboard.PhysicalActivity;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import app.dev.lead.utilities.pedometer.StepDetector;
import app.dev.lead.utilities.pedometer.StepListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.Methods.goNextActivityWithFinish;
import static app.dev.lead.utilities.PreferenceHandler.getUserData;

public class AddStepsActivity extends AppCompatActivity implements SensorEventListener, StepListener {

    // Pedometer Variables
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;

    //Variables used in calculations
    long startTime = 0;
    long timeInMilliseconds = 0;
    long elapsedTime = 0;
    long updatedTime = 0;
    String timeString;

    int stepCount = 0;
    double distance = 0;
    double calories = 0;

    private boolean active = false; //Used to checked if the counter is running
    private Handler handler = new Handler(); //Used to update the time in the UI

    View view;
    TextView toolbar;
    TextView tvSteps, tvTime, tvDistance, tvCalories;
    Button bEnd, bStart;

    ProgressDialog loading;
    String TAG = "theH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_steps);

        initializeViews();

        setDataInToolbar();

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        tvSteps.setText(String.format(getResources().getString(R.string.steps), String.valueOf(0)));
        tvTime.setText(String.format(getResources().getString(R.string.time), String.valueOf(0)));
        tvDistance.setText(String.format(getResources().getString(R.string.distance), String.valueOf(0)));
        tvCalories.setText(String.format(getResources().getString(R.string.calories), String.valueOf(0)));


        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!active) {
                    bStart.setText(R.string.pause);
                    sensorManager.registerListener(AddStepsActivity.this, stepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(timerRunnable, 0);
                    active = true;
                } else {
                    bStart.setText(R.string.start);
                    sensorManager.unregisterListener(AddStepsActivity.this, stepDetectorSensor);
                    elapsedTime += timeInMilliseconds;
                    handler.removeCallbacks(timerRunnable);
                    active = false;
                }
            }
        });

        bEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepCount != 0)
                    sendStepsDataOnServer();
                else
                    onBackPressed();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void step(long timeNs) {
        //Step count
        stepCount++;
        tvSteps.setText(String.format(getResources().getString(R.string.steps), String.valueOf(stepCount)));

        //Distance calculation
        distance = stepCount * 0.8; //Average step length in an average adult
        String distanceString = String.format("%.2f", distance);
        tvDistance.setText(String.format(getResources().getString(R.string.distance), distanceString));

        calories = stepCount * 0.0072;
        String caloriesString = String.format("%.2f", calories);
        tvCalories.setText(String.format(getResources().getString(R.string.calories), caloriesString));
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        tvSteps = findViewById(R.id.tv_steps);
        tvTime = findViewById(R.id.tv_time);
        tvDistance = findViewById(R.id.tv_distance);
        tvCalories = findViewById(R.id.tv_calories);
        bStart = findViewById(R.id.btn_start);
        bEnd = findViewById(R.id.btn_end);
    }

    public void setDataInToolbar() {
        toolbar.setText("Add Steps");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    //Runnable that calculates the elapsed time since the user presses the "start" button
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = elapsedTime + timeInMilliseconds;

            int seconds = (int) (updatedTime / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            minutes = minutes % 60;

            timeString = String.format("%d:%s:%s", hours, String.format("%02d", minutes), String.format("%02d", seconds));

            tvTime.setText(String.format(getResources().getString(R.string.time), timeString));

            handler.postDelayed(this, 0);
        }
    };

    public void sendStepsDataOnServer() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Log.d(TAG, "onClick: stepCount: " + stepCount + ", distance: " + distance + ", calories: " + calories + ", timeString: " + timeString + ", date: " + sdf.format(new Date()));
        loading = ProgressDialog.show(AddStepsActivity.this, null, "Please Wait", true, false);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<BasicPojo> call = apiService.addStepsData(getUserData(getApplicationContext()).getUserId(), stepCount, sdf.format(new Date()),timeString, String.format("%.2f", distance), String.format("%.2f", calories));
        call.enqueue(new Callback<BasicPojo>() {
            @Override
            public void onResponse(Call<BasicPojo> call, Response<BasicPojo> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("success")) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some problems occurred, please try again.", Toast.LENGTH_SHORT).show();
                }
                goNextActivityWithFinish(AddStepsActivity.this, PhysicalActivity.class);
            }

            @Override
            public void onFailure(Call<BasicPojo> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                goNextActivityWithFinish(AddStepsActivity.this, PhysicalActivity.class);

            }
        });

    }
}
