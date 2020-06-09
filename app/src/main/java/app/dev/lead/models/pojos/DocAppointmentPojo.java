package app.dev.lead.models.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocAppointmentPojo {
    @SerializedName("data")
    @Expose
    private List<DocData> data = null;

    public List<DocData> getData() {
        return data;
    }

    public void setData(List<DocData> data) {
        this.data = data;
    }
}
