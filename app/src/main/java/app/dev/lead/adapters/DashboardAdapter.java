package app.dev.lead.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import app.dev.lead.activities.dashboard.EvaluationActivity;
import app.dev.lead.activities.dashboard.HealthyFoods;
import app.dev.lead.activities.dashboard.HelpActivity;
import app.dev.lead.activities.dashboard.Medication;
import app.dev.lead.activities.dashboard.PhysicalActivity;
import app.dev.lead.R;
import app.dev.lead.activities.vitalSign.VitalSignActivity;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardHolder> {
    Context context;
    String[] titles = {"Medications", "Vital Signs", "Physical Activity", "Healthy Foods", "Evaluation", "Help"};
    Integer[] images = {R.drawable.me_time, R.drawable.family_time, R.drawable.lovely_time, R.drawable.team_time, R.drawable.friends, R.drawable.calendar};

    public DashboardAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DashboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        return new DashboardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardHolder holder, final int position) {
        holder.textView.setText(titles[position]);
        Glide.with(context)
                .load(images[position])
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextActivity(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class DashboardHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public DashboardHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_title);
            imageView = itemView.findViewById(R.id.item_img);
        }
    }

    public void goToNextActivity(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(context, Medication.class);
                break;
            case 1:
                intent = new Intent(context, VitalSignActivity.class);
                break;
            case 2:
                intent = new Intent(context, PhysicalActivity.class);
                break;
            case 3:
                intent = new Intent(context, HealthyFoods.class);
                break;
            case 4:
                intent = new Intent(context, EvaluationActivity.class);
                break;
            case 5:
                intent = new Intent(context, HelpActivity.class);
                break;
        }
        context.startActivity(intent);
    }
}
