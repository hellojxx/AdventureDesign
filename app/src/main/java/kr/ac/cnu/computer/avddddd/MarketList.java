package kr.ac.cnu.computer.avddddd;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.libraries.places.api.model.Place;
import com.google.gson.Gson;

public class MarketList extends AppCompatActivity {
    RecyclerView recyclerView;
    String response;
    ShopAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marketlist);
        recyclerView=findViewById(R.id.list_item);
        Intent intent=getIntent();
        response=intent.getStringExtra("json");
        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ShopAdapter();
        recyclerView.setAdapter(adapter);
        if(response!=null){
            processResponse(response);
        }
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ShopAdapter.ViewHolder viewHolder, View v, int position) {
                Shop item=adapter.getItem(position);
                String data=item.name;
                Intent intent1=new Intent(MarketList.this, Shopinfo.class);
                intent1.putExtra("name", data);
                startActivity(intent1);
            }
        });
        recyclerView.addItemDecoration(new RecyclerViewDecoration(30, 20));
        ImageButton imgbutton = findViewById(R.id.back);
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //화면전환
                finish();
            }
        });
    }

    public void processResponse(String response) {
        Gson gson=new Gson();
        PlaceList placeList=gson.fromJson(response, PlaceList.class);
        for(int i=0;i<placeList.results.size();i++){
            Shop shop=placeList.results.get(i);
            adapter.addItem(shop);
        }
        adapter.notifyDataSetChanged();
    }
}
