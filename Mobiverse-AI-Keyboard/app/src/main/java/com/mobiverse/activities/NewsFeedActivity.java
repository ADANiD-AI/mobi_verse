
package com.mobiverse.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiverse.R;
import com.mobiverse.adapters.NewsAdapter;
import com.mobiverse.models.NewsArticle;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<NewsArticle> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data for now, will be replaced with real API data
        loadDummyData();

        newsAdapter = new NewsAdapter(articleList);
        recyclerView.setAdapter(newsAdapter);
    }

    private void loadDummyData() {
        articleList.add(new NewsArticle("Crypto Market Soars", "Bitcoin reaches a new all-time high.", "url", "img_url", "Crypto News"));
        articleList.add(new NewsArticle("Tech Giant Releases New Phone", "A new smartphone with AI features is announced.", "url", "img_url", "Tech Today"));
        articleList.add(new NewsArticle("Global Stock Market Update", "Markets show mixed results after recent announcements.", "url", "img_url", "Finance World"));
    }
}
