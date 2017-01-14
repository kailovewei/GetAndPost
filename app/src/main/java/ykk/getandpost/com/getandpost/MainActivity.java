package ykk.getandpost.com.getandpost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private EditText name_editText;
    private  EditText pwd_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button_Id);
        name_editText= (EditText) findViewById(R.id.nameText_Id);
        pwd_editText= (EditText) findViewById(R.id.pwdText_Id);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name=name_editText.getText().toString();
        String pwd=pwd_editText.getText().toString();
        //使用Post方法向服务器发送请求
        PostThread thread=new PostThread(name,pwd);
        thread.start();
        //使用get方法向服务器发送请求
        //GetThread thread=new GetThread(name,pwd);
        // thread.start();

    }
    //对线程使用post方法向服务器发送请求
    class PostThread extends Thread
    {
        String name;
        String pwd;
        public PostThread(String name,String pwd)
        {
            this.name=name;
            this.pwd=pwd;
        }
        @Override
        public void run() {
           HttpClient httpClient=new DefaultHttpClient();
            String url="http://192.168.1.122:8080/s02e14.jsp";
            //生成使用POSt方法的请求对象
            HttpPost httpPost=new HttpPost(url);
            //NameValuePair对象代表一个需要发往服务器的键值对
            NameValuePair pair1=new BasicNameValuePair("name",name);
            NameValuePair pair2=new BasicNameValuePair("password",pwd);
            //将准备好的键值对对象放置在一个List当中
            ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
            pairs.add(pair1);
            pairs.add(pair2);
            //创建代表请求体的对象
            try {
                HttpEntity requestEntity=new UrlEncodedFormEntity(pairs);
                httpPost.setEntity(requestEntity);
                try {
                    HttpResponse response=httpClient.execute(httpPost);
                    if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
                    {
                        HttpEntity entity=response.getEntity();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(entity.getContent()));
                        String result=reader.readLine();
                        Log.d("GetAndPost",result);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    //对线程使用Get方法向服务器发送请求
    class GetThread extends Thread
    {
        String name;
        String pwd;
        public GetThread(String name,String pwd)
        {
            this.name=name;
            this.pwd=pwd;
        }
        @Override
        public void run()
        {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://192.168.1.122:8080/s02e14.jsp?name=" + name + "&password=" + pwd;
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse response=httpClient.execute(httpGet);
                if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
                {
                    HttpEntity entity=response.getEntity();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(entity.getContent()));
                    String result=reader.readLine();
                    Log.d("GetAndPost",result);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
