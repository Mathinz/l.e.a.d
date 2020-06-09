package app.dev.lead.activities.startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.dev.lead.R;
import app.dev.lead.models.pojos.LoginPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import app.dev.lead.utilities.PreferenceHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.Methods.isValidEmail;
import static app.dev.lead.utilities.Methods.showToast;
import static app.dev.lead.utilities.PreferenceHandler.saveUserData;

public class SignInActivity extends AppCompatActivity {
    View view;
    TextView toolbar;
    EditText etEmail, etPassword;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initializeViews();

        setDataInToolbar();
    }

    public void initializeViews() {
        view = findViewById(R.id.sign_in_layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        etEmail = findViewById(R.id.sign_in_email);
        etPassword = findViewById(R.id.sign_in_password);

//        setFakeData();
    }

    public void setDataInToolbar() {
        toolbar.setText("Sign In");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void doLogin(View view) {
        String password = etPassword.getText().toString(),
                email = etEmail.getText().toString();
        if (!isValidEmail(email)) {
            showToast(SignInActivity.this, "Enter valid email.");
        } else if (password.isEmpty()) {
            showToast(SignInActivity.this, "Enter your password.");
        }else {
            loading = ProgressDialog.show(SignInActivity.this, null, "Please Wait", true, false);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<LoginPojo> call = apiService.makeLogin(email, password);
            call.enqueue(new Callback<LoginPojo>() {
                @Override
                public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                    loading.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("success")) {
                                saveUserData(getApplicationContext(),response.body());
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                PreferenceHandler.saveLogin(SignInActivity.this,true);
                                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
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
                public void onFailure(Call<LoginPojo> call, Throwable t) {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    public void setFakeData(){
        etPassword.setText("123456");
        etEmail.setText("test@lead.com");
    }
}
