package com.mobiverse.keyboard.news

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repository to fetch news articles from the API.
 */
class NewsRepository {

    private val newsApiService: NewsApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/") // Base URL for the NewsAPI
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        newsApiService = retrofit.create(NewsApiService::class.java)
    }

    // This function will need to be called from a background thread
    fun getTopHeadlines(apiKey: String, category: String?): List<NewsArticle> {
        try {
            val response = newsApiService.getTopHeadlines(apiKey, category = category).execute()
            if (response.isSuccessful) {
                val apiArticles = response.body()?.articles ?: emptyList()
                // Convert ApiArticle to our app's NewsArticle format
                return apiArticles.mapNotNull { apiArticle ->
                    if (apiArticle.title.isNullOrBlank() || apiArticle.urlToImage.isNullOrBlank()) {
                        return@mapNotNull null
                    }
                    NewsArticle(
                        title = apiArticle.title,
                        imageUrl = apiArticle.urlToImage,
                        source = apiArticle.source.name,
                        category = category?.uppercase(Locale.ROOT) ?: "GENERAL",
                        timeAgo = formatTimeAgo(apiArticle.publishedAt),
                        articleUrl = apiArticle.url
                    )
                }
            }
        } catch (e: Exception) {
            // In a real app, handle this error properly (e.g., logging)
            e.printStackTrace()
        }
        return emptyList()
    }

    private fun formatTimeAgo(publishedAt: String): String {
        // Simple time formatting logic (can be improved)
        try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val past = format.parse(publishedAt)
            val now = Date()
            val seconds = (now.time - past.time) / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            return when {
                days > 0 -> "${days}d ago"
                hours > 0 -> "${hours}h ago"
                minutes > 0 -> "${minutes}m ago"
                else -> "${seconds}s ago"
            }
        } catch (e: Exception) {
            return ""
        }
    }
}
