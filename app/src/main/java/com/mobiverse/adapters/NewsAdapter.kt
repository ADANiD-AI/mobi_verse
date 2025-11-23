package com.mobiverse.keyboard.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobiverse.keyboard.R

/**
 * Adapter for the news articles RecyclerView.
 */
class NewsAdapter(private val articles: List<NewsArticle>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_article, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount() = articles.size

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.article_title)
        private val sourceTextView: TextView = itemView.findViewById(R.id.article_source)
        private val categoryTextView: TextView = itemView.findViewById(R.id.article_category)
        private val timeTextView: TextView = itemView.findViewById(R.id.article_time)
        private val imageView: ImageView = itemView.findViewById(R.id.article_image)

        fun bind(article: NewsArticle) {
            titleTextView.text = article.title
            sourceTextView.text = article.source
            categoryTextView.text = article.category
            timeTextView.text = article.timeAgo
            
            Glide.with(itemView.context)
                .load(article.imageUrl)
                .into(imageView)
        }
    }
}
