package com.mbostic.employeeslist;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for viewing the first 5 results from google.com when searching for the name of the employee
 */
public class PublicProfileActivity extends AppCompatActivity {
    /**
     * Displays the name of the employee in a TextView, then fetches the first 5 links from google.com and displays them
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView nameTV = findViewById(R.id.name);
        final String name = getIntent().getExtras().getString("name");
        nameTV.setText(name);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonStr;
                try {
                    String key = "AIzaSyBCztYXhEDpOrnG_DpV_1rM9WG4CZDeFck";
                    URL url = new URL(
                            "https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=013036536707430787589:_pqjad5hr1a&q=" + name + "&alt=json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    String line;
                    System.out.println("Output from Server .... \n");

                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    jsonStr = sb.toString();
                    conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                List<String> results = new ArrayList<>();

                try {
                    JSONObject json = new JSONObject(jsonStr);

                    JSONArray arr = json.getJSONArray("items");
                    for (int i = 0; i < arr.length(); i++)
                    {
                        String title = arr.getJSONObject(i).getString("title");
                        results.add(title);
                        if(results.size() == 5){
                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final ListView resultsLV = findViewById(R.id.resultsLV);
                final ArrayAdapter arrayAdapter = new ArrayAdapter<>(PublicProfileActivity.this, android.R.layout.simple_list_item_1, results);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultsLV.setAdapter(arrayAdapter);
                    }
                });

            }
        }).start();
    }
}
