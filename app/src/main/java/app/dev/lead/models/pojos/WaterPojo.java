package app.dev.lead.models.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WaterPojo {

    @SerializedName("data")
    @Expose
    private List<WaterData> data = null;

    public List<WaterData> getData() {
        return data;
    }

    public void setData(List<WaterData> data) {
        this.data = data;
    }
}
