package com.example.user.bbs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SecondActivitiy extends AppCompatActivity {

    private EditText editTexttitle;
    private EditText editTextwrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activitiy);

        editTexttitle = (EditText) findViewById(R.id.title);
        editTextwrite = (EditText) findViewById(R.id.write);

    }

    public void insert(View view) {
        String title = editTexttitle.getText().toString();
        String write = editTextwrite.getText().toString();
        insertToDatavase(title,write);
    }

    private void insertToDatavase(String title,String write) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SecondActivitiy.this, "개시중", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                 String title = (String)params[0];
                    String write = (String)params[1];


                    String link="http://1.224.44.55/tjqjarb/insert_text.php";
                    String data = URLEncoder.encode("title","UTF-8") + "="+ URLEncoder.encode(title, "UTF-8");
                     data += "&"+URLEncoder.encode("write","UTF-8") + "="+ URLEncoder.encode(write, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null)

                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }
        InsertData task = new InsertData();
        task.execute(title,write);
    }

}
