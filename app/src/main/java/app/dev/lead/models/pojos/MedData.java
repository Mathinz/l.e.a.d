package app.dev.lead.models.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedData implements Parcelable {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("medicine_id")
    @Expose
    private String medicineId;
    @SerializedName("medicine_name")
    @Expose
    private String medicineName;
    @SerializedName("medicine_reason")
    @Expose
    private String medicineReason;
    @SerializedName("often")
    @Expose
    private String often;
    @SerializedName("other")
    @Expose
    private String other;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineReason() {
        return medicineReason;
    }

    public void setMedicineReason(String medicineReason) {
        this.medicineReason = medicineReason;
    }

    public String getOften() {
        return often;
    }

    public void setOften(String often) {
        this.often = often;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    protected MedData(Parcel in) {
        userId = in.readString();
        medicineId = in.readString();
        medicineName = in.readString();
        medicineReason = in.readString();
        often = in.readString();
        other = in.readString();
    }

    public static final Creator<MedData> CREATOR = new Creator<MedData>() {
        @Override
        public MedData createFromParcel(Parcel in) {
            return new MedData(in);
        }

        @Override
        public MedData[] newArray(int size) {
            return new MedData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(medicineId);
        parcel.writeString(medicineName);
        parcel.writeString(medicineReason);
        parcel.writeString(often);
        parcel.writeString(other);
    }
}
