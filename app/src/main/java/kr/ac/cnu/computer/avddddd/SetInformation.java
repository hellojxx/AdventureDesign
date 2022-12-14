package kr.ac.cnu.computer.avddddd;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.*;
import java.util.ArrayList;

public class SetInformation extends AppCompatActivity {
    RecyclerView settextView;
    UrlAdepter adapter;

    String name="";

    ArrayList<String> testDataSet;

    protected void onResume() {

        super.onResume();

        //리사이클러뷰

        settextView = findViewById(R.id.setrecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);//매니저 생성

        settextView.setLayoutManager(layoutManager);//매니저 설정

        SetTextAdepter settextAdepter = new SetTextAdepter(testDataSet);//값 넣어주기
        settextView.setAdapter(settextAdepter);//어댑터
        //리사이클러뷰

        Button addinformation = findViewById(R.id.addimformation);

        addinformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testDataSet.add(" ");
            }
        });//추가 버튼

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setinformation);
        name=getIntent().getStringExtra("name");
        TextView title=findViewById(R.id.ShopName2);
        title.setText(name);

        testDataSet = new ArrayList<>();

        File fileAlarms = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        String dirPath = fileAlarms.getPath();
        //String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);
        Toast.makeText(getApplicationContext(),dirPath,Toast.LENGTH_LONG);
        File datafile = new File(dirPath+"/"+name+".txt");

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

        settextView = findViewById(R.id.setrecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);//매니저 생성

        settextView.setLayoutManager(layoutManager);//매니저 설정

        SetTextAdepter settextAdepter = new SetTextAdepter(testDataSet);//값 넣어주기
        settextView.setAdapter(settextAdepter);//어댑터
        //리사이클러뷰

        //뒤로가기 버튼
        Button ToBackButton = findViewById(R.id.backtoinformation);
        ToBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //뒤로가기 버튼

        //수정 버튼
        Button setinformation = findViewById(R.id.Set);
        setinformation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                ArrayList<String> data = settextAdepter.getinformation();
                int num = data.size();
                String datas = "";

                for(int i=0;i<num-1;i++){
                    datas=datas+data.get(i)+"\n";
                }
                datas=datas+data.get(num-1);

                try {

                    //2.(쓰기) 자바랑은 다르게 openFileOutput(name, MODE_PRIVATE) 이렇게 사용하는데
                    // 받아온 파일이름넣어주고 쉐어드프리퍼런드때 배웠던것처럼 나만 사용하게 하는 모드이다.
                    FileOutputStream outputStream = new FileOutputStream(datafile);
                    //3.(쓰기) OutputStreamWriter 여기에 위에서 파일 이름 설정을 해줌
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                    //실제 내용으로 작성하기
                    writer.write(datas);
                    //모든것들 종료 해줌
                    writer.flush();
                    writer.close();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    System.out.println("파일을 찾을 수 없음");
                } catch (IOException e) {
                    System.out.println("출력 문제");
                }
            }
        });//수정완료 버튼

        Button addinformation = findViewById(R.id.addimformation);

        addinformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testDataSet.add("");
            }
        });//추가 버튼


    }//Onclick

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
}
