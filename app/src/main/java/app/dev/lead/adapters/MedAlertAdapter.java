package app.dev.lead.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.dev.lead.R;
import app.dev.lead.activities.medication.DoctorAppointmentActivity;
import app.dev.lead.activities.medication.MedicationAlertActivity;
import app.dev.lead.activities.startup.DashBoardActivity;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.models.pojos.LoginPojo;
import app.dev.lead.models.pojos.MedData;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.PreferenceHandler.saveUserData;

public class MedAlertAdapter extends RecyclerView.Adapter<MedAlertAdapter.AlertHolder> {
    Context context;
    List<MedData> dataList;
    ProgressDialog loading;
    public MedAlertAdapter(Context context, List<MedData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MedAlertAdapter.AlertHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new AlertHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedAlertAdapter.AlertHolder h, final int position) {
        final MedData data = dataList.get(position);
        h.tvName.setText(data.getMedicineName());
        h.tvOften.setText(data.getOften());
        h.tvReason.setText(data.getMedicineReason());
        h.viewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MedicationAlertActivity.class);
                intent.putExtra("data",dataList.get(position));
                (context).startActivity(intent);
                ((Activity)context).finish();
            }
        });
        h.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class AlertHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvOften,tvReason;
        ImageView viewEdit,viewDelete;
        public AlertHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.item_medicine_name);
            tvOften = itemView.findViewById(R.id.item_often);
            tvReason = itemView.findViewById(R.id.item_reason);
            viewEdit = itemView.findViewById(R.id.item_edit);
            viewDelete = itemView.findViewById(R.id.item_delete);
        }
    }

    public void delete(final int position){
        loading = ProgressDialog.show(context, null, "Please Wait", true, false);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BasicPojo> call = apiService.deleteMedicationAlert(dataList.get(position).getMedicineId());
        call.enqueue(new Callback<BasicPojo>() {
            @Override
            public void onResponse(Call<BasicPojo> call, Response<BasicPojo> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("success")) {
                            removeAt(position);
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(context, "Some problems occurred, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicPojo> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void removeAt(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }
}
