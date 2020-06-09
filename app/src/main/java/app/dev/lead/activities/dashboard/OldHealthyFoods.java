package app.dev.lead.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.dev.lead.R;
import app.dev.lead.activities.healthyFoods.FoodActivity;

import static app.dev.lead.utilities.Methods.goNextActivityWithOutFinish;

public class OldHealthyFoods extends AppCompatActivity implements View.OnClickListener {
    View view;
    TextView toolbar;
    CardView viewFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_healthy_foods);
        initializeViews();

        setDataInToolbar();
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        viewFoods = findViewById(R.id.card_foods);

        viewFoods.setOnClickListener(this);
    }

    public void setDataInToolbar() {
        toolbar.setText("Healthy Foods");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.card_foods) {
            goNextActivityWithOutFinish(OldHealthyFoods.this, FoodActivity.class);
        }
    }
}
