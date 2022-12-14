package kr.ac.cnu.computer.avddddd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TextAdepter extends RecyclerView.Adapter<TextAdepter.ViewHolder> {

    private ArrayList<String> localDataSet;

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.fix_title);
        }
        public TextView getTextView(){
            return textView;
        }

    }

    public TextAdepter (ArrayList<String> dataSet){
        localDataSet = dataSet;
    }

    @NonNull
    @NotNull
    @Override
    public TextAdepter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textview, parent, false);
        TextAdepter.ViewHolder viewHolder = new TextAdepter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TextAdepter.ViewHolder holder, int position) {
            String text = localDataSet.get(position);
            holder.textView.setText((position+1)+". "+text);


    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
