package app.dev.lead.activities.vitalSign;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.dev.lead.R;
import app.dev.lead.activities.dashboard.PhysicalActivity;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.Methods.goNextActivityWithFinish;
import static app.dev.lead.utilities.Methods.showToast;
import static app.dev.lead.utilities.PreferenceHandler.getUserData;

public class AddVitalSignActivity extends AppCompatActivity {
    View view;
    TextView toolbar;
    EditText etGlucoseLevel, etBloodPressureSy, etBloodPressureDia, etBodyTemperature;
    ProgressDialog loading;
    String TAG = "theH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vital_sign);

        initializeViews();

        setDataInToolbar();

        etBodyTemperature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String v = editable.toString();
                if (TextUtils.isEmpty(v) && v.length() > 0) {
                    double t = Double.parseDouble(v);
                    if (t > 97 && t < 99) {
                        Toast.makeText(getApplicationContext(), "Your body temperature is normal.", Toast.LENGTH_SHORT).show();
                    } else if (t > 100.4) {
                        Toast.makeText(getApplicationContext(), "You are ill.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Your body temperature is below from normal.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        etGlucoseLevel = findViewById(R.id.et_glucose_level);
        etBloodPressureSy = findViewById(R.id.et_blood_pressure_sy);
        etBloodPressureDia = findViewById(R.id.et_blood_pressure_dia);
        etBodyTemperature = findViewById(R.id.et_body_temperature);
    }

    public void setDataInToolbar() {
        toolbar.setText("Add Vital Sign");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void addVitalSign(View view) {
        String glucoseLevel = etGlucoseLevel.getText().toString();
        String bloodPressureSy = etBloodPressureSy.getText().toString();
        String bloodPressureDia = etBloodPressureDia.getText().toString();
        String bodyTemperature = etBodyTemperature.getText().toString();

        if (glucoseLevel.isEmpty() && (bloodPressureSy.isEmpty() || bloodPressureDia.isEmpty()) && bodyTemperature.isEmpty()) {
            showToast(AddVitalSignActivity.this, "Please enter values.");
        } else {
            String bloodPressure = "0/0";
            if (glucoseLevel.isEmpty())
                glucoseLevel = "0";
            if (bodyTemperature.isEmpty())
                bodyTemperature = "0";
            if (!(bloodPressureSy.isEmpty() && bloodPressureDia.isEmpty()))
                bloodPressure = bloodPressureSy + "/" + bloodPressureDia;

            Log.d(TAG, "addVitalSign: bloodPressure: " + bloodPressure);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            loading = ProgressDialog.show(AddVitalSignActivity.this, null, "Please Wait", true, false);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<BasicPojo> call = apiService.addVitalSignData(getUserData(getApplicationContext()).getUserId(), sdf.format(new Date()), glucoseLevel, bloodPressure, bodyTemperature);
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
                    goNextActivityWithFinish(AddVitalSignActivity.this, VitalSignActivity.class);
                }

                @Override
                public void onFailure(Call<BasicPojo> call, Throwable t) {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    goNextActivityWithFinish(AddVitalSignActivity.this, VitalSignActivity.class);

                }
            });
        }
    }
}
