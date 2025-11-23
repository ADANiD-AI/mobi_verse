package com.mobiverse.keyboard.news

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the News API.
 */
interface NewsApiService {

    // Using a placeholder endpoint, this will be replaced with a real News API
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String = "us",
        @Query("category") category: String? = null
    ): Call<NewsApiResponse> // NewsApiResponse will be defined next
}

/**
 * Data class to parse the top-level response from the News API.
 */
data class NewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ApiArticle>
)

/**
 * Data class for a single article coming from the API.
 */
data class ApiArticle(
    val source: ApiSource,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String
)

/**
 * Data class for the source of an article.
 */
data class ApiSource(
    val id: String?,
    val name: String
)
