package app.dev.lead.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.dev.lead.R;
import app.dev.lead.activities.healthyFoods.FoodActivity;
import app.dev.lead.activities.help.DisplayActivity;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    View view;
    TextView toolbar;
    Button btnAbout,btnWhat,btnContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initializeViews();

        setDataInToolbar();
    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        btnAbout = findViewById(R.id.btn_about);
        btnWhat = findViewById(R.id.btn_what);
        btnContact = findViewById(R.id.btn_contact);

        btnAbout.setOnClickListener(this);
        btnWhat.setOnClickListener(this);
        btnContact.setOnClickListener(this);
    }

    public void setDataInToolbar() {
        toolbar.setText("Help");
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_about:
                sendData(1);
                break;
            case R.id.btn_what:
                sendData(2);
                break;
            case R.id.btn_contact:
                sendData(3);
                break;
        }
    }

    public void sendData(int position){
        Intent intent = new Intent(HelpActivity.this, DisplayActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);

    }
}
