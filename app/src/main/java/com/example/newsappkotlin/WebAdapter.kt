package com.example.newsappkotlin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class WebAdapter(private val websites: ArrayList<Website>, private val context: Context) :
    RecyclerView.Adapter<WebAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.rowlayout, parent, false)
        return ViewHolder(itemView, context, websites)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btnView.text = websites[position].title
    }

    override fun getItemCount(): Int {
        return websites.size
    }

    class ViewHolder(itemView: View, private val context: Context,
                           private val websites: ArrayList<Website>
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val btnView: TextView = itemView.findViewById(R.id.btn)
        override fun onClick(v: View) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("URL_NAME", websites[absoluteAdapterPosition].url)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}