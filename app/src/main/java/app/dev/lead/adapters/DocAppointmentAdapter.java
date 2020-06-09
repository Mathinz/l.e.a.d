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
import app.dev.lead.activities.startup.DashBoardActivity;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.models.pojos.DocData;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocAppointmentAdapter extends RecyclerView.Adapter<DocAppointmentAdapter.AppointmentHolder> {
    Context context;
    List<DocData> dataList;
    ProgressDialog loading;
    public DocAppointmentAdapter(Context context, List<DocData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
        return new AppointmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentHolder h,final int position) {
        DocData data = dataList.get(position);
        h.tvTime.setText(data.getDate() +", "+data.getTime());
        h.tvName.setText(data.getDoctorName());
        h.tvLocation.setText(data.getAppointmentLocation());
        h.tvReason.setText(data.getAppointmentReason());
        h.viewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorAppointmentActivity.class);
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

    public class AppointmentHolder extends RecyclerView.ViewHolder {
        TextView tvTime,tvName,tvLocation,tvReason;
        ImageView viewEdit,viewDelete;
        public AppointmentHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.item_time);
            tvName = itemView.findViewById(R.id.item_doctor_name);
            tvLocation = itemView.findViewById(R.id.item_location);
            tvReason = itemView.findViewById(R.id.item_reason);
            viewEdit = itemView.findViewById(R.id.item_edit);
            viewDelete = itemView.findViewById(R.id.item_delete);
        }
    }

    private void delete(final int position) {
        loading = ProgressDialog.show(context, null, "Please Wait", true, false);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BasicPojo> call = apiService.deleteDoctorAppointment(dataList.get(position).getAppointmentId());
        call.enqueue(new Callback<BasicPojo>() {
            @Override
            public void onResponse(Call<BasicPojo> call, Response<BasicPojo> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("success")) {
                            removeAt(position);
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
