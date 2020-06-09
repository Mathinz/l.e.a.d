package app.dev.lead.activities.evaluation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.dev.lead.R;
import app.dev.lead.utilities.PreferenceHandler;
import app.dev.lead.utilities.RadioValueChangeListener;
import app.dev.lead.adapters.Option2Adapter;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluationProgramActivity extends AppCompatActivity implements RadioValueChangeListener {

    View view;
    TextView toolbar;
    RecyclerView rvProgram, rvInfoGraphic, rvHealth;
    String TAG="theH",type = "";
    List<String> programList, infographicList, healthList;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_program);

        initializeViews();

        setDataInToolbar();

        addDefaultValues();

        setRecyclerView();
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);

        rvProgram = findViewById(R.id.rv_program);
        rvInfoGraphic = findViewById(R.id.rv_infographic);
        rvHealth = findViewById(R.id.rv_health);

        type = getIntent().getStringExtra("type");
    }

    public void setDataInToolbar() {
        toolbar.setText(type + "Evaluation");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void setRecyclerView() {

        String[] arrayProgram = getResources().getStringArray(R.array.array_program);
        Option2Adapter option2Adapter = new Option2Adapter(EvaluationProgramActivity.this, this, arrayProgram, programList, "1");
        rvProgram.setLayoutManager(new LinearLayoutManager(EvaluationProgramActivity.this, LinearLayoutManager.VERTICAL, false));
        rvProgram.setAdapter(option2Adapter);

        String[] arrayInfographic = getResources().getStringArray(R.array.array_infographic);
        Option2Adapter option2Adapter1 = new Option2Adapter(EvaluationProgramActivity.this, this, arrayInfographic, infographicList, "2");
        rvInfoGraphic.setLayoutManager(new LinearLayoutManager(EvaluationProgramActivity.this, LinearLayoutManager.VERTICAL, false));
        rvInfoGraphic.setAdapter(option2Adapter1);

        String[] arrayHealth = getResources().getStringArray(R.array.array_health);
        Option2Adapter option2Adapter2 = new Option2Adapter(EvaluationProgramActivity.this, this, arrayHealth, healthList, "3");
        rvHealth.setLayoutManager(new LinearLayoutManager(EvaluationProgramActivity.this, LinearLayoutManager.VERTICAL, false));
        rvHealth.setAdapter(option2Adapter2);
    }

    public void onNext(View view) {
        loading = ProgressDialog.show(EvaluationProgramActivity.this, null, "Please Wait", true, false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BasicPojo> call = apiInterface.addProgramEvaluationSurvey(PreferenceHandler.getUserData(EvaluationProgramActivity.this).getUserId(),type,
                programList.get(0),programList.get(1),programList.get(2),programList.get(3),programList.get(4),
                infographicList.get(0),infographicList.get(1),healthList.get(0),healthList.get(1));
        Log.d(TAG, "onDone: "+call.request().url());
        call.enqueue(new Callback<BasicPojo>() {
            @Override
            public void onResponse(Call<BasicPojo> call, Response<BasicPojo> response) {
                loading.dismiss();
                Log.d(TAG, "onResponse: "+response.code());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("success")) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EvaluationProgramActivity.this, EvaluationLiveActivity.class);
                            intent.putExtra("type",type);
                            startActivity(intent);
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

    public void addDefaultValues() {
        programList = new ArrayList<>();
        programList.add("No");
        programList.add("No");
        programList.add("No");
        programList.add("No");
        programList.add("No");

        infographicList = new ArrayList<>();
        infographicList.add("No");
        infographicList.add("No");

        healthList = new ArrayList<>();
        healthList.add("No");
        healthList.add("No");
    }

    @Override
    public void onValueChanged(List<String> stringList) {

    }

    @Override
    public void onValueChangedWithTag(String tag, List<String> stringList) {
        Log.d(TAG, "onValueChangedWithTag: stringList: "+stringList.size());
        if (tag.equals("1")) {
            programList = new ArrayList<>();
            programList.addAll(stringList);
            Log.d(TAG, "onValueChangedWithTag: programList: "+programList.size());
        } else if (tag.equals("2")) {
            infographicList = new ArrayList<>();
            infographicList.addAll(stringList);
            Log.d(TAG, "onValueChangedWithTag: infographicList: "+infographicList.size());
        } else {
            healthList = new ArrayList<>();
            healthList.addAll(stringList);
            Log.d(TAG, "onValueChangedWithTag: healthList: "+healthList.size());
        }
    }
}
