package app.dev.lead.activities.help;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.dev.lead.R;

public class DisplayActivity extends AppCompatActivity {

    View view;
    TextView toolbar;
    TextView textView;
    int position = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        initializeViews();

        setDataInToolbar();

        textView.setText(getDetails(position));

    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        textView = findViewById(R.id.tv_text);
        toolbar = view.findViewById(R.id.toolbar_title);

        position = getIntent().getIntExtra("position", 1);
    }

    public void setDataInToolbar() {
        toolbar.setText(getTitle(position));
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public String getDetails(int position) {
        String title = getResources().getString(R.string.about_details);
        switch (position) {
            case 1:
                title = getResources().getString(R.string.about_details);
                break;
            case 2:
                title = getResources().getString(R.string.lead_details);
                break;
            case 3:
                title = getResources().getString(R.string.contact_details);
                break;
        }
        return title;
    }

    public String getTitle(int position) {
        String title = getResources().getString(R.string.about_us);
        switch (position) {
            case 1:
                title = getResources().getString(R.string.about_us);
                break;
            case 2:
                title = getResources().getString(R.string.what_is_lead);
                break;
            case 3:
                title = getResources().getString(R.string.contact_us);
                break;
        }
        return title;
    }
}
