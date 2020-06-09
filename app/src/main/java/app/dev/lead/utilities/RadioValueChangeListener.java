package app.dev.lead.utilities;

import java.util.List;

public interface RadioValueChangeListener {
     void onValueChanged(List<String> stringList);
     void onValueChangedWithTag(String tag,List<String> stringList);
}
