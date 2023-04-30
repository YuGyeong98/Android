package com.example.good_place_map

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.good_place_map.MainActivity.Companion.isMapInit
import com.example.good_place_map.MainActivity.Companion.naverMap
import com.example.good_place_map.databinding.ActivitySearchResultBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate

class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private val searchResultAdapter = SearchResultAdapter {
        moveCamera(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchItemList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(
                getString(R.string.search_item_list),
                SearchItem::class.java
            )
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

    private fun moveCamera(position: LatLng) {
        if (!isMapInit) return

        val cameraUpdate = CameraUpdate.scrollAndZoomTo(position, 17.0).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}