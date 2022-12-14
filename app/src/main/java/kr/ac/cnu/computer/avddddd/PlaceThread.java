package kr.ac.cnu.computer.avddddd;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PlaceThread extends Thread{
    String urlStr;
    StringBuilder sb = new StringBuilder();

    PlaceThread (String urlStr) {
        this.urlStr = urlStr;
    }
    public void run() {
        try{
            URL url = new URL(urlStr);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line="";
            while ((line = reader.readLine()) != null) {
                sb.append(line+"\n");
            }
            reader.close();
            conn.disconnect();
        }catch (Exception e){}
    }
}
