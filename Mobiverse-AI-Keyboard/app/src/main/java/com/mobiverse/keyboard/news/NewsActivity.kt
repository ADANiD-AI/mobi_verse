package com.mobiverse.keyboard.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobiverse.keyboard.R

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRecyclerView: RecyclerView = findViewById(R.id.news_recycler_view)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)

        val dummyArticles = createDummyArticles()
        newsRecyclerView.adapter = NewsAdapter(dummyArticles)
    }

    private fun createDummyArticles(): List<NewsArticle> {
        return listOf(
            NewsArticle(
                title = "Bitcoin Hits New All-Time High Above $70,000",
                imageUrl = "https://example.com/image1.jpg", // Replace with actual image URL
                source = "CoinDesk",
                category = "CRYPTO",
                timeAgo = "2h ago",
                articleUrl = "https://example.com/article1"
            ),
            NewsArticle(
                title = "Ethereum's Dencun Upgrade Goes Live, Reducing Fees",
                imageUrl = "https://example.com/image2.jpg",
                source = "The Block",
                category = "CRYPTO",
                timeAgo = "5h ago",
                articleUrl = "https://example.com/article2"
            ),
            NewsArticle(
                title = "NVIDIA Unveils Blackwell, The Next-Generation AI Chip",
                imageUrl = "https://example.com/image3.jpg",
                source = "TechCrunch",
                category = "TECHNOLOGY",
                timeAgo = "1d ago",
                articleUrl = "https://example.com/article3"
            )
        )
    }
}
