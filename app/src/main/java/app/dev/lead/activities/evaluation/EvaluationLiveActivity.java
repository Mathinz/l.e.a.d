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

public class EvaluationLiveActivity extends AppCompatActivity implements RadioValueChangeListener {

    View view;
    TextView toolbar;
    RecyclerView rvLive;
    List<String> liveList;
    String TAG="theH",type = "";
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_live);

        initializeViews();

        setDataInToolbar();

        addDefaultValues();

        setRecyclerView();

    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);

        rvLive = findViewById(R.id.rv_live);

        type = getIntent().getStringExtra("type");
    }

    public void setDataInToolbar() {
        toolbar.setText("Live Well with Diabetes");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void setRecyclerView(){
        String[] arrayLive = getResources().getStringArray(R.array.array_live);
        Option3Adapter option3Adapter = new Option3Adapter(EvaluationLiveActivity.this,this,arrayLive,liveList);
        rvLive.setLayoutManager(new LinearLayoutManager(EvaluationLiveActivity.this,LinearLayoutManager.VERTICAL,false));
        rvLive.setAdapter(option3Adapter);
    }

    public void onNext(View view) {
        loading = ProgressDialog.show(EvaluationLiveActivity.this, null, "Please Wait", true, false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BasicPojo> call = apiInterface.addLiveEvaluationSurvey(PreferenceHandler.getUserData(EvaluationLiveActivity.this).getUserId(),type,liveList.get(0)
                ,liveList.get(1),liveList.get(2),liveList.get(3),liveList.get(4)
                ,liveList.get(5),liveList.get(6),liveList.get(7),liveList.get(8)
                ,liveList.get(9),liveList.get(10),liveList.get(11));
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
                            Intent intent = new Intent(EvaluationLiveActivity.this,EvaluationEatActivity.class);
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

    public void addDefaultValues(){
        liveList = new ArrayList<>();
        liveList.add("No");
        liveList.add("No");
        liveList.add("No");
        liveList.add("No");
        liveList.add("No");
        liveList.add("No");
        liveList.add("No");
        liveList.add("No");
        liveList.add("No");
        liveList.add("No");
        liveList.add("No");
        liveList.add("No");
    }

    @Override
    public void onValueChanged(List<String> stringList) {
        liveList = new ArrayList<>();
        liveList.addAll(stringList);
        Log.d("theT", "EvaluationLiveActivity: onValueChanged: ");
    }

    @Override
    public void onValueChangedWithTag(String tag, List<String> stringList) {

    }
}
