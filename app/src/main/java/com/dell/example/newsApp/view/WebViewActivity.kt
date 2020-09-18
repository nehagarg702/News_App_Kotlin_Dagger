package com.dell.example.newsApp.view

/***
 * Activity to open the news in webView
 */
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.TextView
import com.dell.example.newsApp.R
import com.dell.example.newsApp.model.Constants

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private var progressBar: ProgressBar? = null
    private lateinit var url: String
    private var montserratRegular: Typeface? = null
    private lateinit var mTitle: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        url = intent.getStringExtra(Constants.INTENT_URL)!!
        createToolbar()
        val assetManager = this.applicationContext.assets
        montserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf")
        webView = findViewById(R.id.webView_article)
        progressBar = findViewById(R.id.progressBar)
        if (savedInstanceState == null) {
            webView.loadUrl(url)
            initWebView()
        }
    }

    private fun createToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_web_view)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mTitle = findViewById(R.id.toolbar_title_web_view)
        mTitle.setText(url)
        mTitle.setTypeface(montserratRegular)
    }

    private fun initWebView() {
        webView.webChromeClient = MyWebChromeClient(this)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar!!.visibility = View.VISIBLE
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                webView.loadUrl(url)
                progressBar!!.visibility = View.GONE
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar!!.visibility = View.GONE
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
            }
        }
        webView.clearCache(true)
        webView.clearHistory()
        webView.isHorizontalScrollBarEnabled = false
    }

    private inner class MyWebChromeClient(var context: Context) : WebChromeClient()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right)
    }
}