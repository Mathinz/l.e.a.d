package app.dev.lead.activities.dashboard;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.dev.lead.R;
import app.dev.lead.activities.medication.DoctorAppointmentActivity;
import app.dev.lead.activities.medication.MedicationAlertActivity;
import app.dev.lead.activities.medication.MedicationTrackerActivity;

public class Medication extends AppCompatActivity {

    View view;
    TextView toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        initializeViews();

        setDataInToolbar();
    }

    public void initializeViews(){
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
    }

    public void setDataInToolbar(){
        toolbar.setText("Medication");
    }

    public void onBackClick(View view){
        onBackPressed();
    }

    public void pillAlert(View view) {
        Intent intent = new Intent(Medication.this, MedicationAlertActivity.class);
        startActivity(intent);
    }

    public void appointment(View view) {
        Intent intent = new Intent(Medication.this, DoctorAppointmentActivity.class);
        startActivity(intent);
    }

    public void track(View view) {
        Intent intent = new Intent(Medication.this, MedicationTrackerActivity.class);
        startActivity(intent);
    }
}
