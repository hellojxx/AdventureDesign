package kr.ac.cnu.computer.avddddd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.*;
import java.util.ArrayList;

public class Shopinfo extends AppCompatActivity {
    TextView textView1;
    String name="";
    String f_name="shopinfo";
    RecyclerView recyclerView;
    TextView date;
    String url;
    ImageView imageView;
    byte[] byteArray;

    protected void onResume() {

        super.onResume();
        Button b_naver=findViewById(R.id.b_naver);
        Button b_instar=findViewById(R.id.b_instar);
        Button b_kakao=findViewById(R.id.b_kakao);
        Button b_google=findViewById(R.id.b_google);
        imageView=findViewById(R.id.imageView);
        ArrayList<Button> b_arr=new ArrayList<>();
        b_arr.add(b_naver);
        b_arr.add(b_instar);
        b_arr.add(b_kakao);
        b_arr.add(b_google);
        File fileAlarms = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        String dirPath = fileAlarms.getPath();
        //String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);
        File datafile1 = new File(dirPath+"/"+f_name+"-time.txt");
        String content1 = null;
        String timearr1="";
        try {
            //불러올 파일 이름을 던져주며 메소드 실행
            content1 = readFromFile(datafile1);
            String temp1 [] = content1.split("\n");
            for(int i=0;i<temp1.length;i++) {
                timearr1+=temp1[i]+"\n";
            }
            date.setText(timearr1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        File datafile2 = new File(dirPath+"/"+f_name+"-url.txt");
        String content2 = null;
        try {
            //불러올 파일 이름을 던져주며 메소드 실행
            content2 = readFromFile(datafile2);
            url=content2;
            String temp2 [] = content2.split("\n");
            for(int i=0;i<temp2.length;i++){
                int finalI = i;
                b_arr.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(temp2[finalI]));
                            startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(Shopinfo.this, "올바른 URL주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file_path=new File(dirPath+"/"+name+"/");
        if(!file_path.isDirectory()){
            file_path.mkdirs();
        }
        Bitmap bitmap= BitmapFactory.decodeFile(dirPath+"/"+name+"/"+name+".JPG");
        imageView.setImageBitmap(bitmap);
        if(bitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = (float) (1024 / (float) bitmap.getWidth());
            int image_w = (int) (bitmap.getWidth() * scale);
            int image_h = (int) (bitmap.getHeight() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
            resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
        }
    }//onResume



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopinfo);
        textView1=findViewById(R.id.sname);
        date=findViewById(R.id.date);

        //파일 만들기
        File fileAlarms = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        String dirPath = fileAlarms.getPath();
        //String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);
        if(!file.exists()) {
            file.mkdirs();
        }
        String content1;
        File datafile1 = new File(dirPath+"/"+f_name+"-time.txt");
        try {
            content1 = readFromFile(datafile1);

            if(content1==null){
                //2.(쓰기)
                FileOutputStream outputStream = new FileOutputStream(datafile1);
                //3.(쓰기)
                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                //실제 내용으로 작성하기
                writer.write(" \n \n \n \n \n \n \n \n \n \n ");
                //모든것들 종료 해줌
                writer.flush();
                writer.close();
                outputStream.close();
            }else {
                //2.(쓰기)
                FileOutputStream outputStream = new FileOutputStream(datafile1);
                //3.(쓰기)
                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                //실제 내용으로 작성하기
                writer.write("");
                //모든것들 종료 해줌
                writer.flush();
                writer.close();
                outputStream.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없음");
        } catch (IOException e) {
            System.out.println("출력 문제");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //파일 만들기

        content1 = null;
        try {
            //불러올 파일 이름을 던져주며 메소드 실행
            content1 = readFromFile(datafile1);
            String temp [] = content1.split("\n");

        } catch (Exception e) {
            e.printStackTrace();
        }


        String content2;
        File datafile2 = new File(dirPath+"/"+f_name+"-url.txt");
        try {
            content2 = readFromFile(datafile2);

            if(content2==null){
                //2.(쓰기)
                FileOutputStream outputStream = new FileOutputStream(datafile2);
                //3.(쓰기)
                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                //실제 내용으로 작성하기
                writer.write(" \n \n \n \n \n \n \n \n \n \n ");
                //모든것들 종료 해줌
                writer.flush();
                writer.close();
                outputStream.close();
            }else {
                //2.(쓰기)
                FileOutputStream outputStream = new FileOutputStream(datafile2);
                //3.(쓰기)
                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                //실제 내용으로 작성하기
                writer.write("");
                //모든것들 종료 해줌
                writer.flush();
                writer.close();
                outputStream.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없음");
        } catch (IOException e) {
            System.out.println("출력 문제");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //파일 만들기

        content2 = null;
        try {
            //불러올 파일 이름을 던져주며 메소드 실행
            content2 = readFromFile(datafile2);
            String temp [] = content2.split("\n");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        TextView sname = findViewById(R.id.sname);
        sname.setText(name);

        f_name=name+f_name;

        textView1.setText(name);
        Button Tosetinformationbutton = findViewById(R.id.shopinfor);
        Tosetinformationbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent setinformationIntent = new Intent(Shopinfo.this, Information.class);
                setinformationIntent.putExtra("name", name);
                startActivity(setinformationIntent);


            }
        });
        //정보창으로 넘기기

        //수정창으로 넘기기

        Button Toshopinfosetbutton = findViewById(R.id.toshopedit);
        Toshopinfosetbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent toshopinfosetIntent = new Intent(Shopinfo.this, ShopInfoSet.class);
                toshopinfosetIntent.putExtra("name", name);
                toshopinfosetIntent.putExtra("date", date.getText().toString());
                toshopinfosetIntent.putExtra("url", url);
                toshopinfosetIntent.putExtra("img", byteArray);

                startActivity(toshopinfosetIntent);
            }
        });
        //수정창으로 넘기기
        Button e_menu=findViewById(R.id.menu);
        e_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Shopinfo.this, Img.class);
                intent1.putExtra("name", name);
                startActivity(intent1);
            }
        });
        Button b_button=findViewById(R.id.button_backtolist);
        b_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public String readFromFile(File file) throws Exception {
        //2.(읽기) 받아온 이름경로 설정 하고
        FileInputStream fileInputStream = new FileInputStream(file);
        //3.(읽기) 버퍼에 연동해주기
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
        //4.(읽기) 스트링 버퍼 생성
        StringBuffer stringBuffer = new StringBuffer();

        String content = null; // 4.(읽기) 리더에서 라인을 받아오는데 받아올게 없을떄까지 반복
        while ((content = reader.readLine()) != null) {
            stringBuffer.append(content + "\n");
        }
        //사용한것들은 종료
        reader.close();
        fileInputStream.close();
        //5.(읽기)받아온 정보를 다시 리턴해준다
        return stringBuffer.toString();
    }


    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grandResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
        if (requestCode == 1000) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result == true) {

            } else {
                finish();
            }
        }
    }
}
