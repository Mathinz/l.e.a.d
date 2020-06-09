package app.dev.lead.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import app.dev.lead.fragments.DoctorAppointmentFragment;
import app.dev.lead.fragments.MedicationAlertFragment;

public class MedicationTrackerAdapter extends FragmentPagerAdapter {
    private Context context;

    private Fragment medicationAlertFragment;
    private Fragment doctorAppointmentFragment;

    public MedicationTrackerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;
        medicationAlertFragment = new MedicationAlertFragment();
        doctorAppointmentFragment = new DoctorAppointmentFragment();
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return medicationAlertFragment;
        } else {
            return doctorAppointmentFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Medication Alert";
        }else {
            return "Doctor Appointment";
        }
    }
}
