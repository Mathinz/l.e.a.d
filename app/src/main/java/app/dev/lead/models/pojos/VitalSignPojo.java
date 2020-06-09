package app.dev.lead.models.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VitalSignPojo {
    @SerializedName("data")
    @Expose
    private List<VitalSignData> data = null;

    public List<VitalSignData> getData() {
        return data;
    }

    public void setData(List<VitalSignData> data) {
        this.data = data;
    }
}
