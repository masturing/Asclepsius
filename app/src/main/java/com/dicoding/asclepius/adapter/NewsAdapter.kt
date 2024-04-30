package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.datasource.Article

class NewsAdapter(private val listArticle: ArrayList<Article>) : RecyclerView.Adapter<NewsAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<Article>){
        listArticle.clear()
        listArticle.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemCLickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_history, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val article = listArticle[position]
        if(article.urlToImage != null ){
            Glide.with(holder.itemView.context)
                .load(article.urlToImage)
                .into(holder.imgView)
        } else {
            holder.imgView.setImageResource(R.drawable.ic_logo)
        }
        holder.tvItemDate.text = article.source?.name
        holder.tvDesc.text = article.title
        holder.tvItemDate.textSize = 12f
        holder.tvDesc.textSize = 12f
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listArticle[holder.absoluteAdapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listArticle.size
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imgView: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvItemDate : TextView = itemView.findViewById(R.id.tv_item_date)
        var tvDesc : TextView = itemView.findViewById(R.id.tv_item_desc)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Article)
    }
}