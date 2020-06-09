package app.dev.lead.activities.evaluation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.dev.lead.R;

public class DaySelectionActivity extends AppCompatActivity {

    View view;
    TextView toolbar;
    Button btnWeekday,btnWeekend;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_selection);

        initializeViews();

        setDataInToolbar();

        btnWeekday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DaySelectionActivity.this, MealActivity.class);
                intent1.putExtra("day","Weekday ");
                intent1.putExtra("type",type);
                startActivity(intent1);
            }
        });

        btnWeekend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DaySelectionActivity.this, MealActivity.class);
                intent1.putExtra("day","Weekend ");
                intent1.putExtra("type",type);
                startActivity(intent1);
            }
        });

    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);

        btnWeekday = findViewById(R.id.btn_weekday);
        btnWeekend = findViewById(R.id.btn_weekend);

        type = getIntent().getStringExtra("type");
    }

    public void setDataInToolbar() {
        toolbar.setText(type+"Meal");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

}
