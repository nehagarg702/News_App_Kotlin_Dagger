package com.dell.example.newsApp.view

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import com.dell.example.newsApp.*
import com.dell.example.newsApp.component.DaggerRetrofitComponent
import com.dell.example.newsApp.model.ArticleStructure
import com.dell.example.newsApp.model.Constants
import com.dell.example.newsApp.model.NewsResponse
import com.dell.example.newsApp.module.ContextModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/*
Search Activity allow us to search about news contain that data and display them
*/   @Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity(), OnRefreshListener {
    private lateinit var mEdtSearch: EditText
    private var mTxvNoResultsFound: TextView? = null
    private lateinit var mSwipeRefreshSearch: SwipeRefreshLayout
    private lateinit var mRecyclerViewSearch: RecyclerView
    private var adapter: DataAdapter? = null
    private var montserratRegular: Typeface? = null
    private var articleStructure: ArrayList<ArticleStructure> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val assetManager = this.applicationContext.assets
        montserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf")
        createToolbar()
        initViews()
        mEdtSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val mgr = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                mgr.hideSoftInputFromWindow(v.windowToken, 0)
                searchEverything(mEdtSearch.text.toString().trim { it <= ' ' })
                return@OnEditorActionListener true
            }
            false
        })
        mSwipeRefreshSearch.isEnabled = false
        mSwipeRefreshSearch.setColorSchemeResources(R.color.colorPrimary)
    }

    private fun createToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_search)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    private fun initViews() {
        mEdtSearch = findViewById(R.id.editText_search)
        mEdtSearch.typeface=montserratRegular
        mSwipeRefreshSearch = findViewById(R.id.swipe_refresh_layout_search)
        mSwipeRefreshSearch.setOnRefreshListener(this)
        mRecyclerViewSearch = findViewById(R.id.search_recycler_view)
        mTxvNoResultsFound = findViewById(R.id.tv_no_results)
        mRecyclerViewSearch.layoutManager = LinearLayoutManager(this@SearchActivity)
    }

    private fun searchEverything(search: String) {
        mSwipeRefreshSearch.isEnabled = true
        mSwipeRefreshSearch.isRefreshing = true
        val sortBy = "publishedAt"
        val language = "en"
        val component=DaggerRetrofitComponent.builder().contextModule(ContextModule(this)).build()
        val call = component.getRetrofitUri().getSearchResults(search, sortBy, language, Constants.API_KEY)
        call!!.enqueue(object : Callback<NewsResponse?> {
            override fun onResponse(call: Call<NewsResponse?>, response: Response<NewsResponse?>) {
                if (response.isSuccessful && response.body()!!.articles != null) {
                    if (response.body()!!.totalResults != 0) {
                        if (articleStructure.isNotEmpty()) {
                            articleStructure.clear()
                        }
                        articleStructure = response.body()!!.articles!!
                        adapter = DataAdapter(this@SearchActivity, articleStructure)
                        mRecyclerViewSearch.visibility = View.VISIBLE
                        mTxvNoResultsFound!!.visibility = View.GONE
                        mRecyclerViewSearch.adapter = adapter
                        mSwipeRefreshSearch.isRefreshing = false
                        mSwipeRefreshSearch.isEnabled = true
                    } else if (response.body()!!.totalResults == 0) {
                        mSwipeRefreshSearch.isRefreshing = false
                        mSwipeRefreshSearch.isEnabled = false
                        mTxvNoResultsFound!!.visibility = View.VISIBLE
                        mRecyclerViewSearch.visibility = View.GONE
                        mTxvNoResultsFound!!.text = "No Results found for \"$search\"."
                    }
                } else {
                    mSwipeRefreshSearch.isRefreshing = false
                    mSwipeRefreshSearch.isEnabled = true
                    mTxvNoResultsFound!!.visibility = View.VISIBLE
                    mRecyclerViewSearch.visibility = View.GONE
                    mTxvNoResultsFound!!.text = "Please check your Internet Connection and Again load the Data"
                }
            }

            override fun onFailure(call: Call<NewsResponse?>, t: Throwable) {
                mSwipeRefreshSearch.isRefreshing = false
                mSwipeRefreshSearch.isEnabled = false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cancel -> {
                mEdtSearch.setText("")
                mEdtSearch.requestFocus()
                val mgr = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                mgr.showSoftInput(mEdtSearch, InputMethodManager.SHOW_IMPLICIT)
                mRecyclerViewSearch.visibility = View.GONE
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right)
    }

    override fun onRefresh() {
        searchEverything(mEdtSearch.text.toString())
    }
}