package com.example.image_album

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.image_album.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ImageAdapter.ItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageAdapter: ImageAdapter
    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            updateImage(uriList)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadImageButton.setOnClickListener {
            checkPermission()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        imageAdapter = ImageAdapter(this)
        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.READ_MEDIA_IMAGES
            ) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestReadMediaImages()
            }
        }
    }

    private fun loadImage() {
        imageLoadLauncher.launch("image/\\*")
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 위해 미디어 이미지 읽기 권한이 필요합니다.")
            setPositiveButton("확인") { _, _ ->
                requestReadMediaImages()
            }
            setNegativeButton("취소", null)
        }.show()
    }

    private fun requestReadMediaImages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_READ_MEDIA_IMAGES
            )
        }
    }

    private fun updateImage(uriList: List<Uri>) {
        val images = uriList.map { ImageItems.Image(it) }
        val updatedImages = imageAdapter.currentList.toMutableList().apply { addAll(images) }
        imageAdapter.submitList(updatedImages)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_MEDIA_IMAGES -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                } else {
                    AlertDialog.Builder(this).apply {
                        setMessage("이미지 읽기 권한 거부로 이미지를 가져올 수 없습니다.")
                        setPositiveButton("확인", null)
                    }.show()
                }
            }
        }
    }

    override fun onLoadMoreClick() {
        checkPermission()
    }

    companion object {
        const val REQUEST_READ_MEDIA_IMAGES = 100
    }
}