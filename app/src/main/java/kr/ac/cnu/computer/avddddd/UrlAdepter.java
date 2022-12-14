package kr.ac.cnu.computer.avddddd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UrlAdepter extends RecyclerView.Adapter<UrlAdepter.ViewHolder> {

    private ArrayList<String> items=new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder{
        private Button button;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            button= itemView.findViewById(R.id.url);
        }
        public void setButton(String urlname){
            button.setText(urlname);
        }

    }

    @NonNull
    @NotNull
    @Override
    public UrlAdepter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textview, parent, false);
        UrlAdepter.ViewHolder viewHolder = new UrlAdepter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UrlAdepter.ViewHolder holder, int position) {
            String text = items.get(position);
            holder.setButton(text);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addButton(String urlname){
        items.add(urlname);
    }
}
