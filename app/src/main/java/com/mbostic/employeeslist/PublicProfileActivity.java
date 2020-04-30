package com.mbostic.employeeslist;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for viewing the first 5 results from google.com when searching for the name of the employee
 */
public class PublicProfileActivity extends AppCompatActivity {
    /**
     * Displays the name of the employee, then fetches the first 5 links from google.com and displays them
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
                List<String> results = new ArrayList<>();
                try {
                    Document doc = Jsoup
                            .connect("https://www.google.com/search?q=" + name + "&num=7")
                            .userAgent(
                                    "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                            .timeout(5000).get();

                    // get all links
                    Elements links = doc.select("a[href]");
                    int count = 0;
                    for (Element link : links) {
                        // show 5 links and no more
                        if(count == 5){
                            break;
                        }
                        String temp = link.attr("href");
                        // if link text length is less than 10, the link is usually not what we are looking for
                        if(temp.startsWith("/url?q=") && link.text().length() > 10){
                            String result = link.text();
                            Log.d("result", result);
                            if(result.contains("http")){
                                result = result.substring(0, result.indexOf("http"));
                            }
                            results.add(result);
                            count++;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PublicProfileActivity.this, R.string.results_fetch_error, Toast.LENGTH_LONG).show();
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
