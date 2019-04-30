package com.example.cs125final;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String API_KEY = "5d291f9fc3694a00bbac1e3c18cb1bf9";
    String NEWS_SOURCE = "bbc-news";
    ListView listNews;
    ProgressBar loader;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String authorName = "author";
    static final String title = "title";
    static final String description = "description";
    static final String url = "url";
    static final String urlToImage = "urlToImage";
    static final String publishedAt = "publishedAt";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listNews = (ListView) findViewById(R.id.listNews);
        loader = (ProgressBar) findViewById(R.id.loader);
        listNews.setEmptyView(loader);
        //Check if online
        if (Functions.isNetworkAvailable(getApplicationContext())) {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    class DownloadNews extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Get json with NEWS API
        protected String doInBackground(String... args) {
            String json = "";
            String urlParameters = "";
            json = Functions.executeGet("https://newsapi.org/v1/articles?source="
                    + NEWS_SOURCE + "&sortBy=top&apiKey=" + API_KEY, urlParameters);
            return json;
        }

        protected void onPostExecute(String json) {
            if (json.length() > 10) {
                try {
                    // Parse json w/ google Gson and turn it into a hashmap
                    JsonObject jsonResponse = new JsonParser().parse(json).getAsJsonObject();
                    JsonArray jsonArray = jsonResponse.getAsJsonArray("articles");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(authorName, jsonObject.get(authorName).getAsString());
                        map.put(title, jsonObject.get(title).getAsString());
                        map.put(description, jsonObject.get(description).getAsString());
                        map.put(url, jsonObject.get(url).getAsString());
                        map.put(urlToImage, jsonObject.get(urlToImage).getAsString());
                        map.put(publishedAt, jsonObject.get(publishedAt).getAsString());
                        dataList.add(map);
                    }
                    // Show message w/ toast when exception is caught
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Unexpected error",
                            Toast.LENGTH_SHORT).show();
                }
                // Show in the listView
                ListNewsAdapter listNewsAdapter = new ListNewsAdapter(MainActivity.this, dataList);
                listNews.setAdapter(listNewsAdapter);
                listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                        i.putExtra("url", dataList.get(+position).get(url));
                        startActivity(i);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No news found",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
