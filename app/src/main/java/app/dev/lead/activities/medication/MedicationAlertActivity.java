package app.dev.lead.activities.medication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.dev.lead.R;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.models.pojos.MedData;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.Methods.showToast;
import static app.dev.lead.utilities.PreferenceHandler.getUserData;

public class MedicationAlertActivity extends AppCompatActivity {

    String TAG = "theH";

    View view;
    TextView toolbar;
    EditText etName, etReason, etOther;
    Spinner spinner;
    ProgressDialog loading;

    String[] timeList;
    ArrayAdapter<String> spinnerAdapter = null;
    String often = "1X a day", name, reason, other;

    Intent intent = null;
    MedData data = null;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_alert);

        initializeViews();

        setDataInToolbar();

        spinnerWork();

        setUpdateData();

    }

    private void setUpdateData() {
        intent = getIntent();

        Log.d(TAG, "----------MedicationAlertActivity----------");

        if (intent != null) {
            data = intent.getParcelableExtra("data");
            if (data != null) {
                isEditMode = true;
                etName.setText(data.getMedicineName());
                etReason.setText(data.getMedicineReason());
                etOther.setText(data.getOther());
                often = data.getOften();
                int spinnerPosition = spinnerAdapter.getPosition(data.getOften());
                spinner.setSelection(spinnerPosition);
                Log.d(TAG, "onCreate: getMedicineName: " + data.getMedicineName());
            } else {
                isEditMode = false;
                Log.d(TAG, "onCreate: data is null");
            }
        } else {
            isEditMode = false;
            Log.d(TAG, "onCreate: intent is null");
        }
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        etName = findViewById(R.id.alert_name);
        etReason = findViewById(R.id.alert_reason);
        etOther = findViewById(R.id.alert_other);
        spinner = findViewById(R.id.alert_spinner);
    }

    public void setDataInToolbar() {
        toolbar.setText("Medication Alert");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void spinnerWork() {
        timeList = getResources().getStringArray(R.array.time_array);
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.item_spinner1, timeList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                often = timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addMedicationAlert(View view) {
        name = etName.getText().toString();
        reason = etReason.getText().toString();
        other = etOther.getText().toString();

        if (name.isEmpty()) {
            showToast(MedicationAlertActivity.this, "Enter medicine name.");
        }
//        else if (reason.isEmpty()) {
//            showToast(MedicationAlertActivity.this, "Enter medicine reason.");
//        }
        else {
            loading = ProgressDialog.show(MedicationAlertActivity.this, null, "Please Wait", true, false);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<BasicPojo> call = apiService.addMedicationAlert(getUserData(getApplicationContext()).getUserId(), name, reason, often, other);
            if (isEditMode) {
                call = apiService.updateMedicationAlert(data.getMedicineId(), name, reason, often, other);
            }
            call.enqueue(new Callback<BasicPojo>() {
                @Override
                public void onResponse(Call<BasicPojo> call, Response<BasicPojo> response) {
                    loading.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("success")) {
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Some problems occurred, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BasicPojo> call, Throwable t) {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
