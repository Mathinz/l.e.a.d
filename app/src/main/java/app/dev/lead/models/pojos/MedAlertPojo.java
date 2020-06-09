package app.dev.lead.models.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MedAlertPojo {
    @SerializedName("data")
    @Expose
    private List<MedData> data = null;

    public List<MedData> getData() {
        return data;
    }

    public void setData(List<MedData> data) {
        this.data = data;
    }
}
