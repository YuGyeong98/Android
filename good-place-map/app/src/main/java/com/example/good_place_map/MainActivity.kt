package com.example.good_place_map

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.good_place_map.databinding.ActivityMainBinding
import com.naver.maps.map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var naverMap: NaverMap
    private val searchResultAdapter = SearchResultAdapter()
    private var isMapInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment? ?: MapFragment.newInstance()
                .also {
                    fm.beginTransaction().add(R.id.map_fragment, it).commit()
                }
        mapFragment.getMapAsync {
            binding.logoView.setMap(it)
        }

        binding.bottomSheetLayout.searchResultRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchResultAdapter
        }

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isNotEmpty() == true) {
                    SearchRepository.getGoodPlace(query).enqueue(object : Callback<SearchResult> {
                        override fun onResponse(
                            call: Call<SearchResult>,
                            response: Response<SearchResult>
                        ) {
                            val searchItemList = response.body()?.items.orEmpty()
                            if (searchItemList.isEmpty()) {
                                Toast.makeText(this@MainActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                                return
                            } else if (!isMapInit) {
                                Toast.makeText(this@MainActivity, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                                return
                            }
                            searchResultAdapter.submitList(searchItemList)
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