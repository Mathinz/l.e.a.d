package app.dev.lead.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.dev.lead.R;
import app.dev.lead.utilities.RadioValueChangeListener;

public class Option2Adapter extends RecyclerView.Adapter<Option2Adapter.Holder> {

    Context context;
    String[] questionList;
    RadioValueChangeListener listener;
    List<String> stringList;
    String tag;

    public Option2Adapter(Context context, RadioValueChangeListener listener, String[] questionList, List<String> stringList, String tag) {
        this.context = context;
        this.listener = listener;
        this.questionList = questionList;
        this.stringList = stringList;
        this.tag = tag;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_option2, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        if (stringList.get(position).equals("Yes")) {
            holder.rbYes.setChecked(true);
            holder.rbNo.setChecked(false);
        } else if (stringList.get(position).equals("No")) {
            holder.rbYes.setChecked(false);
            holder.rbNo.setChecked(true);

        }

        holder.radioGroup.setOnCheckedChangeListener(null);

        holder.tvQuestion.setText(questionList[position]);

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_yes:
                        stringList.set(position,"Yes");
                        Log.d("theT", "onCheckedChanged: position: "+position+" value: Yes checkedId: "+checkedId);
                        break;
                    case R.id.rb_no:
                        stringList.set(position,"No");
                        Log.d("theT", "onCheckedChanged: position: "+position+" value: No checkedId: "+checkedId);
                        break;

                }
                listener.onValueChangedWithTag(tag,stringList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.length;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvQuestion;
        RadioGroup radioGroup;
        RadioButton rbYes, rbNo;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            radioGroup = itemView.findViewById(R.id.rg_answer);
            rbYes = itemView.findViewById(R.id.rb_yes);
            rbNo = itemView.findViewById(R.id.rb_no);
        }
    }
}
