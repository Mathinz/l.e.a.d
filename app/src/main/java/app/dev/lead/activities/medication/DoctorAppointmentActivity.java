package app.dev.lead.activities.medication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.dev.lead.R;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.models.pojos.DocData;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.Methods.showToast;
import static app.dev.lead.utilities.PreferenceHandler.getUserData;

public class DoctorAppointmentActivity extends AppCompatActivity {

    String TAG = "theH";
    View view;
    TextView toolbar;
    TextView tvDate, tvTime;
    EditText etName, etReason, etLocation;

    ProgressDialog loading;
    String name= "", reason= "", location= "", formattedTime = "", formattedDate = "";

    Intent intent = null;
    DocData data = null;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment);

        initializeViews();

        setUpdateData();

        setDataInToolbar();

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog();
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog();
            }
        });


    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        tvDate = findViewById(R.id.appointment_date);
        tvTime = findViewById(R.id.appointment_time);
        etName = findViewById(R.id.appointment_name);
        etReason = findViewById(R.id.appointment_reason);
        etLocation = findViewById(R.id.appointment_location);
    }


    public void setDataInToolbar() {
        toolbar.setText("Doctor Appointment");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void timePickerDialog() {
        Calendar mCalendar = Calendar.getInstance();
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(DoctorAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                datetime.set(Calendar.MINUTE, selectedMinute);
                String time;
                if (String.valueOf(selectedHour).length() == 1) {
                    if (String.valueOf(selectedMinute).length() == 1) {
                        time = "0" + selectedHour + ":0" + selectedMinute;
                    } else {
                        time = "0" + selectedHour + ":" + selectedMinute;

                    }

                } else {

                    if (String.valueOf(selectedMinute).length() == 1) {
                        time = selectedHour + ":0" + selectedMinute;
                    } else {
                        time = selectedHour + ":" + selectedMinute;
                    }

                }

                SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat outFormat = new SimpleDateFormat("hh:mm a");
                try {
                    formattedTime = outFormat.format(inFormat.parse(time));
                    tvTime.setText(formattedTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void datePickerDialog() {
        DatePickerDialog mDatePicker;
        final Calendar mCalendar = Calendar.getInstance();

        mDatePicker = new DatePickerDialog(DoctorAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                formattedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(mCalendar.getTime());
                tvDate.setText(formattedDate);
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }

    public void addDoctorAppointment(View view) {
        name = etName.getText().toString();
        reason = etReason.getText().toString();
        location = etLocation.getText().toString();

        if (formattedDate.isEmpty()) {
            showToast(DoctorAppointmentActivity.this, "Enter appointment date.");
        } else if (formattedTime.isEmpty()) {
            showToast(DoctorAppointmentActivity.this, "Enter appointment time.");
        }
//        else if (name.isEmpty()) {
//            showToast(DoctorAppointmentActivity.this, "Enter doctor name.");
//        }
        else if (reason.isEmpty()) {
            showToast(DoctorAppointmentActivity.this, "Enter appointment reason.");
        }
//        else if (location.isEmpty()) {
//            showToast(DoctorAppointmentActivity.this, "Enter doctor location.");
//        }
        else {
            loading = ProgressDialog.show(DoctorAppointmentActivity.this, null, "Please Wait", true, false);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<BasicPojo> call = apiService.addDoctorAppointment(getUserData(getApplicationContext()).getUserId(), name, location, reason, formattedDate, formattedTime);
            if (isEditMode) {
                Log.d(TAG, "addDoctorAppointment: update is called");
                call = apiService.updateDoctorAppointment(data.getAppointmentId(), name, location, reason, formattedDate, formattedTime);
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

    public void setUpdateData() {
        intent = getIntent();
        Log.d(TAG, "----------DoctorAppointmentActivity----------");
        if (intent != null) {
            data = intent.getParcelableExtra("data");
            if (data != null) {
                isEditMode = true;
                formattedDate = data.getDate();
                formattedTime = data.getTime();
                tvDate.setText(formattedDate);
                tvTime.setText(formattedTime);
                etName.setText(data.getDoctorName());
                etLocation.setText(data.getAppointmentLocation());
                etReason.setText(data.getAppointmentReason());

                Log.d(TAG, "onCreate: getDoctorName: " + data.getDoctorName());
            } else {
                isEditMode = false;
                Log.d(TAG, "onCreate: data is null");
            }
        } else {
            isEditMode = false;
            Log.d(TAG, "onCreate: intent is null");
        }
    }
}
