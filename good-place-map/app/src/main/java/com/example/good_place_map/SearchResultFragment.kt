package com.example.good_place_map

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.good_place_map.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultBinding
    private val searchResultAdapter = SearchResultAdapter {
//        moveCamera(it, 17.0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchItemList = requireArguments().getParcelableArrayList(
            getString(R.string.search_item_list),
            SearchItem::class.java
        )

        binding.toolbar.apply {
            (activity as AppCompatActivity).setSupportActionBar(this)
        }
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHasOptionsMenu(true)
        }

        binding.searchResultRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchResultAdapter
        }
        searchResultAdapter.submitList(searchItemList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun moveCamera(position: LatLng, zoom: Double) {
//        if (!MainActivity.isMapInit) return
//
//        val cameraUpdate = CameraUpdate.scrollAndZoomTo(position, zoom).animate(CameraAnimation.Easing)
//        MainActivity.naverMap.moveCamera(cameraUpdate)
//    }
}