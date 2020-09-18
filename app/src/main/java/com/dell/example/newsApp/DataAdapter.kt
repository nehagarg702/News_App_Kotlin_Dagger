package com.dell.example.newsApp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Typeface
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dell.example.newsApp.model.ArticleStructure
import com.dell.example.newsApp.model.Constants
import com.dell.example.newsApp.view.ArticleActivity
import java.util.*

/*
** Adapter for adding data to Recycle View. Also used Glide to load the Image from url.
**/
class DataAdapter(private val mContext: Context, private val articles: ArrayList<ArticleStructure>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    private var lastPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_row, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var title = articles[position].title
        if (title!!.endsWith("- Times of India")) {
            title = title.replace("- Times of India", "")
        } else if (title.endsWith(" - Firstpost")) {
            title = title.replace(" - Firstpost", "")
        }
        holder.tvCardMainTitle.text = title
        Glide.with(mContext)
                .load(articles[position].urlToImage)
                .thumbnail(0.1f)
                .centerCrop()
                .error(R.drawable.ic_placeholder)
                .into(holder.imgCardMain)
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_fall_down)
            holder.cardView.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val tvCardMainTitle: TextView = view.findViewById(R.id.tv_card_main_title)
        val imgCardMain: ImageView
        val cardView: CardView
        private var assetManager: AssetManager = mContext.applicationContext.assets
        private var montserratRegular: Typeface = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf")
        override fun onClick(v: View) {
            var headLine = articles[adapterPosition].title
            if (headLine!!.endsWith(" - Times of India")) {
                headLine = headLine.replace(" - Times of India", "")
            } else if (headLine.endsWith(" - Firstpost")) {
                headLine = headLine.replace(" - Firstpost", "")
            }
            val description = articles[adapterPosition].description
            val date = articles[adapterPosition].publishedAt
            val imgURL = articles[adapterPosition].urlToImage
            val url = articles[adapterPosition].url
            val intent = Intent(mContext, ArticleActivity::class.java)
            intent.putExtra(Constants.INTENT_HEADLINE, headLine)
            intent.putExtra(Constants.INTENT_DESCRIPTION, description)
            intent.putExtra(Constants.INTENT_DATE, date)
            intent.putExtra(Constants.INTENT_IMG_URL, imgURL)
            intent.putExtra(Constants.INTENT_ARTICLE_URL, url)
            mContext.startActivity(intent)
            (mContext as Activity).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left)
        }

        init {
            tvCardMainTitle.typeface = montserratRegular
            imgCardMain = view.findViewById(R.id.img_card_main)
            cardView = view.findViewById(R.id.card_row)
            view.setOnClickListener(this)
        }
    }
}