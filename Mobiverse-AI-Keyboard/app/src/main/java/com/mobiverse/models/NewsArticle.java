
package com.mobiverse.models;

public class NewsArticle {
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String sourceName;

    public NewsArticle(String title, String description, String url, String imageUrl, String sourceName) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.sourceName = sourceName;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
    public String getImageUrl() { return imageUrl; }
    public String getSourceName() { return sourceName; }
}
