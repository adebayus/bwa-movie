package com.sebade.mymovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sebade.mymovie.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var homeAdapter : HomeAdapter
    private lateinit var activityHomeBinding : ActivityHomeBinding
    private val film = mutableListOf<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)

        homeAdapter =  HomeAdapter()
        val rvNowPlaying = findViewById<RecyclerView>(R.id.recyclerView)

        rvNowPlaying.adapter = homeAdapter
        rvNowPlaying.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
        rvNowPlaying.addItemDecoration(DividerItemDecoration(this@HomeActivity, LinearLayoutManager.HORIZONTAL))


    }
}