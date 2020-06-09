package app.dev.lead.activities.physicalActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.dev.lead.R;
import app.dev.lead.activities.dashboard.PhysicalActivity;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.Methods.goNextActivityWithFinish;
import static app.dev.lead.utilities.Methods.goNextActivityWithOutFinish;
import static app.dev.lead.utilities.PreferenceHandler.getUserData;

public class AddWaterActivity extends AppCompatActivity {

    View view;
    TextView toolbar;
    Button bSave;
    EditText editText;
    LinearLayout layoutGlass, layout1Bottle, layout2Bottle;
    TextView tvGlass, tv1Bottle, tv2Bottle;
    DonutProgress dpGlass, dp1Bottle, dp2Bottle;

    ProgressDialog loading;
    String TAG = "theH";
    String waterInFlOz ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_water);

        initializeViews();

        setDataInToolbar();

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().isEmpty()){
                    if (waterInFlOz.isEmpty()){
                        Toast.makeText(AddWaterActivity.this, "Please enter water.", Toast.LENGTH_SHORT).show();
                    }else {
                        sendWaterDataOnServer();
                    }
                }else{
                    waterInFlOz = editText.getText().toString().trim();
                    sendWaterDataOnServer();
                }
            }
        });

        layoutGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterInFlOz= "4";
                layoutGlass.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                layout1Bottle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                layout2Bottle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tvGlass.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tv1Bottle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                tv2Bottle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                dpGlass.setUnfinishedStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                dp1Bottle.setUnfinishedStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                dp2Bottle.setUnfinishedStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
            }
        });

        layout1Bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterInFlOz= "16";
                layoutGlass.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                layout1Bottle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                layout2Bottle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tvGlass.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                tv1Bottle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tv2Bottle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                dpGlass.setUnfinishedStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                dp1Bottle.setUnfinishedStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                dp2Bottle.setUnfinishedStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
            }
        });

        layout2Bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterInFlOz= "32";
                layoutGlass.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                layout1Bottle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                layout2Bottle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                tvGlass.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                tv1Bottle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                tv2Bottle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                dpGlass.setUnfinishedStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                dp1Bottle.setUnfinishedStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWater));
                dp2Bottle.setUnfinishedStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            }
        });
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        editText = findViewById(R.id.et_water);
        layoutGlass = findViewById(R.id.layout_glass);
        layout1Bottle = findViewById(R.id.layout_1bottle);
        layout2Bottle = findViewById(R.id.layout_2bottle);
        tvGlass = findViewById(R.id.tv_glass);
        tv1Bottle = findViewById(R.id.tv_1bottle);
        tv2Bottle = findViewById(R.id.tv_2bottle);
        dpGlass = findViewById(R.id.dp_glass);
        dp1Bottle = findViewById(R.id.dp_1bottle);
        dp2Bottle = findViewById(R.id.dp_2bottle);
        bSave = findViewById(R.id.btn_save);
    }

    public void setDataInToolbar() {
        toolbar.setText("Add Water");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }


    public void sendWaterDataOnServer() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String waterInML =  String.format("%.2f", Double.parseDouble(waterInFlOz)*29.574);    // multiply the volume value by 29.574
        String waterInL = String.format("%.2f", Double.parseDouble(waterInML) / 1000); //divide the volume value by 1000

        Log.d(TAG, "sendWaterDataOnServer: waterInFlOz: "+waterInFlOz +", waterInML: "+waterInML +", waterInL: "+waterInL);
        loading = ProgressDialog.show(AddWaterActivity.this, null, "Please Wait", true, false);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BasicPojo> call = apiService.addWaterData(getUserData(getApplicationContext()).getUserId(), sdf.format(new Date()),waterInL);
        call.enqueue(new Callback<BasicPojo>() {
            @Override
            public void onResponse(Call<BasicPojo> call, Response<BasicPojo> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("success")) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some problems occurred, please try again.", Toast.LENGTH_SHORT).show();
                }
                goNextActivityWithFinish(AddWaterActivity.this, PhysicalActivity.class);

            }

            @Override
            public void onFailure(Call<BasicPojo> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                goNextActivityWithFinish(AddWaterActivity.this, PhysicalActivity.class);

            }
        });

    }

}
