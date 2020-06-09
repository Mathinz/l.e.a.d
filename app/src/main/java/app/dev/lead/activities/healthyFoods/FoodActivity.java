package app.dev.lead.activities.healthyFoods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import app.dev.lead.R;
import app.dev.lead.adapters.FoodAdapter;

public class FoodActivity extends AppCompatActivity {

    View view;
    TextView toolbar;
    RecyclerView recyclerView;
    int position = 1;
    FoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        initializeViews();

        setDataInToolbar();

        setRecyclerView();

    }

    public void initializeViews() {
        view = findViewById(R.id.layout);
        toolbar = view.findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.recycler_view);

        position = getIntent().getIntExtra("position", 1);
    }

    public void setDataInToolbar() {
        toolbar.setText(getTitle(position));
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void setRecyclerView() {
        adapter = new FoodAdapter(FoodActivity.this, Arrays.asList(getArray(position)));
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    public String[] getArray(int position) {
        String[] array = getResources().getStringArray(R.array.array_non_starchy);
        switch (position) {
            case 1:
                array = getResources().getStringArray(R.array.array_non_starchy);
                break;
            case 2:
                array = getResources().getStringArray(R.array.array_starchy);
                break;
            case 3:
                array = getResources().getStringArray(R.array.array_protein);
                break;
            case 4:
                array = getResources().getStringArray(R.array.array_fruit);
                break;
            case 5:
                array = getResources().getStringArray(R.array.array_dairy);
                break;
        }
        return array;
    }

    public String getTitle(int position) {
        String title = getResources().getString(R.string.non_starchy_vegetables);
        switch (position) {
            case 1:
                title = getResources().getString(R.string.non_starchy_vegetables);
                break;
            case 2:
                title = getResources().getString(R.string.starches);
                break;
            case 3:
                title = getResources().getString(R.string.protein);
                break;
            case 4:
                title = getResources().getString(R.string.fruit);
                break;
            case 5:
                title = getResources().getString(R.string.dairy);
                break;
        }
        return title;
    }
}
