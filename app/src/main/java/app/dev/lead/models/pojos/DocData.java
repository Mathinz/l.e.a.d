package app.dev.lead.models.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocData implements Parcelable {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("appointment_id")
    @Expose
    private String appointmentId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("doctor_name")
    @Expose
    private String doctorName;
    @SerializedName("appointment_location")
    @Expose
    private String appointmentLocation;
    @SerializedName("appointment_reason")
    @Expose
    private String appointmentReason;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public String getAppointmentReason() {
        return appointmentReason;
    }

    public void setAppointmentReason(String appointmentReason) {
        this.appointmentReason = appointmentReason;
    }


    protected DocData(Parcel in) {
        userId = in.readString();
        appointmentId = in.readString();
        date = in.readString();
        time = in.readString();
        doctorName = in.readString();
        appointmentLocation = in.readString();
        appointmentReason = in.readString();
    }

    public static final Creator<DocData> CREATOR = new Creator<DocData>() {
        @Override
        public DocData createFromParcel(Parcel in) {
            return new DocData(in);
        }

        @Override
        public DocData[] newArray(int size) {
            return new DocData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(appointmentId);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(doctorName);
        parcel.writeString(appointmentLocation);
        parcel.writeString(appointmentReason);
    }
}
