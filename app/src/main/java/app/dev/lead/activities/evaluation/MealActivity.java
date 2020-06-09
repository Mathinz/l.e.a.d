package app.dev.lead.activities.evaluation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import app.dev.lead.R;
import app.dev.lead.activities.medication.MedicationAlertActivity;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import app.dev.lead.utilities.PreferenceHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.PreferenceHandler.getUserData;

public class MealActivity extends AppCompatActivity {

    View view;
    TextView toolbar;
    String TAG = "theM", day = "", type = "";
    Spinner spinMealTime, spinBreakfast, spinLunch, spinDinner, spinBreakfastMuch, spinLunchMuch, spinDinnerMuch,
            spinBreakfastSnackMuch, spinLunchSnackMuch, spinDinnerSnackMuch,spinOtherMuch;
    EditText etBreakfastSnack,etOther;
    TextView tvMeal;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        initializeViews();

        setDataInToolbar();

        setSpinners();
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        tvMeal = findViewById(R.id.tv_meal);

        etBreakfastSnack = findViewById(R.id.et_breakfast_snack);
        etOther = findViewById(R.id.et_other);

        spinBreakfast = findViewById(R.id.spin_breakfast);
//        spinLunch = findViewById(R.id.spin_lunch);
//        spinDinner = findViewById(R.id.spin_dinner);
        spinBreakfastMuch = findViewById(R.id.spin_breakfast_much);
//        spinLunchMuch = findViewById(R.id.spin_lunch_much);
//        spinDinnerMuch = findViewById(R.id.spin_dinner_much);
        spinBreakfastSnackMuch = findViewById(R.id.spin_breakfast_snack_much);
//        spinLunchSnackMuch = findViewById(R.id.spin_lunch_snack_much);
//        spinDinnerSnackMuch = findViewById(R.id.spin_dinner_snack_much);
        spinMealTime = findViewById(R.id.spin_meal);
        spinOtherMuch = findViewById(R.id.spin_other_much);

        day = getIntent().getStringExtra("day");
        type = getIntent().getStringExtra("type");
    }

    public void setDataInToolbar() {
        toolbar.setText(day + "Meal");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void setSpinners() {

        final String[] array = getResources().getStringArray(R.array.array_meal);
        ArrayAdapter adapter = new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinMealTime.setAdapter(adapter);

        final String[] array1 = getResources().getStringArray(R.array.array_food);
        ArrayAdapter adapter1 = new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array1);
        adapter1.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinBreakfast.setAdapter(adapter1);

        final String[] array2 = getResources().getStringArray(R.array.array_much);
        ArrayAdapter adapter2 = new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array2);
        adapter2.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinBreakfastMuch.setAdapter(adapter2);

        final String[] array3 = getResources().getStringArray(R.array.array_much);
        ArrayAdapter adapter3 = new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array3);
        adapter3.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinBreakfastSnackMuch.setAdapter(adapter3);

        final String[] array4 = getResources().getStringArray(R.array.array_much);
        ArrayAdapter adapter4 = new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array4);
        adapter4.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinOtherMuch.setAdapter(adapter4);

//        //////////////////////////////////////////////////////////////////

      /*  final String[] array4 = getResources().getStringArray(R.array.array_food);
        ArrayAdapter adapter4 = new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array4);
        adapter4.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinLunch.setAdapter(adapter4);

        final String[] array5 = getResources().getStringArray(R.array.array_much);
        ArrayAdapter adapter5= new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array5);
        adapter5.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinLunchMuch.setAdapter(adapter5);

        final String[] array6 = getResources().getStringArray(R.array.array_much);
        ArrayAdapter adapter6 = new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array6);
        adapter6.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinLunchSnackMuch.setAdapter(adapter6);
*/

//        //////////////////////////////////////////////////////////////////////////

       /* final String[] array7 = getResources().getStringArray(R.array.array_food);
        ArrayAdapter adapter7 = new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array7);
        adapter7.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinDinner.setAdapter(adapter7);

        final String[] array8 = getResources().getStringArray(R.array.array_much);
        ArrayAdapter adapter8 = new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array8);
        adapter8.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinDinnerMuch.setAdapter(adapter8);

        final String[] array9 = getResources().getStringArray(R.array.array_much);
        ArrayAdapter adapter9 = new ArrayAdapter(MealActivity.this, R.layout.item_spinner, array9);
        adapter9.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinDinnerSnackMuch.setAdapter(adapter9);*/

        spinMealTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvMeal.setText(spinMealTime.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onDone(View view) {
        loading = ProgressDialog.show(MealActivity.this, null, "Please Wait", true, false);

        Log.d(TAG, "onDone: type: "+type +" day: "+day+" spinMealTime: "+ spinMealTime.getSelectedItem().toString()
                + " spin "+spinBreakfast.getSelectedItem().toString()+" spinMuch: "+spinBreakfastMuch.getSelectedItem().toString()
                + " etOther "+etOther.getText().toString()+" spinOtherMuch: "+spinOtherMuch.getSelectedItem().toString()
                + " etBreakfastSnack "+etBreakfastSnack.getText().toString()+" spinBreakfastSnackMuch: "+spinBreakfastSnackMuch.getSelectedItem().toString()
        );
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BasicPojo> call = apiInterface.addMealSurvey(PreferenceHandler.getUserData(MealActivity.this).getUserId(), type, day,
                spinMealTime.getSelectedItem().toString(),
                spinBreakfast.getSelectedItem().toString(), spinBreakfastMuch.getSelectedItem().toString(),
                etOther.getText().toString(), spinOtherMuch.getSelectedItem().toString(),
                etBreakfastSnack.getText().toString(), spinBreakfastSnackMuch.getSelectedItem().toString());
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
}
