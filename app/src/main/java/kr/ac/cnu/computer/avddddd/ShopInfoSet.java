package kr.ac.cnu.computer.avddddd;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.*;
import java.util.ArrayList;

public class ShopInfoSet extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;
    ImageView imageView;

    RecyclerView settextView;
    EditText instar, kakao, google;
    EditText naver, mon, tuse, wen, tur, fri, sat, sun;

    String name="";
    String f_name="shopinfo";
    String e_naver, e_instar, e_kakao, e_google;
    String m, t, w, t2, f, s, s2;

    Bitmap s_img;
    ArrayList<String> testDataSet = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopinfoset);
        imageView=findViewById(R.id.recent_img);
        showDialog();
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });


        naver=findViewById(R.id.naver);
        instar=findViewById(R.id.instar);
        kakao=findViewById(R.id.kakao);
        google=findViewById(R.id.google);
        mon=findViewById(R.id.mon);
        tuse=findViewById(R.id.tuse);
        wen=findViewById(R.id.wen);
        tur=findViewById(R.id.tur);
        fri=findViewById(R.id.fri);
        sat=findViewById(R.id.sat);
        sun=findViewById(R.id.sun);
        ArrayList<EditText> textarr=new ArrayList<>();
        textarr.add(mon);
        textarr.add(tuse);
        textarr.add(wen);
        textarr.add(tur);
        textarr.add(fri);
        textarr.add(sat);
        textarr.add(sun);

        ArrayList<EditText> textarr2=new ArrayList<>();

        textarr2.add(naver);
        textarr2.add(instar);
        textarr2.add(kakao);
        textarr2.add(google);
        name = getIntent().getStringExtra("name");
        String time=getIntent().getStringExtra("date");
        String url=getIntent().getStringExtra("url");
        byte[] bytes = getIntent().getByteArrayExtra("img");
        if(bytes!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
        }
        if(time!=null){
            String[] timearr=time.split("\n");
            for(int i=0;i<timearr.length;i++){
                textarr.get(i).setText(timearr[i]);
            }
        }
        if(url!=null){
            String[] urlarr=url.split("\n");
            for(int i=0;i<urlarr.length;i++){
                textarr2.get(i).setText(urlarr[i]);
            }
        }
        TextView sname2 = findViewById(R.id.sname2);
        sname2.setText(name);
        f_name=name+f_name;

        Button ToBackButton = findViewById(R.id.toshopinfoback);
        ToBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button edbutton=findViewById(R.id.toshopinfobutton);
        edbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m = mon.getText().toString();
                t = tuse.getText().toString();
                w = wen.getText().toString();
                t2 = tur.getText().toString();
                f = fri.getText().toString();
                s = sat.getText().toString();
                s2 = sun.getText().toString();


                e_naver = naver.getText().toString();
                e_instar=instar.getText().toString();
                e_kakao=kakao.getText().toString();
                e_google=google.getText().toString();




                String temp1 = m + "\n" + t + "\n" + w + "\n" + t2 + "\n" + f + "\n" + s + "\n" + s2 ;
                String temp2 = e_naver + "\n" + e_instar + "\n" + e_kakao + "\n" + e_google;

                String datas = temp1;
                File fileAlarms = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
                String dirPath = fileAlarms.getPath();
                //String dirPath = getFilesDir().getAbsolutePath();
                File file = new File(dirPath);
                File datafile1 = new File(dirPath+"/"+f_name+"-time.txt");
                String content1="";
                try {

                    //2.(쓰기) 자바랑은 다르게 openFileOutput(name, MODE_PRIVATE) 이렇게 사용하는데
                    // 받아온 파일이름넣어주고 쉐어드프리퍼런드때 배웠던것처럼 나만 사용하게 하는 모드이다.
                    FileOutputStream outputStream = new FileOutputStream(datafile1);
                    //3.(쓰기) OutputStreamWriter 여기에 위에서 파일 이름 설정을 해줌
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);

                    content1 = readFromFile(datafile1);
                    //실제 내용으로 작성하기
                    writer.write(content1);
                    writer.write(datas);
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

                File datafile2 = new File(dirPath+"/"+f_name+"-url.txt");
                String content2="";
                try {

                    //2.(쓰기) 자바랑은 다르게 openFileOutput(name, MODE_PRIVATE) 이렇게 사용하는데
                    // 받아온 파일이름넣어주고 쉐어드프리퍼런드때 배웠던것처럼 나만 사용하게 하는 모드이다.
                    FileOutputStream outputStream = new FileOutputStream(datafile2);
                    //3.(쓰기) OutputStreamWriter 여기에 위에서 파일 이름 설정을 해줌
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);

                    content2 = readFromFile(datafile2);
                    //실제 내용으로 작성하기
                    writer.write(content2);
                    writer.write(temp2);
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
                if(s_img!=null) {
                    saveBitmaptoJpeg(s_img, dirPath, name);
                }
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
    public void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(ShopInfoSet.this)
                .setTitle("알람")
                .setMessage("사진(가게, 음식)을 선택해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
    protected void onActivityResult(int requetCode, int resultCode, Intent data) {
        super.onActivityResult(requetCode, resultCode, data);
        if (requetCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);

                    s_img = img;

                    in.close();

                    imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
    public static void saveBitmaptoJpeg(Bitmap bitmap, String folder, String name){
        String foler_name = folder+"/"+name+"/";
        String file_name = name+".jpg";
        String string_path = foler_name;

        File file_path;
        try{
            file_path=new File(string_path);
            if(!file_path.isDirectory()){
                file_path.mkdirs();
            }

            FileOutputStream out = new FileOutputStream(string_path+file_name);
            if(out!=null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();
            }
        }catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
    }
}
