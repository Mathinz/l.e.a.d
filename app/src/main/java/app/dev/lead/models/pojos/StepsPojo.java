package app.dev.lead.models.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StepsPojo {
    @SerializedName("data")
    @Expose
    private List<StepsData> data = null;

    public List<StepsData> getData() {
        return data;
    }

    public void setData(List<StepsData> data) {
        this.data = data;
    }
}
