package com.dell.example.newsApp.view

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dell.example.newsApp.R
import com.dell.example.newsApp.model.Constants

/*
Activity which display the article image and little bit description and a button to open the full article. Include Glide to load the image from url
*/   class ArticleActivity : AppCompatActivity() {
    private var url: String? = null
    private var montserratRegular: Typeface? = null
    private var montserratSemibold: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        assetManager()
        createToolbar()
        receiveFromDataAdapter()
        buttonLinktoFullArticle()
    }

    private fun assetManager() {
        val assetManager = this.applicationContext.assets
        montserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf")
        montserratSemibold = Typeface.createFromAsset(assetManager, "fonts/Montserrat-SemiBold.ttf")
    }

    private fun buttonLinktoFullArticle() {
        val linkToFullArticle = findViewById<Button>(R.id.article_button)
        linkToFullArticle.typeface = montserratRegular
        linkToFullArticle.setOnClickListener { openWebViewActivity() }
    }

    private fun openWebViewActivity() {
        val browserIntent = Intent(this@ArticleActivity, WebViewActivity::class.java)
        browserIntent.putExtra(Constants.INTENT_URL, url)
        startActivity(browserIntent)
    }

    private fun receiveFromDataAdapter() {
        val headLine = intent.getStringExtra(Constants.INTENT_HEADLINE)
        val description = intent.getStringExtra(Constants.INTENT_DESCRIPTION)
        val imgURL = intent.getStringExtra(Constants.INTENT_IMG_URL)
        url = intent.getStringExtra(Constants.INTENT_ARTICLE_URL)
        val contentHeadline = findViewById<TextView>(R.id.content_Headline)
        contentHeadline.text = headLine
        contentHeadline.typeface = montserratSemibold
        val contentDescription = findViewById<TextView>(R.id.content_Description)
        contentDescription.text = description
        contentDescription.typeface = montserratRegular
        val collapsingImage = findViewById<ImageView>(R.id.collapsingImage)
        Glide.with(this)
                .load(imgURL)
                .centerCrop()
                .error(R.drawable.ic_placeholder)
                .crossFade()
                .into(collapsingImage)
    }

    private fun createToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_article)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right)
    }
}