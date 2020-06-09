package app.dev.lead.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.dev.lead.R;
import app.dev.lead.activities.medication.MedicationAlertActivity;
import app.dev.lead.adapters.DocAppointmentAdapter;
import app.dev.lead.models.pojos.DocAppointmentPojo;
import app.dev.lead.utilities.ApiClient;
import app.dev.lead.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.dev.lead.utilities.PreferenceHandler.getUserData;

public class DoctorAppointmentFragment extends Fragment {
    RecyclerView recyclerView;
    DocAppointmentAdapter adapter;
    ProgressDialog loading;
    TextView tvAlertMsg;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctor_appointment,container,false);

        initializeViews();

        setDataInRecycler();

        return view;
    }

    private void setDataInRecycler() {
        loading = ProgressDialog.show(getContext(), null, "Please Wait", true, false);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DocAppointmentPojo> call = apiService.getDoctorAppointment(getUserData(getContext()).getUserId());
        call.enqueue(new Callback<DocAppointmentPojo>() {
            @Override
            public void onResponse(Call<DocAppointmentPojo> call, Response<DocAppointmentPojo> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getData().isEmpty()) {
                            tvAlertMsg.setVisibility(View.VISIBLE);
                        } else {
                            tvAlertMsg.setVisibility(View.GONE);
                            adapter = new DocAppointmentAdapter(getContext(),response.body().getData());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
                            recyclerView.setAdapter(adapter);                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Some problems occurred, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DocAppointmentPojo> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initializeViews() {
        recyclerView = view.findViewById(R.id.appointment_recycler_view);
        tvAlertMsg = view.findViewById(R.id.tv_alert_message);

    }
}
