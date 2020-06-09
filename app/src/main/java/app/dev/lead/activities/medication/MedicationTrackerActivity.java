package app.dev.lead.activities.medication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import app.dev.lead.R;
import app.dev.lead.activities.dashboard.Medication;
import app.dev.lead.adapters.MedicationTrackerAdapter;

public class MedicationTrackerActivity extends AppCompatActivity {

    View view;
    TextView toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    MedicationTrackerAdapter adapter;
    ImageView viewAdd;
    String[] listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_tracker);


        initializeViews();

        setDataInToolbar();

        setViewPager();
    }

    public void initializeViews(){
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewAdd = findViewById(R.id.img_add);
        listItems = getResources().getStringArray(R.array.medication_array);
    }

    public void setDataInToolbar(){
        toolbar.setText("Medication Tracker");
    }

    public void onBackClick(View view){
        onBackPressed();
    }

    public void setViewPager(){
        adapter = new MedicationTrackerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onAddClick(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MedicationTrackerActivity.this);
        mBuilder.setTitle("Selection");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    Intent intent = new Intent(MedicationTrackerActivity.this, MedicationAlertActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(MedicationTrackerActivity.this, DoctorAppointmentActivity.class);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(MedicationTrackerActivity.this, listItems[i], Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
}
