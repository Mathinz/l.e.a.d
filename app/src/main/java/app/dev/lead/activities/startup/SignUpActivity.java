package app.dev.lead.activities.startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.dev.lead.R;
import app.dev.lead.activities.help.TermsActivity;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.Methods.isValidEmail;
import static app.dev.lead.utilities.Methods.showToast;

public class SignUpActivity extends AppCompatActivity {

    View view;
    CheckBox cbTAC;
    TextView tvTAC;
    TextView toolbar;
    EditText etFName, etLName, etPassword, etEmail, etNumber, etAddress;
    RadioGroup radioGroup;
    ProgressDialog loading;
    Spinner spinAge, spinEthnicity;
    String[] ageList, raceList;
    String age = "18-24", race = "African American", gender = "Male";
    String TAG = "theH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();

        setDataInToolbar();

        spinnerWork();

        radioGroupWork();

        tvTAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, TermsActivity.class);
                startActivity(intent);
            }
        });
    }


    public void initializeViews() {
        view = findViewById(R.id.sign_up_layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        etFName = findViewById(R.id.sign_up_fname);
        etLName = findViewById(R.id.sign_up_lname);
        etPassword = findViewById(R.id.sign_up_password);
        etEmail = findViewById(R.id.sign_up_email);
        etNumber = findViewById(R.id.sign_up_number);
        etAddress = findViewById(R.id.sign_up_address);
        spinAge = findViewById(R.id.age_spinner);
        spinEthnicity = findViewById(R.id.ethnicity_spinner);
        radioGroup = findViewById(R.id.sign_up_rg);
        cbTAC = findViewById(R.id.cb_terms_and_conditions);
        tvTAC = findViewById(R.id.tv_terms_and_conditions);
//        setFakeData();
    }

    public void setDataInToolbar() {
        toolbar.setText("Sign Up");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void spinnerWork() {
        ageList = getResources().getStringArray(R.array.age_array);
        ArrayAdapter adapterA = new ArrayAdapter<String>(this, R.layout.item_spinner1, ageList);
        adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAge.setAdapter(adapterA);

        spinAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                age = ageList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        raceList = getResources().getStringArray(R.array.race_array);
        ArrayAdapter adapterR = new ArrayAdapter<String>(this, R.layout.item_spinner1, raceList);
        adapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinEthnicity.setAdapter(adapterR);

        spinEthnicity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                race = raceList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void radioGroupWork() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rg_male) {
                    gender = "Male";
                } else if (checkedId == R.id.rg_female) {
                    gender = "Female";
                }
            }
        });
    }

    public void createAccount(View view) {
        final String fName = etFName.getText().toString(),
                lName = etLName.getText().toString(),
                password = etPassword.getText().toString(),
                email = etEmail.getText().toString(),
                number = etNumber.getText().toString(),
                address = etAddress.getText().toString();
        if (fName.isEmpty()) {
            showToast(SignUpActivity.this, "Enter your first name.");
        } else if (lName.isEmpty()) {
            showToast(SignUpActivity.this, "Enter your last name.");
        } else if (password.isEmpty()) {
            showToast(SignUpActivity.this, "Enter your password.");
        } else if (!isValidEmail(email)) {
            showToast(SignUpActivity.this, "Enter valid email.");
        } else if (number.isEmpty()) {
            showToast(SignUpActivity.this, "Enter your contact number.");
        } else if (address.isEmpty()) {
            showToast(SignUpActivity.this, "Enter your address.");
        }else if (!cbTAC.isChecked()) {
            showToast(SignUpActivity.this, "Please check Terms and Conditions.");
        } else {
            loading = ProgressDialog.show(SignUpActivity.this, null, "Please Wait", true, false);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<BasicPojo> call = apiService.makeAccount(fName, lName, email, password, number, age, race, gender, address);
            Log.d(TAG, "onResponse: " + call.request().url());
            call.enqueue(new Callback<BasicPojo>() {
                @Override
                public void onResponse(Call<BasicPojo> call, Response<BasicPojo> response) {
                    Log.d(TAG, "onResponse: " + response.code());
                    loading.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("success")) {
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.code() + ": Some problems occurred, please try again.", Toast.LENGTH_SHORT).show();
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


    public void setFakeData() {
        etFName.setText("Developer");
        etLName.setText("Testing");
        etPassword.setText("123456");
        etEmail.setText("test@lead.com");
        etNumber.setText("0123456789");
        etAddress.setText("Abc");
    }

}
