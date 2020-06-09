package app.dev.lead.activities.startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import app.dev.lead.R;
import app.dev.lead.utilities.PreferenceHandler;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (PreferenceHandler.isLogin(MainActivity.this)) {
            Intent intent = new Intent(MainActivity.this, DashBoardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onSignInClick(View view) {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
//        finish();
    }

    public void onSignUpClick(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
//        finish();
    }
}
