package com.dell.example.newsApp.model

/*
** Class contain all the parameter for news
**/
data class Article(var author: String? = null, var title: String? = null, var description: String? = null, var url: String? = null,
                   var urlToImage: String? = null, var publishedAt: String? = null, )