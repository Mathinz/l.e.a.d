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

public class Option3Adapter extends RecyclerView.Adapter<Option3Adapter.Holder> {

    Context context;
    String[] questionList;
    RadioValueChangeListener listener;
    List<String> stringList;

    public Option3Adapter(Context context,RadioValueChangeListener listener, String[] questionList, List<String> stringList) {
        this.context = context;
        this.listener = listener;
        this.questionList = questionList;
        this.stringList = stringList;
            }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_option3, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {

        holder.radioGroup.setOnCheckedChangeListener(null);

        if (stringList.get(position).equals("Yes")) {
            holder.rbYes.setChecked(true);
            holder.rbNo.setChecked(false);
            holder.rbSometimes.setChecked(false);
        } else if (stringList.get(position).equals("No")) {
            holder.rbYes.setChecked(false);
            holder.rbNo.setChecked(true);
            holder.rbSometimes.setChecked(false);
        } else {
            holder.rbYes.setChecked(false);
            holder.rbNo.setChecked(false);
            holder.rbSometimes.setChecked(true);
        }

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
                    case R.id.rb_sometimes:
                        stringList.set(position,"Sometimes");
                        Log.d("theT", "onCheckedChanged: position: "+position+" value: Sometimes checkedId: "+checkedId);
                        break;
                }
                listener.onValueChanged(stringList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.length;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvQuestion;
        RadioGroup radioGroup;
        RadioButton rbYes,rbNo,rbSometimes;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            radioGroup = itemView.findViewById(R.id.rg_answer);
            rbYes = itemView.findViewById(R.id.rb_yes);
            rbNo = itemView.findViewById(R.id.rb_no);
            rbSometimes = itemView.findViewById(R.id.rb_sometimes);
        }
    }
}
