package app.dev.lead.activities.startup;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.dev.lead.R;
import app.dev.lead.adapters.DashboardAdapter;
import app.dev.lead.utilities.PreferenceHandler;

public class DashBoardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DashboardAdapter adapter;
    ImageView exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.recycler_view);
        exist = findViewById(R.id.img_exit);

        adapter = new DashboardAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceHandler.saveLogin(DashBoardActivity.this, false);
                Intent intent = new Intent(DashBoardActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
