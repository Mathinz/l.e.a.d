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
import app.dev.lead.adapters.Option3Adapter;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluationEatActivity extends AppCompatActivity implements RadioValueChangeListener {

    View view;
    TextView toolbar;
    RecyclerView rvEat;
    List<String> eatList;
    String TAG = "theH", type = "";
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_eat);

        initializeViews();

        setDataInToolbar();

        addDefaultValues();

        setRecyclerView();

    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);

        rvEat = findViewById(R.id.rv_eat);

        type = getIntent().getStringExtra("type");
    }

    public void setDataInToolbar() {
        toolbar.setText("Eat Well with Diabetes");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void setRecyclerView() {
        String[] arrayEat = getResources().getStringArray(R.array.array_eat);
        Option3Adapter option3Adapter = new Option3Adapter(EvaluationEatActivity.this, this, arrayEat, eatList);
        rvEat.setLayoutManager(new LinearLayoutManager(EvaluationEatActivity.this, LinearLayoutManager.VERTICAL, false));
        rvEat.setAdapter(option3Adapter);
    }

    public void onNext(View view) {
        loading = ProgressDialog.show(EvaluationEatActivity.this, null, "Please Wait", true, false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BasicPojo> call = apiInterface.addEatEvaluationSurvey(PreferenceHandler.getUserData(EvaluationEatActivity.this).getUserId(), type, eatList.get(0)
                , eatList.get(1), eatList.get(2), eatList.get(3), eatList.get(4)
                , eatList.get(5), eatList.get(6), eatList.get(7), eatList.get(8)
                , eatList.get(9), eatList.get(10), eatList.get(11), eatList.get(12)
                , eatList.get(13), eatList.get(14), eatList.get(15));
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
                            Intent intent = new Intent(EvaluationEatActivity.this, EvaluationActiveActivity.class);
                            intent.putExtra("type", type);
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
        eatList = new ArrayList<>();
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
        eatList.add("No");
    }

    @Override
    public void onValueChanged(List<String> stringList) {
        eatList = new ArrayList<>();
        eatList.addAll(stringList);
        Log.d("theT", "EvaluationEatActivity: onValueChanged: ");
    }

    @Override
    public void onValueChangedWithTag(String tag, List<String> stringList) {

    }
}
