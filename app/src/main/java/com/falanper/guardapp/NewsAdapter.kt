package com.falanper.guardapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class NewsAdapter(private val listOfNews: MutableList<DataRowItem>) : BaseAdapter() {
    override fun getCount() = listOfNews.size

    override fun getItem(position: Int) = listOfNews[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val context = parent?.context
        var rowView = convertView

        val inflater: LayoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (rowView == null)
            rowView = inflater.inflate(R.layout.list_row, parent, false)

        val item = listOfNews[position]

        val title = rowView?.findViewById<TextView>(R.id.text_result)
        title?.text = item.webTitle

        title?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.webUrl))
            context.startActivity(intent)
        }
        return  rowView!!
    }

}