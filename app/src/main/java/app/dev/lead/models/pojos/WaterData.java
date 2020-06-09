package app.dev.lead.models.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaterData implements Parcelable {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("water_date")
    @Expose
    private String waterDate;
    @SerializedName("total_water")
    @Expose
    private String totalWater;

    protected WaterData(Parcel in) {
        userId = in.readString();
        waterDate = in.readString();
        totalWater = in.readString();
    }

    public static final Creator<WaterData> CREATOR = new Creator<WaterData>() {
        @Override
        public WaterData createFromParcel(Parcel in) {
            return new WaterData(in);
        }

        @Override
        public WaterData[] newArray(int size) {
            return new WaterData[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWaterDate() {
        return waterDate;
    }

    public void setWaterDate(String waterDate) {
        this.waterDate = waterDate;
    }

    public String getTotalWater() {
        return totalWater;
    }

    public void setTotalWater(String totalWater) {
        this.totalWater = totalWater;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(waterDate);
        parcel.writeString(totalWater);
    }
}
