package com.dell.example.newsApp.model

import java.util.*

/**
 * Class contain the details regarding News after retyriving from API
 */
class NewsResponse {
    var status: String? = null
    var totalResults = 0
    var articles: ArrayList<ArticleStructure>? = null
}