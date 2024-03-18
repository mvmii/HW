package com.example.hw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hw.api.PlantReq
import com.example.hw.ui.PlantAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: PlantAdapter // 假設您已經創建了一個適配器
    private lateinit var recyclerView: RecyclerView
    private var limit = 20
    private lateinit var viewModel : MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        initView()
        fetchPlantData()
        viewModel.records.observe(this) { records ->
            adapter.update(records)
        }
        viewModel.error.observe(this) { errorMessage ->
            showErrorDialog(errorMessage)
        }
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this)
            .setTitle("錯誤")
            .setMessage(errorMessage)
            .setPositiveButton("確認") { _, _ ->
                finish() // 關閉當前Activity，即結束App（如果這是唯一一個Activity）
            }
            .show()
    }

    private fun fetchPlantData() {
        viewModel.fetchData(PlantReq("",limit,0))

    }

    private fun initView() {
        recyclerView = findViewById(R.id.rvPlant)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PlantAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) { // 1 for down
                    limit += 20
                    fetchPlantData()
                }
            }
        })
    }
}