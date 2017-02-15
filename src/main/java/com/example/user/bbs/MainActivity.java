package com.example.user.bbs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    String result;

    String _title, _write;
    ArrayList list_title = new ArrayList(); // id array
    ArrayList list_write = new ArrayList(); // pw array
    ListView list;
    TextView text;
    ArrayAdapter adapter;

    JSONArray people = null;

    ArrayList<HashMap<String, String>> personList;


    public String getresult() {
        Log.d("asd", result);
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        check();
        adapter.add(list_title.get(list_title.size()-1).toString());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list = (ListView) findViewById(R.id.Listview);
        TextView textView = (TextView) findViewById(R.id.mainEt);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SecondActivitiy.class);
                startActivity(intent);
            }
        });
        check();
        for (int i = 0; i < list_title.size(); i++) {
            adapter.add(list_title.get(i).toString());
            adapter.notifyDataSetChanged();
        }
    }

    public void check() {

        class checkTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                StringBuilder jsonHtml = new StringBuilder();
                String link = "http://1.224.44.55/tjqjarb/jsonselect.php";
                try {
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    while (true) {
                        String line = br.readLine();
                        if (line == null) break;
                        jsonHtml.append(line + "\n");
                    }
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result = login(jsonHtml.toString());
                Log.d("asdzxc", jsonHtml.toString());
                return result;
            }
        }
        try {
            result = new checkTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("asdx", result);
    }

    public String login(String json) {
        String r = "";
        String title;
        String write;
        try {
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                Log.d("vvvvvvvvvvvvv", String.valueOf(ja.length()));
                JSONObject j = ja.getJSONObject(i);
                r += String.format("제목 : %s 글쓰기 : %s\n", j.getString("title"), j.getString("writing"));
                title = j.getString("title");
                Log.d("ccccccccccccc", title);
                write = j.getString("writing");
                Log.d("aaaaaaaaaaa", write);
                list_title.add(title);
                Log.d("nnnnnnnnnnnnnn", list_title.get(i).toString());
                list_write.add(write);
                Log.d("nnnnnnnnnnnnnnnnnss", list_write.get(i).toString());
            }
            Log.d("asdzxc", r);

        } catch (Exception e) {
        }

        return r;
    }


}

