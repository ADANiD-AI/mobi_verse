package com.mobiverse.keyboard.news

/**
 * Data class representing a single news article.
 */
data class NewsArticle(
    val title: String,
    val imageUrl: String,
    val source: String,
    val category: String,
    val timeAgo: String,
    val articleUrl: String
)
