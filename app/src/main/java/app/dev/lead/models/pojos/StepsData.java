package app.dev.lead.models.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StepsData implements Parcelable  {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("total_steps")
    @Expose
    private int totalSteps;
    @SerializedName("steps_date")
    @Expose
    private String stepsDate;
    @SerializedName("steps_time")
    @Expose
    private String stepsTime;
    @SerializedName("steps_distance")
    @Expose
    private String stepsDistance;
    @SerializedName("steps_calories")
    @Expose
    private String stepsCalories;

    protected StepsData(Parcel in) {
        userId = in.readString();
        totalSteps = in.readInt();
        stepsDate = in.readString();
        stepsTime = in.readString();
        stepsDistance = in.readString();
        stepsCalories = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeInt(totalSteps);
        dest.writeString(stepsDate);
        dest.writeString(stepsTime);
        dest.writeString(stepsDistance);
        dest.writeString(stepsCalories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StepsData> CREATOR = new Creator<StepsData>() {
        @Override
        public StepsData createFromParcel(Parcel in) {
            return new StepsData(in);
        }

        @Override
        public StepsData[] newArray(int size) {
            return new StepsData[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public String getStepsDate() {
        return stepsDate;
    }

    public void setStepsDate(String stepsDate) {
        this.stepsDate = stepsDate;
    }

    public String getStepsTime() {
        return stepsTime;
    }

    public void setStepsTime(String stepsTime) {
        this.stepsTime = stepsTime;
    }

    public String getStepsDistance() {
        return stepsDistance;
    }

    public void setStepsDistance(String stepsDistance) {
        this.stepsDistance = stepsDistance;
    }

    public String getStepsCalories() {
        return stepsCalories;
    }

    public void setStepsCalories(String stepsCalories) {
        this.stepsCalories = stepsCalories;
    }
}
