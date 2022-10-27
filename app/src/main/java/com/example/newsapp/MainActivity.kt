package com.example.newsapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NewsItemClicked {


//    companion object {
//        private const val JSON_PLACEHOLDER_API_URL = "https://newsapi.org/v2/top-headlines?country=in&apiKey=825ee82baf1f4e55acb964f755749463"
//    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter : NewsListAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdaptor(this)
        binding.recyclerView.adapter = mAdapter

    }

    private fun fetchData()   {
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=825ee82baf1f4e55acb964f755749463"
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener{
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray =ArrayList<News>()
                for (i in 0 until newsJsonArray.length()){
                    val newsJsonObject =newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    //Toast.makeText(this,"Entered",Toast.LENGTH_LONG).show()
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()

            }

        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
//        val queue = MySingleton.getInstance(this).requestQueue
//        queue.add(jsonObjectRequest)
    }
//    private fun fetchData() {
//        val queue = Volley.newRequestQueue(this)
//        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=825ee82baf1f4e55acb964f755749463"
//        val getRequest: JsonObjectRequest = object : JsonObjectRequest(
//            Request.Method.GET,
//            url,
//            null,
//            Response.Listener {
//                Log.e("sdsadas","$it")
//                val newsJsonArray = it.getJSONArray("articles")
//                val newsArray = ArrayList<News>()
//                for(i in 0 until  newsJsonArray.length()){
//                    val newsJsonObject = newsJsonArray.getJSONObject(i)
//                    val news = News(
//                        newsJsonObject.getString("author"),
//                        newsJsonObject.getString("title"),
//                        newsJsonObject.getString("url"),
//                        newsJsonObject.getString("urlToImage")
//                    )
//                    //Toast.makeText(this,"Entered",Toast.LENGTH_LONG).show()
//                    newsArray.add(news)
//                }
//                mAdapter.updateNews(newsArray)
//            },
//            Response.ErrorListener {
//
//            }
//        ) {
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["User-Agent"] = "Mozilla/5.0"
//                return params
//            }
//        }
//    queue.add(getRequest)
//    }

    override fun onItemClicked(item: News) {
        //Toast.makeText(this,"clicked item is $item", Toast.LENGTH_SHORT).show()
        val builder= CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))


    }
}