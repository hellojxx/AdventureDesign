package kr.ac.cnu.computer.avddddd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Img extends AppCompatActivity {
    String name;
    byte[] bytes;
    TextView title;
    ImageView imageView;
    public void onResume() {
        super.onResume();
        imageView=findViewById(R.id.recent_img);

        File fileAlarms = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        String dirPath = fileAlarms.getPath();
        String img_path = dirPath+"/"+name+"/"+name+"-menu.jpg";

        Bitmap bitmap=BitmapFactory.decodeFile(img_path);
        imageView.setImageBitmap(bitmap);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        name=getIntent().getStringExtra("name");
        title=findViewById(R.id.textView_title);
        title.setText(name);

        Button fixbutton = (Button) findViewById(R.id.button_fix);


        fixbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FixActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });


        ImageView imageview2 = (ImageView)findViewById(R.id.recent_img);
        Intent intent = new Intent();
        byte[] bytes = getIntent().getByteArrayExtra("image");
        if(bytes!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageview2.setImageBitmap(bitmap);
        }
        Button b_button=findViewById(R.id.button_back);
        b_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}