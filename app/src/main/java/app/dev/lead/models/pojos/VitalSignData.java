package app.dev.lead.models.pojos;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VitalSignData implements Parcelable {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("glucose_level")
    @Expose
    private String glucoseLevel;
    @SerializedName("blood_sugar")
    @Expose
    private String bloodPressure;
    @SerializedName("body_temperature")
    @Expose
    private String bodyTemperature;


    protected VitalSignData(Parcel in) {
        userId = in.readString();
        date = in.readString();
        glucoseLevel = in.readString();
        bloodPressure = in.readString();
        bodyTemperature = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(date);
        dest.writeString(glucoseLevel);
        dest.writeString(bloodPressure);
        dest.writeString(bodyTemperature);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VitalSignData> CREATOR = new Creator<VitalSignData>() {
        @Override
        public VitalSignData createFromParcel(Parcel in) {
            return new VitalSignData(in);
        }

        @Override
        public VitalSignData[] newArray(int size) {
            return new VitalSignData[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGlucoseLevel() {
        return glucoseLevel.isEmpty() ? "0" : glucoseLevel;
    }

    public void setGlucoseLevel(String glucoseLevel) {
        this.glucoseLevel = glucoseLevel;
    }

    public String getBloodPressure() {
        String result = bloodPressure.isEmpty() ? "0/0" : bloodPressure.contains("/")?  bloodPressure: bloodPressure+"/80";
        Log.e("theH", "getBloodPressure: bloodPressure: "+bloodPressure+" result: "+result, null);
        return result;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public static Creator<VitalSignData> getCREATOR() {
        return CREATOR;
    }

    public String getBodyTemperature() {
        return bodyTemperature.isEmpty() ? "0" : bodyTemperature;
    }

    public void setBodyTemperature(String bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }
}
