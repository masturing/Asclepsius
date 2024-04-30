package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.HistoryEntity
import com.dicoding.asclepius.helper.BitmapConverter

class HistoryAdapter(private val listHistory: ArrayList<HistoryEntity>) : RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<HistoryEntity>){
        listHistory.clear()
        listHistory.addAll(items)
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
        val history = listHistory[position]
        val bitmap = history.image?.let { BitmapConverter.converterStringToBitmap(it) }
        holder.imgPhoto.setImageBitmap(bitmap)
        holder.tvItemDate.text = history.date
        holder.tvDesc.text = buildString {
        append(history.prediction)
        append(" - ")
        append(history.confident)
    }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listHistory[holder.absoluteAdapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvItemDate : TextView = itemView.findViewById(R.id.tv_item_date)
        var tvDesc : TextView = itemView.findViewById(R.id.tv_item_desc)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: HistoryEntity)
    }
}