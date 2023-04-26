package com.example.good_place_map

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.good_place_map.databinding.ActivitySearchResultBinding
import com.example.good_place_map.databinding.FragmentSearchResultBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate

class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private val searchResultAdapter = SearchResultAdapter {
//        moveCamera(it, 17.0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchItemList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(getString(R.string.search_item_list), SearchItem::class.java)
        } else {
            intent.getParcelableArrayListExtra(getString(R.string.search_item_list))
        }

        binding.toolbar.apply {
            setSupportActionBar(this)
        }
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.searchResultRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchResultAdapter
        }
        searchResultAdapter.submitList(searchItemList)
    }

//    private fun moveCamera(position: LatLng, zoom: Double) {
//        if (!isMapInit) return
//
//        val cameraUpdate = CameraUpdate.scrollAndZoomTo(position, zoom).animate(CameraAnimation.Easing)
//        naverMap.moveCamera(cameraUpdate)
//    }
}