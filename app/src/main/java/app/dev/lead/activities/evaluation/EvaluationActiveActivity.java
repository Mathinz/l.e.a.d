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
import app.dev.lead.activities.dashboard.EvaluationActivity;
import app.dev.lead.adapters.Option3Adapter;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluationActiveActivity extends AppCompatActivity implements RadioValueChangeListener {

    View view;
    TextView toolbar;
    RecyclerView rvActive;
    List<String> activeList;
    String TAG = "theH", type = "";
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_active);

        initializeViews();

        setDataInToolbar();

        addDefaultValues();

        setRecyclerView();

    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);

        rvActive = findViewById(R.id.rv_active);

        type = getIntent().getStringExtra("type");
    }

    public void setDataInToolbar() {
        toolbar.setText("Be Active with Diabetes");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void setRecyclerView(){
        String[] arrayActive = getResources().getStringArray(R.array.array_active);
        Option3Adapter option3Adapter = new Option3Adapter(EvaluationActiveActivity.this,this,arrayActive,activeList);
        rvActive.setLayoutManager(new LinearLayoutManager(EvaluationActiveActivity.this,LinearLayoutManager.VERTICAL,false));
        rvActive.setAdapter(option3Adapter);
    }

    public void onNext(View view) {
        loading = ProgressDialog.show(EvaluationActiveActivity.this, null, "Please Wait", true, false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BasicPojo> call = apiInterface.addActiveEvaluationSurvey(PreferenceHandler.getUserData(EvaluationActiveActivity.this).getUserId(), type, activeList.get(0)
                , activeList.get(1), activeList.get(2), activeList.get(3), activeList.get(4)
                , activeList.get(5), activeList.get(6), activeList.get(7), activeList.get(8)
                , activeList.get(9), activeList.get(10));
        Log.d(TAG, "onDone: " + call.request().url());
        call.enqueue(new Callback<BasicPojo>() {
            @Override
            public void onResponse(Call<BasicPojo> call, Response<BasicPojo> response) {
                loading.dismiss();
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("success")) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EvaluationActiveActivity.this, EvaluationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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

    public void addDefaultValues(){
        activeList = new ArrayList<>();
        activeList.add("No");
        activeList.add("No");
        activeList.add("No");
        activeList.add("No");
        activeList.add("No");
        activeList.add("No");
        activeList.add("No");
        activeList.add("No");
        activeList.add("No");
        activeList.add("No");
        activeList.add("No");
    }

    @Override
    public void onValueChanged(List<String> stringList) {
        activeList = new ArrayList<>();
        activeList.addAll(stringList);
        Log.d("theT", "EvaluationActiveActivity onValueChanged: ");
    }

    @Override
    public void onValueChangedWithTag(String tag, List<String> stringList) {

    }
}
