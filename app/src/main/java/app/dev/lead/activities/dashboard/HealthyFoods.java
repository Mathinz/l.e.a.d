package app.dev.lead.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import app.dev.lead.R;
import app.dev.lead.activities.healthyFoods.FoodActivity;


public class HealthyFoods extends AppCompatActivity implements View.OnClickListener {

    View view;
    TextView toolbar;
    Button btnNonStarchyVegetables,btnStarches,btnProtein,btnFruit,btnDairy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_foods);
        initializeViews();

        setDataInToolbar();
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        btnNonStarchyVegetables = findViewById(R.id.btn_non_starchy_vegetables);
        btnStarches = findViewById(R.id.btn_starches);
        btnProtein = findViewById(R.id.btn_protein);
        btnFruit = findViewById(R.id.btn_fruit);
        btnDairy = findViewById(R.id.btn_dairy);

        btnNonStarchyVegetables.setOnClickListener(this);
        btnStarches.setOnClickListener(this);
        btnProtein.setOnClickListener(this);
        btnFruit.setOnClickListener(this);
        btnDairy.setOnClickListener(this);
    }

    public void setDataInToolbar() {
        toolbar.setText("Healthy Foods");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_non_starchy_vegetables:
                sendData(1);
                break;
            case R.id.btn_starches:
                sendData(2);
                break;
            case R.id.btn_protein:
                sendData(3);
                break;
            case R.id.btn_fruit:
                sendData(4);
                break;
            case R.id.btn_dairy:
                sendData(5);
                break;
        }
    }

    public void sendData(int position){
        Intent intent = new Intent(HealthyFoods.this, FoodActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);

    }
}