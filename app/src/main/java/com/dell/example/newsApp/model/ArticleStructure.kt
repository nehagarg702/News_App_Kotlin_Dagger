package com.dell.example.newsApp.model

import java.io.Serializable

/**
 * Class contain all the parameter that define News Article Structure.
 */
class ArticleStructure : Serializable {
    var source: Source? = null
    var author: String? = null
    var title: String? = null
    var description: String? = null
    var url: String? = null
    var urlToImage: String? = null
    var publishedAt: String? = null
}