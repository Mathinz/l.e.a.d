package app.dev.lead.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.dev.lead.R;
import app.dev.lead.activities.evaluation.EvaluationProgramActivity;
import app.dev.lead.activities.evaluation.DaySelectionActivity;

public class EvaluationActivity extends AppCompatActivity implements View.OnClickListener {

    View view;
    TextView toolbar;
    Button btnPreMeal,btnPostMeal,btnPreEvaluation,btnPostEvaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        initializeViews();

        setDataInToolbar();
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);

        btnPreMeal = findViewById(R.id.btn_pre_meal);
        btnPostMeal = findViewById(R.id.btn_post_meal);
        btnPreEvaluation = findViewById(R.id.btn_pre_evaluation);
        btnPostEvaluation = findViewById(R.id.btn_post_evaluation);

        btnPreMeal.setOnClickListener(this);
        btnPostMeal.setOnClickListener(this);
        btnPreEvaluation.setOnClickListener(this);
        btnPostEvaluation.setOnClickListener(this);
    }

    public void setDataInToolbar() {
        toolbar.setText("Evaluation");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pre_meal:
                Intent intent1 = new Intent(EvaluationActivity.this, DaySelectionActivity.class);
                intent1.putExtra("type","Pre ");
                startActivity(intent1);
                break;
            case R.id.btn_post_meal:
                Intent intent2 = new Intent(EvaluationActivity.this, DaySelectionActivity.class);
                intent2.putExtra("type","Post ");
                startActivity(intent2);
                break;
            case R.id.btn_pre_evaluation:
                Intent intent3 = new Intent(EvaluationActivity.this, EvaluationProgramActivity.class);
                intent3.putExtra("type","Pre ");
                startActivity(intent3);
                break;
            case R.id.btn_post_evaluation:
                Intent intent4 = new Intent(EvaluationActivity.this, EvaluationProgramActivity.class);
                intent4.putExtra("type","Post ");
                startActivity(intent4);
                break;
        }
    }
}
