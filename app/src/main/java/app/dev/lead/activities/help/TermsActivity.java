package app.dev.lead.activities.help;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.dev.lead.R;

public class TermsActivity extends AppCompatActivity {

    View view;
    TextView toolbar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        initializeViews();

        setDataInToolbar();

        textView.setText(getResources().getString(R.string.terms_and_conditions_details));

    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        textView = findViewById(R.id.tv_text);
        toolbar = view.findViewById(R.id.toolbar_title);
    }

    public void setDataInToolbar() {
        toolbar.setText(getResources().getString(R.string.terms_and_conditions));
    }

    public void onBackClick(View view) {
        onBackPressed();
    }
}
