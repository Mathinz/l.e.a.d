package app.dev.lead.activities.healthyFoods;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.dev.lead.R;

public class OldFoodActivity extends AppCompatActivity {

    View view;
    TextView toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_food);

        initializeViews();

        setDataInToolbar();
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
    }

    public void setDataInToolbar() {
        toolbar.setText("Food");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }
}
