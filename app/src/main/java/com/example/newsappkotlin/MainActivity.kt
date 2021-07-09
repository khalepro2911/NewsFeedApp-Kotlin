package com.example.newsappkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.util.*
import okhttp3.Call

import org.jsoup.Jsoup

import org.jsoup.nodes.Document

import org.jsoup.nodes.Element

import org.jsoup.select.Elements

import okhttp3.Callback

import okhttp3.OkHttpClient

import okhttp3.Request

import okhttp3.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope


class MainActivity : AppCompatActivity() {
    //private TextView mTextViewResult;
    private val url = "https://vnexpress.net/"
    private var titles: ArrayList<String> = ArrayList()
    private var links: ArrayList<String> = ArrayList()
    private var websites: ArrayList<Website> = ArrayList()
    private var client = OkHttpClient()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<*>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        fetch(url)
    }

    private fun getTitles(html: String?): ArrayList<String> {
        val doc: Document = Jsoup.parse(html, "utf-8")
        val titles = ArrayList<String>()
        val link: Elements = doc.getElementsByClass("title-news")
        for (e in link) {
            titles.add(e.getElementsByTag("a").attr("title"))
        }
        return titles
    }

    private fun getURL(html: String?): ArrayList<String> {
        val doc: Document = Jsoup.parse(html, "utf-8")
        val links = ArrayList<String>()
        val link: Elements = doc.getElementsByClass("title-news")
        for (e in link) {
            links.add(e.getElementsByTag("a").attr("href"))
        }
        return links
    }
    private fun fetch(url: String){
        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(url)
            if (result != null) {
                try {

                    titles = getTitles(result)
                    links = getURL(result)
                    var size = titles.size - 1
                    for(i in 0..size){
                        val ele = Website(titles[i],links[i])
                        websites.add(ele)
                    }

                    withContext(Dispatchers.Main) {
                        // Update view model
                        adapter = WebAdapter(websites,applicationContext)
                        recyclerView.adapter = adapter
                    }
                }
                catch(err:Error) {
                    print("Error when parsing JSON: "+err.localizedMessage)
                }
            }
            else {
                print("Error: Get request returned no response")
            }
        }
    }
    private fun getRequest(url: String):String?
    {

        var result: String? = null
        try {
            // Build request
            val request = Request.Builder().url(url).build()

            // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
        }
        catch(err:Error) {
            print("Error when executing get request: "+err.localizedMessage)
        }
        return result
    }

    private fun init() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }
}