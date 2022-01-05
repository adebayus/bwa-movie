package com.sebade.mymovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binding(){
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_now_playing, parent, false)
        return HomeViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20
    }
}