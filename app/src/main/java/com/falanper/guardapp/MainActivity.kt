package com.falanper.guardapp

import android.app.Activity
import android.app.Fragment
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.falanper.guardapp.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var list = mutableListOf<DataRowItem>()
    private var pageNumber = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            list = mutableListOf()
            sendRequest()
            hideKeyboard()
        }

        binding.loadMoreButton.setOnClickListener {
            pageNumber += 1
            sendRequest()
            hideKeyboard()
        }
    }

    // Assembling the url to make the request
    private fun getUrl(): String {
        val word = binding.searchEditText.text
        // Inside quotation marks insert your own key obtained at https://open-platform.theguardian.com/access/
        val apiKey = "YOUR KEY HERE"

        val pageSize = 10
        return "https://content.guardianapis.com/search?page=$pageNumber&page-size=$pageSize&q=$word&api-key=$apiKey"
    }

    // Sending the request
    private fun sendRequest() {
        val url = getUrl()
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    extractJSON(response)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            },
            { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() })
        queue.add(jsonObjectRequest)
    }

    // Extracting data from response and adding as a DataRowItem to our mutable list
    private fun extractJSON(response: String) {

        val jsonObject = JSONObject(response) // main response's json object
        val jsonResponseBody = jsonObject.getJSONObject("response")
        val results = jsonResponseBody.getJSONArray("results") // contains 10 json objects

        for (i in 0..9) {
            val item = results.getJSONObject(i)
            val webTitle = item.getString("webTitle")
            val webUrl = item.getString("webUrl")
            val data = DataRowItem(webTitle, webUrl)
            list.add(data)
        }

        val adapter = NewsAdapter(list)
        binding.listView.adapter = adapter
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


