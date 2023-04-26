package com.example.good_place_map

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import com.example.good_place_map.databinding.ActivityMainBinding
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var naverMap: NaverMap
    private var isMapInit = false
    private var searchItemList = emptyList<SearchItem>()
    private var markers = emptyList<Marker>()
    private var itemList = arrayListOf<SearchItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.apply {
            setSupportActionBar(this)
        }
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.mapFragment) as MapFragment? ?: MapFragment.newInstance()
                .also {
                    fm.beginTransaction().apply {
                        add(R.id.mapFragment, it)
                        commit()
                    }
                }
        mapFragment.getMapAsync(this)

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isNotEmpty() == true) {
                    SearchRepository.getGoodPlace(query).enqueue(object : Callback<SearchResult> {
                        override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                            searchItemList = response.body()?.items.orEmpty()
                            if (searchItemList.isEmpty()) {
                                Toast.makeText(this@MainActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                                return
                            } else if (!isMapInit) {
                                Toast.makeText(this@MainActivity, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                                return
                            }

                            itemList.clear()
                            searchItemList.forEach {
                                itemList.add(it)
                            }

                            Intent(this@MainActivity, SearchResultActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                putExtra(getString(R.string.search_item_list), itemList)
                                startActivity(this)
                            }
                        }

                        override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                    return false
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }


    override fun onMapReady(map: NaverMap) {
        naverMap = map
        isMapInit = true
        naverMap.minZoom = 5.0
        naverMap.maxZoom = 18.0
    }
}