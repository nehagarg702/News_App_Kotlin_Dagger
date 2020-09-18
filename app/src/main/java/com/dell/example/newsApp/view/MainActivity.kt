package com.dell.example.newsApp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.dell.example.newsApp.*
import com.dell.example.newsApp.component.DaggerRetrofitComponent
import com.dell.example.newsApp.model.ArticleStructure
import com.dell.example.newsApp.model.Constants
import com.dell.example.newsApp.model.NewsResponse
import com.dell.example.newsApp.module.ContextModule
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/******
 *
 * Main Activity which display the top headline on the basis of source. List of sources are displayed by using Drawer.
 */
class MainActivity : AppCompatActivity(), OnRefreshListener {
    private val sourceArray = arrayOf("google-news-in", "abc-news", "bbc-news", "cnn", "espn", "the-hindu", "the-times-of-india",
            "buzzfeed", "mashable", "mtv-news", "bbc-sport", "espn-cric-info", "fox-sports", "talksport", "the-sport-bible", "medical-news-today",
            "national-geographic", "crypto-coins-news", "engadget", "the-next-web", "the-verge", "techcrunch", "techradar", "ign", "polygon")
    private var source: String? = null
    private var articleStructure: ArrayList<ArticleStructure> = ArrayList()
    private var adapter: DataAdapter? = null
    private var mTxvNoResultsFound: TextView? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var result: Drawer? = null
    private lateinit var accountHeader: AccountHeader
    private var toolbar: Toolbar? = null
    private lateinit var recyclerView: RecyclerView
    private var listState: Parcelable? = null
    private lateinit var mTitle: TextView
    private var montserratRegular: Typeface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createToolbar()
        createRecyclerView()
        source = sourceArray[0]
        mTitle.setText(R.string.toolbar_default_text)
        onLoadingSwipeRefreshLayout()
        val assetManager = this.applicationContext.assets
        montserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf")
        createDrawer(savedInstanceState, toolbar)
    }

    private fun createToolbar() {
        toolbar = findViewById(R.id.toolbar_main_activity)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        mTitle = findViewById(R.id.toolbar_title)
        mTitle.typeface = montserratRegular
    }

    private fun createRecyclerView() {
        mTxvNoResultsFound = findViewById(R.id.tv_no_results)
        recyclerView = findViewById(R.id.card_recycler_view)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun createDrawer(savedInstanceState: Bundle?, toolbar: Toolbar?) {
        val item0 = PrimaryDrawerItem().withIdentifier(0).withName("GENERAL").withSelectable(false).withTypeface(montserratRegular)
        val item1 = PrimaryDrawerItem().withIdentifier(1).withName("Google News India")
                .withIcon(R.drawable.ic_googlenews).withTypeface(montserratRegular)
        val item2 = PrimaryDrawerItem().withIdentifier(2).withName("Abc News")
                .withIcon(R.drawable.abcnews).withTypeface(montserratRegular)
        val item3 = PrimaryDrawerItem().withIdentifier(3).withTypeface(montserratRegular).withName("BBC News").withIcon(R.drawable.ic_bbcnews)
        val item4 = PrimaryDrawerItem().withIdentifier(4).withName("CNN")
                .withIcon(R.drawable.ic_ccnnews).withTypeface(montserratRegular)
        val item5 = PrimaryDrawerItem().withIdentifier(5).withName("ESPN")
                .withIcon(R.drawable.espn).withTypeface(montserratRegular)
        val item6 = PrimaryDrawerItem().withIdentifier(6).withName("The Hindu")
                .withIcon(R.drawable.ic_thehindu).withTypeface(montserratRegular)
        val item7 = PrimaryDrawerItem().withIdentifier(7).withName("The Times of India")
                .withIcon(R.drawable.ic_timesofindia).withTypeface(montserratRegular)
        val item8 = SectionDrawerItem().withIdentifier(8).withName("ENTERTAINMENT").withTypeface(montserratRegular)
        val item9 = PrimaryDrawerItem().withIdentifier(9).withName("Buzzfeed")
                .withIcon(R.drawable.ic_buzzfeednews).withTypeface(montserratRegular)
        val item10 = PrimaryDrawerItem().withIdentifier(10).withName("Mashable")
                .withIcon(R.drawable.ic_mashablenews).withTypeface(montserratRegular)
        val item11 = PrimaryDrawerItem().withIdentifier(11).withName("MTV News")
                .withIcon(R.drawable.ic_mtvnews).withTypeface(montserratRegular)
        val item12 = SectionDrawerItem().withIdentifier(12).withName("SPORTS").withTypeface(montserratRegular)
        val item13 = PrimaryDrawerItem().withIdentifier(13).withName("BBC Sports")
                .withIcon(R.drawable.ic_bbcsports).withTypeface(montserratRegular)
        val item14 = PrimaryDrawerItem().withIdentifier(14).withName("ESPN Cric Info")
                .withIcon(R.drawable.ic_espncricinfo).withTypeface(montserratRegular)
        val item15 = PrimaryDrawerItem().withIdentifier(15).withName("Fox Sports")
                .withIcon(R.drawable.fox).withTypeface(montserratRegular)
        val item16 = PrimaryDrawerItem().withIdentifier(16).withName("TalkSport")
                .withIcon(R.drawable.ic_talksport).withTypeface(montserratRegular)
        val item17 = PrimaryDrawerItem().withIdentifier(17).withName("The Sport Bible")
                .withIcon(R.drawable.sport_bible).withTypeface(montserratRegular)
        val item18 = SectionDrawerItem().withIdentifier(18).withName("SCIENCE").withTypeface(montserratRegular)
        val item19 = PrimaryDrawerItem().withIdentifier(19).withName("Medical News Today")
                .withIcon(R.drawable.ic_medicalnewstoday).withTypeface(montserratRegular)
        val item20 = PrimaryDrawerItem().withIdentifier(20).withName("National Geographic")
                .withIcon(R.drawable.ic_nationalgeographic).withTypeface(montserratRegular)
        val item21 = SectionDrawerItem().withIdentifier(21).withName("TECHNOLOGY").withTypeface(montserratRegular)
        val item22 = PrimaryDrawerItem().withIdentifier(22).withName("Crypto Coins News")
                .withIcon(R.drawable.ic_ccnnews).withTypeface(montserratRegular)
        val item23 = PrimaryDrawerItem().withIdentifier(23).withName("Engadget")
                .withIcon(R.drawable.ic_engadget).withTypeface(montserratRegular)
        val item24 = PrimaryDrawerItem().withIdentifier(24).withName("The Next Web")
                .withIcon(R.drawable.ic_thenextweb).withTypeface(montserratRegular)
        val item25 = PrimaryDrawerItem().withIdentifier(25).withName("The Verge")
                .withIcon(R.drawable.ic_theverge).withTypeface(montserratRegular)
        val item26 = PrimaryDrawerItem().withIdentifier(26).withName("TechCrunch")
                .withIcon(R.drawable.ic_techcrunch).withTypeface(montserratRegular)
        val item27 = PrimaryDrawerItem().withIdentifier(27).withName("TechRadar")
                .withIcon(R.drawable.ic_techradar).withTypeface(montserratRegular)
        val item28 = SectionDrawerItem().withIdentifier(28).withName("GAMING").withTypeface(montserratRegular)
        val item29 = PrimaryDrawerItem().withIdentifier(29).withName("IGN")
                .withIcon(R.drawable.ic_ignnews).withTypeface(montserratRegular)
        val item30 = PrimaryDrawerItem().withIdentifier(30).withName("Polygon")
                .withIcon(R.drawable.ic_polygonnews).withTypeface(montserratRegular)
        accountHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.ic_header)
                .withSavedInstance(savedInstanceState)
                .build()
        result = DrawerBuilder()
                .withAccountHeader(accountHeader)
                .withActivity(this)
                .withToolbar(toolbar!!)
                .withSelectedItem(1)
                .addDrawerItems(item0, item1, item2, item3, item4, item5, item6, item7, item8, item9,
                        item10, item11, item12, item13, item14, item15, item16, item17, item18, item19,
                        item20, item21, item22, item23, item24, item25, item26, item27, item28, item29, item30)
                .withOnDrawerItemClickListener { _, _, drawerItem ->
                            source = sourceArray[drawerItem.identifier.toInt()]
                            onLoadingSwipeRefreshLayout()
                            mTitle.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
                    return@withOnDrawerItemClickListener true
                }
                .withSavedInstance(savedInstanceState)
                .build()
    }

    private fun loadJSON() {
        swipeRefreshLayout.isRefreshing = true
        val component=DaggerRetrofitComponent.builder().contextModule(ContextModule(this)).build()
        val call = component.getRetrofitUri().getHeadlines(source, Constants.API_KEY)
        call!!.enqueue(object : Callback<NewsResponse?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<NewsResponse?>, response: Response<NewsResponse?>) {
                if (response.isSuccessful && response.body()!!.articles != null) {
                    if (articleStructure.isNotEmpty()) {
                        articleStructure.clear()
                    }
                    mTxvNoResultsFound!!.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    articleStructure = response.body()!!.articles!!
                    adapter = DataAdapter(this@MainActivity, articleStructure)
                    recyclerView.adapter = adapter
                    swipeRefreshLayout.isRefreshing = false
                } else {
                    swipeRefreshLayout.isRefreshing = false
                    swipeRefreshLayout.isEnabled = true
                    mTxvNoResultsFound!!.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    mTxvNoResultsFound!!.text = "Please check your Internet Connection and Again load the Data"
                }
            }

            override fun onFailure(call: Call<NewsResponse?>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    override fun onRefresh() {
        loadJSON()
    }

    private fun onLoadingSwipeRefreshLayout() {
        if (!UtilityMethods.isNetworkAvailable) {
            Toast.makeText(this@MainActivity, "Could not load latest News. Please turn on the Internet.", Toast.LENGTH_SHORT).show()
        }
        swipeRefreshLayout.post { loadJSON() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> openSearchActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openSearchActivity() {
        val searchIntent = Intent(this, SearchActivity::class.java)
        startActivity(searchIntent)
    }

    override fun onBackPressed() {
        if (result!!.isDrawerOpen) {
            result!!.closeDrawer()
        } else {
            val builder = AlertDialog.Builder(this@MainActivity, R.style.MyAlertDialogStyle)
            builder.setTitle("News Reader ")
            builder.setIcon(R.mipmap.ic_launcher_round)
            builder.setMessage("Do you want to Exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ -> finish() }
                    .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }
    }

    override fun onSaveInstanceState(bundle1: Bundle) {
        var bundle = bundle1
        bundle = result!!.saveInstanceState(bundle)
        bundle = accountHeader.saveInstanceState(bundle)
        super.onSaveInstanceState(bundle)
        listState = recyclerView.layoutManager.onSaveInstanceState()
        bundle.putParcelable(Constants.RECYCLER_STATE_KEY, listState)
        bundle.putString(Constants.SOURCE, source)
        bundle.putString(Constants.TITLE_STATE_KEY, mTitle.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        source = savedInstanceState.getString(Constants.SOURCE)
        createToolbar()
        mTitle.text = savedInstanceState.getString(Constants.TITLE_STATE_KEY)
        listState = savedInstanceState.getParcelable(Constants.RECYCLER_STATE_KEY)
        createDrawer(savedInstanceState, toolbar)
    }

    override fun onResume() {
        super.onResume()
        if (listState != null) {
            recyclerView.layoutManager.onRestoreInstanceState(listState)
        }
    }
}