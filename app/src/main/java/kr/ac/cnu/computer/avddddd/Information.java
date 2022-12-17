package kr.ac.cnu.computer.avddddd;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.*;
import java.util.ArrayList;


public class Information extends AppCompatActivity{
    RecyclerView textView;
    UrlAdepter adapter;
    String s_name;
    String name="";

    protected void onResume() {

        super.onResume();

        File fileAlarms = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        String dirPath = fileAlarms.getPath();
        //String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);
        File datafile = new File(dirPath+"/"+name+".txt");


        ArrayList<String> testDataSet = new ArrayList<>();

        //1.(읽기) 에디트 텍스트에서 불러올 파일 받아오기
        String content = null;
        try {
            //불러올 파일 이름을 던져주며 메소드 실행
            content = readFromFile(datafile);
            String temp [] = content.split("\n");
            for(int i=0;i<temp.length;i++) {
                testDataSet.add(temp[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //리사이클러뷰
        textView = findViewById(R.id.setrecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);//매니저 생성

        textView.setLayoutManager(layoutManager);//매니저 설정

        TextAdepter textAdepter = new TextAdepter(testDataSet);
        textView.setAdapter(textAdepter);
        //리사이클러뷰
/*
        try {
            //2.(쓰기)
            FileOutputStream outputStream = new FileOutputStream(datafile);
            //3.(쓰기)
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            //실제 내용으로 작성하기
            //writer.write("hi\nbye");
            //모든것들 종료 해줌
            writer.flush();
            writer.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없음");
        } catch (IOException e) {
            System.out.println("출력 문제");
        }
 */
    }//Resume

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        s_name=getIntent().getStringExtra("name");
        name = s_name+name;

        TextView shopname = findViewById(R.id.ShopName2);
        shopname.setText(s_name);

        // 권한ID를 가져옵니다
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        // 권한이 열려있는지 확인
        if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
            // 마쉬멜로우 이상버전부터 권한을 물어본다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1000);
            }
            return;
        }

        File fileAlarms = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        String dirPath = fileAlarms.getPath();
        //String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);
        if(!file.exists()) {
            file.mkdirs();
        }
        String content;
        File datafile = new File(dirPath+"/"+name+".txt");
        try {
            content = readFromFile(datafile);
            //2.(쓰기)
            FileOutputStream outputStream = new FileOutputStream(datafile);
            //3.(쓰기)
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            //실제 내용으로 작성하기
            writer.write(content);
            //모든것들 종료 해줌
            writer.flush();
            writer.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없음");
        } catch (IOException e) {
            System.out.println("출력 문제");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        ArrayList<String> testDataSet = new ArrayList<>();

        //1.(읽기) 에디트 텍스트에서 불러올 파일 받아오기
        content = null;
        try {
            //불러올 파일 이름을 던져주며 메소드 실행
            content = readFromFile(datafile);
            String temp [] = content.split("\n");
            for(int i=0;i<temp.length;i++) {
                testDataSet.add(temp[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //리사이클러뷰
        textView = findViewById(R.id.setrecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);//매니저 생성

        textView.setLayoutManager(layoutManager);//매니저 설정

        TextAdepter textAdepter = new TextAdepter(testDataSet);
        textView.setAdapter(textAdepter);
        //리사이클러뷰

        //다음창넘기기

        Button Tosetinformationbutton = findViewById(R.id.Set);
        Tosetinformationbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent setinformationIntent = new Intent(Information.this, SetInformation.class);
                setinformationIntent.putExtra("name",s_name);
                startActivity(setinformationIntent);
            }
        });
        //다음창넘기기
        Button b_button=findViewById(R.id.backtoinformation);
        b_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    } // Oncreate

    //파일을 읽기위한 메소드
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
