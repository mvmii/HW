package com.example.hw.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hw.R
import com.example.hw.api.PlantInfo

class PlantAdapter(private val ctx : Context): RecyclerView.Adapter<PlantAdapter.MyViewHolder>() {

    private val dataList: MutableList<PlantInfo> = mutableListOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val tvFeature: TextView = itemView.findViewById(R.id.tvFeature)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view_plant, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataList[position]
        holder.tvName.text = item.name
        holder.tvLocation.text = item.location
        holder.tvFeature.text = item.feature
        Glide.with(ctx).load(item.imgUrl.replace("http://","https://")).into(holder.imgIcon)
    }

    override fun getItemCount() = dataList.size

    fun update(dataList: List<PlantInfo>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }
}