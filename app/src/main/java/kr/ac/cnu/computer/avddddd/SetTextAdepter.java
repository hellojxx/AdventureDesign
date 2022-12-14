package kr.ac.cnu.computer.avddddd;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SetTextAdepter extends RecyclerView.Adapter<SetTextAdepter.ViewHolder> {

    private ArrayList<String> localDataSet;

    class ViewHolder extends RecyclerView.ViewHolder{
        private EditText settextView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            settextView = itemView.findViewById(R.id.setTextView);
        }
        public TextView getTextView(){
            return settextView;
        }

    }

    public SetTextAdepter (ArrayList<String> dataSet){
        localDataSet = dataSet;
    }

    @NonNull
    @NotNull
    @Override
    public SetTextAdepter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settextview, parent, false);
        SetTextAdepter.ViewHolder viewHolder = new SetTextAdepter.ViewHolder(view);

        return viewHolder;
    }


    public void onBindViewHolder(@NonNull @NotNull SetTextAdepter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String text = localDataSet.get(position);
        holder.settextView.setText(text);

        holder.settextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                localDataSet.set(position, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public ArrayList<String> getinformation(){

        return localDataSet;
    }
}
